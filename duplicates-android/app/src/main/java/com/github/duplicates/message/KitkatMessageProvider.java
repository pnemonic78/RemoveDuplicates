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

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.*;

import static android.provider.BaseColumns._ID;

/**
 * Provide duplicate messages for KitKat versions and newer.
 *
 * @author moshe.w
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class KitkatMessageProvider extends JellybeanMessageProvider {

    private static final String[] PROJECTION = {
            _ID,
            Telephony.TextBasedSmsColumns.ADDRESS,
            Telephony.TextBasedSmsColumns.BODY,
            Telephony.TextBasedSmsColumns.DATE,
            Telephony.TextBasedSmsColumns.DATE_SENT,
            Telephony.TextBasedSmsColumns.ERROR_CODE,
            Telephony.TextBasedSmsColumns.LOCKED,
            Telephony.TextBasedSmsColumns.PERSON,
            Telephony.TextBasedSmsColumns.PROTOCOL,
            Telephony.TextBasedSmsColumns.READ,
            Telephony.TextBasedSmsColumns.SEEN,
            Telephony.TextBasedSmsColumns.STATUS,
            Telephony.TextBasedSmsColumns.SUBJECT,
            Telephony.TextBasedSmsColumns.THREAD_ID,
            Telephony.TextBasedSmsColumns.TYPE
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

    public KitkatMessageProvider(Context context) {
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
}
