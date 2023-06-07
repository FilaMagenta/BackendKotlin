package com.arnyminerz.security.permissions

object Permissions: PermissionsList() {
    object Inventory: PermissionsList() {
        object CreateNewItem: Permission()
    }
}