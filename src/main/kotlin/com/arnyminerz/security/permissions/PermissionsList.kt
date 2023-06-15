package com.arnyminerz.security.permissions

import kotlin.reflect.javaType

abstract class PermissionsList {
    /**
     * Uses a bit of reflection magic to collect all the permissions inside the list.
     */
    @Suppress("VariableNaming", "PropertyName")
    @OptIn(ExperimentalStdlibApi::class)
    val All: Array<Permission>
        get() = this::class.nestedClasses
            .flatMap { nested ->
                val types = nested.supertypes.map { it.javaType.typeName }
                if (types.contains(Permission::class.qualifiedName)) {
                    listOf(nested.objectInstance as Permission)
                } else if (types.contains(PermissionsList::class.qualifiedName)) {
                    (nested.objectInstance as PermissionsList).All.toList()
                } else {
                    emptyList()
                }
            }
            .toTypedArray()
}
