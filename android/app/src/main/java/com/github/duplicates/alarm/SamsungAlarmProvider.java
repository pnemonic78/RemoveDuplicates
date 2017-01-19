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

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.github.duplicates.DuplicateProvider;

import static android.provider.BaseColumns._ID;

/**
 * Provide duplicate messages for Samsung devices.
 *
 * @author moshe.w
 */
public class SamsungAlarmProvider extends DuplicateProvider<AlarmItem> {

    public static final String PACKAGE = "com.sec.android.app.clockpackage";

    private static String[] PERMISSIONS_READ = {"com.sec.android.app.clockpackage.permission.READ_ALARM"};
    private static String[] PERMISSIONS_WRITE = {"com.sec.android.app.clockpackage.permission.WRITE_ALARM"};

    private static final String[] PROJECTION = {
            _ID,
            "active",
            "createtime",
            "alerttime",
            "alarmtime",
            "repeattype",
            "notitype",
            "snzactive",
            "snzduration",
            "snzrepeat",
            "snzcount",
            "dailybrief",
            "sbdactive",
            "sbdduration",
            "sbdtone",
            "alarmsound",
            "alarmtone",
            "volume",
            "sbduri",
            "alarmuri",
            "name"
    };

    private static final int INDEX_ID = 0;
    private static final int INDEX_ACTIVE = 1;
    private static final int INDEX_CREATETIME = 2;
    private static final int INDEX_ALERTTIME = 3;
    private static final int INDEX_ALARMTIME = 4;
    private static final int INDEX_REPEATTYPE = 5;
    private static final int INDEX_NOTITYPE = 6;
    private static final int INDEX_SNZACTIVE = 7;
    private static final int INDEX_SNZDURATION = 8;
    private static final int INDEX_SNZREPEAT = 9;
    private static final int INDEX_SNZCOUNT = 10;
    private static final int INDEX_DAILYBRIEF = 11;
    private static final int INDEX_SBDACTIVE = 12;
    private static final int INDEX_SBDDURATION = 13;
    private static final int INDEX_SBDTONE = 14;
    private static final int INDEX_ALARMSOUND = 15;
    private static final int INDEX_ALARMTONE = 16;
    private static final int INDEX_VOLUME = 17;
    private static final int INDEX_SBDURI = 18;
    private static final int INDEX_ALARMURI = 19;
    private static final int INDEX_NAME = 20;

    public SamsungAlarmProvider(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return Uri.parse("content://com.samsung.sec.android.clockpackage/alarm");
    }

    @Override
    protected String[] getCursorProjection() {
        return PROJECTION;
    }

    @Override
    public AlarmItem createItem() {
        return new AlarmItem();
    }

    @Override
    public void populateItem(Cursor cursor, AlarmItem item) {
        item.setId(cursor.getLong(INDEX_ID));
        item.setActivate(cursor.getInt(INDEX_ACTIVE));
        item.setSoundTone(cursor.getInt(INDEX_ALARMSOUND));
        item.setAlarmTime(cursor.getInt(INDEX_ALARMTIME));
        item.setSoundTone(cursor.getInt(INDEX_ALARMTONE));
        item.setSoundUri(cursor.getString(INDEX_ALARMURI));
        item.setAlertTime(cursor.getLong(INDEX_ALERTTIME));
        item.setCreateTime(cursor.getLong(INDEX_CREATETIME));
        item.setDailyBriefing(cursor.getInt(INDEX_DAILYBRIEF) != 0);
        item.setName(cursor.getString(INDEX_NAME));
        item.setNotificationType(cursor.getInt(INDEX_NOTITYPE));
        item.setRepeatType(cursor.getInt(INDEX_REPEATTYPE));
        item.setSubdueActivate(cursor.getInt(INDEX_SBDACTIVE) != 0);
        item.setSubdueDuration(cursor.getInt(INDEX_SBDDURATION));
        item.setSubdueTone(cursor.getInt(INDEX_SBDTONE));
        item.setSubdueUri(cursor.getInt(INDEX_SBDURI));
        item.setSnoozeActivate(cursor.getInt(INDEX_SNZACTIVE) != 0);
        item.setSnoozeDoneCount(cursor.getInt(INDEX_SNZCOUNT));
        item.setSnoozeDuration(cursor.getInt(INDEX_SNZDURATION));
        item.setSnoozeRepeat(cursor.getInt(INDEX_SNZREPEAT));
        item.setVolume(cursor.getInt(INDEX_VOLUME));
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