package com.imams.data.repository

import android.content.SharedPreferences
import com.imams.data.model.local.ExchangeRatesDao
import com.imams.data.model.mapper.Mapper.toEntity
import com.imams.data.model.mapper.Mapper.toViewParam
import com.imams.data.model.mapper.Mapper.toViewParams
import com.imams.data.model.param.ExchangeViewParam
import com.imams.data.model.param.ResultState
import com.imams.data.model.remote.ExchangeCurrencyResponse
import com.imams.data.service.ExchangeCurrencyApi
import com.imams.data.utils.APP_ID
import com.imams.data.utils.LATEST_FETCH_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val apiService: ExchangeCurrencyApi,
    private val dao: ExchangeRatesDao,
    private val sharedPreferences: SharedPreferences,
): ExchangeRepository {

    private val appId: String = APP_ID

    override suspend fun getAllCurrencies(): ResultState<HashMap<String, String>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getALlCurrenciesAsMap()
                printLog("response $response")
                when {
                    response.isNullOrEmpty() -> {
                        printLog("response isNullOrEmpty")
                        ResultState.Empty
                    }
                    else -> {
                        printLog("response success $response")
                        ResultState.Success(response)
                    }
                }
            } catch (e: Exception) {
                ResultState.Exception(e)
            }
        }
    }

    override suspend fun getExchangeRatesFromRemote(input: Long, from: String): ResultState<ExchangeViewParam> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getExchangeRates(appId)
                when {
                    response == null -> {
                        ResultState.Empty
                    }
                    response.rates.isNullOrEmpty() -> {
                        ResultState.Empty
                    }
                    else -> {
                        ResultState.Success(response.toViewParam())
                    }
                }
            } catch (e: Exception) {
                ResultState.Exception(e)
            }
        }
    }

    override suspend fun getAllExchangeRatesFromDb(base: String): Flow<List<ExchangeViewParam>> {
        return dao.getExchangeRates(base).map {it.toViewParams() }.flowOn(Dispatchers.IO)
    }

    override suspend fun saveExchangeRatesToDb(data: ExchangeCurrencyResponse) {
        dao.addExchangeRates(data.toEntity())
    }

    override suspend fun saveExchangeRatesToDb(data: ExchangeViewParam) {
        dao.addExchangeRates(data.toEntity())
    }

    override suspend fun isExchangeRateExist(base: String): Boolean {
        return dao.isBaseExchangeExist(base)
    }

    override suspend fun getLatestFetchDate(): Long {
        return sharedPreferences.getLong(LATEST_FETCH_KEY, 0)
    }

    override suspend fun saveLatestFetchDate(timeStamp: Long) {
        sharedPreferences.edit().putLong(LATEST_FETCH_KEY, timeStamp).apply()
    }

    private fun printLog(msg: String) {
        println("MainActivityRepo $msg")
    }

}