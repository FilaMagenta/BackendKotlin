package com.arnyminerz.utils.validation

import java.util.regex.Pattern

private val EmailPattern = Pattern.compile(
    "[a-zA-Z0-9+._%\\-]{1,256}" +
            "@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)

val String.isValidEmail: Boolean
    get() = isNotBlank() && EmailPattern.matcher(this).matches()
