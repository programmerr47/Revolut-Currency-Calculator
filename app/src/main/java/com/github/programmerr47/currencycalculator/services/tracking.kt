package com.github.programmerr47.currencycalculator.services

import com.github.programmerr47.currencycalculator.db.CurrencyDao
import com.github.programmerr47.currencycalculator.db.getAllAsMap
import com.github.programmerr47.currencycalculator.db.insertAll
import com.github.programmerr47.currencycalculator.net.ServerApi
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.math.BigDecimal
import java.util.concurrent.TimeUnit.*

interface LatestCurrencyTracker : Emitter<Map<String, BigDecimal>>, Tracker

class LatestCurrencyTrackingService(
        private val serverApi: ServerApi,
        private val currencyDao: CurrencyDao,
        private val ioScheduler: Scheduler
) : LatestCurrencyTracker {
    private var timerDisposable: Disposable? = null

    private var latestSubject: BehaviorSubject<Map<String, BigDecimal>> = BehaviorSubject.create()

    override fun startTracking() {
        stopTracking()
        timerDisposable = Observable.interval(0, 1, SECONDS)
                .flatMapSingle {
                    serverApi.getLatest()
                            .map { it.rates }
                            .doAfterSuccess { currencyDao.insertAll(it) }
                }
                .onErrorResumeNext { thr: Throwable ->
                    if (latestSubject.hasValue()) Observable.just(latestSubject.value)
                    else currencyDao.getAllAsMap()
                            .toObservable()
                            .subscribeOn(ioScheduler)
                }
                .observeOn(AndroidSchedulers.mainThread()) //todo inject scheduler
                .subscribe{ latestSubject.onNext(it) }
    }

    override fun stopTracking() {
        timerDisposable?.let {
            if (!it.isDisposed)
                it.dispose()
        }
        timerDisposable = null
    }

    override fun observe() = latestSubject.hide().distinctUntilChanged()
}