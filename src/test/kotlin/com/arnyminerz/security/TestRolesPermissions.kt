package com.arnyminerz.security

import com.arnyminerz.security.permissions.Permissions
import com.arnyminerz.security.permissions.Role
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Test

class TestRolesPermissions {
    @Test
    fun `test role permissions - DEFAULT`() {
        val permissions = Permissions.All
        for (permission in permissions)
            assertFalse(Role.DEFAULT.hasPermission(permission))
    }

    @Test
    fun `test role permissions - ADMIN`() {
        val permissions = Permissions.All
        for (permission in permissions)
            assertTrue(Role.ADMIN.hasPermission(permission))
    }
}