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

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import com.github.duplicates.DuplicateProvider;

import java.util.HashMap;
import java.util.Map;

import static android.provider.BaseColumns._ID;
import static android.provider.CalendarContract.Events.ALL_DAY;
import static android.provider.CalendarContract.Events.CALENDAR_ACCESS_LEVEL;
import static android.provider.CalendarContract.Events.CALENDAR_COLOR;
import static android.provider.CalendarContract.Events.CALENDAR_DISPLAY_NAME;
import static android.provider.CalendarContract.Events.CALENDAR_ID;
import static android.provider.CalendarContract.Events.CALENDAR_TIME_ZONE;
import static android.provider.CalendarContract.Events.DESCRIPTION;
import static android.provider.CalendarContract.Events.DTEND;
import static android.provider.CalendarContract.Events.DTSTART;
import static android.provider.CalendarContract.Events.EVENT_COLOR;
import static android.provider.CalendarContract.Events.EVENT_END_TIMEZONE;
import static android.provider.CalendarContract.Events.EVENT_LOCATION;
import static android.provider.CalendarContract.Events.EVENT_TIMEZONE;
import static android.provider.CalendarContract.Events.EXDATE;
import static android.provider.CalendarContract.Events.EXRULE;
import static android.provider.CalendarContract.Events.HAS_ATTENDEE_DATA;
import static android.provider.CalendarContract.Events.OWNER_ACCOUNT;
import static android.provider.CalendarContract.Events.RDATE;
import static android.provider.CalendarContract.Events.RRULE;
import static android.provider.CalendarContract.Events.TITLE;
import static android.provider.CalendarContract.Events.VISIBLE;

/**
 * Provide duplicate calendar events.
 *
 * @author moshe.w
 */
public class CalendarProvider extends DuplicateProvider<CalendarItem> {

    private static String[] PERMISSIONS_READ = {Manifest.permission.READ_CALENDAR};
    private static String[] PERMISSIONS_WRITE = {Manifest.permission.WRITE_CALENDAR};

    private static final String[] PROJECTION = {
            // Event.
            _ID,
            ALL_DAY,
            DESCRIPTION,
            DTEND,
            DTSTART,
            EVENT_COLOR,
            EVENT_END_TIMEZONE,
            EVENT_LOCATION,
            EVENT_TIMEZONE,
            EXDATE,
            EXRULE,
            HAS_ATTENDEE_DATA,
            RDATE,
            RRULE,
            TITLE,

            // Owner calendar.
            CALENDAR_ACCESS_LEVEL,
            CALENDAR_COLOR,
            CALENDAR_DISPLAY_NAME,
            CALENDAR_ID,
            CALENDAR_TIME_ZONE,
            OWNER_ACCOUNT,
            VISIBLE
    };

    private static final int INDEX_ID = 0;
    private static final int INDEX_ALL_DAY = 1;
    private static final int INDEX_DESCRIPTION = 2;
    private static final int INDEX_DTEND = 3;
    private static final int INDEX_DTSTART = 4;
    private static final int INDEX_EVENT_COLOR = 5;
    private static final int INDEX_EVENT_END_TIMEZONE = 6;
    private static final int INDEX_EVENT_LOCATION = 7;
    private static final int INDEX_EVENT_TIMEZONE = 8;
    private static final int INDEX_EXDATE = 9;
    private static final int INDEX_EXRULE = 10;
    private static final int INDEX_HAS_ATTENDEE_DATA = 11;
    private static final int INDEX_RDATE = 12;
    private static final int INDEX_RRULE = 13;
    private static final int INDEX_TITLE = 14;
    private static final int INDEX_CALENDAR_ACCESS_LEVEL = 15;
    private static final int INDEX_CALENDAR_COLOR = 16;
    private static final int INDEX_CALENDAR_DISPLAY_NAME = 17;
    private static final int INDEX_CALENDAR_ID = 18;
    private static final int INDEX_CALENDAR_TIME_ZONE = 19;
    private static final int INDEX_OWNER_ACCOUNT = 20;
    private static final int INDEX_VISIBLE = 21;

    private final Map<Long, CalendarEntity> calendars = new HashMap<>();

    public CalendarProvider(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return CalendarContract.Events.CONTENT_URI;
    }

    @Override
    protected String[] getCursorProjection() {
        return PROJECTION;
    }

    @Override
    public CalendarItem createItem() {
        return new CalendarItem();
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        calendars.clear();
    }

    @Override
    public void populateItem(Cursor cursor, CalendarItem item) {
        // Event data.
        item.setId(cursor.getLong(INDEX_ID));
        item.setAllDay(cursor.getInt(INDEX_ALL_DAY) != 0);
        item.setDescription(cursor.getString(INDEX_DESCRIPTION));
        item.setEnd(cursor.getLong(INDEX_DTEND));
        item.setStart(cursor.getLong(INDEX_DTSTART));
        item.setColor(cursor.getInt(INDEX_EVENT_COLOR));
        item.setEndTimeZone(cursor.getString(INDEX_EVENT_END_TIMEZONE));
        item.setLocation(cursor.getString(INDEX_EVENT_LOCATION));
        item.setStartTimeZone(cursor.getString(INDEX_EVENT_TIMEZONE));
        item.setExceptionDate(cursor.getString(INDEX_EXDATE));
        item.setExceptionRule(cursor.getString(INDEX_EXRULE));
        item.setHasAttendeeData(cursor.getInt(INDEX_HAS_ATTENDEE_DATA) != 0);
        item.setRecurrenceDate(cursor.getString(INDEX_RDATE));
        item.setRecurrenceRule(cursor.getString(INDEX_RRULE));
        item.setTitle(cursor.getString(INDEX_TITLE));

        // Calendar data.
        long calendarId = cursor.getLong(INDEX_CALENDAR_ID);
        CalendarEntity cal = calendars.get(calendarId);
        if (cal == null) {
            cal = item.getCalendar();
            cal.setId(calendarId);
            cal.setAccess(cursor.getInt(INDEX_CALENDAR_ACCESS_LEVEL));
            cal.setColor(cursor.getInt(INDEX_CALENDAR_COLOR));
            cal.setName(cursor.getString(INDEX_CALENDAR_DISPLAY_NAME));
            cal.setTimeZone(cursor.getString(INDEX_CALENDAR_TIME_ZONE));
            cal.setAccount(cursor.getString(INDEX_OWNER_ACCOUNT));
            cal.setVisible(cursor.getInt(INDEX_VISIBLE) != 0);
            calendars.put(calendarId, cal);
        }
    }

    @Override
    public boolean deleteItem(ContentResolver cr, CalendarItem item) {
        return cr.delete(getContentUri(), _ID + "=" + item.getId(), null) > 0;
    }

    @Override
    public String[] getReadPermissions() {
        return PERMISSIONS_READ;
    }

    @Override
    public String[] getDeletePermissions() {
        return PERMISSIONS_WRITE;
    }
}
