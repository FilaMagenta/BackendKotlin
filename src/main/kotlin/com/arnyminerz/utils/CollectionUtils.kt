package com.arnyminerz.utils

/**
 * Returns the initial map without the given key.
 */
fun <K, V> Map<K, V>.extract(key: K): Map<K, V> = filter { (k, _) -> k != key }

/**
 * Returns the initial map replacing the given key by the desired value.
 */
fun <K, V> Map<K, V>.change(key: K, value: V): Map<K, V> = mapValues { (k, v) -> value.takeIf { k == key } ?: v }
