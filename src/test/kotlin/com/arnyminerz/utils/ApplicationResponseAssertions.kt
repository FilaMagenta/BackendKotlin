package com.arnyminerz.utils

import com.arnyminerz.errors.Error
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.json.JSONObject
import org.junit.Assert.assertEquals

suspend fun HttpResponse.assertSuccess(
    statusCode: HttpStatusCode = HttpStatusCode.OK,
    assertData: suspend (data: JSONObject?) -> Unit = {}
) {
    assertEquals(
        statusCode,
        status,
        bodyAsJson().let { body ->
            "Response was not successful. HTTP#%d: %s.\n#%d: %s%s"
                .format(
                    status.value,
                    status.description,
                    body.getIntOrNull("code"),
                    body.getStringOrNull("message"),
                    if (body.has("error")) {
                        val error = body.getJSONObject("error")
                        val msg = error.getStringOrNull("message")
                        val stackTrace = error
                            .getStringOrNull("stackTrace")
                            ?.split(", ")
                            ?.joinToString("\n")
                            ?.prependIndent("    ")
                        "\nError: $msg. Stacktrace:\n$stackTrace"
                    } else {
                        ""
                    }
                )
        }
    )
    val body = bodyAsJson()
    assertTrue(body.getBoolean("success"))
    assertData(body.getJSONObjectOrNull("data"))
}

suspend fun HttpResponse.assertFailure(error: Error) {
    val body = bodyAsJson()
    val success = body.getBooleanOrNull("success")
    val code = body.getIntOrNull("code")
    val message = body.getStringOrNull("message")

    assertEquals(
        error.httpStatusCode,
        status,
        "Error #$code: $message.\t"
    )
    assertEquals(false, success, "Response was successful. Expected unsuccessful")
    assertEquals(error.code, code)
    assertEquals(error.message, message)
}
