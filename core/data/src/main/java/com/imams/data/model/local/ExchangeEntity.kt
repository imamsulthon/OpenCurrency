package com.imams.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.imams.data.utils.DAO_EXCHANGE

@Entity(tableName = DAO_EXCHANGE)
data class ExchangeEntity(
    @PrimaryKey
    @ColumnInfo(name = "time_stamp") val timeStamp: Int,
    @ColumnInfo(name = "base_name") val baseName: String,
    @ColumnInfo(name = "rates") val rates: String,
)
