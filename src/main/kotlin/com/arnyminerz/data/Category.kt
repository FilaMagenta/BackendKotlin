package com.arnyminerz.data

enum class Category {
    UNKNOWN, ALEVI, INFANTIL, JUVENIL, SIT_ESPECIAL, FESTER, JUBILAT, JUBILAT_PLUS, COL_FESTER, COL_PROTECTOR,
    COL_FAMILIAR;

    companion object {
        /**
         * The maximum length allowed as the name of a Rank.
         */
        const val NAME_MAX_LENGTH = 16
    }
}
