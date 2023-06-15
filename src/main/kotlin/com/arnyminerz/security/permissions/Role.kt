package com.arnyminerz.security.permissions

@Suppress("SpreadOperator")
enum class Role(vararg val permissions: Permission) {
    // IMPORTANT! MAX LENGTH: 10
    DEFAULT,
    ADMIN(
        *Permissions.All
    );

    fun hasPermission(permission: Permission) = permissions.contains(permission)
}
