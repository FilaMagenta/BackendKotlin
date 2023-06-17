package com.arnyminerz.database

object ProductionDatabase : ServerDatabase(path = "database.db") {
    object Instance : ServerDatabaseCompanion<ProductionDatabase>(ProductionDatabase)
}
