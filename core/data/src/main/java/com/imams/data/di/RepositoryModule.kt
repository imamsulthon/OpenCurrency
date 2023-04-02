package com.imams.data.di

import android.content.Context
import android.content.SharedPreferences
import com.imams.data.model.local.ExchangeRatesDao
import com.imams.data.repository.ExchangeRepository
import com.imams.data.repository.ExchangeRepositoryImpl
import com.imams.data.service.ExchangeCurrencyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideExchangeRepository(
        apiService: ExchangeCurrencyApi,
        exchangeRatesDao: ExchangeRatesDao,
        sharedPreferences: SharedPreferences,
    ): ExchangeRepository {
        return ExchangeRepositoryImpl(apiService, exchangeRatesDao, sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("preferences_name", Context.MODE_PRIVATE)
    }

}