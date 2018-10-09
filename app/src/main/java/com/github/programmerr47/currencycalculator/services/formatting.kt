package com.github.programmerr47.currencycalculator.services

import com.github.programmerr47.currencycalculator.util.intDigitsCount
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.text.DecimalFormat

private val DEF_DECIMAL_FORMAT = DecimalFormat("#0.##")
private val BIG_DECIMAL_FORMAT = DecimalFormat("#0")

interface CurrencyFormatter<T> {
    fun fromStr(representation: String): T
    fun toStr(currency: T): String
}

object BigDecimalCurrencyFormatter : CurrencyFormatter<BigDecimal> {
    override fun fromStr(representation: String) = try {
        BigDecimal(representation)
    } catch (e: NumberFormatException) {
        ZERO
    }

    override fun toStr(currency: BigDecimal): String {
        val formatter = if (currency.intDigitsCount > 4)
            BIG_DECIMAL_FORMAT
        else
            DEF_DECIMAL_FORMAT

        return formatter.format(currency)
    }
}