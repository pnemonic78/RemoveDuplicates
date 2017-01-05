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
    public float match(MessageItem lhs, MessageItem rhs) {
        float match = 1f;

        if (lhs.getType() != rhs.getType()) {
            match *= 0.8f;
        }
        if (lhs.getDateReceived() != rhs.getDateReceived()) {
            match *= 0.8f;
        }
        if (lhs.getDateSent() != rhs.getDateSent()) {
            match *= 0.8f;
        }
        if (compare(lhs.getAddress(), rhs.getAddress()) != SAME) {
            match *= 0.8f;
        }
        if (lhs.getPerson() != rhs.getPerson()) {
            match *= 0.8f;
        }
        if (compare(lhs.getBody(), rhs.getBody()) != SAME) {
            match *= 0.8f;
        }

        if (compare(lhs.getSubject(), rhs.getSubject()) != SAME) {
            match *= 0.9f;
        }
        if (lhs.getThreadId() != rhs.getThreadId()) {
            match *= 0.9f;
        }

        if (lhs.getStatus() != rhs.getStatus()) {
            match *= 0.95f;
        }
        if (lhs.getErrorCode() != rhs.getErrorCode()) {
            match *= 0.95f;
        }
        if (lhs.isLocked() != rhs.isLocked()) {
            match *= 0.95f;
        }
        if (lhs.getProtocol() != rhs.getProtocol()) {
            match *= 0.95f;
        }
        if (lhs.isRead() != rhs.isRead()) {
            match *= 0.95f;
        }
        if (lhs.isSeen() != rhs.isSeen()) {
            match *= 0.95f;
        }

        return match;
    }
}
