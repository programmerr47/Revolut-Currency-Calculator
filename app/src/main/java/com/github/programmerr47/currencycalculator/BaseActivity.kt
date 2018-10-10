package com.github.programmerr47.currencycalculator

import androidx.appcompat.app.AppCompatActivity
import com.github.programmerr47.currencycalculator.services.DisposableLifecycleHolder
import io.reactivex.disposables.Disposable


abstract class BaseActivity : AppCompatActivity() {
    private val disposableTracker = DisposableLifecycleHolder()

    init {
        lifecycle.addObserver(disposableTracker)
    }

    fun Disposable.attachToLifecycle() = disposableTracker.add(this)
}