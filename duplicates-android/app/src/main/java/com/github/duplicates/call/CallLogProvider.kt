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
package com.github.duplicates.call

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog

import com.github.duplicates.DuplicateProvider

import android.provider.BaseColumns._ID
import android.provider.CallLog.Calls.CACHED_NAME
import android.provider.CallLog.Calls.CACHED_NUMBER_LABEL
import android.provider.CallLog.Calls.CACHED_NUMBER_TYPE
import android.provider.CallLog.Calls.DATE
import android.provider.CallLog.Calls.DURATION
import android.provider.CallLog.Calls.IS_READ
import android.provider.CallLog.Calls.NEW
import android.provider.CallLog.Calls.NUMBER
import android.provider.CallLog.Calls.TYPE

/**
 * Provide duplicate calls.
 *
 * @author moshe.w
 */
class CallLogProvider(context: Context) : DuplicateProvider<CallLogItem>(context) {

    override fun getContentUri(): Uri {
        return CallLog.Calls.CONTENT_URI
    }

    override fun getCursorProjection(): Array<String>? {
        return PROJECTION
    }

    override fun createItem(cursor: Cursor): CallLogItem? {
        return CallLogItem()
    }

    override fun populateItem(cursor: Cursor, item: CallLogItem) {
        item.id = cursor.getLong(INDEX_ID)
        item.name = cursor.getString(INDEX_CACHED_NAME)
        item.numberLabel = cursor.getString(INDEX_CACHED_NUMBER_LABEL)
        item.numberType = cursor.getInt(INDEX_CACHED_NUMBER_TYPE)
        item.date = cursor.getLong(INDEX_DATE)
        item.duration = cursor.getLong(INDEX_DURATION)
        item.isNew = cursor.getInt(INDEX_NEW) != 0
        item.number = cursor.getString(INDEX_NUMBER)
        item.isRead = cursor.getInt(INDEX_READ) != 0
        item.type = cursor.getInt(INDEX_TYPE)
    }

    override fun deleteItem(cr: ContentResolver, item: CallLogItem): Boolean {
        return cr.delete(getContentUri(), _ID + "=" + item.id, null) > 0
    }

    override fun getReadPermissions(): Array<String>? {
        return PERMISSIONS_READ
    }

    override fun getDeletePermissions(): Array<String>? {
        return PERMISSIONS_WRITE
    }

    companion object {

        private val PERMISSIONS_READ = arrayOf("android.permission.READ_CALL_LOG", Manifest.permission.READ_CONTACTS)
        private val PERMISSIONS_WRITE = arrayOf("android.permission.WRITE_CALL_LOG", Manifest.permission.WRITE_CONTACTS)

        private val PROJECTION = arrayOf(_ID, CACHED_NAME, CACHED_NUMBER_LABEL, CACHED_NUMBER_TYPE, DATE, DURATION, IS_READ, NEW, NUMBER, TYPE)

        private const val INDEX_ID = 0
        private const val INDEX_CACHED_NAME = 1
        private const val INDEX_CACHED_NUMBER_LABEL = 2
        private const val INDEX_CACHED_NUMBER_TYPE = 3
        private const val INDEX_DATE = 4
        private const val INDEX_DURATION = 5
        private const val INDEX_READ = 6
        private const val INDEX_NEW = 7
        private const val INDEX_NUMBER = 8
        private const val INDEX_TYPE = 9
    }
}
