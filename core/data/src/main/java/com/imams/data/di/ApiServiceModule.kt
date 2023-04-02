package com.imams.data.di

import com.imams.data.service.ExchangeCurrencyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {


    @Provides
    @Singleton
    fun provideExchangeCurrencyService(retrofit: Retrofit): ExchangeCurrencyApi {
        return retrofit.create(ExchangeCurrencyApi::class.java)
    }

}