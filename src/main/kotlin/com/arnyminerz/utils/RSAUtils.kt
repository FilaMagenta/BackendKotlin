package com.arnyminerz.utils

import java.security.Key
import java.security.PrivateKey
import java.security.PublicKey
import java.util.Base64
import org.json.JSONException
import org.json.JSONObject

/**
 * Converts the given Key into a [JSONObject] that can be converted into string.
 * @see JSONObject.getRSAPrivateKey
 * @see JSONObject.getRSAPublicKey
 */
fun Key.toJSON(): JSONObject = JSONObject().apply {
    put("algorithm", algorithm)
    put("encoded", Base64.getMimeEncoder().encodeToString(encoded))
    put("format", format)
}

fun JSONObject.getRSAPublicKey(key: String): PublicKey = getJSONObject(key).let { json ->
    if (!json.has("algorithm") || !json.has("format") || !json.has("encoded")) {
        throw JSONException("JSONObject doesn't contain a valid RSA key.")
    }

    object : PublicKey {
        override fun getAlgorithm(): String = json.getString("algorithm")

        override fun getFormat(): String = json.getString("format")

        override fun getEncoded(): ByteArray = Base64.getMimeDecoder().decode(json.getString("encoded"))
    }
}

fun JSONObject.getRSAPrivateKey(key: String): PrivateKey = getJSONObject(key).let { json ->
    if (!json.has("algorithm") || !json.has("format") || !json.has("encoded")) {
        throw JSONException("JSONObject doesn't contain a valid RSA key.")
    }

    object : PrivateKey {
        override fun getAlgorithm(): String = json.getString("algorithm")

        override fun getFormat(): String = json.getString("format")

        override fun getEncoded(): ByteArray = Base64.getMimeDecoder().decode(json.getString("encoded"))
    }
}
