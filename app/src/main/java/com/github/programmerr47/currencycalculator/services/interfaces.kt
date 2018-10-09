package com.github.programmerr47.currencycalculator.services

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Observable

interface Emitter<T> {
    fun observe(): Observable<T>
}

interface Tracker : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startTracking()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopTracking()
}