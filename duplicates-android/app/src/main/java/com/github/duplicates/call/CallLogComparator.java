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
package com.github.duplicates.call;

import android.text.format.DateUtils;

import com.github.duplicates.DuplicateComparator;

/**
 * Compare duplicate calls.
 *
 * @author moshe.w
 */
public class CallLogComparator extends DuplicateComparator<CallLogItem> {

    public static final int DATE = 0;
    public static final int DURATION = 1;
    public static final int NUMBER = 2;
    public static final int NUMBER_TYPE = 3;
    public static final int NAME = 4;
    public static final int READ = 5;
    public static final int NEW = 6;
    public static final int TYPE = 7;

    @Override
    public int compare(CallLogItem lhs, CallLogItem rhs) {
        int c;

        c = compare(lhs.getType(), rhs.getType());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getDate(), rhs.getDate());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getDuration(), rhs.getDuration());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getNumber(), rhs.getNumber());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getNumberType(), rhs.getNumberType());
        if (c != SAME) {
            return c;
        }
        c = compareIgnoreCase(lhs.getName(), rhs.getName());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.isRead(), rhs.isRead());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.isNew(), rhs.isNew());
        if (c != SAME) {
            return c;
        }

        return super.compare(lhs, rhs);
    }

    @Override
    public boolean[] difference(CallLogItem lhs, CallLogItem rhs) {
        boolean[] result = new boolean[8];

        result[DATE] = compareTime(lhs.getDate(), rhs.getDate(), DateUtils.SECOND_IN_MILLIS) != SAME;
        result[DURATION] = compareTime(lhs.getDuration(), rhs.getDuration(), 1) != SAME;
        result[NAME] = compareIgnoreCase(lhs.getName(), rhs.getName()) != SAME;
        result[NEW] = compare(lhs.isNew(), rhs.isNew()) != SAME;
        result[NUMBER] = comparePhoneNumber(lhs.getNumber(), rhs.getNumber()) != SAME;
        result[NUMBER_TYPE] = compare(lhs.getNumberType(), rhs.getNumberType()) != SAME;
        result[READ] = compare(lhs.isRead(), rhs.isRead()) != SAME;
        result[TYPE] = compare(lhs.getType(), rhs.getType()) != SAME;

        return result;
    }

    @Override
    public float match(CallLogItem lhs, CallLogItem rhs, boolean[] difference) {
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
        if (difference[DURATION]) {
            match *= 0.8f;
        }
        if (difference[NUMBER]) {
            match *= 0.8f;
        }

        if (difference[NUMBER_TYPE]) {
            match *= 0.9f;
        }
        if (difference[NAME]) {
            match *= matchTitle(lhs.getName(), rhs.getName(), 0.9f);
        }

        if (difference[READ]) {
            match *= 0.95f;
        }
        if (difference[NEW]) {
            match *= 0.95f;
        }

        return match;
    }
}
