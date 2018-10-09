package com.github.programmerr47.currencycalculator.services

class CurrencyService(
        private val latestCurrencyTracker: LatestCurrencyTracker,
        private val accepter: CurrencyRatesAccepter,
        private val errorListener: (Throwable) -> Unit
) : Tracker by latestCurrencyTracker {
    private val tracker: Tracker by lazy { createDisposableTracker() }

    override fun startTracking() {
        tracker.startTracking()
        latestCurrencyTracker.startTracking()
    }

    override fun stopTracking() {
        tracker.stopTracking()
        latestCurrencyTracker.stopTracking()
    }

    private fun createDisposableTracker() = DisposableTracker {
        latestCurrencyTracker.observe()
                .subscribe(
                        { accepter.setNewRates(it) },
                        { errorListener(it) }
                )
    }
}