package com.arnyminerz.database

import com.arnyminerz.database.dsl.*
import com.arnyminerz.database.`interface`.EventsInterface
import com.arnyminerz.database.`interface`.InventoryInterface
import com.arnyminerz.database.`interface`.TransactionsInterface
import com.arnyminerz.database.`interface`.UsersInterface
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.flushCache
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.DriverManager

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

    lateinit var usersInterface: UsersInterface

    lateinit var eventsInterface: EventsInterface

    lateinit var transactionsInterface: TransactionsInterface

    lateinit var inventoryInterface: InventoryInterface

    init {
        runBlocking { init() }
    }

    protected suspend fun init() {
        println("Initializing interfaces...")
        usersInterface = UsersInterface(this)
        eventsInterface = EventsInterface(this)
        transactionsInterface = TransactionsInterface(this)
        inventoryInterface = InventoryInterface(this)

        println("Initializing database ($databaseUrl)...")
        transaction {
            println("Adding logger...")
            addLogger(StdOutSqlLogger)

            println("Creating required schema...")
            SchemaUtils.create(
                Users, Events, UserAssistances, EventTables, TableMembers, TableGuests, InventoryItems, Transactions
            )
        }
    }

    suspend fun <Result> transaction(statement: suspend Transaction.() -> Result): Result = transaction(database) {
        runBlocking { statement() }
    }

    suspend fun flushCache() = transaction {
        flushCache()
    }

    open fun dispose() { }

}
