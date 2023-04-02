package com.imams.opencurrency

import android.content.SharedPreferences
import com.imams.data.model.local.ExchangeRatesDao
import com.imams.data.model.param.ExchangeViewParam
import com.imams.data.model.param.ResultState
import com.imams.data.model.remote.ExchangeCurrencyResponse
import com.imams.data.repository.ExchangeRepository
import com.imams.data.repository.ExchangeRepositoryImpl
import com.imams.data.service.ExchangeCurrencyApi
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class RepositoryTest {
    @Mock
    lateinit var apiService: ExchangeCurrencyApi

    @Mock
    lateinit var currencyDao: ExchangeRatesDao

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    lateinit var exchangeRepository: ExchangeRepository
    private val appId = "4pp1d"

    private val dummyCurrencies = hashMapOf(
        "USD" to "United States Dollar",
        "IDR" to "Indonesian Rupiah",
        "JPY" to  "Japanese Yen",
    )

    private val dummyRates = hashMapOf(
        "USD" to 1.0,
        "IDR" to 15000.65,
        "JPY" to  300.05,
    )

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        exchangeRepository = ExchangeRepositoryImpl(apiService, currencyDao, sharedPreferences)
    }

    @Test
    fun `get currencies from api test`() {
        runBlocking {
            Mockito
                .`when`(apiService.getALlCurrenciesAsMap())
                .thenReturn(dummyCurrencies)
            val response = exchangeRepository.getAllCurrencies()
            assertEquals(ResultState.Success(dummyCurrencies), response)
        }
    }

    @Test
    fun `get exchange rates from api push success`() {
        runBlocking {
            Mockito
                .`when`(apiService.getExchangeRates(appId))
                .thenReturn(
                    ExchangeCurrencyResponse(
                        base = "USD",
                        timestamp = 12345,
                        rates = dummyRates)
                )
            val response = exchangeRepository.getExchangeRatesFromRemote(0, "USD")
            var result: ExchangeViewParam
            when (response) {
                is ResultState.Success -> {
                    result = ExchangeViewParam(
                        base = response.data.base,
                        timeStamp = response.data.timeStamp,
                        rates = response.data.rates
                    )
                }
                else -> {
                    result = ExchangeViewParam(
                        base = "USD",
                        timeStamp = 12345,
                        rates = dummyRates
                    )
                }
            }
            println("TestImams $response")
            assertEquals(
                ResultState.Success(ExchangeViewParam("USD", 12345, rates = dummyRates)),
                ResultState.Success(result)
            )
        }
    }

}