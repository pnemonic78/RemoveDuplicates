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
package com.github.duplicates.bookmark;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.github.duplicates.DuplicateProvider;
import com.github.provider.Browser;

import static android.provider.BaseColumns._ID;
import static com.github.provider.Browser.BookmarkColumns.BOOKMARK;
import static com.github.provider.Browser.BookmarkColumns.CREATED;
import static com.github.provider.Browser.BookmarkColumns.DATE;
import static com.github.provider.Browser.BookmarkColumns.FAVICON;
import static com.github.provider.Browser.BookmarkColumns.TITLE;
import static com.github.provider.Browser.BookmarkColumns.URL;
import static com.github.provider.Browser.BookmarkColumns.VISITS;

/**
 * Provide duplicate bookmarks.
 *
 * @author moshe.w
 */
public class BookmarkProvider extends DuplicateProvider<BookmarkItem> {

    private static final String[] PERMISSIONS_READ = {"com.android.browser.permission.READ_HISTORY_BOOKMARKS"};
    private static final String[] PERMISSIONS_WRITE = {"com.android.browser.permission.WRITE_HISTORY_BOOKMARKS"};

    private static final String[] PROJECTION = {
            _ID,
            CREATED,
            DATE,
            FAVICON,
            TITLE,
            URL,
            VISITS
    };

    private static final int INDEX_ID = 0;
    private static final int INDEX_CREATED = 1;
    private static final int INDEX_DATE = 2;
    private static final int INDEX_FAVICON = 3;
    private static final int INDEX_TITLE = 4;
    private static final int INDEX_URL = 5;
    private static final int INDEX_VISITS = 6;

    public BookmarkProvider(Context context) {
        super(context);
    }

    @Override
    @NonNull
    protected Uri getContentUri() {
        return Browser.BOOKMARKS_URI;
    }

    @Override
    protected String[] getCursorProjection() {
        return PROJECTION;
    }

    @Override
    protected String getCursorSelection() {
        return BOOKMARK + "=1";
    }

    @Override
    public BookmarkItem createItem(Cursor cursor) {
        return new BookmarkItem();
    }

    @Override
    public void populateItem(Cursor cursor, BookmarkItem item) {
        item.setId(cursor.getLong(INDEX_ID));
        item.setCreated(cursor.getLong(INDEX_CREATED));
        item.setDate(cursor.getLong(INDEX_DATE));
        item.setFavIcon(cursor.getBlob(INDEX_FAVICON));
        item.setTitle(empty(cursor, INDEX_TITLE));
        item.setUrl(cursor.getString(INDEX_URL));
        item.setVisits(cursor.getInt(INDEX_VISITS));
    }

    @Override
    public boolean deleteItem(ContentResolver cr, BookmarkItem item) {
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
