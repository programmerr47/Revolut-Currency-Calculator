package com.github.programmerr47.currencycalculator.util

import java.math.BigDecimal

val BigDecimal.intDigitsCount
    get() = if (signum() == 0) 1 else precision() - scale()