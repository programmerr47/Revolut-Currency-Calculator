package com.github.programmerr47.currencycalculator

import android.app.Application
import androidx.room.Room
import com.github.programmerr47.currencycalculator.db.AppDatabase
import com.github.programmerr47.currencycalculator.net.ServerApi
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class CurrencyApplication : Application() {

    private val appModule = module {
        single("ioScheduler") { Schedulers.from(Executors.newFixedThreadPool(3)) }
        single { createRetrofit() }
        single { get<Retrofit>().create(ServerApi::class.java) }
        single { createDB().currencyDao() }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }

    private fun createRetrofit() = Retrofit.Builder()
            .baseUrl("https://revolut.duckdns.org")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(get("ioScheduler")))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private fun createDB() =
            Room.databaseBuilder(this, AppDatabase::class.java, "app_db").build()
}