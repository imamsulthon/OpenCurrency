package com.imams.opencurrency.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.data.model.mapper.Mapper.toViewParams
import com.imams.data.model.param.CurrencyViewParam
import com.imams.data.model.param.ResultState
import com.imams.data.repository.ExchangeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ExchangeRepository
): ViewModel() {

    private val _error = MutableLiveData<String>()
    private val _currencies = MutableLiveData<List<CurrencyViewParam>>()
    private val _currenciesRates = MutableLiveData<List<CurrencyViewParam>>()
    private val _latestTimestamp = MutableLiveData<String>()

    val currencies: LiveData<List<CurrencyViewParam>> = _currencies
    val currenciesRates: LiveData<List<CurrencyViewParam>> = _currenciesRates
    val latestTimeStamp: LiveData<String> = _latestTimestamp

    fun fetchAllCurrency() {
        viewModelScope.launch {
            when (val response = repository.getAllCurrencies()) {
                is ResultState.Success -> {
                    printLog("Success")
                    val data = response.data.toViewParams().sortedBy { it.code }
                    _currencies.postValue(data)

                    val time = System.currentTimeMillis()
                    printLog("t: currentTimeMillis $time")
                }
                is ResultState.Empty -> {
                    printLog("Empty")
                }
                is ResultState.Exception -> {
                    printLog("exception ${response.e} ${response.e.message}")
                }
                else -> {
                    printLog("else")
                }
            }
        }
    }

    fun performConversion(amount: Long = 0, base: String) {
        viewModelScope.launch {
            val result = repository.getAllExchangeRatesFromDb("USD")
            result.collectLatest {
                printLog("performConversion -> size, ${it.size}, latest: ${it.last().timeStamp}")
                val data = it.last()
                val factor = data.rates[base] ?: 0.0
                val list = data.rates.map {rate ->
                    CurrencyViewParam(
                        code = rate.key,
                        name = "",
                        rate = (amount * factor) / rate.value,
                        fromCode = base,
                    )
                }.sortedBy { result -> result.code }
                _currenciesRates.postValue(list)
                _latestTimestamp.postValue(data.timeStamp.toString())
                printLog("timestamp: ${data.timeStamp} local: ${getCurrentTimeMillis()}")
            }
        }
    }

    private fun getCurrentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    private fun printLog(msg: String) {
        println("MainActivityVM $msg")
    }

}