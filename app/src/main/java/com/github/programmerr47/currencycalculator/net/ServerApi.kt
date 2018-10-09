package com.github.programmerr47.currencycalculator.net

import io.reactivex.Single
import retrofit2.http.GET

const val BASE_CURRENCY = "EUR"

interface ServerApi {
    @GET("/latest?base=$BASE_CURRENCY")
    fun getLatest(): Single<LatestCurrencies>
}