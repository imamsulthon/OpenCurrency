package com.imams.data.di

import android.app.Application
import androidx.room.Room
import com.imams.data.model.local.ExchangeRatesDao
import com.imams.data.model.local.OpenExchangeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): OpenExchangeDatabase {
        return Room.databaseBuilder(
            app,
            OpenExchangeDatabase::class.java,
            "open_exchange_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserFavoriteDao(database: OpenExchangeDatabase): ExchangeRatesDao {
        return database.exchangeRatesDao()
    }


}