package com.arnyminerz.database.types

import org.json.JSONObject

data class UserType(
    val nif: String,
    val name: String,
    val surname: String,
    val email: String,
    val birthday: String? = null
): DataType {
    override fun toJSON(): JSONObject = JSONObject().apply {
        put("nif", nif)
        put("name", name)
        put("surname", surname)
        put("email", email)
        put("birthday", birthday)
    }

    override fun toString(): String = toJSON().toString()
}
