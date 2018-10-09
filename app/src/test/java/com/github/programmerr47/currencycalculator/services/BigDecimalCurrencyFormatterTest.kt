package com.github.programmerr47.currencycalculator.services

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.math.BigDecimal

@RunWith(Parameterized::class)
class BigDecimalCurrencyFromStrFormatterTest(
        val representation: String,
        val expected: BigDecimal
) {
    private val formatter = BigDecimalCurrencyFormatter

    @Test
    fun checkConvertionFromDecimalStr() {
        assertEquals(expected, formatter.fromStr(representation))
    }

    companion object {
        @JvmStatic
        @Parameters
        fun data(): Collection<Array<Any>> = listOf(
                ".34" to BigDecimal.valueOf(34, 2),
                "432" to BigDecimal.valueOf(432),
                "0.53" to BigDecimal.valueOf(53, 2),
                "." to BigDecimal.ZERO,
                "" to BigDecimal.ZERO,
                "fsfsdf" to BigDecimal.ZERO
        ).map { arrayOf(it.first, it.second) }
    }
}

@RunWith(Parameterized::class)
class BigDecimalCurrencyToStrFormatterTest(
        val currency: BigDecimal,
        val expected: String
) {
    private val formatter = BigDecimalCurrencyFormatter

    @Test
    fun checkConvertionFromDecimalStr() {
        assertEquals(expected, formatter.toStr(currency))
    }

    companion object {
        @JvmStatic
        @Parameters
        fun data(): Collection<Array<Any>> = listOf(
                BigDecimal.valueOf(34, 2) to "0.34",
                BigDecimal.valueOf(34, 20) to "0",
                BigDecimal.valueOf(432) to "432",
                BigDecimal.valueOf(987654321, 5) to "9876.54",
                BigDecimal.valueOf(987654321, 4) to "98765"
        ).map { arrayOf(it.first, it.second) }
    }
}