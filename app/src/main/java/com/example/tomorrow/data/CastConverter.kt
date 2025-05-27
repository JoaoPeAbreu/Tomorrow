package com.example.tomorrow.data

import androidx.room.TypeConverter

class CastConverter {
    @TypeConverter
    fun fromList(list: List<String>): String = list.joinToString(separator = ";")
    @TypeConverter
    fun toList(str: String): List<String> =
        if (str.isEmpty()) emptyList() else str.split(";")
}