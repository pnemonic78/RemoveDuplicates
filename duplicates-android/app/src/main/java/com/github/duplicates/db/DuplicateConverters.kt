package com.github.duplicates.db

import androidx.room.TypeConverter
import com.github.duplicates.DuplicateItemType

class DuplicateConverters {
    @TypeConverter
    fun fromType(type: DuplicateItemType): String {
        return type.name
    }

    @TypeConverter
    fun toType(typeName: String): DuplicateItemType {
        return DuplicateItemType.valueOf(typeName)
    }
}