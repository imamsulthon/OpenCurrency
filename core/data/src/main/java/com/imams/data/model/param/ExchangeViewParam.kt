package com.imams.data.model.param

data class ExchangeViewParam(
    val base: String,
    val timeStamp: Int = 0,
    val rates: HashMap<String, Double>,
)
