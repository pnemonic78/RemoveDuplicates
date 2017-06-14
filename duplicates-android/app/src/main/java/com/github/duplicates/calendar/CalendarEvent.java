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

import java.util.TimeZone;

import static android.provider.CalendarContract.Attendees.ATTENDEE_STATUS_NONE;
import static android.provider.CalendarContract.Events.ACCESS_DEFAULT;
import static android.provider.CalendarContract.Events.AVAILABILITY_BUSY;
import static android.provider.CalendarContract.Events.STATUS_TENTATIVE;

/**
 * Calendar event.
 *
 * @author moshe.w
 */
public class CalendarEvent {

    private long id;
    private long calendarId;
    private String title;
    private String location;
    private String description;
    private int color;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(long calendarId) {
        this.calendarId = calendarId;
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
    }

    public TimeZone getStartTimeZone() {
        return startTimeZone;
    }

    public void setStartTimeZone(TimeZone startTimeZone) {
        this.startTimeZone = startTimeZone;
    }

    public void setStartTimeZone(String startTimeZone) {
        setStartTimeZone(TimeZone.getTimeZone(startTimeZone));
    }

    public TimeZone getEndTimeZone() {
        return endTimeZone;
    }

    public void setEndTimeZone(TimeZone endTimeZone) {
        this.endTimeZone = endTimeZone;
    }

    public void setEndTimezone(String endTimezone) {
        setEndTimeZone(TimeZone.getTimeZone(endTimezone));
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
}
