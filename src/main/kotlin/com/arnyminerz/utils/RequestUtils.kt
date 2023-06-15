package com.arnyminerz.utils

import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveText
import org.json.JSONException
import org.json.JSONObject

suspend fun ApplicationCall.receiveJson(): JSONObject {
    val text = receiveText()
    return try {
        JSONObject(text)
    } catch (_: JSONException) {
        JSONObject()
    }
}
