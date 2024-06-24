package edu.card.clarity.data.converters

import android.icu.util.Calendar
import androidx.room.TypeConverter

class CalendarConverter {
    @TypeConverter
    fun toCalendar(i: Int?): Calendar? =
        if (i == null) null else Calendar.getInstance().apply {
            isLenient = true
            set(Calendar.DAY_OF_MONTH, i)
        }

    @TypeConverter
    fun fromCalendar(c: Calendar?): Int? = c?.get(Calendar.DAY_OF_MONTH)
}