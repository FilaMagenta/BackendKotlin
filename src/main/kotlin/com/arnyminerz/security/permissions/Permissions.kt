package com.arnyminerz.security.permissions

object Permissions : PermissionsList() {
    object Usage : Permission()

    object Inventory : PermissionsList() {
        object Create : Permission()
    }

    object Events : PermissionsList() {
        object Create : Permission()
    }

    object Transactions : PermissionsList() {
        object Create : Permission()
    }

    object Users : PermissionsList() {
        object ChangeCategory : Permission()

        object UpdateCategoryInfo : Permission()

        object List : Permission()
    }
}
