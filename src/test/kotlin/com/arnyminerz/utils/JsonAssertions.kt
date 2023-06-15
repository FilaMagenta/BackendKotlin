package com.arnyminerz.utils

import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import org.json.JSONArray
import org.json.JSONObject

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
            assertEquals(
                expectedValue,
                actualValue,
                "Expected \"%s\" at position %d but got \"%s\""
                    .format(expectedValue.toString(), i, actualValue.toString())
            )
        }
    }
}

fun assertEqualsJson(expected: JSONObject, actual: JSONObject) {
    for (key in actual.keys()) {
        assertTrue(expected.has(key))

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
            assertEquals(
                expectedValue,
                actualValue,
                "Expected \"%s\" at %s but got \"%s\""
                    .format(expectedValue.toString(), key, actualValue.toString())
            )
        }
    }
}
