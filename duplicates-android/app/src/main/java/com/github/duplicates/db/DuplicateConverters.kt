package com.github.duplicates.db

import androidx.room.TypeConverter
import com.github.duplicates.MainSpinnerItem

class DuplicateConverters {
    @TypeConverter
    fun fromType(type: MainSpinnerItem): String {
        return type.name
    }

    @TypeConverter
    fun toType(typeName: String): MainSpinnerItem {
        return MainSpinnerItem.valueOf(typeName)
    }
}