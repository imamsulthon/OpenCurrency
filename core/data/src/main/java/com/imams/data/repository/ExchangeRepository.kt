package com.imams.data.repository

import com.imams.data.model.param.ExchangeViewParam
import com.imams.data.model.param.ResultState
import com.imams.data.model.remote.ExchangeCurrencyResponse
import kotlinx.coroutines.flow.Flow

interface ExchangeRepository {

    /* fetch all currencies from remote */
    suspend fun getAllCurrencies(): ResultState<HashMap<String, String>>

    /* fetch exchange base from remote */
    suspend fun getExchangeRatesFromRemote(input: Long, from: String): ResultState<ExchangeViewParam>

    /* fetch exchange base from local */
    suspend fun getAllExchangeRatesFromDb(base: String): Flow<List<ExchangeViewParam>>

    /* save exchanges to DB, object: Response */
    suspend fun saveExchangeRatesToDb(data: ExchangeCurrencyResponse)

    /* save exchanges to DB, object: ViewParam */
    suspend fun saveExchangeRatesToDb(data: ExchangeViewParam)

    /* fetch exchange rates if existed on db */
    suspend fun isExchangeRateExist(base: String): Boolean

    suspend fun getLatestFetchDate(): Long

    suspend fun saveLatestFetchDate(timeStamp: Long)

}