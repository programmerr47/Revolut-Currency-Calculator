package com.github.programmerr47.currencycalculator.services

import com.github.programmerr47.currencycalculator.net.ServerApi
import com.github.programmerr47.currencycalculator.util.subscribeCancelable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

class DisposableTracker(
        private val starter: () -> Disposable
) : Tracker {
    private var disposable: Disposable? = null

    override fun startTracking() {
        disposable = starter()
    }

    override fun stopTracking() {
        disposable?.let {
            if (!it.isDisposed)
                it.dispose()
        }
        disposable = null
    }
}

interface LatestCurrencyTracker : Emitter<Map<String, BigDecimal>>, Tracker

class LatestCurrencyTrackingService(
        private val serverApi: ServerApi
) : LatestCurrencyTracker {
    private val tracker: Tracker by lazy { createDisposableTracker() }

    private var latestSubject: BehaviorSubject<Map<String, BigDecimal>> = BehaviorSubject.createDefault(mapOf())

    override fun startTracking() = tracker.startTracking()

    override fun stopTracking() = tracker.stopTracking()

    private fun createDisposableTracker() = DisposableTracker {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .flatMapSingle {
                    serverApi.getLatest().map { it.rates }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeCancelable(latestSubject)
    }

    override fun observe() = latestSubject.hide()
}