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

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.Time;

import com.android.calendarcommon2.EventRecurrence;
import com.android.calendarcommon2.RecurrenceSet;
import com.github.duplicates.DuplicateItem;

import java.util.Calendar;
import java.util.TimeZone;

import static android.provider.CalendarContract.Attendees.ATTENDEE_STATUS_NONE;
import static android.provider.CalendarContract.Events.ACCESS_DEFAULT;
import static android.provider.CalendarContract.Events.AVAILABILITY_BUSY;
import static android.provider.CalendarContract.Events.STATUS_TENTATIVE;

/**
 * Duplicate calendar event.
 *
 * @author moshe.w
 */
public class CalendarItem extends DuplicateItem {

    public static final long NEVER = Long.MIN_VALUE;

    private long id;
    private String title;
    private String location;
    private String description;
    private int color = Color.TRANSPARENT;
    private int status = STATUS_TENTATIVE;
    private int selfAttendeeStatus = ATTENDEE_STATUS_NONE;
    private long start;
    private long end;
    private TimeZone startTimeZone;
    private TimeZone endTimeZone;
    private boolean allDay;
    private int accessLevel = ACCESS_DEFAULT;
    private int availability = AVAILABILITY_BUSY;
    private String recurrenceDate;
    private String recurrenceRule;
    private String exceptionRule;
    private String exceptionDate;
    private long originalEventId;
    private long originalInstanceTime;
    private boolean originalAllDay;
    private Long lastDate;
    private boolean hasAttendeeData;
    private String organizer;

    private CalendarEntity calendar;
    private Long endEffective;
    private RecurrenceSet recurrenceSet;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSelfAttendeeStatus() {
        return selfAttendeeStatus;
    }

    public void setSelfAttendeeStatus(int selfAttendeeStatus) {
        this.selfAttendeeStatus = selfAttendeeStatus;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
        this.endEffective = null;
    }

    public TimeZone getStartTimeZone() {
        return startTimeZone;
    }

    public void setStartTimeZone(TimeZone startTimeZone) {
        this.startTimeZone = startTimeZone;
    }

    public void setStartTimeZone(String startTimeZone) {
        setStartTimeZone(startTimeZone != null ? TimeZone.getTimeZone(startTimeZone) : null);
    }

    public TimeZone getEndTimeZone() {
        if (endTimeZone == null) {
            return getStartTimeZone();
        }
        return endTimeZone;
    }

    public void setEndTimeZone(TimeZone endTimeZone) {
        this.endTimeZone = endTimeZone;
        this.endEffective = null;
    }

    public void setEndTimeZone(String endTimeZone) {
        setEndTimeZone(endTimeZone != null ? TimeZone.getTimeZone(endTimeZone) : null);
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getRecurrenceDate() {
        return recurrenceDate;
    }

    public void setRecurrenceDate(String recurrenceDate) {
        this.recurrenceDate = recurrenceDate;
    }

    public String getRecurrenceRule() {
        return recurrenceRule;
    }

    public void setRecurrenceRule(String recurrenceRule) {
        this.recurrenceRule = recurrenceRule;
    }

    public String getExceptionRule() {
        return exceptionRule;
    }

    public void setExceptionRule(String exceptionRule) {
        this.exceptionRule = exceptionRule;
    }

    public String getExceptionDate() {
        return exceptionDate;
    }

    public void setExceptionDate(String exceptionDate) {
        this.exceptionDate = exceptionDate;
    }

    public long getOriginalEventId() {
        return originalEventId;
    }

    public void setOriginalEventId(long originalEventId) {
        this.originalEventId = originalEventId;
    }

    public long getOriginalInstanceTime() {
        return originalInstanceTime;
    }

    public void setOriginalInstanceTime(long originalInstanceTime) {
        this.originalInstanceTime = originalInstanceTime;
    }

    public boolean isOriginalAllDay() {
        return originalAllDay;
    }

    public void setOriginalAllDay(boolean originalAllDay) {
        this.originalAllDay = originalAllDay;
    }

    public Long getLastDate() {
        return lastDate;
    }

    public void setLastDate(Long lastDate) {
        this.lastDate = lastDate;
        this.endEffective = null;
    }

    public boolean isHasAttendeeData() {
        return hasAttendeeData;
    }

    public void setHasAttendeeData(boolean hasAttendeeData) {
        this.hasAttendeeData = hasAttendeeData;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    @NonNull
    public CalendarEntity getCalendar() {
        if (calendar == null) {
            calendar = new CalendarEntity();
        }
        return calendar;
    }

    public void setCalendar(CalendarEntity calendar) {
        this.calendar = calendar;
    }

    public long getEndEffective() {
        if (endEffective == null) {
            long start = getStart();
            long end = getEnd();
            if (end < start) {
                if (isAllDay()) {
                    Calendar gcal = Calendar.getInstance(getEndTimeZone());
                    gcal.setTimeInMillis(start);
                    gcal.set(Calendar.HOUR_OF_DAY, 23);
                    gcal.set(Calendar.MINUTE, 0);
                    gcal.set(Calendar.SECOND, 0);
                    gcal.set(Calendar.MILLISECOND, 0);
                    end = gcal.getTimeInMillis();
                } else {
                    RecurrenceSet recurrenceSet = getRecurrenceSet();
                    if ((recurrenceSet.rrules != null) && (recurrenceSet.rrules.length > 0)) {
                        EventRecurrence recurrence = recurrenceSet.rrules[0];
                        if (!TextUtils.isEmpty(recurrence.until)) {
                            Time until = new Time();
                            until.parse(recurrence.until);
                            end = until.normalize(true);
                        }
                    }
                    if (end == NEVER) {
                        if (getLastDate() != null) {
                            end = getLastDate();
                        }
                    }
                }
            }
            endEffective = end;
        }
        return endEffective;
    }

    public int getDisplayColor() {
        return getColor() == Color.TRANSPARENT ? getCalendar().getColor() : getColor();
    }

    public boolean hasRecurrence() {
        return getRecurrenceSet().hasRecurrence();
    }

    @NonNull
    public RecurrenceSet getRecurrenceSet() {
        if (recurrenceSet == null) {
            recurrenceSet = new RecurrenceSet(getRecurrenceRule(), getRecurrenceDate(), getExceptionRule(), getExceptionDate());
        }
        return recurrenceSet;
    }
}
