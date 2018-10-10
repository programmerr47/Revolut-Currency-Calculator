package com.github.programmerr47.currencycalculator.services

import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class DisposableLifecycleHolder : LifecycleObserver {
    private var disposables: CompositeDisposable? = null

    @OnLifecycleEvent(ON_CREATE)
    fun prepare() {
        disposables = CompositeDisposable()
    }

    @OnLifecycleEvent(ON_DESTROY)
    fun stop() {
        disposables?.let {
            if (!it.isDisposed)
                it.dispose()
        }
        disposables = null
    }

    fun add(disposable: Disposable) = disposables?.add(disposable)
}