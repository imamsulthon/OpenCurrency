package com.imams.data.model.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ExchangeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class OpenExchangeDatabase: RoomDatabase() {

    abstract fun exchangeRatesDao(): ExchangeRatesDao

}