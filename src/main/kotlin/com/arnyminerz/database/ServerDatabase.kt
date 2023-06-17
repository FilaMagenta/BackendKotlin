package com.arnyminerz.database

import com.arnyminerz.database.connector.EventsInterface
import com.arnyminerz.database.connector.InventoryInterface
import com.arnyminerz.database.connector.TransactionsInterface
import com.arnyminerz.database.connector.UsersInterface
import com.arnyminerz.database.dsl.CategoryInformations
import com.arnyminerz.database.dsl.EventTables
import com.arnyminerz.database.dsl.Events
import com.arnyminerz.database.dsl.InventoryItems
import com.arnyminerz.database.dsl.TableGuests
import com.arnyminerz.database.dsl.TableMembers
import com.arnyminerz.database.dsl.Transactions
import com.arnyminerz.database.dsl.UserAssistances
import com.arnyminerz.database.dsl.UserCategories
import com.arnyminerz.database.dsl.Users
import java.net.URLEncoder
import java.sql.DriverManager
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.flushCache
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

abstract class ServerDatabase(
    private val databaseType: String = "sqlite",
    private val mode: DatabaseMode = DatabaseMode.MEMORY,
    protected val host: String = ":memory:",
    driver: String? = null,
    arguments: Map<String, String> = emptyMap(),
    options: Map<String, String> = emptyMap()
) {
    companion object {
        private val entityClasses = arrayOf(
            CategoryInformations,
            Events,
            EventTables,
            InventoryItems,
            TableGuests,
            TableMembers,
            Transactions,
            UserAssistances,
            UserCategories,
            Users
        )

        lateinit var instance: ServerDatabase

        /**
         * Gets the [instance] cast as the desired database type.
         * @throws ClassCastException If [instance] is not [T]
         */
        @Suppress("UNCHECKED_CAST")
        fun <T : ServerDatabase> instance() = instance as T
    }

    enum class DatabaseMode { MEMORY, FILE, REMOTE }

    open class ServerDatabaseCompanion<T : ServerDatabase>(private val database: T) {
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

    private val queryArguments = arguments.takeIf { it.isNotEmpty() }
        ?.map { (k, v) -> URLEncoder.encode(k, Charsets.UTF_8) to URLEncoder.encode(v, Charsets.UTF_8) }
        ?.joinToString("&") { (k, v) -> "$k=${URLEncoder.encode(v, Charsets.UTF_8)}" }

    private val databaseUrl: String
        get() {
            val sb = StringBuilder("jdbc:$databaseType:")
            when (mode) {
                DatabaseMode.MEMORY -> sb.append(":memory:")
                DatabaseMode.FILE -> sb.append(host)
                DatabaseMode.REMOTE -> sb.append("//$host")
            }
            queryArguments?.let { sb.append("?$it") }
            databaseOptions?.let { sb.append(";$it") }
            return sb.toString()
        }

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
        println("Target database: $databaseUrl")
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
            for (entity in entityClasses)
                SchemaUtils.create(entity)
        }
    }

    suspend fun <Result> transaction(statement: suspend Transaction.() -> Result): Result = transaction(database) {
        runBlocking {
            statement()
        }
    }

    suspend fun flushCache() = transaction {
        flushCache()
    }

    open fun dispose() {}
}
