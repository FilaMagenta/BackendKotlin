package com.arnyminerz.payments

import com.arnyminerz.payments.redsys.RedsysCard
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFails
import org.junit.Test

class TestRedsysCard {
    @Test
    fun `test RedsysCard expiry`() {
        val thisYearShort = LocalDate.now()
            .year
            .toString()
            .substring(2)
            .toByte()

        println("This year: $thisYearShort")

        assertEquals(23, RedsysCard.EXPIRY_YEAR_MIN)
        assertEquals(32, RedsysCard.EXPIRY_YEAR_MAX)

        assertFails { RedsysCard("", -1, thisYearShort, 100) }
        assertFails { RedsysCard("", 13, thisYearShort, 100) }
        assertFails { RedsysCard("", 10, (thisYearShort - 1).toByte(), 100) }
        assertFails { RedsysCard("", 10, (thisYearShort + 10).toByte(), 100) }

        RedsysCard("", 10, thisYearShort, 100)
    }

    @Test
    fun `test RedsysCard CVV`() {
        // CVV is checked first, so expiry doesn't matter here
        assertFails { RedsysCard("", 0, 0, -1) }
        assertFails { RedsysCard("", 0, 0, 1000) }
    }
}
