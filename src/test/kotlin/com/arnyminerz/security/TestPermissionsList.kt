package com.arnyminerz.security

import com.arnyminerz.filamagenta.commons.data.security.permissions.TestPermissions
import kotlin.test.assertContentEquals
import org.junit.Test

class TestPermissionsList {
    @Test
    fun `test all`() {
        assertContentEquals(
            arrayOf(TestPermissions.Nested.NestedPermission, TestPermissions.Test),
            TestPermissions.All,
        )
    }
}
