package com.github.programmerr47.currencycalculator.services

import io.reactivex.Observable

class CurrencyService(
        private val latestCurrencyTracker: LatestCurrencyTracker,
        private val accepter: CurrencyRatesAccepter,
        private val netTracker: NetworkChangeTracker
) {

    fun startCurrencyTracking() = netTracker.observe()
            .switchMap {
                if (it) latestCurrencyTracker.run {
                    restart()
                    observe()
                }
                else Observable.just(mapOf())
            }
            .doOnNext { accepter.setNewRates(it) }
            .subscribe()
}