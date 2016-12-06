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
package com.github.duplicates.message;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.github.duplicates.DuplicateProvider;

import static android.provider.BaseColumns._ID;

/**
 * Provide duplicate messages for Jellybean versions and older.
 *
 * @author moshe.w
 */
public class JellybeanMessageProvider extends DuplicateProvider<MessageItem> {

    private static final String[] PROJECTION = {
            _ID,
            "address",
            "body",
            "date",
            "date_sent",
            "error_code",
            "locked",
            "person",
            "protocol",
            "read",
            "seen",
            "status",
            "subject",
            "thread_id",
            "type"
    };

    private static final int INDEX_ID = 0;
    private static final int INDEX_ADDRESS = 1;
    private static final int INDEX_BODY = 2;
    private static final int INDEX_DATE_RECEIVED = 3;
    private static final int INDEX_DATE_SENT = 4;
    private static final int INDEX_ERROR_CODE = 5;
    private static final int INDEX_LOCKED = 6;
    private static final int INDEX_PERSON = 7;
    private static final int INDEX_PROTOCOL = 8;
    private static final int INDEX_READ = 9;
    private static final int INDEX_SEEN = 10;
    private static final int INDEX_STATUS = 11;
    private static final int INDEX_SUBJECT = 12;
    private static final int INDEX_THREAD_ID = 13;
    private static final int INDEX_TYPE = 14;

    public JellybeanMessageProvider(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return Uri.parse("content://sms");
    }

    @Override
    protected String[] getCursorProjection() {
        return PROJECTION;
    }

    @Override
    public MessageItem createItem() {
        return new MessageItem();
    }

    @Override
    public void populateItem(Cursor cursor, MessageItem item) {
        item.setId(cursor.getLong(INDEX_ID));
        item.setAddress(cursor.getString(INDEX_ADDRESS));
        item.setBody(cursor.getString(INDEX_BODY));
        item.setDateReceived(cursor.getLong(INDEX_DATE_RECEIVED));
        item.setDateSent(cursor.getLong(INDEX_DATE_SENT));
        item.setErrorCode(cursor.getInt(INDEX_ERROR_CODE));
        item.setLocked(cursor.getInt(INDEX_LOCKED) != 0);
        item.setPerson(cursor.getInt(INDEX_PERSON));
        item.setProtocol(cursor.getInt(INDEX_PROTOCOL));
        item.setRead(cursor.getInt(INDEX_READ) != 0);
        item.setSeen(cursor.getInt(INDEX_SEEN) != 0);
        item.setStatus(cursor.getInt(INDEX_STATUS));
        item.setSubject(cursor.getString(INDEX_SUBJECT));
        item.setThreadId(cursor.getLong(INDEX_THREAD_ID));
        item.setType(cursor.getInt(INDEX_TYPE));
    }
}
