package com.arnyminerz

import com.arnyminerz.utils.change
import com.arnyminerz.utils.extract
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.junit.Test

class CollectionUtilsTest {
    @Test
    fun test_map_extract() {
        val initial = mapOf("first" to "value", "second" to "other")
        assertEquals("value", initial["first"])
        assertEquals("other", initial["second"])
        val extracted = initial.extract("second")
        assertEquals("value", extracted["first"])
        assertNull(extracted["second"])
    }

    @Test
    fun test_map_replace() {
        val initial = mapOf("first" to "value", "second" to "other")
        assertEquals("value", initial["first"])
        assertEquals("other", initial["second"])
        val extracted = initial.change("second", "changed")
        assertEquals("value", extracted["first"])
        assertEquals("changed", extracted["second"])
    }
}