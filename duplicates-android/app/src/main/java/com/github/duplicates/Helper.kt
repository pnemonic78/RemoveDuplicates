package com.github.duplicates

import android.text.format.DateUtils
import java.util.*

fun Calendar.setToMax(field: Int) {
    set(field, getMaximum(field))
}

fun String?.toTimeZone(): TimeZone? {
    return if (this == null) null else TimeZone.getTimeZone(this)
}

const val SHOW_DATE_TIME = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_MONTH or DateUtils.FORMAT_ABBREV_WEEKDAY