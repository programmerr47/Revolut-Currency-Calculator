package com.github.programmerr47.currencycalculator.currencylist

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.github.programmerr47.currencycalculator.BaseActivity
import com.github.programmerr47.currencycalculator.R
import com.github.programmerr47.currencycalculator.db.CurrencyDao
import com.github.programmerr47.currencycalculator.net.ServerApi
import com.github.programmerr47.currencycalculator.services.*
import com.github.programmerr47.currencycalculator.util.bind
import com.github.programmerr47.currencycalculator.util.showSnackbar
import com.google.android.material.snackbar.Snackbar.*
import io.reactivex.Scheduler
import org.koin.android.ext.android.inject

class CurrencyListActivity : BaseActivity() {

    private val serverApi: ServerApi by inject()
    private val currencyDao: CurrencyDao by inject()
    private val ioScheduler: Scheduler by inject()

    private val networkTracker: NetworkChangeTracker by lazy { NetworkChangeTracker(this) }

    private val latestCurrencyTracker: LatestCurrencyTracker by lazy {
        LatestCurrencyTrackingService(serverApi, currencyDao, ioScheduler)
    }
    private val currencyCalculator: CurrencyCalculator by lazy { CurrencyCalculator() }
    private val currencyService: CurrencyService by lazy {
        CurrencyService(latestCurrencyTracker, currencyDao, currencyCalculator, networkTracker, ioScheduler)
    }

    private val adapter: CurrencyListAdapter by lazy { CurrencyListAdapter(currencyCalculator, BigDecimalCurrencyFormatter) }

    init {
        lifecycle.run {
            addObserver(latestCurrencyTracker)
            addObserver(networkTracker)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_list)

        val listView = bind<RecyclerView>(R.id.rv_list) {
            adapter = this@CurrencyListActivity.adapter
        }

        networkTracker.observe().subscribe {
            if (it) {
                listView.showSnackbar(R.string.message_internet_available)
            } else {
                listView.showSnackbar(R.string.message_no_internet, LENGTH_INDEFINITE)
            }
        }.attachToLifecycle()
        currencyService.startCurrencyTracking().attachToLifecycle()
        currencyCalculator.observe().subscribe { adapter.update(it) }.attachToLifecycle()
    }
}
