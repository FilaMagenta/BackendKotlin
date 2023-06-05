package com.arnyminerz

import com.arnyminerz.utils.getIntOrNull
import com.arnyminerz.utils.getJSONObjectOrNull
import com.arnyminerz.utils.getStringOrNull
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.json.JSONObject
import org.junit.Test

class JsonUtilsTest {
    @Test
    fun test_getStringOrNull() {
        val json = JSONObject().apply {
            put("key", "value")
        }
        assertNull(json.getStringOrNull("not-found"))
        assertEquals("value", json.getStringOrNull("key"))
    }

    @Test
    fun test_getStringOrNull_emptyIsNull() {
        val json = JSONObject().apply {
            put("empty", "")
        }
        assertEquals("", json.getStringOrNull("empty"))
        assertNull(json.getStringOrNull("empty", true))
    }

    @Test
    fun test_getIntOrNull() {
        val json = JSONObject().apply {
            put("key", 1)
        }
        assertNull(json.getIntOrNull("not-found"))
        assertEquals(1, json.getIntOrNull("key"))
    }

    @Test
    fun test_getJSONObjectOrNull() {
        val json = JSONObject().apply {
            put("key", JSONObject().apply { put("inner", "value") })
        }
        assertNull(json.getJSONObjectOrNull("not-found"))
        val obj = json.getJSONObject("key")
        assertEquals("value", obj.getString("inner"))
    }

}
