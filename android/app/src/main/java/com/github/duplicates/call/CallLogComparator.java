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
        c = compare(lhs.getDuration(), rhs.getDuration());
        if (c != 0) {
            return c;
        }
        c = compare(lhs.getNumber(), rhs.getNumber());
        if (c != 0) {
            return c;
        }
        c = compare(lhs.getNumberType(), rhs.getNumberType());
        if (c != 0) {
            return c;
        }
        c = compare(lhs.getName(), rhs.getName());
        if (c != 0) {
            return c;
        }
        c = compare(lhs.isRead(), rhs.isRead());
        if (c != 0) {
            return c;
        }
        c = compare(lhs.isNew(), rhs.isNew());
        if (c != 0) {
            return c;
        }

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
        if (lhs.getDuration() != rhs.getDuration()) {
            match *= 0.8f;
        }
        if (compare(lhs.getNumber(), rhs.getNumber()) != SAME) {
            match *= 0.8f;
        }

        if (lhs.getNumberType() != rhs.getNumberType()) {
            match *= 0.9f;
        }
        if (compare(lhs.getName(), rhs.getName()) != SAME) {
            match *= 0.9f;
        }

        if (lhs.isRead() != rhs.isRead()) {
            match *= 0.95f;
        }
        if (lhs.isNew() != rhs.isNew()) {
            match *= 0.95f;
        }

        return match;
    }
}
