package com.arnyminerz.payments

import com.arnyminerz.payments.redsys.RedsysCurrency
import kotlin.test.assertEquals
import org.junit.Test

class TestRedsysCurrency {
    @Test
    fun `test RedsysCurrency shortCode`() {
        assertEquals("USD", RedsysCurrency.DOLAR_USA_USD.shortCode)
    }

    @Test
    fun `test RedsysCurrency displayName`() {
        assertEquals("Dolar USA", RedsysCurrency.DOLAR_USA_USD.displayName)
    }
}
