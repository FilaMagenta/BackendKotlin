package com.arnyminerz.database.types

data class UserType(
    val nif: String,
    val name: String,
    val surname: String,
    val email: String,
    val birthday: String? = null
): DataType
