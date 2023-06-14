package com.arnyminerz.security.permissions

object Permissions: PermissionsList() {
    object Inventory: PermissionsList() {
        object Create: Permission()
    }

    object Events: PermissionsList() {
        object Create: Permission()
    }

    object Transactions: PermissionsList() {
        object Create: Permission()
    }
}