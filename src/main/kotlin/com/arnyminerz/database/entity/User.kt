package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.Users
import com.arnyminerz.database.types.UserType
import java.util.Base64
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.json.JSONObject

class User(id: EntityID<Int>): DataEntity<UserType>(id) {
    companion object: IntEntityClass<User>(Users)

    var nif by Users.nif
    var name by Users.name
    var surname by Users.surname
    var passwordHash by Users.passwordHash
    var passwordSalt by Users.passwordSalt
    var email by Users.email
    var birthday by Users.birthday

    fun passwordHash(): ByteArray = Base64.getMimeDecoder().decode(passwordHash)

    fun passwordSalt(): ByteArray = Base64.getMimeDecoder().decode(passwordSalt)

    override fun fill(type: UserType) {
        this.nif = type.nif
        this.name = type.name
        this.surname = type.surname
        this.email = type.email
        this.birthday = type.birthday
    }

    override fun toJSON(): JSONObject = JSONObject().apply {
        put("nif", nif)
        put("name", name)
        put("surname", surname)
        put("email", email)
        put("birthday", birthday)
    }
}
