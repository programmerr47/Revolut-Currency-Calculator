package com.github.programmerr47.currencycalculator

import io.reactivex.Single
import retrofit2.http.GET

interface ServerApi {
    @GET("/latest?base=EUR")
    fun getLatest(): Single<LatestCurrencies>
}