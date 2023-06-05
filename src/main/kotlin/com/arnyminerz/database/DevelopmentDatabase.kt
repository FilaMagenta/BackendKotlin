package com.arnyminerz.database

object DevelopmentDatabase: ServerDatabase(path = "database.db") {
    object Instance: ServerDatabaseCompanion<DevelopmentDatabase>(DevelopmentDatabase)
}
