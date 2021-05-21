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

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns._ID
import android.provider.CalendarContract.Events.ALL_DAY
import android.provider.CalendarContract.Events.CALENDAR_ACCESS_LEVEL
import android.provider.CalendarContract.Events.CALENDAR_COLOR
import android.provider.CalendarContract.Events.CALENDAR_DISPLAY_NAME
import android.provider.CalendarContract.Events.CALENDAR_ID
import android.provider.CalendarContract.Events.CALENDAR_TIME_ZONE
import android.provider.CalendarContract.Events.CONTENT_URI
import android.provider.CalendarContract.Events.DELETED
import android.provider.CalendarContract.Events.DESCRIPTION
import android.provider.CalendarContract.Events.DTEND
import android.provider.CalendarContract.Events.DTSTART
import android.provider.CalendarContract.Events.EVENT_COLOR
import android.provider.CalendarContract.Events.EVENT_END_TIMEZONE
import android.provider.CalendarContract.Events.EVENT_LOCATION
import android.provider.CalendarContract.Events.EVENT_TIMEZONE
import android.provider.CalendarContract.Events.EXDATE
import android.provider.CalendarContract.Events.EXRULE
import android.provider.CalendarContract.Events.HAS_ATTENDEE_DATA
import android.provider.CalendarContract.Events.LAST_DATE
import android.provider.CalendarContract.Events.OWNER_ACCOUNT
import android.provider.CalendarContract.Events.RDATE
import android.provider.CalendarContract.Events.RRULE
import android.provider.CalendarContract.Events.TITLE
import android.provider.CalendarContract.Events.VISIBLE
import android.util.LongSparseArray
import com.github.android.removeduplicates.BuildConfig
import com.github.duplicates.DuplicateProvider
import com.github.duplicates.calendar.CalendarItem.Companion.NEVER
import com.github.duplicates.toTimeZone

/**
 * Provide duplicate calendar events.
 *
 * @author moshe.w
 */
class CalendarProvider(context: Context) : DuplicateProvider<CalendarItem>(context) {

    private val calendars = LongSparseArray<CalendarEntity>()

    override fun getContentUri(): Uri {
        return CONTENT_URI
    }

    override fun getCursorProjection(): Array<String> {
        return PROJECTION
    }

    override fun getCursorSelection(): String {
        return "$DELETED=0"
    }

    override fun getCursorOrder(): String {
        return if (BuildConfig.DEBUG) {
            "$_ID ASC"
        } else {
            "$DTSTART ASC"
        }
    }

    override fun createItem(cursor: Cursor): CalendarItem {
        return CalendarItem()
    }

    override fun onPreExecute() {
        super.onPreExecute()
        calendars.clear()
    }

    override fun populateItem(cursor: Cursor, item: CalendarItem) {
        // Event data.
        item.id = cursor.getLong(INDEX_ID)
        item.isAllDay = cursor.getInt(INDEX_ALL_DAY) != 0
        item.description = empty(cursor, INDEX_DESCRIPTION)
        item.end = if (cursor.isNull(INDEX_DTEND)) NEVER else cursor.getLong(INDEX_DTEND)
        item.start = if (cursor.isNull(INDEX_DTSTART)) NEVER else cursor.getLong(INDEX_DTSTART)
        item.color = cursor.getInt(INDEX_EVENT_COLOR)
        item.endTimeZone = cursor.getString(INDEX_EVENT_END_TIMEZONE).toTimeZone()
        item.location = empty(cursor, INDEX_EVENT_LOCATION)
        item.startTimeZone = cursor.getString(INDEX_EVENT_TIMEZONE).toTimeZone()
        item.exceptionDate = cursor.getString(INDEX_EXDATE)
        item.exceptionRule = cursor.getString(INDEX_EXRULE)
        item.isHasAttendeeData = cursor.getInt(INDEX_HAS_ATTENDEE_DATA) != 0
        item.lastDate =
            if (cursor.isNull(INDEX_LAST_DATE)) null else cursor.getLong(INDEX_LAST_DATE)
        item.recurrenceDate = cursor.getString(INDEX_RDATE)
        item.recurrenceRule = cursor.getString(INDEX_RRULE)
        item.title = empty(cursor, INDEX_TITLE)

        // Calendar data.
        val calendarId = cursor.getLong(INDEX_CALENDAR_ID)
        var cal: CalendarEntity? = calendars.get(calendarId)
        if (cal == null) {
            cal = item.calendar
            cal.id = calendarId
            cal.access = cursor.getInt(INDEX_CALENDAR_ACCESS_LEVEL)
            cal.color = cursor.getInt(INDEX_CALENDAR_COLOR)
            cal.name = cursor.getString(INDEX_CALENDAR_DISPLAY_NAME)
            cal.timeZone = cursor.getString(INDEX_CALENDAR_TIME_ZONE).toTimeZone()
            cal.account = cursor.getString(INDEX_OWNER_ACCOUNT)
            cal.isVisible = cursor.getInt(INDEX_VISIBLE) != 0
            calendars.put(calendarId, cal)
        }
        item.calendar = cal
    }

    override fun deleteItem(cr: ContentResolver, contentUri: Uri, item: CalendarItem): Boolean {
        return cr.delete(contentUri, _ID + "=" + item.id, null) > 0
    }

    override fun getReadPermissions(): Array<String> {
        return PERMISSIONS_READ
    }

    override fun getDeletePermissions(): Array<String> {
        return PERMISSIONS_WRITE
    }

    companion object {

        private val PERMISSIONS_READ = arrayOf(Manifest.permission.READ_CALENDAR)
        private val PERMISSIONS_WRITE = arrayOf(Manifest.permission.WRITE_CALENDAR)

        private val PROJECTION = arrayOf(
            // Event.
            _ID,
            ALL_DAY,
            DESCRIPTION,
            DTEND,
            DTSTART,
            EVENT_COLOR,
            EVENT_END_TIMEZONE,
            EVENT_LOCATION,
            EVENT_TIMEZONE,
            EXDATE,
            EXRULE,
            HAS_ATTENDEE_DATA,
            LAST_DATE,
            RDATE,
            RRULE,
            TITLE,

            // Owner calendar.
            CALENDAR_ACCESS_LEVEL,
            CALENDAR_COLOR,
            CALENDAR_DISPLAY_NAME,
            CALENDAR_ID,
            CALENDAR_TIME_ZONE,
            OWNER_ACCOUNT,
            VISIBLE
        )

        private const val INDEX_ID = 0
        private const val INDEX_ALL_DAY = 1
        private const val INDEX_DESCRIPTION = 2
        private const val INDEX_DTEND = 3
        private const val INDEX_DTSTART = 4
        private const val INDEX_EVENT_COLOR = 5
        private const val INDEX_EVENT_END_TIMEZONE = 6
        private const val INDEX_EVENT_LOCATION = 7
        private const val INDEX_EVENT_TIMEZONE = 8
        private const val INDEX_EXDATE = 9
        private const val INDEX_EXRULE = 10
        private const val INDEX_HAS_ATTENDEE_DATA = 11
        private const val INDEX_LAST_DATE = 12
        private const val INDEX_RDATE = 13
        private const val INDEX_RRULE = 14
        private const val INDEX_TITLE = 15
        private const val INDEX_CALENDAR_ACCESS_LEVEL = 16
        private const val INDEX_CALENDAR_COLOR = 17
        private const val INDEX_CALENDAR_DISPLAY_NAME = 18
        private const val INDEX_CALENDAR_ID = 19
        private const val INDEX_CALENDAR_TIME_ZONE = 20
        private const val INDEX_OWNER_ACCOUNT = 21
        private const val INDEX_VISIBLE = 22
    }
}
