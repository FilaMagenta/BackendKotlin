package com.arnyminerz.utils

import com.arnyminerz.utils.serialization.JsonSerializable
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Get the string associated with a key.
 * @param key A key string.
 * @param emptyIsNull If true, stored empty values will be considered as if there was no value stored at all.
 * @return A string which is the value, or null, if there isn't any stored value with the given key.
 */
fun JSONObject.getStringOrNull(key: String, emptyIsNull: Boolean = false): String? = try {
    if (has(key)) getString(key).takeIf { if (!emptyIsNull) true else it.isNotEmpty() } else null
} catch (_: JSONException) {
    null
}

/**
 * Get the integer associated with a key.
 * @param key A key string.
 * @return An integer which is the value, or null, if there isn't any stored value with the given key.
 */
fun JSONObject.getIntOrNull(key: String): Int? = try {
    if (has(key)) getInt(key) else null
} catch (_: JSONException) {
    null
}

/**
 * Get the float associated with a key.
 * @param key A key string.
 * @return A float which is the value, or null, if there isn't any stored value with the given key.
 */
fun JSONObject.getFloatOrNull(key: String): Float? = try {
    if (has(key)) getFloat(key) else null
} catch (_: JSONException) {
    null
}

/**
 * Get the JSONObject associated with a key.
 * @param key A key string.
 * @return A JSONObject which is the value, or null, if there isn't any stored value with the given key.
 */
fun JSONObject.getJSONObjectOrNull(key: String): JSONObject? = try {
    if (has(key)) getJSONObject(key) else null
} catch (_: JSONException) {
    null
}

fun jsonOf(vararg pairs: Pair<String, Any>) =
    JSONObject().apply {
        for ((key, value) in pairs)
            put(key, value)
    }

/**
 * Converts the list into a JSON array.
 */
fun <T : JsonSerializable> Iterable<T>.toJSONArray(): JSONArray = JSONArray().apply {
    for (item in this@toJSONArray)
        put(item.toJSON())
}
