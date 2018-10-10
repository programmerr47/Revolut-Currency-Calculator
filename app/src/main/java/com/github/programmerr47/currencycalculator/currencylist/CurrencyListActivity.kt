package com.github.programmerr47.currencycalculator.currencylist

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.github.programmerr47.currencycalculator.BaseActivity
import com.github.programmerr47.currencycalculator.R
import com.github.programmerr47.currencycalculator.net.ServerApi
import com.github.programmerr47.currencycalculator.services.*
import com.github.programmerr47.currencycalculator.util.bind
import com.github.programmerr47.currencycalculator.util.showSnackbar
import com.google.android.material.snackbar.Snackbar.*
import org.koin.android.ext.android.inject

class CurrencyListActivity : BaseActivity() {

    private val serverApi: ServerApi by inject()

    private val networkTracker: NetworkChangeTracker by lazy { NetworkChangeTracker(this) }

    private val latestCurrencyTracker: LatestCurrencyTracker by lazy { LatestCurrencyTrackingService(serverApi) }
    private val currencyCalculator: CurrencyCalculator by lazy { CurrencyCalculator() }
    private val currencyService: CurrencyService by lazy {
        CurrencyService(
                latestCurrencyTracker,
                currencyCalculator,
                networkTracker
        )
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
    }
}
