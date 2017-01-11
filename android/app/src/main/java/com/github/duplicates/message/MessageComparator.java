/*
 * Source file of the Remove Duplicates project.
 * Copyright (c) 2016. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/2.0
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 *
 * https://github.com/pnemonic78/RemoveDuplicates
 *
 * Contributor(s):
 *   Moshe Waisberg
 *
 */
package com.github.duplicates.message;

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

        result[ADDRESS] = compare(lhs.getAddress(), rhs.getAddress()) != SAME;
        result[BODY] = compare(lhs.getBody(), rhs.getBody()) != SAME;
        result[DATE] = compare(lhs.getDateReceived(), rhs.getDateReceived()) != SAME;
        result[DATE_SENT] = compare(lhs.getDateSent(), rhs.getDateSent()) != SAME;
        result[ERROR_CODE] = compare(lhs.getErrorCode(), rhs.getErrorCode()) != SAME;
        result[LOCKED] = compare(lhs.isLocked(), rhs.isLocked()) != SAME;
        result[PERSON] = compare(lhs.getPerson(), rhs.getPerson()) != SAME;
        result[PROTOCOL] = compare(lhs.getProtocol(), rhs.getProtocol()) != SAME;
        result[READ] = compare(lhs.isRead(), rhs.isRead()) != SAME;
        result[SEEN] = compare(lhs.isSeen(), rhs.isSeen()) != SAME;
        result[STATUS] = compare(lhs.getStatus(), rhs.getStatus()) != SAME;
        result[SUBJECT] = compare(lhs.getSubject(), rhs.getSubject()) != SAME;
        result[THREAD_ID] = compare(lhs.getThreadId(), rhs.getThreadId()) != SAME;
        result[TYPE] = compare(lhs.getType(), rhs.getType()) != SAME;

        return result;
    }

    @Override
    public float match(boolean[] difference) {
        float match = 1f;

        if (difference[DATE]) {
            match *= 0.75f;
        }

        if (difference[TYPE]) {
            match *= 0.8f;
        }
        if (difference[DATE_SENT]) {
            match *= 0.8f;
        }
        if (difference[ADDRESS]) {
            match *= 0.8f;
        }
        if (difference[PERSON]) {
            match *= 0.8f;
        }
        if (difference[BODY]) {
            match *= 0.8f;
        }

        if (difference[SUBJECT]) {
            match *= 0.9f;
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
