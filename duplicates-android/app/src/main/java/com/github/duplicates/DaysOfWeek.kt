/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.duplicates

import android.content.Context
import com.github.android.removeduplicates.R
import java.text.DateFormatSymbols
import java.util.*

/*
 * Days of week code as a single int.
 * 0x00: no day
 * 0x01: Monday
 * 0x02: Tuesday
 * 0x04: Wednesday
 * 0x08: Thursday
 * 0x10: Friday
 * 0x20: Saturday
 * 0x40: Sunday
 */
class DaysOfWeek(days: Int) : Comparable<DaysOfWeek> {

    // Bitmask of all repeating days
    var days: Int = 0
        private set

    init {
        this.days = days
    }

    fun toString(context: Context, showNever: Boolean): String {
        var days = this.days

        // no days
        if (days == 0) {
            return if (showNever)
                context.getString(R.string.never)
            else
                ""
        }

        // every day
        if (days == 0x7f) {
            return context.getString(R.string.every_day)
        }

        // count selected days
        var dayCount = 0
        while (days > 0) {
            if (days and 1 == 1) dayCount++
            days = days shr 1
        }

        // short or long form?
        val dfs = DateFormatSymbols()
        val dayList = if (dayCount > 1)
            dfs.shortWeekdays
        else
            dfs.weekdays

        // selected days
        val ret = StringBuilder()
        for (i in 0..6) {
            if (this.days and (1 shl i) != 0) {
                ret.append(dayList[DAY_MAP[i]])
                dayCount -= 1
                if (dayCount > 0)
                    ret.append(context.getString(R.string.day_concat))
            }
        }
        return ret.toString()
    }

    private fun isSet(day: Int): Boolean {
        return days and (1 shl day) > 0
    }

    operator fun set(day: Int, set: Boolean) {
        days = if (set) {
            days or (1 shl day)
        } else {
            days and (1 shl day).inv()
        }
    }

    fun set(dow: DaysOfWeek) {
        days = dow.days
    }

    fun getCoded(): Int = days

    // Returns days of week encoded in an array of booleans.
    val booleanArray: BooleanArray
        get() {
            val ret = BooleanArray(7)
            for (i in 0..6) {
                ret[i] = isSet(i)
            }
            return ret
        }

    fun isRepeatSet(): Boolean = days != 0

    /**
     * returns number of days from today until next alarm
     *
     * @param c must be set to today
     */
    fun getNextAlarm(c: Calendar): Int {
        if (days == 0) {
            return -1
        }

        val today = (c.get(Calendar.DAY_OF_WEEK) + 5) % 7

        var day: Int
        var dayCount = 0
        while (dayCount < 7) {
            day = (today + dayCount) % 7
            if (isSet(day)) {
                break
            }
            dayCount++
        }
        return dayCount
    }

    override fun compareTo(other: DaysOfWeek): Int {
        return this.days - other.days
    }

    companion object {

        private val DAY_MAP = intArrayOf(
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY,
            Calendar.SUNDAY
        )
    }
}