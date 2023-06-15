package com.arnyminerz

import com.arnyminerz.utils.getBooleanOrNull
import com.arnyminerz.utils.getFloatOrNull
import com.arnyminerz.utils.getIntOrNull
import com.arnyminerz.utils.getJSONObjectOrNull
import com.arnyminerz.utils.getStringOrNull
import com.arnyminerz.utils.serialization.JsonSerializable
import com.arnyminerz.utils.toJSONArray
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
    fun test_getFloatOrNull() {
        val json = JSONObject().apply {
            put("key", 1.5f)
        }
        assertNull(json.getFloatOrNull("not-found"))
        assertEquals(1.5f, json.getFloatOrNull("key"))
    }

    @Test
    fun test_getBooleanOrNull() {
        val json = JSONObject().apply {
            put("key", true)
        }
        assertNull(json.getBooleanOrNull("not-found"))
        assertEquals(true, json.getBooleanOrNull("key"))
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

    @Test
    fun `test converting list of JsonSerializable to JSONArray`() {
        class TestObject(val name: String) : JsonSerializable {
            override fun toJSON(): JSONObject = JSONObject().apply {
                put("name", name)
            }
        }

        val list = listOf(
            TestObject("obj1"),
            TestObject("obj2"),
            TestObject("obj3")
        )
        val json = list.toJSONArray()

        assertEquals(3, json.length())
        assertEquals(list[0].toJSON().toString(), json.getJSONObject(0).toString())
        assertEquals(list[1].toJSON().toString(), json.getJSONObject(1).toString())
        assertEquals(list[2].toJSON().toString(), json.getJSONObject(2).toString())
    }
}
