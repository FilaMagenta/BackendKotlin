package com.arnyminerz.database.types

import org.json.JSONObject

data class UserType(
    val nif: String,
    val role: Role,
    val name: String,
    val surname: String,
    val email: String,
    val birthday: String? = null
): DataType {
    enum class Role(val code: Int) {
        DEFAULT(0)
    }

    override fun toJSON(): JSONObject = JSONObject().apply {
        put("nif", nif)
        put("role", role.code)
        put("name", name)
        put("surname", surname)
        put("email", email)
        put("birthday", birthday)
    }

    override fun toString(): String = toJSON().toString()
}
