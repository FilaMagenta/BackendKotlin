package com.arnyminerz

import java.util.Properties

object Information {
    private val versionProperties: Properties by lazy {
        val properties = Properties()
        this::class.java.getResourceAsStream("/version.properties").use {
            properties.load(it)
        }
        properties
    }

    /**
     * Provides the version of the backend.
     */
    val version: String by lazy {
        versionProperties.getProperty("version")
    }
}
