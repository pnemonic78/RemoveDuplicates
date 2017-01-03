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
package com.github.duplicates.call;

import com.github.duplicates.DuplicateComparator;

/**
 * Compare duplicate calls.
 *
 * @author moshe.w
 */
public class CallLogComparator extends DuplicateComparator<CallLogItem> {

//    private String cachedName;
//    private String cachedNumberLabel;
//    private int cachedNumberType;
//    private String contentItemType;
//    private String contentType;
//    private long date;
//    private long duration;
//    private boolean read;
//    private boolean acknowledged;
//    private String number;
//    private int type;

    @Override
    public int compare(CallLogItem lhs, CallLogItem rhs) {
        int c;

        c = compare(lhs.getType(), rhs.getType());
        if (c != 0) {
            return c;
        }
        c = compare(lhs.getDate(), rhs.getDate());
        if (c != 0) {
            return c;
        }
//        c = compare(lhs.getDateSent(), rhs.getDateSent());
//        if (c != 0) {
//            return c;
//        }
//        c = compare(lhs.getAddress(), rhs.getAddress());
//        if (c != 0) {
//            return c;
//        }
//        c = compare(lhs.getPerson(), rhs.getPerson());
//        if (c != 0) {
//            return c;
//        }
//        c = compare(lhs.getBody(), rhs.getBody());
//        if (c != 0) {
//            return c;
//        }
//        c = compare(lhs.getSubject(), rhs.getSubject());
//        if (c != 0) {
//            return c;
//        }
//        c = compare(lhs.getThreadId(), rhs.getThreadId());
//        if (c != 0) {
//            return c;
//        }
//        c = compare(lhs.getStatus(), rhs.getStatus());
//        if (c != 0) {
//            return c;
//        }
//        c = compare(lhs.getErrorCode(), rhs.getErrorCode());
//        if (c != 0) {
//            return c;
//        }
//        c = compare(lhs.isLocked(), rhs.isLocked());
//        if (c != 0) {
//            return c;
//        }
//        c = compare(lhs.getProtocol(), rhs.getProtocol());
//        if (c != 0) {
//            return c;
//        }
        c = compare(lhs.isRead(), rhs.isRead());
        if (c != 0) {
            return c;
        }
//        c = compare(lhs.isSeen(), rhs.isSeen());
//        if (c != 0) {
//            return c;
//        }

        return super.compare(lhs, rhs);
    }

    @Override
    public float match(CallLogItem lhs, CallLogItem rhs) {
        float match = 1f;

        if (lhs.getType() != rhs.getType()) {
            match *= 0.8f;
        }
        if (lhs.getDate() != rhs.getDate()) {
            match *= 0.8f;
        }
//        if (lhs.getDateSent() != rhs.getDateSent()) {
//            match *= 0.8f;
//        }
//        if (compare(lhs.getAddress(), rhs.getAddress()) != SAME) {
//            match *= 0.8f;
//        }
//        if (lhs.getPerson() != rhs.getPerson()) {
//            match *= 0.8f;
//        }
//        if (compare(lhs.getBody(), rhs.getBody()) != SAME) {
//            match *= 0.8f;
//        }
//
//        if (compare(lhs.getSubject(), rhs.getSubject()) != SAME) {
//            match *= 0.9f;
//        }
//        if (lhs.getThreadId() != rhs.getThreadId()) {
//            match *= 0.9f;
//        }
//
//        if (lhs.getStatus() != rhs.getStatus()) {
//            match *= 0.95f;
//        }
//        if (lhs.getErrorCode() != rhs.getErrorCode()) {
//            match *= 0.95f;
//        }
//        if (lhs.isLocked() != rhs.isLocked()) {
//            match *= 0.95f;
//        }
//        if (lhs.getProtocol() != rhs.getProtocol()) {
//            match *= 0.95f;
//        }
        if (lhs.isRead() != rhs.isRead()) {
            match *= 0.95f;
        }
//        if (lhs.isSeen() != rhs.isSeen()) {
//            match *= 0.95f;
//        }

        return match;
    }
}
