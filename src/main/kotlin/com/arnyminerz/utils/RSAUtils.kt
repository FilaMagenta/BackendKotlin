package com.arnyminerz.utils

import java.security.Key
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.EncodedKeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
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

private fun JSONObject.toRSAKeySpec(): Pair<EncodedKeySpec, KeyFactory> {
    if (!has("algorithm") || !has("format") || !has("encoded")) {
        throw JSONException("JSONObject doesn't contain a valid RSA key.")
    }

    val encoded = getString("encoded")
    val format = getString("format")

    val bytes = Base64.getMimeDecoder().decode(encoded)
    val ks = when (format) {
        "PKCS#8" -> PKCS8EncodedKeySpec(bytes)
        "X.509" -> X509EncodedKeySpec(bytes)
        else -> throw UnsupportedOperationException("Got an unsupported key format: $format")
    }
    val kf = KeyFactory.getInstance("RSA")
    return ks to kf
}

/**
 * Converts the given [JSONObject] into a [PublicKey]. Requires `algorithm`, `format` and `encoded`.
 * @throws JSONException If the object doesn't contain all the required fields.
 * @throws UnsupportedOperationException If the object contains an unsupported key format.
 */
fun JSONObject.toRSAPublicKey(): PublicKey {
    val (ks, kf) = toRSAKeySpec()
    return kf.generatePublic(ks)
}

/**
 * Converts the given [JSONObject] into a [PrivateKey]. Requires `algorithm`, `format` and `encoded`.
 * @throws JSONException If the object doesn't contain all the required fields.
 * @throws UnsupportedOperationException If the object contains an unsupported key format.
 */
fun JSONObject.toRSAPrivateKey(): PrivateKey {
    val (ks, kf) = toRSAKeySpec()
    return kf.generatePrivate(ks)
}

/**
 * Uses [JSONObject.toRSAPublicKey] to get the key stored at position [key].
 * @throws JSONException If the object doesn't contain all the required fields.
 * @throws UnsupportedOperationException If the object contains an unsupported key format.
 */
fun JSONObject.getRSAPublicKey(key: String): PublicKey = getJSONObject(key).toRSAPublicKey()

/**
 * Uses [JSONObject.toRSAPrivateKey] to get the key stored at position [key].
 * @throws JSONException If the object doesn't contain all the required fields.
 * @throws UnsupportedOperationException If the object contains an unsupported key format.
 */
fun JSONObject.getRSAPrivateKey(key: String): PrivateKey = getJSONObject(key).toRSAPrivateKey()
