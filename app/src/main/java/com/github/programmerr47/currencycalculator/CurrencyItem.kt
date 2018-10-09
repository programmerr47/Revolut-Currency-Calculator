package com.github.programmerr47.currencycalculator

import java.math.BigDecimal
import java.text.DecimalFormat

data class CurrencyItem(
        val type: String,
        val value: BigDecimal
) {
    val formattedValue: String by lazy {
        val formatter = if (value.intDigitsCount > 4)
            BIG_DECIMAL_FORMAT
        else
            DEF_DECIMAL_FORMAT

        formatter.format(value)
    }
}

private val DEF_DECIMAL_FORMAT = DecimalFormat("#0.##")
private val BIG_DECIMAL_FORMAT = DecimalFormat("#0")