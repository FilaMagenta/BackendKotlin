package com.arnyminerz.security.permissions

enum class Role(vararg val permissions: Permission) {
    // IMPORTANT! MAX LENGTH: 10
    DEFAULT,
    ADMIN(
        *Permissions.All
    );

    fun hasPermission(permission: Permission) = permissions.contains(permission)
}
