package com.arnyminerz.database.`interface`

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.dsl.Users
import com.arnyminerz.database.entity.User
import com.arnyminerz.database.types.UserType
import com.arnyminerz.security.Passwords
import java.util.Base64

class UsersInterface(database: ServerDatabase) : DataObjectInterface<UserType, User, User.Companion>(database, User.Companion) {
    suspend fun <Result> findWithNif(nif: String, block: suspend (user: User?) -> Result) = database.transaction {
        val user = User.find { Users.nif eq nif }.singleOrNull()
        block(user)
    }

    override fun User.processExtras(extras: Map<String, String>) {
        val password = extras.getValue("password")

        val salt = Passwords.generateSalt()
        this.passwordSalt = Base64.getMimeEncoder().encodeToString(salt)
        this.passwordHash = Base64.getMimeEncoder().encodeToString(Passwords.hash(password, salt))
    }
}
