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
package com.github.duplicates.message;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;

import com.github.duplicates.DuplicateProvider;

import static android.provider.BaseColumns._ID;
import static android.provider.Telephony.TextBasedSmsColumns.ADDRESS;
import static android.provider.Telephony.TextBasedSmsColumns.BODY;
import static android.provider.Telephony.TextBasedSmsColumns.DATE;
import static android.provider.Telephony.TextBasedSmsColumns.DATE_SENT;
import static android.provider.Telephony.TextBasedSmsColumns.ERROR_CODE;
import static android.provider.Telephony.TextBasedSmsColumns.LOCKED;
import static android.provider.Telephony.TextBasedSmsColumns.PERSON;
import static android.provider.Telephony.TextBasedSmsColumns.PROTOCOL;
import static android.provider.Telephony.TextBasedSmsColumns.READ;
import static android.provider.Telephony.TextBasedSmsColumns.SEEN;
import static android.provider.Telephony.TextBasedSmsColumns.STATUS;
import static android.provider.Telephony.TextBasedSmsColumns.SUBJECT;
import static android.provider.Telephony.TextBasedSmsColumns.THREAD_ID;
import static android.provider.Telephony.TextBasedSmsColumns.TYPE;

/**
 * Provide duplicate messages.
 *
 * @author moshe.w
 */
public class MessageProvider extends DuplicateProvider<MessageItem> {

    private static String[] PERMISSIONS_READ = {Manifest.permission.READ_SMS};
    private static String[] PERMISSIONS_WRITE = {"android.permission.WRITE_SMS"};

    private static final String[] PROJECTION = {
            _ID,
            ADDRESS,
            BODY,
            DATE,
            DATE_SENT,
            ERROR_CODE,
            LOCKED,
            PERSON,
            PROTOCOL,
            READ,
            SEEN,
            STATUS,
            SUBJECT,
            THREAD_ID,
            TYPE
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

    public MessageProvider(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return Telephony.Sms.CONTENT_URI;
    }

    @Override
    protected String[] getCursorProjection() {
        return PROJECTION;
    }

    @Override
    public MessageItem createItem(Cursor cursor) {
        return new MessageItem();
    }

    @Override
    public void populateItem(Cursor cursor, MessageItem item) {
        item.setId(cursor.getLong(INDEX_ID));
        item.setAddress(empty(cursor, INDEX_ADDRESS));
        item.setBody(empty(cursor, INDEX_BODY));
        item.setDateReceived(cursor.getLong(INDEX_DATE_RECEIVED));
        item.setDateSent(cursor.getLong(INDEX_DATE_SENT));
        item.setErrorCode(cursor.getInt(INDEX_ERROR_CODE));
        item.setLocked(cursor.getInt(INDEX_LOCKED) != 0);
        item.setPerson(cursor.getInt(INDEX_PERSON));
        item.setProtocol(cursor.getInt(INDEX_PROTOCOL));
        item.setRead(cursor.getInt(INDEX_READ) != 0);
        item.setSeen(cursor.getInt(INDEX_SEEN) != 0);
        item.setStatus(cursor.getInt(INDEX_STATUS));
        item.setSubject(empty(cursor, INDEX_SUBJECT));
        item.setThreadId(cursor.getLong(INDEX_THREAD_ID));
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
