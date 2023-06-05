package com.arnyminerz.utils

import com.arnyminerz.errors.Error
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.json.JSONObject
import org.junit.Assert.assertEquals

suspend fun HttpResponse.assertSuccess(assertData: suspend (data: JSONObject?) -> Unit = {}) {
    assertEquals(
        HttpStatusCode.OK,
        status,
        bodyAsJson().let { body ->
            "Response was not successful. HTTP#${status.value}: ${status.description}.\n" +
                    "#${body.getIntOrNull("code")}: ${body.getStringOrNull("message")}" +
                    if (body.has("error")) {
                        val error = body.getJSONObject("error")
                        "\nError: ${error.getStringOrNull("message")}. Stacktrace:\n    " +
                                error.getStringOrNull("stackTrace")?.split(", ")?.joinToString("\n    ")
                    } else ""
        }
    )
    val body = bodyAsJson()
    assertTrue(body.getBoolean("success"))
    assertData(body.getJSONObjectOrNull("data"))
}

suspend fun HttpResponse.assertFailure(error: Error) {
    assertEquals(error.httpStatusCode, status)
    val body = bodyAsJson()
    assertFalse(body.getBoolean("success"))
    assertEquals(error.code, body.getInt("code"))
    assertEquals(error.message, body.getString("message"))
}
