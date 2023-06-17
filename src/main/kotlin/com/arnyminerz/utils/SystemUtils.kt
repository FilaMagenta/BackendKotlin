package com.arnyminerz.utils

/**
 * Uses [System.getenv] to get the environment variable keyed as the first element of [pair]. If not present, returns
 * [Pair.second] of [pair].
 */
fun getenv(pair: Pair<String, String>): String = System.getenv(pair.first) ?: pair.second
