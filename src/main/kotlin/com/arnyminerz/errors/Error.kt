package com.arnyminerz.errors

import io.ktor.http.HttpStatusCode

data class Error(
    val code: Int,
    val message: String,
    val httpStatusCode: HttpStatusCode
)
