package com.imams.data.service

import com.imams.data.model.remote.ExchangeCurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeCurrencyApi {

    @GET("currencies.json")
    suspend fun getALlCurrenciesAsMap(): HashMap<String, String>?

    @GET("latest.json")
    suspend fun getExchangeRates(
        @Query("app_id") apiKey: String,
    ): ExchangeCurrencyResponse?

}