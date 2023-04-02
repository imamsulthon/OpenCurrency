package com.imams.data.model.mapper

import com.imams.data.model.local.ExchangeEntity
import com.imams.data.model.param.CurrencyViewParam
import com.imams.data.model.param.ExchangeViewParam
import com.imams.data.model.remote.ExchangeCurrencyResponse

object Mapper {

    fun HashMap<String, String>.toViewParams(): List<CurrencyViewParam> {
        return this.map {
            CurrencyViewParam(it.key, it.value)
        }
    }

    fun ExchangeCurrencyResponse.toViewParam(): ExchangeViewParam {
        return ExchangeViewParam(
            base = this.base,
            rates = this.rates ?: hashMapOf(),
            timeStamp = this.timestamp,
        )
    }

    fun ExchangeCurrencyResponse.toEntity(): ExchangeEntity {
        return ExchangeEntity(
            baseName = this.base,
            rates = HashMapTypeConverter.mapToString(rates),
            timeStamp = this.timestamp,
        )
    }

    fun ExchangeViewParam.toEntity(): ExchangeEntity {
        return ExchangeEntity(
            baseName = this.base,
            rates = HashMapTypeConverter.mapToString(rates),
            timeStamp = this.timeStamp,
        )
    }

    fun ExchangeEntity.toViewParam(): ExchangeViewParam {
        return ExchangeViewParam(
            base = this.baseName,
            rates = HashMapTypeConverter.stringToMap(rates),
            timeStamp = this.timeStamp,
        )
    }

    fun List<ExchangeEntity>.toViewParams(): List<ExchangeViewParam> {
        return this.map { it.toViewParam() }
    }

    fun ExchangeCurrencyResponse.toRatesHashMap(): List<CurrencyViewParam> {
        return this.rates?.toViewParams(this.base) ?: listOf()
    }

    fun HashMap<String, Double>.toViewParams(fromCode: String): List<CurrencyViewParam> {
        return this.map {
            CurrencyViewParam(
                code = it.key,
                name = "",
                fromCode = fromCode,
                rate = it.value
            )
        }
    }

}