package com.arnyminerz.database.dsl

object DSLConst {
    @Deprecated("Size should not be specified. Use timestamp.")
    const val DATE_LENGTH = 64

    const val EMAIL_LENGTH = 256

    const val PERSON_NAME_LENGTH = 128
    const val PERSON_SURNAME_LENGTH = PERSON_NAME_LENGTH * 2

    const val NIF_LENGTH = 9

    const val SIGNATURE_LENGTH = 1024 * 8
    const val EXTERNAL_REFERENCE_LENGTH = 2048
    const val UUID_V4_LENGTH = 36
}
