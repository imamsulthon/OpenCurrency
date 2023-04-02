package com.imams.data.model.mapper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object HashMapTypeConverter {

    @JvmStatic
    @TypeConverter
    fun stringToMap(value: String): HashMap<String, Double> {
        return Gson().fromJson(value, object : TypeToken<HashMap<String, Double>>() {}.type)
    }

    @JvmStatic
    @TypeConverter
    fun mapToString(value: HashMap<String, Double>?): String {
        return if(value == null) "" else Gson().toJson(value)
    }

}