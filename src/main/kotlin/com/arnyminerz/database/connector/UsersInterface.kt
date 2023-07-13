package com.arnyminerz.database.connector

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.dsl.CategoryInformations
import com.arnyminerz.database.dsl.Users
import com.arnyminerz.database.entity.CategoryInformation
import com.arnyminerz.database.entity.User
import com.arnyminerz.database.entity.UserCategory
import com.arnyminerz.filamagenta.commons.data.Category
import com.arnyminerz.filamagenta.commons.data.security.Passwords
import com.arnyminerz.filamagenta.commons.data.security.permissions.Role
import com.arnyminerz.filamagenta.commons.data.types.UserType
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
            ?.let { cat -> Category.entries.find { it.name == cat.category } }
            ?: Category.UNKNOWN

    suspend fun setCategory(user: User, category: Category) = database.transaction {
        UserCategory.new {
            this.timestamp = System.currentTimeMillis()
            this.category = category.name
            this.user = user
        }
    }

    suspend fun getCategoryInformation(category: Category): CategoryInformation? =
        database
            .transaction {
                CategoryInformation.all()
                    .filter { it.category == category.name }
                    .maxByOrNull { it.timestamp }
            }

    suspend fun setRole(user: User, role: Role) = database.transaction {
        user.role = role.name
    }

    suspend fun updateCategoryInformation(
        category: Category,
        ageRange: IntRange,
        price: Float,
        rightsData: CategoryInformations.RightsData,
        paysData: CategoryInformations.PaysData
    ) = database.transaction {
        CategoryInformation.new {
            this.timestamp = System.currentTimeMillis()
            this.category = category.name

            this.ageMin = ageRange.first
            this.ageMax = ageRange.last

            this.price = price

            this.votesMeeting = rightsData.votesMeeting

            this.diana = rightsData.diana
            this.diana2 = rightsData.diana2
            this.esquadra = rightsData.esquadra
            this.entrada = rightsData.entrada
            this.processo = rightsData.processo
            this.alardo = rightsData.alardo

            this.paysDina = paysData.paysDina
            this.paysMigAnyAndMusics = paysData.paysMigAnyAndMusics
            this.paysAssaig = paysData.paysAssaig
            this.paysEntradetaOficial = paysData.paysEntradetaOficial
            this.paysEsmorzarGloria = paysData.paysEsmorzarGloria
            this.paysEsmorzarFestes = paysData.paysEsmorzarFestes
            this.paysDinarEntrada = paysData.paysDinarEntrada
            this.paysDinarSantJordi = paysData.paysDinarSantJordi
            this.paysSoparSantJordi = paysData.paysSoparSantJordi
            this.paysDinarTrons = paysData.paysDinarTrons
        }
    }
}
