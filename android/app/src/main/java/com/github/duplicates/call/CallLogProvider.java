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

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import com.github.duplicates.DuplicateProvider;

import static android.provider.BaseColumns._ID;
import static android.provider.CallLog.Calls.CACHED_NAME;
import static android.provider.CallLog.Calls.CACHED_NUMBER_LABEL;
import static android.provider.CallLog.Calls.CACHED_NUMBER_TYPE;
import static android.provider.CallLog.Calls.CONTENT_ITEM_TYPE;
import static android.provider.CallLog.Calls.CONTENT_TYPE;
import static android.provider.CallLog.Calls.DATE;
import static android.provider.CallLog.Calls.DURATION;
import static android.provider.CallLog.Calls.IS_READ;
import static android.provider.CallLog.Calls.NEW;
import static android.provider.CallLog.Calls.NUMBER;
import static android.provider.CallLog.Calls.TYPE;

/**
 * Provide duplicate calls.
 *
 * @author moshe.w
 */
public class CallLogProvider extends DuplicateProvider<CallLogItem> {

    private static String[] PERMISSIONS_READ = {"android.permission.READ_CALL_LOG"};
    private static String[] PERMISSIONS_WRITE = {"android.permission.WRITE_CALL_LOG"};

    private static final String[] PROJECTION = {
            _ID,
            CACHED_NAME,
            CACHED_NUMBER_LABEL,
            CACHED_NUMBER_TYPE,
            CONTENT_ITEM_TYPE,
            CONTENT_TYPE,
            DATE,
            DURATION,
            IS_READ,
            NEW,
            NUMBER,
            TYPE
    };

    private static final int INDEX_ID = 0;
    private static final int INDEX_CACHED_NAME = 1;
    private static final int INDEX_CACHED_NUMBER_LABEL = 2;
    private static final int INDEX_CACHED_NUMBER_TYPE = 3;
    private static final int INDEX_CONTENT_ITEM_TYPE = 4;
    private static final int INDEX_CONTENT_TYPE = 5;
    private static final int INDEX_DATE = 6;
    private static final int INDEX_DURATION = 7;
    private static final int INDEX_READ = 8;
    private static final int INDEX_NEW = 9;
    private static final int INDEX_NUMBER = 10;
    private static final int INDEX_TYPE = 11;

    public CallLogProvider(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return CallLog.CONTENT_URI;
    }

    @Override
    protected String[] getCursorProjection() {
        return PROJECTION;
    }

    @Override
    public CallLogItem createItem() {
        return new CallLogItem();
    }

    @Override
    public void populateItem(Cursor cursor, CallLogItem item) {
        item.setId(cursor.getLong(INDEX_ID));
        item.setName(cursor.getString(INDEX_CACHED_NAME));
        item.setNumberLabel(cursor.getString(INDEX_CACHED_NUMBER_LABEL));
        item.setNumberType(cursor.getInt(INDEX_CACHED_NUMBER_TYPE));
        item.setContentItemType(cursor.getString(INDEX_CONTENT_ITEM_TYPE));
        item.setContentType(cursor.getString(INDEX_CONTENT_TYPE));
        item.setDate(cursor.getLong(INDEX_DATE));
        item.setDuration(cursor.getLong(INDEX_DURATION));
        item.setNew(cursor.getInt(INDEX_NEW) != 0);
        item.setNumber(cursor.getString(INDEX_NUMBER));
        item.setRead(cursor.getInt(INDEX_READ) != 0);
        item.setType(cursor.getInt(INDEX_TYPE));
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