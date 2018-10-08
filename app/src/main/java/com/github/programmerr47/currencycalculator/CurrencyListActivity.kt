package com.github.programmerr47.currencycalculator

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.inject
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.concurrent.TimeUnit.SECONDS

class CurrencyListActivity : AppCompatActivity() {

    private val serverApi: ServerApi by inject()
    private val calculator: CurrencyCalculator by lazy {
        CurrencyCalculator(
                serverApi,
                { adapter.update(it) },
                { Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show() }
        )
    }
    private val adapter: CurrencyListAdapter by lazy { CurrencyListAdapter(calculator) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_list)
        lifecycle.addObserver(calculator)

        bind<RecyclerView>(R.id.rv_list) {
            adapter = this@CurrencyListActivity.adapter
        }
    }
}

interface CurrencyEvaluator {
    fun pushOnTop(currencyType: String): List<String>
    fun acceptNew(currencyType: String, value: BigDecimal)
}

class CurrencyCalculator(
        private val serverApi: ServerApi,
        private val listener: (List<CurrencyItem>) -> Unit,
        private val errorListener: (Throwable) -> Unit
) : LifecycleObserver, CurrencyEvaluator {
    private var currencyOrderedTypes: List<String> = listOf()
    private var timerDisposable: Disposable? = null

    @OnLifecycleEvent(ON_RESUME)
    fun startTracking() {
        timerDisposable = Observable.interval(0, 5, SECONDS)
                .flatMapSingle {
                    serverApi.getLatest().map { latest ->
                        if (currencyOrderedTypes.isEmpty()) {
                            currencyOrderedTypes = latest.rates.keys.toMutableList().apply {
                                add(0, BASE_CURRENCY)
                            }
                        }

                        Log.v("FUCK", "CurrencyOrderedTypes: $currencyOrderedTypes")
                        currencyOrderedTypes.map { CurrencyItem(it, latest.rates[it] ?: ZERO) }
                    }
                }
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

    override fun pushOnTop(currencyType: String) =
        currencyOrderedTypes.move(currencyType, 0).also {
            Log.v("FUCK", "Before push: $currencyOrderedTypes")
            Log.v("FUCK", "After push: $it")
            currencyOrderedTypes = it
        }

    override fun acceptNew(currencyType: String, value: BigDecimal) {
        //todo
    }
}
