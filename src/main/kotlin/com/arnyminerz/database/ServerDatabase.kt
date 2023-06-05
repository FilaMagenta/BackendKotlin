package com.arnyminerz.database

import com.arnyminerz.database.dao.User
import com.arnyminerz.database.dsl.Users
import com.arnyminerz.security.Passwords
import java.io.File
import java.sql.DriverManager
import java.util.Base64
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

abstract class ServerDatabase(
    databaseType: String = "sqlite",
    protected val path: String = ":memory:",
    driver: String? = null,
    options: Map<String, String> = emptyMap()
) {
    companion object {
        lateinit var instance: ServerDatabase

        /**
         * Gets the [instance] cast as the desired database type.
         * @throws ClassCastException If [instance] is not [T]
         */
        @Suppress("UNCHECKED_CAST")
        fun <T: ServerDatabase> instance() = instance as T
    }

    open class ServerDatabaseCompanion<T: ServerDatabase>(private val database: T) {
        /**
         * Sets the [ServerDatabase.instance] to this one.
         */
        fun set() {
            instance = database
        }
    }

    private val databaseOptions = options.takeIf { it.isNotEmpty() }
        ?.map { (k, v) -> "$k=$v" }
        ?.joinToString(";")

    private val databaseUrl = "jdbc:$databaseType:$path" + (databaseOptions?.let { ";$it" } ?: "")

    private val databaseDriver = driver ?: DriverManager.getDriver(databaseUrl)::class.java.name

    val database: Database = Database.connect(databaseUrl, databaseDriver)

    init {
        runBlocking { init() }
    }

    protected suspend fun init() {
        println("Initializing database ($databaseUrl)...")
        transaction {
            println("Adding logger...")
            addLogger(StdOutSqlLogger)

            println("Creating Users schema...")
            SchemaUtils.create(Users)
        }
    }

    private suspend fun <Result> transaction(statement: suspend Transaction.() -> Result): Result = transaction(database) {
        runBlocking { statement() }
    }

    open fun dispose() { }

    suspend fun <Result> getAllUsers(block: (users: SizedIterable<User>) -> Result) = transaction {
        User.all().let(block)
    }

    suspend fun <Result> getUserWithNif(nif: String, block: suspend (user: User?) -> Result) = transaction {
        val user = User.find { Users.nif eq nif }.singleOrNull()
        block(user)
    }

    suspend fun newUser(nif: String, name: String, surname: String, email: String, password: String) = transaction {
        User.new {
            this.nif = nif
            this.name = name
            this.surname = surname
            this.email = email

            val salt = Passwords.generateSalt()
            this.passwordSalt = Base64.getMimeEncoder().encodeToString(salt)
            this.passwordHash = Base64.getMimeEncoder().encodeToString(Passwords.hash(password, salt))
        }
    }
}
