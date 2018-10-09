package com.github.programmerr47.currencycalculator.services

import com.github.programmerr47.currencycalculator.currencylist.CurrencyItem
import com.github.programmerr47.currencycalculator.net.BASE_CURRENCY
import com.github.programmerr47.currencycalculator.util.mapFiltered
import com.github.programmerr47.currencycalculator.util.move
import io.reactivex.subjects.BehaviorSubject
import java.math.BigDecimal
import java.math.RoundingMode

interface CurrencyEvaluator : Emitter<List<CurrencyItem>> {
    fun setNewType(currencyType: String)
    fun setNewValue(value: BigDecimal)
}

interface CurrencyRatesAccepter : CurrencyEvaluator {
    fun setNewRates(rates: Map<String, BigDecimal>)
}

class CurrencyCalculator(
        private var baseType: String = BASE_CURRENCY,
        private var baseValue: BigDecimal = BigDecimal.valueOf(100)
) : CurrencyRatesAccepter {
    private var orderedTypes: List<String> = listOf()
    private var factors: Map<String, BigDecimal> = mapOf()
    private var currencyListSubject: BehaviorSubject<List<CurrencyItem>> = BehaviorSubject.createDefault(listOf())

    override fun setNewType(currencyType: String) {
        orderedTypes = orderedTypes.move(currencyType, 0)

        baseValue = calculateValue(factors[currencyType]!!, factors[baseType]!!, baseValue)
        baseType = currencyType

        currencyListSubject.onNext(generateCurrencyList(orderedTypes, factors))
    }

    override fun setNewValue(value: BigDecimal) {
        baseValue = value
        currencyListSubject.onNext(generateCurrencyList(orderedTypes, factors))
    }

    override fun setNewRates(rates: Map<String, BigDecimal>) {
        if (orderedTypes.isEmpty() && rates.isNotEmpty()) {
            orderedTypes = rates.keys.toMutableList().apply {
                add(0, BASE_CURRENCY)
            }
        }

        factors = rates + (BASE_CURRENCY to BigDecimal.ONE)
        currencyListSubject.onNext(generateCurrencyList(orderedTypes, factors))
    }

    override fun observe() = currencyListSubject.hide()

    private fun generateCurrencyList(order: List<String>, factors: Map<String, BigDecimal>) =
            order.mapFiltered(factors) { type, factor ->
                CurrencyItem(type, calculateValue(factor, factors[baseType]!!, baseValue))
            }

    private fun calculateValue(targetFactor: BigDecimal, currentFactor: BigDecimal, currentValue: BigDecimal) =
            targetFactor.divide(currentFactor, 10, RoundingMode.HALF_EVEN) * currentValue
}