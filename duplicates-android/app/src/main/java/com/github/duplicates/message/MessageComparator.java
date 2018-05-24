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
package com.github.duplicates.message;

import android.text.format.DateUtils;

import com.github.duplicates.DuplicateComparator;

/**
 * Compare duplicate messages.
 *
 * @author moshe.w
 */
public class MessageComparator extends DuplicateComparator<MessageItem> {

    public static final int TYPE = 0;
    public static final int DATE = 1;
    public static final int DATE_SENT = 2;
    public static final int ADDRESS = 3;
    public static final int PERSON = 4;
    public static final int BODY = 5;
    public static final int SUBJECT = 6;
    public static final int THREAD_ID = 7;
    public static final int STATUS = 8;
    public static final int ERROR_CODE = 9;
    public static final int LOCKED = 10;
    public static final int PROTOCOL = 11;
    public static final int READ = 12;
    public static final int SEEN = 13;

    @Override
    public int compare(MessageItem lhs, MessageItem rhs) {
        int c;

        c = compare(lhs.getType(), rhs.getType());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getDateReceived(), rhs.getDateReceived());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getDateSent(), rhs.getDateSent());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getAddress(), rhs.getAddress());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getPerson(), rhs.getPerson());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getBody(), rhs.getBody());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getSubject(), rhs.getSubject());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getThreadId(), rhs.getThreadId());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getStatus(), rhs.getStatus());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getErrorCode(), rhs.getErrorCode());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.isLocked(), rhs.isLocked());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getProtocol(), rhs.getProtocol());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.isRead(), rhs.isRead());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.isSeen(), rhs.isSeen());
        if (c != SAME) {
            return c;
        }

        return super.compare(lhs, rhs);
    }

    @Override
    public boolean[] difference(MessageItem lhs, MessageItem rhs) {
        boolean[] result = new boolean[14];

        result[ADDRESS] = compareIgnoreCase(lhs.getAddress(), rhs.getAddress()) != SAME;
        result[BODY] = compare(lhs.getBody(), rhs.getBody()) != SAME;
        result[DATE] = compareTime(lhs.getDateReceived(), rhs.getDateReceived(), DateUtils.SECOND_IN_MILLIS) != SAME;
        result[DATE_SENT] = compareTime(lhs.getDateSent(), rhs.getDateSent(), DateUtils.SECOND_IN_MILLIS) != SAME;
        result[ERROR_CODE] = compare(lhs.getErrorCode(), rhs.getErrorCode()) != SAME;
        result[LOCKED] = compare(lhs.isLocked(), rhs.isLocked()) != SAME;
        result[PERSON] = compare(lhs.getPerson(), rhs.getPerson()) != SAME;
        result[PROTOCOL] = compare(lhs.getProtocol(), rhs.getProtocol()) != SAME;
        result[READ] = compare(lhs.isRead(), rhs.isRead()) != SAME;
        result[SEEN] = compare(lhs.isSeen(), rhs.isSeen()) != SAME;
        result[STATUS] = compare(lhs.getStatus(), rhs.getStatus()) != SAME;
        result[SUBJECT] = compareIgnoreCase(lhs.getSubject(), rhs.getSubject()) != SAME;
        result[THREAD_ID] = compare(lhs.getThreadId(), rhs.getThreadId()) != SAME;
        result[TYPE] = compare(lhs.getType(), rhs.getType()) != SAME;

        return result;
    }

    @Override
    public float match(MessageItem lhs, MessageItem rhs, boolean[] difference) {
        if (difference == null) {
            difference = difference(lhs, rhs);
        }
        float match = MATCH_SAME;

        if (difference[DATE]) {
            match *= 0.7f;
        }

        if (difference[TYPE]) {
            match *= 0.8f;
        }
        if (difference[DATE_SENT]) {
            match *= 0.8f;
        }
        if (difference[ADDRESS]) {
            match *= matchTitle(lhs.getAddress(), rhs.getAddress(), 0.8f);
        }
        if (difference[PERSON]) {
            match *= 0.8f;
        }
        if (difference[BODY]) {
            match *= 0.75f;
        }

        if (difference[SUBJECT]) {
            match *= matchTitle(lhs.getSubject(), rhs.getSubject(), 0.85f);
        }
        if (difference[THREAD_ID]) {
            match *= 0.9f;
        }

        if (difference[STATUS]) {
            match *= 0.95f;
        }
        if (difference[ERROR_CODE]) {
            match *= 0.95f;
        }
        if (difference[LOCKED]) {
            match *= 0.95f;
        }
        if (difference[PROTOCOL]) {
            match *= 0.95f;
        }
        if (difference[READ]) {
            match *= 0.95f;
        }
        if (difference[SEEN]) {
            match *= 0.95f;
        }

        return match;
    }
}
