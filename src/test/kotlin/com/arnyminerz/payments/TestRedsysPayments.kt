package com.arnyminerz.payments

import com.arnyminerz.payments.redsys.RedsysCard
import org.junit.Before
import org.junit.Test

class TestRedsysPayments {
    companion object {
        const val REST_TESTING = "https://sis-t.redsys.es:25443/sis/rest"

        const val MERCHANT = "999008881"
        const val TERMINAL = "001"
        const val COMMERCE_KEY = "sq7HjrUOBfKmC576ILgskD5srU870gJ7"

        val card = RedsysCard("4548810000000003", "1249", "123")
    }

    @Before
    fun `configure RedSys`() {
        RedSysPayments.RestEndpoint = REST_TESTING
        RedSysPayments.Merchant = MERCHANT
        RedSysPayments.Terminal = TERMINAL
    }

    @Test
    fun `test paying with RedSys`() {
        val response = RedSysPayments.performPayment(10.0, order = "123", card = card)
        println("Response: $response")
    }
}