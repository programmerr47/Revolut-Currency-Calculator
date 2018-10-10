package com.github.programmerr47.currencycalculator.services

import com.github.programmerr47.currencycalculator.db.CurrencyDao
import com.github.programmerr47.currencycalculator.db.getAllAsMap
import io.reactivex.Scheduler

class CurrencyService(
        private val latestCurrencyTracker: LatestCurrencyTracker,
        private val currencyDao: CurrencyDao,
        private val accepter: CurrencyRatesAccepter,
        private val netTracker: NetworkChangeTracker,
        private val ioScheduler: Scheduler
) {

    fun startCurrencyTracking() = netTracker.observe()
            .switchMap {
                if (it) latestCurrencyTracker.run {
                    startTracking()
                    observe()
                }
                else currencyDao.getAllAsMap()
                        .toObservable()
                        .subscribeOn(ioScheduler)
            }
            .doOnNext { accepter.setNewRates(it) }
            .subscribe()
}