package com.github.programmerr47.currencycalculator.currencylist

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.programmerr47.currencycalculator.R
import com.github.programmerr47.currencycalculator.net.ServerApi
import com.github.programmerr47.currencycalculator.services.*
import com.github.programmerr47.currencycalculator.util.bind
import org.koin.android.ext.android.inject

class CurrencyListActivity : AppCompatActivity() {

    private val serverApi: ServerApi by inject()
    private val latestCurrencyTracker: LatestCurrencyTracker by lazy { LatestCurrencyTrackingService(serverApi) }
    private val currencyCalculator: CurrencyRatesAccepter by lazy { CurrencyCalculator() }
    private val currencyService: CurrencyService by lazy {
        CurrencyService(
                latestCurrencyTracker,
                currencyCalculator,
                { Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show() }
        )
    }
    private val adapter: CurrencyListAdapter by lazy { CurrencyListAdapter(currencyCalculator, BigDecimalCurrencyFormatter) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_list)
        lifecycle.addObserver(currencyService)

        bind<RecyclerView>(R.id.rv_list) {
            adapter = this@CurrencyListActivity.adapter
        }
    }
}
