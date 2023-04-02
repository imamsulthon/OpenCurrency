package com.imams.data.model.param

data class CurrencyViewParam(
    val code: String,
    val name: String,
    val fromCode: String = "",
    val rate: Double = 0.0,
)
