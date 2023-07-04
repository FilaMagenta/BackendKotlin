package com.arnyminerz.endpoints.arguments

import com.arnyminerz.errors.Error
import com.arnyminerz.utils.receiveJson
import com.arnyminerz.utils.respondFailure
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext
import kotlin.reflect.KProperty
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

/**
 * Defines a property that might be stored in the body of a request.
 * @param name The key of the property in the body.
 * @param error The error to respond if [name] is not present.
 */
class Argument<R : Any, Type : ArgumentType<R>>(
    name: String,
    type: Type,
    val error: Error
) : ArgumentProto<R, Type>(name, type)

class CalledArgument<R : Any, Type : ArgumentType<R>>(
    private val argument: Argument<R, Type>,
    private val call: ApplicationCall,
    private val body: JSONObject
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): R {
        return try {
            argument.type.fromJson(body, argument.name) ?: runBlocking {
                call.respondFailure(argument.error)
                throw MissingArgumentException(argument)
            }
        } catch (_: IllegalArgumentException) {
            runBlocking { call.respondFailure(argument.error) }
            throw MissingArgumentException(argument)
        }
    }
}

suspend fun <R : Any, Type : ArgumentType<R>> PipelineContext<*, ApplicationCall>.called(
    block: () -> Argument<R, Type>
): CalledArgument<R, Type> {
    val body = call.receiveJson()
    return CalledArgument(block(), call, body)
}
