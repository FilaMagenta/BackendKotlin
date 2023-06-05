package com.arnyminerz.endpoints.arguments

import com.arnyminerz.errors.Error
import com.arnyminerz.utils.getStringOrNull
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
class Argument(
    val name: String,
    val error: Error
)

class CalledArgument(
    private val argument: Argument,
    private val call: ApplicationCall,
    private val body: JSONObject
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return body.getStringOrNull(argument.name, true) ?: runBlocking {
            call.respondFailure(argument.error)
            throw MissingArgumentException(argument)
        }
    }
}

suspend fun PipelineContext<*, ApplicationCall>.called(block: () -> Argument): CalledArgument {
    val body = call.receiveJson()
    return CalledArgument(block(), call, body)
}
