package com.imams.data.utils

import com.imams.data.model.local.ExchangeRatesDao
import com.imams.data.model.mapper.Mapper.toEntity
import com.imams.data.model.mapper.Mapper.toViewParams
import com.imams.data.model.param.CurrencyViewParam
import com.imams.data.model.param.ExchangeViewParam
import com.imams.data.model.param.ResultState
import com.imams.data.model.remote.ExchangeCurrencyResponse
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

abstract class NetworkBoundResource<T> {

    fun asFlow(): Flow<ResultState<T>> = flow {
        val flow = query()
            .onStart { emit(ResultState.Loading<T>(null)) }
            .flatMapConcat { data ->
                if (shouldFetch(data)) {
                    emit(ResultState.Loading(data))
                    try {
                        saveFetchResult(fetch())
                        query().map { ResultState.Success(it) }
                    } catch (throwable: Throwable) {
                        onFetchFailed(throwable)
                        query().map {
                            ResultState.Exception(throwable)
                        }
                    }
                } else {
                    query().map {
                        ResultState.Success(it)
                    }
                }
            }

        emitAll(flow)
    }

    abstract fun query(): Flow<T>
    abstract suspend fun fetch(): T
    abstract suspend fun saveFetchResult(data: T)
    open fun onFetchFailed(throwable: Throwable) = Unit
    open fun shouldFetch(data: T) = true

}

abstract class NetworkBoundResource2<Result, Request> {

    fun asFlow() = flow {
        val flow = loadFromDb()
            .onStart { ResultState.Loading(null) }
            .flatMapConcat {data ->
                if (shouldFetch(data)) {
                    emit(ResultState.Loading(data))
                    try {
                        saveFetchResult(fetchFromService())
                        loadFromDb().map {
                            printLog("onShouldFetch try result $it")
                            ResultState.Success(it)
                        }
                    } catch (throwable: Throwable) {
                        onFetchFailed(throwable)
                        printLog("onShouldFetch with catch ${throwable.message}")
                        loadFromDb().map {
                            ResultState.Exception(throwable)
                        }
                    }
                } else {
                    loadFromDb().map {
                        printLog("onElse with result $it")
                        ResultState.Success(it)
                    }
                }
            }
        emitAll(flow)
    }

    // get from DB
    abstract fun loadFromDb(): Flow<Result>
    // call from api
    abstract suspend fun fetchFromService(): Request
    // save to DB
    abstract suspend fun saveFetchResult(data: Request)
    // throw Error
    open fun onFetchFailed(throwable: Throwable) = Unit
    // check from DB
    open fun shouldFetch(data: Result): Boolean = true

    open fun printLog(msg: String, tagContext: String? = null) {
        println("$tagContext NetworkBoundResource: $msg")
    }

}

class SomethingToTest {

    fun networkBounce(): Flow<ResultState<CurrencyViewParam>> {
        return object: NetworkBoundResource<CurrencyViewParam>() {
            override fun query(): Flow<CurrencyViewParam> {
                TODO("Not yet implemented")
            }

            override suspend fun fetch(): CurrencyViewParam {
                TODO("Not yet implemented")
            }

            override suspend fun saveFetchResult(data: CurrencyViewParam) {
                TODO("Not yet implemented")
            }
        }.asFlow()
    }

    fun networkBounce(
        dao: ExchangeRatesDao,
        coroutine: CoroutineContext,
    ): Flow<ResultState<List<ExchangeViewParam>>> {
        return object : NetworkBoundResource2<List<ExchangeViewParam>, ExchangeCurrencyResponse>() {
            override fun loadFromDb(): Flow<List<ExchangeViewParam>> {
                return dao.getExchangeRates("").map { it.toViewParams() }
            }

            override suspend fun fetchFromService(): ExchangeCurrencyResponse {
                TODO("Not yet implemented")
            }

            override suspend fun saveFetchResult(data: ExchangeCurrencyResponse) {
//                TODO("Not yet implemented")
                dao.addExchangeRates(data.toEntity())
            }

        }.asFlow().flowOn(coroutine)
    }
}

