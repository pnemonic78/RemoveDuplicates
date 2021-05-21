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
package com.github.duplicates.bookmark

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns._ID
import com.github.duplicates.DuplicateProvider
import com.github.provider.Browser
import com.github.provider.Browser.BookmarkColumns.BOOKMARK
import com.github.provider.Browser.BookmarkColumns.CREATED
import com.github.provider.Browser.BookmarkColumns.DATE
import com.github.provider.Browser.BookmarkColumns.FAVICON
import com.github.provider.Browser.BookmarkColumns.TITLE
import com.github.provider.Browser.BookmarkColumns.URL
import com.github.provider.Browser.BookmarkColumns.VISITS

/**
 * Provide duplicate bookmarks.
 *
 * @author moshe.w
 */
class BookmarkProvider(context: Context) : DuplicateProvider<BookmarkItem>(context) {

    override fun getContentUri(): Uri {
        return Browser.BOOKMARKS_URI
    }

    override fun getCursorProjection(): Array<String> {
        return PROJECTION
    }

    override fun getCursorSelection(): String {
        return "$BOOKMARK=1"
    }

    override fun createItem(cursor: Cursor): BookmarkItem {
        return BookmarkItem()
    }

    override fun populateItem(cursor: Cursor, item: BookmarkItem) {
        item.id = cursor.getLong(INDEX_ID)
        item.created = cursor.getLong(INDEX_CREATED)
        item.date = cursor.getLong(INDEX_DATE)
        item.favIcon = cursor.getBlob(INDEX_FAVICON)
        item.title = empty(cursor, INDEX_TITLE)
        item.setUrl(cursor.getString(INDEX_URL))
        item.visits = cursor.getInt(INDEX_VISITS)
    }

    override fun deleteItem(cr: ContentResolver, contentUri: Uri, item: BookmarkItem): Boolean {
        return cr.delete(contentUri, _ID + "=" + item.id, null) > 0
    }

    override fun getReadPermissions(): Array<String> {
        return PERMISSIONS_READ
    }

    override fun getDeletePermissions(): Array<String> {
        return PERMISSIONS_WRITE
    }

    companion object {

        private val PERMISSIONS_READ =
            arrayOf("com.android.browser.permission.READ_HISTORY_BOOKMARKS")
        private val PERMISSIONS_WRITE =
            arrayOf("com.android.browser.permission.WRITE_HISTORY_BOOKMARKS")

        private val PROJECTION = arrayOf(_ID, CREATED, DATE, FAVICON, TITLE, URL, VISITS)

        private const val INDEX_ID = 0
        private const val INDEX_CREATED = 1
        private const val INDEX_DATE = 2
        private const val INDEX_FAVICON = 3
        private const val INDEX_TITLE = 4
        private const val INDEX_URL = 5
        private const val INDEX_VISITS = 6
    }
}
