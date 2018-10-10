package com.github.programmerr47.currencycalculator.services

import com.github.programmerr47.currencycalculator.util.bigDecimal
import com.github.programmerr47.currencycalculator.util.intDigitsCount
import java.math.BigDecimal
import java.text.DecimalFormat

private val DEF_DECIMAL_FORMAT = DecimalFormat("#0.##")
private val BIG_DECIMAL_FORMAT = DecimalFormat("#0")

interface CurrencyFormatter<T> {
    fun fromStr(representation: String): T
    fun toStr(currency: T): String
}

object BigDecimalCurrencyFormatter : CurrencyFormatter<BigDecimal> {
    override fun fromStr(representation: String) = bigDecimal(representation)

    override fun toStr(currency: BigDecimal): String {
        val formatter = if (currency.intDigitsCount > 4)
            BIG_DECIMAL_FORMAT
        else
            DEF_DECIMAL_FORMAT

        return formatter.format(currency)
    }
}