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

import android.text.format.DateUtils
import com.github.duplicates.DuplicateComparator

/**
 * Compare duplicate alarms.
 *
 * @author moshe.w
 */
class AlarmComparator : DuplicateComparator<AlarmItem>() {

    override fun compare(lhs: AlarmItem, rhs: AlarmItem): Int {
        var c: Int

        c = compare(lhs.activate, rhs.activate)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.alarmTime, rhs.alarmTime)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.alertTime, rhs.alertTime)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.createTime, rhs.createTime)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.isDailyBriefing, rhs.isDailyBriefing)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.name, rhs.name)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.notificationType, rhs.notificationType)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.repeat, rhs.repeat)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.isSnoozeActivate, rhs.isSnoozeActivate)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.snoozeDoneCount, rhs.snoozeDoneCount)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.snoozeDuration, rhs.snoozeDuration)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.snoozeRepeat, rhs.snoozeRepeat)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.soundTone, rhs.soundTone)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.soundType, rhs.soundType)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.soundUri, rhs.soundUri)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.isSubdueActivate, rhs.isSubdueActivate)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.subdueDuration, rhs.subdueDuration)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.subdueTone, rhs.subdueTone)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.subdueUri, rhs.subdueUri)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.volume, rhs.volume)
        return if (c != SAME) {
            c
        } else super.compare(lhs, rhs)
    }

    override fun difference(lhs: AlarmItem, rhs: AlarmItem): BooleanArray {
        val result = BooleanArray(4)

        result[ALARM_TIME] = compare(lhs.alarmTime, rhs.alarmTime) != SAME
        result[ALERT_TIME] = compareTime(lhs.alertTime, rhs.alertTime, DateUtils.MINUTE_IN_MILLIS) != SAME
        result[NAME] = compareIgnoreCase(lhs.name, rhs.name) != SAME
        result[REPEAT] = compare(lhs.repeat, rhs.repeat) != SAME

        return result
    }

    override fun match(lhs: AlarmItem, rhs: AlarmItem, difference: BooleanArray?): Float {
        var difference = difference
        if (difference == null) {
            difference = difference(lhs, rhs)
        }
        var match = MATCH_SAME

        if (difference[ALARM_TIME]) {
            match *= 0.7f
        }

        if (difference[REPEAT]) {
            match *= 0.8f
        }

        if (difference[ALERT_TIME]) {
            match *= 0.9f
        }
        if (difference[NAME]) {
            match *= matchTitle(lhs.name, rhs.name, 0.9f)
        }

        return match
    }

    companion object {

        const val ALARM_TIME = 0
        const val ALERT_TIME = 1
        const val NAME = 2
        const val REPEAT = 3
    }
}
