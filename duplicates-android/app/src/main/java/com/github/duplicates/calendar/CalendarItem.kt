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

import android.graphics.Color
import android.provider.CalendarContract.Attendees
import android.provider.CalendarContract.Events
import android.text.TextUtils
import android.text.format.Time
import com.android.calendarcommon2.RecurrenceSet
import com.github.duplicates.DuplicateItem
import com.github.duplicates.DuplicateItemType
import com.github.duplicates.setToMax
import java.util.*

/**
 * Duplicate calendar event.
 *
 * @author moshe.w
 */
class CalendarItem : DuplicateItem(DuplicateItemType.CALENDAR) {

    var title: String = ""
    var location: String? = null
    var description: String? = null
    var color = Color.TRANSPARENT
    var status: Int = Events.STATUS_TENTATIVE
    var selfAttendeeStatus: Int = Attendees.ATTENDEE_STATUS_NONE
    var start: Long = NEVER
    var end: Long = NEVER
        set(end) {
            field = end
            _endEffective = null
        }
    var startTimeZone: TimeZone? = null
    var endTimeZone: TimeZone? = null
        set(value) {
            field = value
            _endEffective = null
        }
    var isAllDay: Boolean = false
    var accessLevel: Int = Events.ACCESS_DEFAULT
    var availability: Int = Events.AVAILABILITY_BUSY
    var recurrenceDate: String? = null
        set(recurrenceDate) {
            field = recurrenceDate
            _recurrenceSet = null
        }
    var recurrenceRule: String? = null
        set(recurrenceRule) {
            field = recurrenceRule
            _recurrenceSet = null
        }
    var exceptionRule: String? = null
        set(exceptionRule) {
            field = exceptionRule
            _recurrenceSet = null
        }
    var exceptionDate: String? = null
        set(exceptionDate) {
            field = exceptionDate
            _recurrenceSet = null
        }
    var originalEventId: Long = 0
    var originalInstanceTime: Long = NEVER
    var isOriginalAllDay: Boolean = false
    var lastDate: Long? = null
        set(lastDate) {
            field = lastDate
            _endEffective = null
        }
    var isHasAttendeeData: Boolean = false
    var organizer: String? = null

    var calendar: CalendarEntity = CalendarEntity()
    private var _endEffective: Long? = null
    var endEffective: Long
        get() {
            if (_endEffective == null) {
                val start = start
                var end = end
                if (end < start) {
                    val last = lastDate
                    if (last != null) {
                        end = last
                    }

                    val recurrenceSet = this.recurrenceSet
                    if ((recurrenceSet.rrules != null) && recurrenceSet.rrules.isNotEmpty()) {
                        val recurrence = recurrenceSet.rrules[0]
                        if (!TextUtils.isEmpty(recurrence.until)) {
                            val until = Time()
                            until.parse(recurrence.until)
                            end = until.normalize(true)
                        }
                    } else if (isAllDay) {
                        val gcal = Calendar.getInstance(getEndTimeZoneNN())
                        gcal.timeInMillis = start
                        gcal.setToMax(Calendar.HOUR_OF_DAY)
                        gcal.setToMax(Calendar.MINUTE)
                        gcal.setToMax(Calendar.SECOND)
                        gcal.setToMax(Calendar.MILLISECOND)
                        end = gcal.timeInMillis
                    }
                }
                _endEffective = end
            }
            return _endEffective!!
        }
        set(value) {
            _endEffective = value
        }
    private var _recurrenceSet: RecurrenceSet? = null
    var recurrenceSet: RecurrenceSet
        get() {
            if (_recurrenceSet == null) {
                _recurrenceSet =
                    RecurrenceSet(recurrenceRule, recurrenceDate, exceptionRule, exceptionDate)
            }
            return _recurrenceSet!!
        }
        set(value) {
            _recurrenceSet = value
        }

    val displayColor: Int
        get() = if (color == Color.TRANSPARENT) calendar.color else color

    fun getStartTimeZoneNN(): TimeZone {
        return startTimeZone ?: TimeZone.getDefault()
    }

    fun getEndTimeZoneNN(): TimeZone {
        return endTimeZone ?: getStartTimeZoneNN()
    }

    fun hasRecurrence(): Boolean {
        return recurrenceSet.hasRecurrence()
    }

    override fun contains(s: CharSequence): Boolean {
        return title.contains(s, true)
            || (description?.contains(s, true) ?: false)
            || (location?.contains(s, true) ?: false)
    }

    companion object {

        const val NEVER = Long.MIN_VALUE
    }
}
