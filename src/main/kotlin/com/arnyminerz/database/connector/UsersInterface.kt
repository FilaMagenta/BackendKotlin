package com.arnyminerz.database.connector

import com.arnyminerz.data.Category
import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.dsl.Users
import com.arnyminerz.database.entity.User
import com.arnyminerz.database.entity.UserCategory
import com.arnyminerz.database.types.UserType
import com.arnyminerz.security.Passwords
import java.util.Base64

class UsersInterface(database: ServerDatabase) : DataObjectInterface<UserType, User, User.Companion>(
    database,
    User.Companion
) {
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

    suspend fun getCategory(user: User): Category =
        database
            .transaction {
                UserCategory.all()
                    .filter { it.user.id == user.id }
                    .maxByOrNull { it.timestamp }
            }
            ?.let { cat -> Category.values().find { it.name == cat.category } }
            ?: Category.UNKNOWN

    suspend fun setCategory(user: User, category: Category) = database.transaction {
        UserCategory.new {
            this.timestamp = System.currentTimeMillis()
            this.category = category.name
            this.user = user
        }
    }
}
