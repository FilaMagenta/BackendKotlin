package com.arnyminerz.endpoints.arguments

import com.arnyminerz.utils.receiveJson
import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import org.json.JSONObject
import kotlin.reflect.KProperty

class OptionalArgument<R: Any, Type: ArgumentType<R>>(
    name: String,
    type: Type,
    val default: R? = null
): ArgumentProto<R, Type>(name, type)

class CalledOptionalArgument<R: Any, Type: ArgumentType<R>>(
    private val argument: OptionalArgument<R, Type>,
    private val body: JSONObject
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): R? {
        return argument.type.fromJson(body, argument.name) ?: argument.default
    }
}

suspend fun <R: Any, Type: ArgumentType<R>> PipelineContext<*, ApplicationCall>.calledOptional(
    block: () -> OptionalArgument<R, Type>
): CalledOptionalArgument<R, Type> {
    val body = call.receiveJson()
    return CalledOptionalArgument(block(), body)
}
