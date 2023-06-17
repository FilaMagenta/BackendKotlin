package com.arnyminerz.database

import com.arnyminerz.utils.getenv

private const val DEFAULT_DATABASE_TYPE = "sqlite"
private const val DEFAULT_DATABASE_HOST = "database.db"

private const val DATABASE_POSTGRE = "postgresql"
private const val DATABASE_SQLITE = "sqlite"

private val DatabaseTypes = mapOf(
    DATABASE_POSTGRE to ServerDatabase.DatabaseMode.REMOTE,
    DATABASE_SQLITE to ServerDatabase.DatabaseMode.FILE
)

private val DATABASE_TYPE = "DATABASE_TYPE" to DEFAULT_DATABASE_TYPE
private val DATABASE_HOST = "DATABASE_HOST" to DEFAULT_DATABASE_HOST

object ProductionDatabase : ServerDatabase(
    databaseType = getenv(DATABASE_TYPE),
    host = getenv(DATABASE_HOST),
    mode = DatabaseTypes.getValue(getenv(DATABASE_TYPE)),
    arguments = mutableMapOf<String, String>().apply {
        System.getenv("DATABASE_USER")?.let { set("user", it) }
        System.getenv("DATABASE_PASS")?.let { set("password", it) }
        System.getenv("DATABASE_SSL")?.let { set("ssl", it) }
    }
) {
    object Instance : ServerDatabaseCompanion<ProductionDatabase>(ProductionDatabase) {
        init {
            val databaseType = getenv(DATABASE_TYPE)
            require(DatabaseTypes.contains(databaseType)) {
                "Got an invalid database type (DATABASE_TYPE): $databaseType"
            }
        }
    }
}
