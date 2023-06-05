package com.arnyminerz.database

import com.arnyminerz.database.dsl.Users
import com.arnyminerz.database.entity.DataEntity
import com.arnyminerz.database.`interface`.UsersInterface
import com.arnyminerz.database.types.DataType
import java.sql.DriverManager
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.IntEntityClass
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

    suspend fun <Result> transaction(statement: suspend Transaction.() -> Result): Result = transaction(database) {
        runBlocking { statement() }
    }

    open fun dispose() { }

    val usersInterface = UsersInterface(this)


    abstract class DataObjectInterface <T: DataType, E: DataEntity<T>, EClass: IntEntityClass<E>> (
        protected val database: ServerDatabase,
        private val entityClass: EClass
    ) {
        suspend fun <Result> getAll(block: (users: SizedIterable<E>) -> Result) = database.transaction {
            entityClass.all().let(block)
        }

        protected abstract fun E.processExtras(extras: Map<String, String>)

        suspend fun new(type: T, vararg extras: Pair<String, String>) = database.transaction {
            entityClass.new {
                fill(type)
                processExtras(extras.toMap())
            }
        }
    }
}
