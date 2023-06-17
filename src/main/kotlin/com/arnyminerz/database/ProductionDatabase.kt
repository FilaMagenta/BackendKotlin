package com.arnyminerz.database

private const val DEFAULT_DATABASE_TYPE = "sqlite"
private const val DEFAULT_DATABASE_HOST = "database.db"

object ProductionDatabase : ServerDatabase(
    databaseType = System.getenv("DATABASE_TYPE") ?: DEFAULT_DATABASE_TYPE,
    host = System.getenv("DATABASE_HOST") ?: DEFAULT_DATABASE_HOST,
    arguments = mutableMapOf<String, String>().apply {
        System.getenv("DATABASE_USER")?.let { set("user", it) }
        System.getenv("DATABASE_PASS")?.let { set("password", it) }
        System.getenv("DATABASE_SSL")?.let { set("ssl", it) }
    }
) {
    private val DatabaseTypes = arrayOf("postgresql", "sqlite")

    object Instance : ServerDatabaseCompanion<ProductionDatabase>(ProductionDatabase) {
        init {
            val databaseType = System.getenv("DATABASE_TYPE") ?: DEFAULT_DATABASE_TYPE
            require(DatabaseTypes.contains(databaseType)) {
                "Got an invalid database type (DATABASE_TYPE): $databaseType"
            }
        }
    }
}
