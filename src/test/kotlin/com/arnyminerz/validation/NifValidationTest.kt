package com.arnyminerz.validation

import com.arnyminerz.utils.validation.isValidDni
import com.arnyminerz.utils.validation.isValidNie
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class NifValidationTest {
    @Test
    fun test_isValidDni() {
        assertFalse("".isValidDni)
        assertFalse("12345678".isValidDni)
        assertFalse("1234567A".isValidDni)
        assertFalse("123456789A".isValidDni)
        assertFalse("12345678A".isValidDni)
        assertTrue("12345678Z".isValidDni)
    }

    @Test
    fun test_isValidNie() {
        assertFalse("".isValidNie)
        assertFalse("1234567".isValidNie)
        assertFalse("123456A".isValidNie)
        assertFalse("12345678A".isValidNie)
        assertFalse("1234567A".isValidNie)
        assertTrue("1234567L".isValidNie)
    }
}
