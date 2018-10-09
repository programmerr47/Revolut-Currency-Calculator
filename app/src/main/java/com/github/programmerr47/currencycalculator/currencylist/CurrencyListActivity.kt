package com.github.programmerr47.currencycalculator.currencylist

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.RecyclerView
import com.github.programmerr47.currencycalculator.R
import com.github.programmerr47.currencycalculator.net.ServerApi
import com.github.programmerr47.currencycalculator.services.*
import com.github.programmerr47.currencycalculator.util.bind
import com.github.programmerr47.currencycalculator.util.bindable
import com.github.programmerr47.currencycalculator.util.showSnackbar
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

class CurrencyListActivity : AppCompatActivity() {

    private val serverApi: ServerApi by inject()
    private val latestCurrencyTracker: LatestCurrencyTracker by lazy { LatestCurrencyTrackingService(serverApi) }
    private val currencyCalculator: CurrencyCalculator by lazy { CurrencyCalculator() }
    private val currencyService: CurrencyService by lazy {
        CurrencyService(
                latestCurrencyTracker,
                currencyCalculator,
                { Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show() }
        )
    }

    private val networkTracker: NetworkChangeTracker by lazy { NetworkChangeTracker(this) }
    private val networkChangeListener: Tracker by lazy { NetworkTrackWrapper(networkTracker,
            { listView.showSnackbar(R.string.message_internet_available) },
            { listView.showSnackbar(R.string.message_no_internet, Snackbar.LENGTH_INDEFINITE) }
    ) }


    private val listView: RecyclerView by bindable(R.id.rv_list)
    private val adapter: CurrencyListAdapter by lazy { CurrencyListAdapter(currencyCalculator, BigDecimalCurrencyFormatter) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_list)
        lifecycle.addObservers(
                currencyService,
                networkChangeListener
        )

        listView.adapter = this@CurrencyListActivity.adapter
    }

    private fun Lifecycle.addObservers(vararg observers: LifecycleObserver) =
            observers.forEach { addObserver(it) }
}
