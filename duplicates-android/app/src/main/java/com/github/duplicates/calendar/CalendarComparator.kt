/*
 * Copyright 2016, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.duplicates.calendar

import android.text.format.DateUtils.MINUTE_IN_MILLIS
import com.android.calendarcommon2.EventRecurrence.DAILY
import com.android.calendarcommon2.EventRecurrence.HOURLY
import com.android.calendarcommon2.EventRecurrence.MINUTELY
import com.android.calendarcommon2.EventRecurrence.MONTHLY
import com.android.calendarcommon2.EventRecurrence.SECONDLY
import com.android.calendarcommon2.EventRecurrence.WEEKLY
import com.android.calendarcommon2.EventRecurrence.YEARLY
import com.android.calendarcommon2.RecurrenceSet
import com.github.duplicates.DuplicateComparator
import com.github.duplicates.calendar.CalendarItem.Companion.NEVER
import java.util.*
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.DAY_OF_WEEK
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MILLISECOND
import java.util.Calendar.MINUTE
import java.util.Calendar.MONTH
import java.util.Calendar.SECOND

/**
 * Compare duplicate calendar events.
 *
 * @author moshe.w
 */
class CalendarComparator : DuplicateComparator<CalendarItem>() {

    private val cal1 = Calendar.getInstance()
    private val cal2 = Calendar.getInstance()

    override fun compare(lhs: CalendarItem, rhs: CalendarItem): Int {
        var c: Int

        c = compareIgnoreCase(lhs.title, rhs.title)
        if (c != SAME) return c
        c = compareIgnoreCase(lhs.description, rhs.description)
        if (c != SAME) return c
        c = compareIgnoreCase(lhs.location, rhs.location)
        if (c != SAME) return c
        c = compare(lhs.start, rhs.start)
        if (c != SAME) return c
        c = compare(lhs.endEffective, rhs.endEffective)
        return if (c != SAME) c else super.compare(lhs, rhs)
    }

    override fun difference(lhs: CalendarItem, rhs: CalendarItem): BooleanArray {
        val result = BooleanArray(6)
        if (lhs.id == rhs.id) {
            return result
        }

        result[ATTENDEES] = isDifferent(lhs.isHasAttendeeData, rhs.isHasAttendeeData)
        result[DESCRIPTION] = isDifferent(lhs.description, rhs.description)
        result[DTSTART] = isDifferent(lhs.start, rhs.start)
        result[DTEND] = isDifferent(lhs.endEffective, rhs.endEffective)
        result[LOCATION] = isDifferent(lhs.location, rhs.location)
        result[TITLE] = isDifferent(lhs.title.toLowerCase(locale), rhs.title.toLowerCase(locale))

        return result
    }

    override fun match(lhs: CalendarItem, rhs: CalendarItem, difference: BooleanArray?): Float {
        val different = difference ?: difference(lhs, rhs)
        var match = MATCH_SAME
        if (lhs.id == rhs.id) {
            return 0f
        }

        if (different[TITLE]) {
            match *= matchTitle(lhs.title, rhs.title, 0.85f)
        }
        if (different[DTSTART]) {
            match *= matchDate(
                lhs.start, lhs.getStartTimeZoneNN(), lhs.recurrenceSet, lhs.isAllDay,
                rhs.start, rhs.getStartTimeZoneNN(), rhs.recurrenceSet, rhs.isAllDay
            )
        }
        if (different[DTEND]) {
            match *= matchDate(
                lhs.endEffective, lhs.getEndTimeZoneNN(), lhs.recurrenceSet, lhs.isAllDay,
                rhs.endEffective, rhs.getEndTimeZoneNN(), rhs.recurrenceSet, rhs.isAllDay
            )
        }
        if (different[DESCRIPTION]) {
            match *= 0.95f
        }
        if (different[LOCATION]) {
            match *= 0.96f
        }
        if (different[ATTENDEES]) {
            match *= 0.96f
        }

        return match
    }

    protected fun matchDate(
        lhs: Long,
        lhsTimeZone: TimeZone,
        lhsRecurrence: RecurrenceSet,
        lhsAllDay: Boolean,
        rhs: Long,
        rhsTimeZone: TimeZone,
        rhsRecurrence: RecurrenceSet,
        rhsAllDay: Boolean
    ): Float {
        cal1.timeZone = lhsTimeZone
        cal1.timeInMillis = lhs
        cal2.timeZone = rhsTimeZone
        cal2.timeInMillis = rhs
        val dt = Math.abs(cal1.timeInMillis - cal2.timeInMillis)
        if (dt < MINUTE_IN_MILLIS) {
            return MATCH_SAME
        }

        if ((lhsRecurrence.rrules != null) && (rhsRecurrence.rrules != null)) {
            if (lhsRecurrence.rrules[0].freq == rhsRecurrence.rrules[0].freq) {
                if ((lhs == NEVER) || (rhs == NEVER)) {
                    return 0.95f
                }

                var same = false

                when (lhsRecurrence.rrules[0].freq) {
                    SECONDLY -> same = isSame(cal1, cal2, MILLISECOND)
                    MINUTELY -> same = isSame(cal1, cal2, SECOND)
                    HOURLY -> same = isSame(cal1, cal2, MINUTE)
                    DAILY -> same = isSame(cal1, cal2, HOUR_OF_DAY, MINUTE)
                    WEEKLY -> same = isSame(cal1, cal2, DAY_OF_WEEK, HOUR_OF_DAY)
                    MONTHLY -> same = isSame(cal1, cal2, DAY_OF_MONTH, HOUR_OF_DAY)
                    YEARLY -> if (lhsAllDay && rhsAllDay) {
                        same = isSame(cal1, cal2, MONTH, DAY_OF_MONTH)
                    } else {
                        same = isSame(cal1, cal2, MONTH, DAY_OF_MONTH, HOUR_OF_DAY)
                    }
                }
                if (same) {
                    return 0.99f
                }
            }
        }

        return 0.8f
    }

    protected fun isSame(lhs: Calendar, rhs: Calendar, vararg fields: Int): Boolean {
        var same = true
        for (field in fields) {
            same = same and (lhs.get(field) == rhs.get(field))
        }
        return same
    }

    companion object {

        const val TITLE = 0
        const val DTSTART = 1
        const val DTEND = 2
        const val DESCRIPTION = 3
        const val LOCATION = 4
        const val ATTENDEES = 5
    }
}
