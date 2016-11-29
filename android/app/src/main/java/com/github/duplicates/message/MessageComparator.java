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

import android.text.TextUtils;

import com.github.duplicates.DuplicateComparator;

/**
 * Compare duplicate messages.
 *
 * @author moshe.w
 */
public class MessageComparator extends DuplicateComparator<MessageItem> {

    private static final int ADDRESS = 1 << 29;
    private static final int BODY = 1 << 27;
    private static final int DATE_RECEIVED = 1 << 31;
    private static final int DATE_SENT = 1 << 30;
    private static final int ERROR_CODE = 1 << 4;
    private static final int LOCKED = 1 << 5;
    private static final int PERSON = 1 << 28;
    private static final int PROTOCOL = 1 << 7;
    private static final int READ = 1 << 8;
    private static final int SEEN = 1 << 9;
    private static final int STATUS = 1 << 10;
    private static final int SUBJECT = 1 << 26;
    private static final int THREAD_ID = 1 << 15;
    private static final int TYPE = 1 << 13;

    @Override
    protected int similar(MessageItem lhs, MessageItem rhs) {
        int similar = SAME;

        if (!TextUtils.equals(lhs.getAddress(), rhs.getAddress())) {
            similar |= ADDRESS;
        }
        if (!TextUtils.equals(lhs.getBody(), rhs.getBody())) {
            similar |= BODY;
        }
        if (lhs.getDateReceived() != lhs.getDateReceived()) {
            similar |= DATE_RECEIVED;
        }
        if (lhs.getDateSent() != lhs.getDateSent()) {
            similar |= DATE_SENT;
        }
        if (lhs.getErrorCode() != lhs.getErrorCode()) {
            similar |= ERROR_CODE;
        }
        if (lhs.isLocked() != lhs.isLocked()) {
            similar |= LOCKED;
        }
        if (lhs.getPerson() != lhs.getPerson()) {
            similar |= PERSON;
        }
        if (lhs.getProtocol() != lhs.getProtocol()) {
            similar |= PROTOCOL;
        }
        if (lhs.isRead() != lhs.isRead()) {
            similar |= READ;
        }
        if (lhs.isSeen() != lhs.isSeen()) {
            similar |= SEEN;
        }
        if (lhs.getStatus() != lhs.getStatus()) {
            similar |= STATUS;
        }
        if (!TextUtils.equals(lhs.getSubject(), rhs.getSubject())) {
            similar |= SUBJECT;
        }
        if (lhs.getThreadId() != lhs.getThreadId()) {
            similar |= THREAD_ID;
        }
        if (lhs.getType() != lhs.getType()) {
            similar |= TYPE;
        }

        return similar;
    }
}
