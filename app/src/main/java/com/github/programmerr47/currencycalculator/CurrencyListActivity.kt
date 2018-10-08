package com.github.programmerr47.currencycalculator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject

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
