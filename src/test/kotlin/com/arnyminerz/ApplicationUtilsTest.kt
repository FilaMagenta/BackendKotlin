package com.arnyminerz

import com.arnyminerz.utils.asEnvironmentVariable
import kotlin.test.assertEquals
import org.junit.Test

class ApplicationUtilsTest {
    @Test
    fun test_asEnvironmentVariable() {
        assertEquals("TESTING_VARIABLE_NAME", "testing.variableName".asEnvironmentVariable)
    }
}
