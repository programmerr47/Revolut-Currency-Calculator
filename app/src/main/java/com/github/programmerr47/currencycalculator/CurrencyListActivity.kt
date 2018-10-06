package com.github.programmerr47.currencycalculator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import org.koin.android.ext.android.inject

class CurrencyListActivity : AppCompatActivity() {

    private val serverApi: ServerApi by inject()
    private val adapter: CurrencyListAdapter by lazy { CurrencyListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_list)

        bind<RecyclerView>(R.id.rv_list) {
            adapter = this@CurrencyListActivity.adapter
        }

        serverApi.getLatest()
                .observeOn(mainThread())
                .subscribe(
                        { adapter.update(it.rates.map { "${it.key}: ${it.value}" }) },
                        { Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT) }
                )
    }
}
