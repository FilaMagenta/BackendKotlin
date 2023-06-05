package com.arnyminerz.endpoints.arguments

import com.arnyminerz.utils.getStringOrNull
import com.arnyminerz.utils.receiveJson
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext
import kotlin.reflect.KProperty
import org.json.JSONObject

class OptionalArgument(val name: String)

class CalledOptionalArgument(
    private val argument: OptionalArgument,
    private val body: JSONObject
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String? {
        return body.getStringOrNull(argument.name, true)
    }
}

suspend fun PipelineContext<*, ApplicationCall>.calledOptional(block: () -> OptionalArgument): CalledOptionalArgument {
    val body = call.receiveJson()
    return CalledOptionalArgument(block(), body)
}
