package com.arnyminerz.endpoints.arguments

import com.arnyminerz.utils.getStringOrNull
import kotlin.reflect.KProperty
import org.json.JSONObject

abstract class ArgumentProto(
    val name: String
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String? {
        if (thisRef !is JSONObject) throw IllegalArgumentException("this must be PipelineContext")
        return thisRef.getStringOrNull(name, true)
    }
}
