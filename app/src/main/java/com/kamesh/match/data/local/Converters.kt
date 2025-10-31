package com.kamesh.match.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kamesh.match.domain.model.ProfileType

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromProfileType(value: ProfileType): String {
        return value.name
    }

    @TypeConverter
    fun toProfileType(value: String): ProfileType {
        return ProfileType.valueOf(value)
    }
}
