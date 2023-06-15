package com.arnyminerz.security.permissions

object TestPermissions : PermissionsList() {
    object Test : Permission()

    object Nested : PermissionsList() {
        object NestedPermission : Permission()
    }
}
