package ru.hotmule.beaconfinder.data.db.converters

import androidx.room.TypeConverter

import java.util.Date

object DateConverter {

    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long) = Date(value)

    @TypeConverter
    @JvmStatic
    fun dateToTimestamp(date: Date) = date.time
}
