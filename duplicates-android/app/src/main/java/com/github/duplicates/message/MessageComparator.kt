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

import android.text.format.DateUtils

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
        if (c != SAME) {
            return c
        }
        c = compare(lhs.dateReceived, rhs.dateReceived)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.dateSent, rhs.dateSent)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.address, rhs.address)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.person, rhs.person)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.body, rhs.body)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.subject, rhs.subject)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.threadId, rhs.threadId)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.status, rhs.status)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.errorCode, rhs.errorCode)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.isLocked, rhs.isLocked)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.protocol, rhs.protocol)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.isRead, rhs.isRead)
        if (c != SAME) {
            return c
        }
        c = compare(lhs.isSeen, rhs.isSeen)
        return if (c != SAME) {
            c
        } else super.compare(lhs, rhs)
    }

    override fun difference(lhs: MessageItem, rhs: MessageItem): BooleanArray {
        val result = BooleanArray(14)

        result[ADDRESS] = compareIgnoreCase(lhs.address, rhs.address) != SAME
        result[BODY] = compare(lhs.body, rhs.body) != SAME
        result[DATE] = compareTime(lhs.dateReceived, rhs.dateReceived, DateUtils.SECOND_IN_MILLIS) != SAME
        result[DATE_SENT] = compareTime(lhs.dateSent, rhs.dateSent, DateUtils.SECOND_IN_MILLIS) != SAME
        result[ERROR_CODE] = compare(lhs.errorCode, rhs.errorCode) != SAME
        result[LOCKED] = compare(lhs.isLocked, rhs.isLocked) != SAME
        result[PERSON] = compare(lhs.person, rhs.person) != SAME
        result[PROTOCOL] = compare(lhs.protocol, rhs.protocol) != SAME
        result[READ] = compare(lhs.isRead, rhs.isRead) != SAME
        result[SEEN] = compare(lhs.isSeen, rhs.isSeen) != SAME
        result[STATUS] = compare(lhs.status, rhs.status) != SAME
        result[SUBJECT] = compareIgnoreCase(lhs.subject, rhs.subject) != SAME
        result[THREAD_ID] = compare(lhs.threadId, rhs.threadId) != SAME
        result[TYPE] = compare(lhs.type, rhs.type) != SAME

        return result
    }

    override fun match(lhs: MessageItem, rhs: MessageItem, difference: BooleanArray?): Float {
        var difference = difference
        if (difference == null) {
            difference = difference(lhs, rhs)
        }
        var match = DuplicateComparator.MATCH_SAME

        if (difference[DATE]) {
            match *= 0.7f
        }

        if (difference[TYPE]) {
            match *= 0.8f
        }
        if (difference[DATE_SENT]) {
            match *= 0.8f
        }
        if (difference[ADDRESS]) {
            match *= matchTitle(lhs.address, rhs.address, 0.8f)
        }
        if (difference[PERSON]) {
            match *= 0.8f
        }
        if (difference[BODY]) {
            match *= 0.75f
        }

        if (difference[SUBJECT]) {
            match *= matchTitle(lhs.subject, rhs.subject, 0.85f)
        }
        if (difference[THREAD_ID]) {
            match *= 0.9f
        }

        if (difference[STATUS]) {
            match *= 0.95f
        }
        if (difference[ERROR_CODE]) {
            match *= 0.95f
        }
        if (difference[LOCKED]) {
            match *= 0.95f
        }
        if (difference[PROTOCOL]) {
            match *= 0.95f
        }
        if (difference[READ]) {
            match *= 0.95f
        }
        if (difference[SEEN]) {
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
