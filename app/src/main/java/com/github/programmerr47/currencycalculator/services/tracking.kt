package com.github.programmerr47.currencycalculator.services

import android.util.Log
import com.github.programmerr47.currencycalculator.net.ServerApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.math.BigDecimal
import java.util.concurrent.TimeUnit.*

interface LatestCurrencyTracker : Emitter<Map<String, BigDecimal>>, Tracker {
    fun restart()
}

class LatestCurrencyTrackingService(
        private val serverApi: ServerApi
) : LatestCurrencyTracker {
    private var timerDisposable: Disposable? = null

    private var latestSubject: BehaviorSubject<Map<String, BigDecimal>> = BehaviorSubject.createDefault(mapOf())

    override fun startTracking() {
        timerDisposable = Observable.interval(0, 1, SECONDS)
                .flatMapSingle {
                    serverApi.getLatest().map { it.rates }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn {
                    latestSubject.value
                }
                .subscribe{ latestSubject.onNext(it) }
    }

    override fun stopTracking() {
        timerDisposable?.let {
            if (!it.isDisposed)
                it.dispose()
        }
        timerDisposable = null
    }

    override fun restart() {
        stopTracking()
        startTracking()
    }

    override fun observe() = latestSubject.hide().distinctUntilChanged()
}