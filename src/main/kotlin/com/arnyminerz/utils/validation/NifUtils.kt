package com.arnyminerz.utils.validation

private const val DNI_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE"
private const val NIE_LETTERS = "XYZAGMYFPDXBNJZSQVHLCKE"

private const val DNI_LENGTH = 9
private const val NIE_LENGTH = 8

val String.dniLetter: Char
    get() {
        // 12345678A
        val num = substring(0, DNI_LENGTH - 1).toIntOrNull() ?: return '\u0000'
        val mod = num % DNI_LETTERS.length
        return DNI_LETTERS[mod]
    }

val String.nieLetter: Char
    get() {
        // 1234567Z
        val num = substring(0, NIE_LENGTH - 1).toIntOrNull() ?: return '\u0000'
        val mod = num % NIE_LETTERS.length
        return NIE_LETTERS[mod]
    }

val String.isValidDni: Boolean
    get() = length == DNI_LENGTH && dniLetter == get(DNI_LENGTH - 1)

val String.isValidNie: Boolean
    get() = length == NIE_LENGTH && nieLetter == get(NIE_LENGTH - 1)
