package com.arnyminerz.database

object DevelopmentDatabase : ServerDatabase(host = "database.db") {
    object Instance : ServerDatabaseCompanion<DevelopmentDatabase>(DevelopmentDatabase)
}
