package com.github.duplicates

import java.util.*

fun Calendar.setToMax(field: Int) {
    set(field, getMaximum(field))
}

fun String?.toTimeZone(): TimeZone? {
    return if (this == null) null else TimeZone.getTimeZone(this)
}
