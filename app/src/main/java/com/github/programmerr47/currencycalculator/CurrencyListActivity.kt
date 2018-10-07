package com.github.programmerr47.currencycalculator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit.*

class CurrencyListActivity : AppCompatActivity() {

    private val serverApi: ServerApi by inject()
    private val adapter: CurrencyListAdapter by lazy { CurrencyListAdapter() }

    private var timerDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_list)

        bind<RecyclerView>(R.id.rv_list) {
            adapter = this@CurrencyListActivity.adapter
        }
    }

    override fun onResume() {
        super.onResume()

        timerDisposable = Observable.interval(1, SECONDS)
                .flatMapSingle { serverApi.getLatest() }
                .observeOn(mainThread())
                .subscribe(
                        { adapter.update(it.rates.map { "${it.key}: ${it.value}" }) },
                        { Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show() }
                )

    }

    override fun onPause() {
        super.onPause()

        timerDisposable?.let {
            if (!it.isDisposed)
                it.dispose()
        }
        timerDisposable = null
    }
}
