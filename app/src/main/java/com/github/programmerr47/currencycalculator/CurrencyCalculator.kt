package com.github.programmerr47.currencycalculator

import androidx.lifecycle.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.util.concurrent.TimeUnit

interface CurrencyListEmitter {
    fun observe(): Observable<List<CurrencyItem>>
}

interface CurrencyEvaluator : CurrencyListEmitter {
    fun pushOnTop(currencyType: String)
    fun acceptNew(currencyType: String, value: BigDecimal)
}

class CurrencyCalculator(
        private val serverApi: ServerApi,
        private val listener: (List<CurrencyItem>) -> Unit,
        private val errorListener: (Throwable) -> Unit
) : LifecycleObserver, CurrencyEvaluator {
    private var currencyOrderedTypes: List<String> = listOf()
    private var currencyFactors: Map<String, BigDecimal> = mapOf()
    private var currencyListSubject: BehaviorSubject<List<CurrencyItem>> = BehaviorSubject.createDefault(listOf())

    private var currentCurrencyType: String = BASE_CURRENCY
    private var currentCurrencyValue: BigDecimal = BigDecimal.valueOf(100)

    private var timerDisposable: Disposable? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startTracking() {
        timerDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .flatMapSingle {
                    serverApi.getLatest().map { latest ->
                        if (currencyOrderedTypes.isEmpty()) {
                            currencyOrderedTypes = latest.rates.keys.toMutableList().apply {
                                add(0, BASE_CURRENCY)
                            }
                        }

                        currencyFactors = latest.rates + (BASE_CURRENCY to ONE)
                        generateCurrencyList(currencyOrderedTypes, currencyFactors)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { currencyListSubject.onNext(it) },
                        { errorListener(it) }
                )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopTracking() {
        timerDisposable?.let {
            if (!it.isDisposed)
                it.dispose()
        }
        timerDisposable = null
    }

    override fun pushOnTop(currencyType: String) {
        currencyOrderedTypes = currencyOrderedTypes.move(currencyType, 0)

        currentCurrencyValue = calculateValue(currencyFactors[currencyType]!!, currencyFactors[currentCurrencyType]!!, currentCurrencyValue)
        currentCurrencyType = currencyType

        currencyListSubject.onNext(generateCurrencyList(currencyOrderedTypes, currencyFactors))
    }

    override fun acceptNew(currencyType: String, value: BigDecimal) {
        //todo
    }

    override fun observe() = currencyListSubject.hide()

    private fun generateCurrencyList(order: List<String>, factors: Map<String, BigDecimal>) =
            order.mapFiltered(factors) { type, factor ->
                CurrencyItem(type, calculateValue(factor, factors[currentCurrencyType]!!, currentCurrencyValue))
            }

    private fun calculateValue(targetFactor: BigDecimal, currentFactor: BigDecimal, currentValue: BigDecimal) =
            currentValue / currentFactor * targetFactor
}