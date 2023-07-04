package com.arnyminerz.security.permissions

@Suppress("SpreadOperator")
enum class Role(vararg val permissions: Permission) {
    // IMPORTANT! MAX LENGTH: 10

    /**
     * The default permission, doesn't allow to use the API.
     */
    DEFAULT,

    /**
     * Members are basic users, with usage permissions. They are considered as "confirmed" users.
     */
    MEMBER(Permissions.Usage),

    /**
     * Administrators have all the permissions granted.
     */
    ADMIN(
        *Permissions.All
    );

    fun hasPermission(permission: Permission) = permissions.contains(permission)
}
