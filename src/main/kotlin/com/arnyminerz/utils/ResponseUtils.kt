package com.arnyminerz.utils

import com.arnyminerz.errors.Error
import com.arnyminerz.utils.serialization.JsonSerializable
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondText
import org.json.JSONObject

suspend fun ApplicationCall.respondSuccess(data: JSONObject? = null, status: HttpStatusCode = HttpStatusCode.OK) {
    val response = JSONObject()
    response.put("success", true)
    data?.let { response.put("data", it) }
    respondJson(response, status)
}

suspend fun <Data : JsonSerializable> ApplicationCall.respondSuccess(
    data: Data? = null,
    status: HttpStatusCode = HttpStatusCode.OK
) {
    respondSuccess(data?.toJSON(), status)
}

suspend fun ApplicationCall.respondSuccess(status: HttpStatusCode = HttpStatusCode.OK) = respondSuccess(null, status)

suspend fun <Extra : JsonSerializable> ApplicationCall.respondFailure(
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

suspend fun <Extra : JsonSerializable> ApplicationCall.respondFailure(
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
    respondText(json.toString(), contentType = ContentType.parse("application/json"), status = status)
}

suspend fun HttpResponse.bodyAsJson(): JSONObject = JSONObject(bodyAsText())
