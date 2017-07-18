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
package com.github.duplicates.calendar;

import com.android.calendarcommon2.RecurrenceSet;
import com.github.duplicates.DuplicateComparator;

import java.util.Calendar;
import java.util.TimeZone;

import static android.text.format.DateUtils.MINUTE_IN_MILLIS;
import static com.android.calendarcommon2.EventRecurrence.DAILY;
import static com.android.calendarcommon2.EventRecurrence.HOURLY;
import static com.android.calendarcommon2.EventRecurrence.MINUTELY;
import static com.android.calendarcommon2.EventRecurrence.MONTHLY;
import static com.android.calendarcommon2.EventRecurrence.SECONDLY;
import static com.android.calendarcommon2.EventRecurrence.WEEKLY;
import static com.android.calendarcommon2.EventRecurrence.YEARLY;

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

    private final Calendar cal1 = Calendar.getInstance();
    private final Calendar cal2 = Calendar.getInstance();

    @Override
    public int compare(CalendarItem lhs, CalendarItem rhs) {
        int c;

        c = compareIgnoreCase(lhs.getTitle(), rhs.getTitle());
        if (c != SAME) {
            return c;
        }
        c = compareIgnoreCase(lhs.getDescription(), rhs.getDescription());
        if (c != SAME) {
            return c;
        }
        c = compareIgnoreCase(lhs.getLocation(), rhs.getLocation());
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
    public float match(CalendarItem lhs, CalendarItem rhs, boolean[] difference) {
        if (difference == null) {
            difference = difference(lhs, rhs);
        }
        float match = MATCH_SAME;

        if (difference[TITLE]) {
            match *= 0.8f;
        }
        if (difference[DTSTART]) {
            match *= matchDate(lhs.getStart(), lhs.getStartTimeZone(), lhs.getRecurrenceSet(), rhs.getStart(), rhs.getStartTimeZone(), rhs.getRecurrenceSet());
        }
        if (difference[DTEND]) {
            match *= matchDate(lhs.getEndEffective(), lhs.getEndTimeZone(), lhs.getRecurrenceSet(), rhs.getEndEffective(), rhs.getEndTimeZone(), rhs.getRecurrenceSet());
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

    protected float matchDate(long lhs, TimeZone lhsTimeZone, RecurrenceSet lhsRecurrence, long rhs, TimeZone rhsTimeZone, RecurrenceSet rhsRecurrence) {
        long dt = Math.abs(lhs - rhs);
        if (dt < MINUTE_IN_MILLIS) {
            return MATCH_SAME;
        }

        if ((lhsRecurrence.rrules != null) && (rhsRecurrence.rrules != null)) {
            cal1.setTimeZone(lhsTimeZone);
            cal1.setTimeInMillis(lhs);
            cal2.setTimeZone(rhsTimeZone);
            cal2.setTimeInMillis(rhs);

            int lhsField = Calendar.MILLISECOND;
            int rhsField = Calendar.MILLISECOND;

            switch (lhsRecurrence.rrules[0].freq) {
                case SECONDLY:
                    lhsField = Calendar.SECOND;
                    break;
                case MINUTELY:
                    lhsField = Calendar.MINUTE;
                    break;
                case HOURLY:
                    lhsField = Calendar.HOUR_OF_DAY;
                    break;
                case DAILY:
                    lhsField = Calendar.DAY_OF_MONTH;
                    break;
                case WEEKLY:
                    lhsField = Calendar.WEEK_OF_YEAR;
                    break;
                case MONTHLY:
                    lhsField = Calendar.MONTH;
                    break;
                case YEARLY:
                    lhsField = Calendar.YEAR;
                    break;
            }

            switch (rhsRecurrence.rrules[0].freq) {
                case SECONDLY:
                    rhsField = Calendar.SECOND;
                    break;
                case MINUTELY:
                    rhsField = Calendar.MINUTE;
                    break;
                case HOURLY:
                    rhsField = Calendar.HOUR_OF_DAY;
                    break;
                case DAILY:
                    rhsField = Calendar.DAY_OF_MONTH;
                    break;
                case WEEKLY:
                    rhsField = Calendar.WEEK_OF_YEAR;
                    break;
                case MONTHLY:
                    rhsField = Calendar.MONTH;
                    break;
                case YEARLY:
                    rhsField = Calendar.YEAR;
                    break;
            }

            if (lhsField == rhsField) {
                if (lhs < rhs) {
                    cal1.add(lhsField, 1);
                } else {
                    cal2.add(rhsField, 1);
                }
                dt = Math.abs(cal1.getTimeInMillis() - cal2.getTimeInMillis());
                if (dt < MINUTE_IN_MILLIS) {
                    return 0.925f;
                }
            }
        }

        return 0.8f;
    }
}
