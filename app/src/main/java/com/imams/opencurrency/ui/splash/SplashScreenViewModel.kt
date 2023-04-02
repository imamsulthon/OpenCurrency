package com.imams.opencurrency.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.data.model.param.ExchangeViewParam
import com.imams.data.model.param.ResultState
import com.imams.data.repository.ExchangeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(private val repository: ExchangeRepository): ViewModel() {

    private var splashState = MutableLiveData<SplashViewState>()
    private val _shouldFetchData = MutableLiveData<Boolean>()

    val splashViewState: LiveData<SplashViewState> = splashState
    val shouldFetchData: LiveData<Boolean> = _shouldFetchData

    fun generateService() {
        viewModelScope.launch {
            if (shouldUpdateData()) {
                fetchCurrencies()
                fetchLatest()
            } else {
                _shouldFetchData.postValue(false)
            }
        }
    }

    private fun fetchCurrencies() {
        splashState.postValue(SplashViewState.ProcessCurrencies(false))
        viewModelScope.launch {
            when (val response = repository.getAllCurrencies()) {
                is ResultState.Success -> {
                    splashState.postValue(SplashViewState.ProcessCurrencies(true))
                }
                is ResultState.Empty -> {
                    splashState.postValue(SplashViewState.ProcessCurrencies(true))
                }
                is ResultState.Exception -> {
                    splashState.postValue(SplashViewState.ProcessCurrencies(true))
                }
                else -> {
                    splashState.postValue(SplashViewState.ProcessCurrencies(true))
                }
            }
        }
    }

    private fun fetchLatest() {
        splashState.postValue(SplashViewState.ProcessLatest(false))
        viewModelScope.launch {
            when (val response = repository.getExchangeRatesFromRemote(0, "USD")) {
                is ResultState.Success -> { saveProcess(response.data) }
                is ResultState.Empty -> {
                    splashState.postValue(SplashViewState.ProcessLatest(false))
                }
                is ResultState.Exception -> {
                    splashState.postValue(SplashViewState.ProcessLatest(false))
                }
                else -> {
                    splashState.postValue(SplashViewState.ProcessLatest(false))
                }
            }
        }
    }

    private fun saveProcess(result: ExchangeViewParam) {
        viewModelScope.launch {
            repository.saveExchangeRatesToDb(result)
            repository.saveLatestFetchDate(System.currentTimeMillis())
            splashState.postValue(SplashViewState.ProcessLatest(true))
        }
    }

    private suspend fun shouldUpdateData(): Boolean {
        return System.currentTimeMillis() - repository.getLatestFetchDate() >= TimeUnit.MINUTES.toMillis(30)
    }

    sealed class SplashViewState {
        data class ProcessCurrencies(val done: Boolean): SplashViewState()
        data class ProcessLatest(val done: Boolean): SplashViewState()
    }
}