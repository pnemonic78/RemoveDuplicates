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
package com.github.duplicates.calendar;

import com.github.duplicates.DuplicateComparator;

/**
 * Compare duplicate calendar events.
 *
 * @author moshe.w
 */
public class CalendarComparator extends DuplicateComparator<CalendarItem> {

    public static final int TITLE = 0;
    public static final int DTSTART = 1;
    public static final int DTEND = 2;
    public static final int DESCRIPTION = 3;
    public static final int LOCATION = 4;
    public static final int ATTENDEES = 5;

    @Override
    public int compare(CalendarItem lhs, CalendarItem rhs) {
        int c;

        c = compare(lhs.getTitle(), rhs.getTitle());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getDescription(), rhs.getDescription());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getLocation(), rhs.getLocation());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getStart(), rhs.getStart());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getEndEffective(), rhs.getEndEffective());
        if (c != SAME) {
            return c;
        }

        return super.compare(lhs, rhs);
    }

    @Override
    public boolean[] difference(CalendarItem lhs, CalendarItem rhs) {
        boolean[] result = new boolean[6];

        result[ATTENDEES] = compare(lhs.isHasAttendeeData(), rhs.isHasAttendeeData()) != SAME;
        result[DESCRIPTION] = compare(lhs.getDescription(), rhs.getDescription()) != SAME;
        result[DTSTART] = compare(lhs.getStart(), rhs.getStart()) != SAME;
        result[DTEND] = compare(lhs.getEndEffective(), rhs.getEndEffective()) != SAME;
        result[LOCATION] = compare(lhs.getLocation(), rhs.getLocation()) != SAME;
        result[TITLE] = compare(lhs.getTitle(), rhs.getTitle()) != SAME;

        return result;
    }

    @Override
    public float match(boolean[] difference) {
        float match = 1f;

        if (difference[TITLE]) {
            match *= 0.8f;
        }
        if (difference[DTSTART]) {
            match *= 0.8f;
        }

        if (difference[DTEND]) {
            match *= 0.85f;
        }

        if (difference[DESCRIPTION]) {
            match *= 0.95f;
        }
        if (difference[LOCATION]) {
            match *= 0.95f;
        }
        if (difference[ATTENDEES]) {
            match *= 0.95f;
        }

        return match;
    }
}
