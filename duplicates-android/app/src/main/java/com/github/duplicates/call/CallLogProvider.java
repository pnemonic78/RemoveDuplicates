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
package com.github.duplicates.call;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import com.github.duplicates.DuplicateProvider;

import androidx.annotation.NonNull;

import static android.provider.BaseColumns._ID;
import static android.provider.CallLog.Calls.CACHED_NAME;
import static android.provider.CallLog.Calls.CACHED_NUMBER_LABEL;
import static android.provider.CallLog.Calls.CACHED_NUMBER_TYPE;
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

    private static final String[] PERMISSIONS_READ = {"android.permission.READ_CALL_LOG", Manifest.permission.READ_CONTACTS};
    private static final String[] PERMISSIONS_WRITE = {"android.permission.WRITE_CALL_LOG", Manifest.permission.WRITE_CONTACTS};

    private static final String[] PROJECTION = {
            _ID,
            CACHED_NAME,
            CACHED_NUMBER_LABEL,
            CACHED_NUMBER_TYPE,
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
    private static final int INDEX_DATE = 4;
    private static final int INDEX_DURATION = 5;
    private static final int INDEX_READ = 6;
    private static final int INDEX_NEW = 7;
    private static final int INDEX_NUMBER = 8;
    private static final int INDEX_TYPE = 9;

    public CallLogProvider(Context context) {
        super(context);
    }

    @Override
    @NonNull
    protected Uri getContentUri() {
        return CallLog.Calls.CONTENT_URI;
    }

    @Override
    protected String[] getCursorProjection() {
        return PROJECTION;
    }

    @Override
    public CallLogItem createItem(Cursor cursor) {
        return new CallLogItem();
    }

    @Override
    public void populateItem(Cursor cursor, CallLogItem item) {
        item.setId(cursor.getLong(INDEX_ID));
        item.setName(cursor.getString(INDEX_CACHED_NAME));
        item.setNumberLabel(cursor.getString(INDEX_CACHED_NUMBER_LABEL));
        item.setNumberType(cursor.getInt(INDEX_CACHED_NUMBER_TYPE));
        item.setDate(cursor.getLong(INDEX_DATE));
        item.setDuration(cursor.getLong(INDEX_DURATION));
        item.setNew(cursor.getInt(INDEX_NEW) != 0);
        item.setNumber(cursor.getString(INDEX_NUMBER));
        item.setRead(cursor.getInt(INDEX_READ) != 0);
        item.setType(cursor.getInt(INDEX_TYPE));
    }

    @Override
    public boolean deleteItem(ContentResolver cr, CallLogItem item) {
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
