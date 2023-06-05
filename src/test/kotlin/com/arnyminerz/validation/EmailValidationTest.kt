package com.arnyminerz.validation

import com.arnyminerz.utils.validation.isValidEmail
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Test

class EmailValidationTest {
    @Test
    fun test_isValidEmail() {
        assertFalse("".isValidEmail)
        assertFalse("invalid".isValidEmail)
        assertFalse("email@test".isValidEmail)
        assertTrue("email@test.cc".isValidEmail)
    }
}