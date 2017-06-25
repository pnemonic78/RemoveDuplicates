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
package com.github.duplicates.alarm;

import android.text.format.DateUtils;

import com.github.duplicates.DuplicateComparator;

/**
 * Compare duplicate alarms.
 *
 * @author moshe.w
 */
public class AlarmComparator extends DuplicateComparator<AlarmItem> {

    public static final int ALARM_TIME = 0;
    public static final int ALERT_TIME = 1;
    public static final int NAME = 2;
    public static final int REPEAT = 3;

    @Override
    public int compare(AlarmItem lhs, AlarmItem rhs) {
        int c;

        c = compare(lhs.getActivate(), rhs.getActivate());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getAlarmTime(), rhs.getAlarmTime());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getAlertTime(), rhs.getAlertTime());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getCreateTime(), rhs.getCreateTime());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.isDailyBriefing(), rhs.isDailyBriefing());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getName(), rhs.getName());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getNotificationType(), rhs.getNotificationType());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getRepeat(), rhs.getRepeat());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.isSnoozeActivate(), rhs.isSnoozeActivate());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getSnoozeDoneCount(), rhs.getSnoozeDoneCount());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getSnoozeDuration(), rhs.getSnoozeDuration());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getSnoozeRepeat(), rhs.getSnoozeRepeat());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getSoundTone(), rhs.getSoundTone());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getSoundType(), rhs.getSoundType());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getSoundUri(), rhs.getSoundUri());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.isSubdueActivate(), rhs.isSubdueActivate());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getSubdueDuration(), rhs.getSubdueDuration());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getSubdueTone(), rhs.getSubdueTone());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getSubdueUri(), rhs.getSubdueUri());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getVolume(), rhs.getVolume());
        if (c != SAME) {
            return c;
        }

        return super.compare(lhs, rhs);
    }

    @Override
    public boolean[] difference(AlarmItem lhs, AlarmItem rhs) {
        boolean[] result = new boolean[4];

        result[ALARM_TIME] = compare(lhs.getAlarmTime(), rhs.getAlarmTime()) != SAME;
        result[ALERT_TIME] = compareTime(lhs.getAlertTime(), rhs.getAlertTime(), DateUtils.MINUTE_IN_MILLIS) != SAME;
        result[NAME] = compareIgnoreCase(lhs.getName(), rhs.getName()) != SAME;
        result[REPEAT] = compare(lhs.getRepeat(), rhs.getRepeat()) != SAME;

        return result;
    }

    @Override
    public float match(AlarmItem lhs, AlarmItem rhs, boolean[] difference) {
        if (difference == null) {
            difference = difference(lhs, rhs);
        }
        float match = MATCH_SAME;

        if (difference[ALARM_TIME]) {
            match *= 0.7f;
        }

        if (difference[REPEAT]) {
            match *= 0.8f;
        }

        if (difference[ALERT_TIME]) {
            match *= 0.9f;
        }
        if (difference[NAME]) {
            match *= 0.9f;
        }

        return match;
    }
}
