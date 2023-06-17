package com.arnyminerz.database

object DevelopmentDatabase : ServerDatabase(host = "database.db", mode = DatabaseMode.FILE) {
    object Instance : ServerDatabaseCompanion<DevelopmentDatabase>(DevelopmentDatabase)
}
