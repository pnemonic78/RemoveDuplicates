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
package com.github.duplicates.message

import android.text.format.DateUtils.SECOND_IN_MILLIS
import com.github.duplicates.DuplicateComparator

/**
 * Compare duplicate messages.
 *
 * @author moshe.w
 */
class MessageComparator : DuplicateComparator<MessageItem>() {

    override fun compare(lhs: MessageItem, rhs: MessageItem): Int {
        var c: Int

        c = compare(lhs.type, rhs.type)
        if (c != SAME) return c
        c = compare(lhs.dateReceived, rhs.dateReceived)
        if (c != SAME) return c
        c = compare(lhs.dateSent, rhs.dateSent)
        if (c != SAME) return c
        c = compare(lhs.address, rhs.address)
        if (c != SAME) return c
        c = compare(lhs.person, rhs.person)
        if (c != SAME) return c
        c = compare(lhs.body, rhs.body)
        if (c != SAME) return c
        c = compare(lhs.subject, rhs.subject)
        if (c != SAME) return c
        c = compare(lhs.threadId, rhs.threadId)
        if (c != SAME) return c
        c = compare(lhs.status, rhs.status)
        if (c != SAME) return c
        c = compare(lhs.errorCode, rhs.errorCode)
        if (c != SAME) return c
        c = compare(lhs.isLocked, rhs.isLocked)
        if (c != SAME) return c
        c = compare(lhs.protocol, rhs.protocol)
        if (c != SAME) return c
        c = compare(lhs.isRead, rhs.isRead)
        if (c != SAME) return c
        c = compare(lhs.isSeen, rhs.isSeen)
        return if (c != SAME) c else super.compare(lhs, rhs)
    }

    override fun difference(lhs: MessageItem, rhs: MessageItem): BooleanArray {
        val result = BooleanArray(14)

        result[ADDRESS] = isDifferentIgnoreCase(lhs.address, rhs.address)
        result[BODY] = isDifferent(lhs.body, rhs.body)
        result[DATE] = isDifferentTime(lhs.dateReceived, rhs.dateReceived, SECOND_IN_MILLIS)
        result[DATE_SENT] = isDifferentTime(lhs.dateSent, rhs.dateSent, SECOND_IN_MILLIS)
        result[ERROR_CODE] = isDifferent(lhs.errorCode, rhs.errorCode)
        result[LOCKED] = isDifferent(lhs.isLocked, rhs.isLocked)
        result[PERSON] = isDifferent(lhs.person, rhs.person)
        result[PROTOCOL] = isDifferent(lhs.protocol, rhs.protocol)
        result[READ] = isDifferent(lhs.isRead, rhs.isRead)
        result[SEEN] = isDifferent(lhs.isSeen, rhs.isSeen)
        result[STATUS] = isDifferent(lhs.status, rhs.status)
        result[SUBJECT] = isDifferentIgnoreCase(lhs.subject, rhs.subject)
        result[THREAD_ID] = isDifferent(lhs.threadId, rhs.threadId)
        result[TYPE] = isDifferent(lhs.type, rhs.type)

        return result
    }

    override fun match(lhs: MessageItem, rhs: MessageItem, difference: BooleanArray?): Float {
        val different = difference ?: difference(lhs, rhs)
        var match = MATCH_SAME

        if (different[DATE]) {
            match *= 0.7f
        }
        if (different[TYPE]) {
            match *= 0.8f
        }
        if (different[DATE_SENT]) {
            match *= 0.8f
        }
        if (different[ADDRESS]) {
            match *= matchTitle(lhs.address, rhs.address, 0.8f)
        }
        if (different[PERSON]) {
            match *= 0.8f
        }
        if (different[BODY]) {
            match *= 0.75f
        }
        if (different[SUBJECT]) {
            match *= matchTitle(lhs.subject, rhs.subject, 0.85f)
        }
        if (different[THREAD_ID]) {
            match *= 0.9f
        }
        if (different[STATUS]) {
            match *= 0.95f
        }
        if (different[ERROR_CODE]) {
            match *= 0.95f
        }
        if (different[LOCKED]) {
            match *= 0.95f
        }
        if (different[PROTOCOL]) {
            match *= 0.95f
        }
        if (different[READ]) {
            match *= 0.95f
        }
        if (different[SEEN]) {
            match *= 0.95f
        }

        return match
    }

    companion object {

        val TYPE = 0
        val DATE = 1
        val DATE_SENT = 2
        val ADDRESS = 3
        val PERSON = 4
        val BODY = 5
        val SUBJECT = 6
        val THREAD_ID = 7
        val STATUS = 8
        val ERROR_CODE = 9
        val LOCKED = 10
        val PROTOCOL = 11
        val READ = 12
        val SEEN = 13
    }
}
