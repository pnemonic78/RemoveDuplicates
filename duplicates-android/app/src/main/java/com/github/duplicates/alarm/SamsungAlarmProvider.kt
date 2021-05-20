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
package com.github.duplicates.alarm

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns._ID
import com.github.duplicates.DaysOfWeek
import com.github.duplicates.DuplicateProvider

/**
 * Provide duplicate messages for Samsung devices.
 *
 * @author moshe.w
 */
class SamsungAlarmProvider(context: Context) : DuplicateProvider<AlarmItem>(context) {

    override fun getContentUri(): Uri? {
        return Uri.parse("content://com.samsung.sec.android.clockpackage/alarm")
    }

    override fun getCursorProjection(): Array<String>? {
        return PROJECTION
    }

    override fun createItem(cursor: Cursor): AlarmItem? {
        return AlarmItem()
    }

    override fun populateItem(cursor: Cursor, item: AlarmItem) {
        item.id = cursor.getLong(INDEX_ID)
        item.activate = cursor.getInt(INDEX_ACTIVE)
        item.soundTone = cursor.getInt(INDEX_ALARMSOUND)
        item.alarmTime = cursor.getInt(INDEX_ALARMTIME)
        item.soundTone = cursor.getInt(INDEX_ALARMTONE)
        item.soundUri = cursor.getString(INDEX_ALARMURI)
        item.alertTime = cursor.getLong(INDEX_ALERTTIME)
        item.createTime = cursor.getLong(INDEX_CREATETIME)
        item.isDailyBriefing = cursor.getInt(INDEX_DAILYBRIEF) != 0
        item.name = empty(cursor, INDEX_NAME)
        item.notificationType = cursor.getInt(INDEX_NOTITYPE)
        item.repeat = toDaysOfWeek(cursor.getInt(INDEX_REPEATTYPE))
        item.isSubdueActivate = cursor.getInt(INDEX_SBDACTIVE) != 0
        item.subdueDuration = cursor.getInt(INDEX_SBDDURATION)
        item.subdueTone = cursor.getInt(INDEX_SBDTONE)
        item.subdueUri = cursor.getInt(INDEX_SBDURI)
        item.isSnoozeActivate = cursor.getInt(INDEX_SNZACTIVE) != 0
        item.snoozeDoneCount = cursor.getInt(INDEX_SNZCOUNT)
        item.snoozeDuration = cursor.getInt(INDEX_SNZDURATION)
        item.snoozeRepeat = cursor.getInt(INDEX_SNZREPEAT)
        item.volume = cursor.getInt(INDEX_VOLUME)
    }

    override fun getReadPermissions(): Array<String>? {
        return PERMISSIONS_READ
    }

    override fun getDeletePermissions(): Array<String>? {
        return PERMISSIONS_WRITE
    }

    protected fun toDaysOfWeek(repeat: Int): DaysOfWeek? {
        if (repeat and REPEAT_WEEKLY != REPEAT_WEEKLY) {
            return null
        }
        val daysOfWeek = DaysOfWeek(0)
        daysOfWeek.set(0, repeat and DAY_SUNDAY == DAY_SUNDAY)
        daysOfWeek.set(1, repeat and DAY_MONDAY == DAY_MONDAY)
        daysOfWeek.set(2, repeat and DAY_TUESDAY == DAY_TUESDAY)
        daysOfWeek.set(3, repeat and DAY_WEDNESDAY == DAY_WEDNESDAY)
        daysOfWeek.set(4, repeat and DAY_THURSDAY == DAY_THURSDAY)
        daysOfWeek.set(5, repeat and DAY_FRIDAY == DAY_FRIDAY)
        daysOfWeek.set(6, repeat and DAY_SATURDAY == DAY_SATURDAY)
        return daysOfWeek
    }

    companion object {

        const val PACKAGE = "com.sec.android.app.clockpackage"

        private val PERMISSIONS_READ =
            arrayOf("com.sec.android.app.clockpackage.permission.READ_ALARM")
        private val PERMISSIONS_WRITE =
            arrayOf("com.sec.android.app.clockpackage.permission.WRITE_ALARM")

        private val PROJECTION = arrayOf(
            _ID,
            "active",
            "createtime",
            "alerttime",
            "alarmtime",
            "repeattype",
            "notitype",
            "snzactive",
            "snzduration",
            "snzrepeat",
            "snzcount",
            "dailybrief",
            "sbdactive",
            "sbdduration",
            "sbdtone",
            "alarmsound",
            "alarmtone",
            "volume",
            "sbduri",
            "alarmuri",
            "name"
        )

        private const val INDEX_ID = 0
        private const val INDEX_ACTIVE = 1
        private const val INDEX_CREATETIME = 2
        private const val INDEX_ALERTTIME = 3
        private const val INDEX_ALARMTIME = 4
        private const val INDEX_REPEATTYPE = 5
        private const val INDEX_NOTITYPE = 6
        private const val INDEX_SNZACTIVE = 7
        private const val INDEX_SNZDURATION = 8
        private const val INDEX_SNZREPEAT = 9
        private const val INDEX_SNZCOUNT = 10
        private const val INDEX_DAILYBRIEF = 11
        private const val INDEX_SBDACTIVE = 12
        private const val INDEX_SBDDURATION = 13
        private const val INDEX_SBDTONE = 14
        private const val INDEX_ALARMSOUND = 15
        private const val INDEX_ALARMTONE = 16
        private const val INDEX_VOLUME = 17
        private const val INDEX_SBDURI = 18
        private const val INDEX_ALARMURI = 19
        private const val INDEX_NAME = 20

        private const val REPEAT_WEEKLY = 0x00000005
        private const val REPEAT_TOMORROW = 0x00000001

        private const val DAY_SUNDAY = 0x10000000
        private const val DAY_MONDAY = 0x01000000
        private const val DAY_TUESDAY = 0x00100000
        private const val DAY_WEDNESDAY = 0x00010000
        private const val DAY_THURSDAY = 0x00001000
        private const val DAY_FRIDAY = 0x00000100
        private const val DAY_SATURDAY = 0x10000010
    }
}
