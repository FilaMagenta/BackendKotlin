package com.arnyminerz.utils.validation


private const val dniLetters = "TRWAGMYFPDXBNJZSQVHLCKE"
private const val nieLetters = "XYZAGMYFPDXBNJZSQVHLCKE"

val String.dniLetter: Char
    get() {
        // 12345678A
        val num = substring(0, 8).toIntOrNull() ?: return '\u0000'
        val mod = num % 23
        return dniLetters[mod]
    }

val String.nieLetter: Char
    get() {
        // 1234567Z
        val num = substring(0, 7).toIntOrNull() ?: return '\u0000'
        val mod = num % 23
        return nieLetters[mod]
    }

val String.isValidDni: Boolean
    get() = length == 9 && dniLetter == get(8)

val String.isValidNie: Boolean
    get() = length == 8 && nieLetter == get(7)
