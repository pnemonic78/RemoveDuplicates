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
package com.github.duplicates.call

import android.text.format.DateUtils.SECOND_IN_MILLIS
import com.github.duplicates.DuplicateComparator

/**
 * Compare duplicate calls.
 *
 * @author moshe.w
 */
class CallLogComparator : DuplicateComparator<CallLogItem>() {

    override fun compare(lhs: CallLogItem, rhs: CallLogItem): Int {
        var c: Int

        c = compare(lhs.type, rhs.type)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.date, rhs.date)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.duration, rhs.duration)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.number, rhs.number)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.numberType, rhs.numberType)
        if (c != SAME) {
            return c
        }
        c = compareIgnoreCase(lhs.name, rhs.name)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.isRead, rhs.isRead)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.isNew, rhs.isNew)
        return if (c != SAME) {
            c
        } else super.compare(lhs, rhs)
    }

    override fun difference(lhs: CallLogItem, rhs: CallLogItem): BooleanArray {
        val result = BooleanArray(8)

        result[DATE] = isDifferentTime(lhs.date, rhs.date, SECOND_IN_MILLIS)
        result[DURATION] = isDifferentTime(lhs.duration, rhs.duration, 1)
        result[NAME] = isDifferentIgnoreCase(lhs.name, rhs.name)
        result[NEW] = isDifferent(lhs.isNew, rhs.isNew)
        result[NUMBER] = isDifferentPhoneNumber(lhs.number, rhs.number)
        result[NUMBER_TYPE] = isDifferent(lhs.numberType, rhs.numberType)
        result[READ] = isDifferent(lhs.isRead, rhs.isRead)
        result[TYPE] = isDifferent(lhs.type, rhs.type)

        return result
    }

    override fun match(lhs: CallLogItem, rhs: CallLogItem, difference: BooleanArray?): Float {
        var difference = difference
        if (difference == null) {
            difference = difference(lhs, rhs)
        }
        var match = MATCH_SAME

        if (difference[DATE]) {
            match *= 0.7f
        }

        if (difference[TYPE]) {
            match *= 0.8f
        }
        if (difference[DURATION]) {
            match *= 0.8f
        }
        if (difference[NUMBER]) {
            match *= 0.8f
        }

        if (difference[NUMBER_TYPE]) {
            match *= 0.9f
        }
        if (difference[NAME]) {
            match *= matchTitle(lhs.name, rhs.name, 0.9f)
        }

        if (difference[READ]) {
            match *= 0.95f
        }
        if (difference[NEW]) {
            match *= 0.95f
        }

        return match
    }

    companion object {

        const val DATE = 0
        const val DURATION = 1
        const val NUMBER = 2
        const val NUMBER_TYPE = 3
        const val NAME = 4
        const val READ = 5
        const val NEW = 6
        const val TYPE = 7
    }
}
