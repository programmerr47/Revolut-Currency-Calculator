package com.github.programmerr47.currencycalculator

import android.app.Application
import com.github.programmerr47.currencycalculator.net.ServerApi
import io.reactivex.schedulers.Schedulers.io
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyApplication : Application() {

    private val appModule = module {
        single { createRetrofit() }
        single { get<Retrofit>().create(ServerApi::class.java) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }

    private fun createRetrofit() = Retrofit.Builder()
            .baseUrl("https://revolut.duckdns.org")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(io()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}