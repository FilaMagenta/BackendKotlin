package com.arnyminerz.utils

import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import org.json.JSONArray
import org.json.JSONObject

/**
 * Converts types for making it easier to compare. For example, String<"1"> is not the same as Integer<1>, but they
 * should be considered as so.
 */
private fun fixTypes(expectedValue: Any, actualValue: Any) = if (expectedValue is Short) {
    expectedValue.toInt()
} else if (expectedValue is String && actualValue is Number) {
    when (actualValue) {
        is Int -> expectedValue.toInt()
        is Float -> expectedValue.toFloat()
        is Long -> expectedValue.toLong()
        is Double -> expectedValue.toDouble()
        else -> expectedValue
    }
} else {
    expectedValue
}

fun assertEqualsJson(expected: JSONArray, actual: JSONArray) {
    for (i in 0 until expected.length()) {
        val expectedValue = expected.get(i)
        val actualValue = actual.get(i)

        if (actualValue is JSONObject) {
            assertIs<JSONObject>(
                expectedValue,
                "Expected an object of type JSONObject at position %d but got a %s instead."
                    .format(i, expectedValue::class.java.simpleName)
            )
            assertEqualsJson(expectedValue, actualValue)
        } else if (expectedValue is JSONArray) {
            assertIs<JSONArray>(
                expectedValue,
                "Expected an object of type JSONArray at position %d but got a %s instead."
                    .format(i, expectedValue::class.java.simpleName)
            )
            actualValue as JSONArray

            assertEqualsJson(expectedValue, actualValue)
        } else {
            val expectConverted = fixTypes(expectedValue, actualValue)

            assertEquals(
                expectConverted,
                actualValue,
                "Expected \"%s\" at position %d but got \"%s\""
                    .format(expectedValue.toString(), i, actualValue.toString())
            )
        }
    }
}

fun assertEqualsJson(expected: JSONObject, actual: JSONObject, ignoreMissing: Boolean = false) {
    for (key in actual.keys()) {
        val has = expected.has(key)
        if (!has) {
            if (ignoreMissing) {
                continue
            } else {
                assertTrue(false, "Expected that the object contained %s, but it didn't.".format(key))
            }
        }

        val actualValue = actual.get(key)
        val expectedValue = expected.get(key)

        if (actualValue is JSONObject) {
            assertIs<JSONObject>(
                expectedValue,
                "Expected an object of type JSONObject at %s but got a %s instead."
                    .format(key, expectedValue::class.java.simpleName)
            )
            assertEqualsJson(expectedValue, actualValue)
        } else if (expectedValue is JSONArray) {
            assertIs<JSONArray>(
                expectedValue,
                "Expected an object of type JSONArray at %s but got a %s instead."
                    .format(key, expectedValue::class.java.simpleName)
            )
            actualValue as JSONArray

            assertEqualsJson(expectedValue, actualValue)
        } else {
            val expectConverted = fixTypes(expectedValue, actualValue)

            assertEquals(
                when (expectConverted) {
                    is Int -> expectConverted.toLong()
                    is Float -> expectConverted.toDouble()
                    else -> expectConverted
                },
                when (actualValue) {
                    is Int -> actualValue.toLong()
                    is Float -> actualValue.toDouble()
                    else -> actualValue
                },
                "Expected \"%s\" at %s but got \"%s\""
                    .format(expectConverted.toString(), key, actualValue.toString())
            )
        }
    }
}
