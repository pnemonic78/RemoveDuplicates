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
package com.github.duplicates.message

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns._ID
import android.provider.Telephony
import android.provider.Telephony.TextBasedSmsColumns.*
import com.github.duplicates.DuplicateProvider

/**
 * Provide duplicate messages.
 *
 * @author moshe.w
 */
class MessageProvider(context: Context) : DuplicateProvider<MessageItem>(context) {

    override fun getContentUri(): Uri? {
        return Telephony.Sms.CONTENT_URI
    }

    override fun getCursorProjection(): Array<String>? {
        return PROJECTION
    }

    override fun createItem(cursor: Cursor): MessageItem? {
        return MessageItem()
    }

    override fun populateItem(cursor: Cursor, item: MessageItem) {
        item.id = cursor.getLong(INDEX_ID)
        item.address = empty(cursor, INDEX_ADDRESS)
        item.body = empty(cursor, INDEX_BODY)
        item.dateReceived = cursor.getLong(INDEX_DATE_RECEIVED)
        item.dateSent = cursor.getLong(INDEX_DATE_SENT)
        item.errorCode = cursor.getInt(INDEX_ERROR_CODE)
        item.isLocked = cursor.getInt(INDEX_LOCKED) != 0
        item.person = cursor.getInt(INDEX_PERSON)
        item.protocol = cursor.getInt(INDEX_PROTOCOL)
        item.isRead = cursor.getInt(INDEX_READ) != 0
        item.isSeen = cursor.getInt(INDEX_SEEN) != 0
        item.status = cursor.getInt(INDEX_STATUS)
        item.subject = empty(cursor, INDEX_SUBJECT)
        item.threadId = cursor.getLong(INDEX_THREAD_ID)
        item.type = cursor.getInt(INDEX_TYPE)
    }

    override fun getReadPermissions(): Array<String>? {
        return PERMISSIONS_READ
    }

    override fun getDeletePermissions(): Array<String>? {
        return PERMISSIONS_WRITE
    }

    companion object {

        private val PERMISSIONS_READ = arrayOf(Manifest.permission.READ_SMS)
        private val PERMISSIONS_WRITE = arrayOf("android.permission.WRITE_SMS")

        private val PROJECTION = arrayOf(
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
        )

        private const val INDEX_ID = 0
        private const val INDEX_ADDRESS = 1
        private const val INDEX_BODY = 2
        private const val INDEX_DATE_RECEIVED = 3
        private const val INDEX_DATE_SENT = 4
        private const val INDEX_ERROR_CODE = 5
        private const val INDEX_LOCKED = 6
        private const val INDEX_PERSON = 7
        private const val INDEX_PROTOCOL = 8
        private const val INDEX_READ = 9
        private const val INDEX_SEEN = 10
        private const val INDEX_STATUS = 11
        private const val INDEX_SUBJECT = 12
        private const val INDEX_THREAD_ID = 13
        private const val INDEX_TYPE = 14
    }
}
