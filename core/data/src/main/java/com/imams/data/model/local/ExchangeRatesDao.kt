package com.imams.data.model.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRatesDao {

    @Query("SELECT * FROM exchange_rates")
    fun getAllExchangeRates(): Flow<List<ExchangeEntity>>

    @Query("SELECT * FROM exchange_rates WHERE base_name =:base")
    fun getExchangeRates(base: String): Flow<List<ExchangeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExchangeRates(data: ExchangeEntity)

    @Query("SELECT EXISTS(SELECT * FROM exchange_rates WHERE base_name =:base)")
    suspend fun isBaseExchangeExist(base: String): Boolean

    @Delete
    suspend fun delete(data: ExchangeEntity)
}