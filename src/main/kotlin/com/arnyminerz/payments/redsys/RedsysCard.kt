package com.arnyminerz.payments.redsys

import es.redsys.rest.api.model.message.RestOperationMessage
import java.time.LocalDate

data class RedsysCard(
    val number: String,
    val expiryMonth: Byte,
    val expiryYear: Byte,
    val cvv: Short
) {
    companion object {
        private const val CVV_MAX = 999
        private const val CVV_MIN = 0

        private const val EXPIRY_MONTH_MIN = 1
        private const val EXPIRY_MONTH_MAX = 12

        val EXPIRY_YEAR_MIN: Byte by lazy {
            LocalDate.now()
                // Get current year
                .year
                // Convert to string to get last 2 digits
                .toString()
                // Get last 2 digits
                .substring(2)
                // Convert to Byte
                .toInt()
                .toByte()
        }
        val EXPIRY_YEAR_MAX: Byte by lazy {
            (EXPIRY_YEAR_MIN + 9).toByte()
        }
    }

    constructor(number: String, expiryDate: String, cvv: String) : this(
        number,
        expiryDate.substring(0, 2).toByte(),
        expiryDate.substring(2, 4).toByte(),
        cvv.toShort()
    )

    init {
        require(cvv <= CVV_MAX) { "CVV cannot be greater than $CVV_MAX" }
        require(cvv >= CVV_MIN) { "CVV cannot be lower than $CVV_MIN" }

        require(expiryMonth <= EXPIRY_MONTH_MAX) { "Expiry month cannot be greater than $EXPIRY_MONTH_MAX" }
        require(expiryMonth >= EXPIRY_MONTH_MIN) { "Expiry month cannot be lower than $EXPIRY_MONTH_MIN" }

        // require(expiryYear <= EXPIRY_YEAR_MAX) { "Expiry year cannot be greater than $EXPIRY_YEAR_MAX" }
        require(expiryYear >= EXPIRY_YEAR_MIN) { "Expiry year cannot be lower than $EXPIRY_YEAR_MIN" }
    }

    val expiryDate: String = "$expiryYear$expiryMonth"
}

var RestOperationMessage.card: RedsysCard
    /**
     * Returns the set card data converted into [RedsysCard].
     * @throws IllegalArgumentException If any data is not valid.
     * @throws NullPointerException If some parameter is not set.
     */
    get() = RedsysCard(cardNumber, cardExpiryDate, cvv2)
    /**
     * Updates the card data using the contained in the given [RedsysCard]
     */
    set(value) {
        cardNumber = value.number
        cardExpiryDate = value.expiryDate
        cvv2 = value.cvv.toString()
    }
