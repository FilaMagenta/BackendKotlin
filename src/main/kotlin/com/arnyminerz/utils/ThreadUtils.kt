package com.arnyminerz.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <Result> io(crossinline block: suspend CoroutineScope.() -> Result): Result =
    withContext(Dispatchers.IO) { block() }
