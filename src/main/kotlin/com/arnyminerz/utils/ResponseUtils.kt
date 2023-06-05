package com.arnyminerz.utils

import com.arnyminerz.errors.Error
import com.arnyminerz.utils.serialization.JsonSerializable
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.util.pipeline.PipelineContext
import org.json.JSONObject

suspend fun ApplicationCall.respondSuccess(data: JSONObject? = null) {
    val response = JSONObject()
    response.put("success", true)
    data?.let { response.put("data", it) }
    respondJson(response)
}

suspend fun <Data: JsonSerializable> ApplicationCall.respondSuccess(data: Data? = null) {
    respondSuccess(data?.toJSON())
}


suspend fun ApplicationCall.respondSuccess() = respondSuccess(null)

suspend fun <Extra: JsonSerializable> ApplicationCall.respondFailure(
    code: Int,
    message: String,
    httpStatusCode: HttpStatusCode = HttpStatusCode.BadRequest,
    error: Throwable? = null,
    extra: Extra? = null
) {
    val response = JSONObject()
    response.put("success", false)
    response.put("code", code)
    response.put("message", message)
    error?.let { err ->
        val errorBlock = JSONObject()
        errorBlock.put("message", err.message)
        errorBlock.put("stackTrace", err.stackTrace.joinToString { it.toString() })
        response.put("error", errorBlock)
    }
    extra?.let { response.put("extra", it) }
    respondJson(response, httpStatusCode)
}

suspend fun <Extra: JsonSerializable> ApplicationCall.respondFailure(
    error: Error,
    exception: Throwable? = null,
    extra: Extra? = null
) {
    respondFailure(error.code, error.message, error.httpStatusCode, exception, extra)
}

suspend fun ApplicationCall.respondFailure(error: Error) {
    respondFailure<JsonSerializable>(error.code, error.message, error.httpStatusCode)
}

suspend fun ApplicationCall.respondJson(json: JSONObject, status: HttpStatusCode = HttpStatusCode.OK) {
    respondText(json.toString(), status = status)
}

suspend fun HttpResponse.bodyAsJson(): JSONObject = JSONObject(bodyAsText())
