package com.github.programmerr47.currencycalculator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit.*

class CurrencyListActivity : AppCompatActivity() {

    private val serverApi: ServerApi by inject()
    private val adapter: CurrencyListAdapter by lazy { CurrencyListAdapter() }
    private val calculator: CurrencyCalculator by lazy { CurrencyCalculator(
            serverApi,
            { adapter.update(it) },
            { Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show() }
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_list)
        lifecycle.addObserver(calculator)

        bind<RecyclerView>(R.id.rv_list) {
            adapter = this@CurrencyListActivity.adapter
        }
    }
}

class CurrencyCalculator(
        private val serverApi: ServerApi,
        private val listener: (List<CurrencyItem>) -> Unit,
        private val errorListener: (Throwable) -> Unit
) : LifecycleObserver {
    private var timerDisposable: Disposable? = null

    @OnLifecycleEvent(ON_RESUME)
    fun startTracking() {
        timerDisposable = Observable.interval(1, SECONDS)
                .flatMapSingle { serverApi.getLatest().map {
                    it.rates.entries.map { CurrencyItem(it.key, it.value) }
                } }
                .observeOn(mainThread())
                .subscribe(
                        { listener(it) },
                        { errorListener(it) }
                )
    }

    @OnLifecycleEvent(ON_PAUSE)
    fun stopTracking() {
        timerDisposable?.let {
            if (!it.isDisposed)
                it.dispose()
        }
        timerDisposable = null
    }
}
