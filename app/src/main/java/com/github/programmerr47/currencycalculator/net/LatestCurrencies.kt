package com.github.programmerr47.currencycalculator.net

import java.math.BigDecimal

data class LatestCurrencies(
        val base: String,
        val date: String,
        val rates: Map<String, BigDecimal>
)