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
package com.github.duplicates.bookmark;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

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

    private static String[] PERMISSIONS_READ = {"com.android.browser.permission.READ_HISTORY_BOOKMARKS"};
    private static String[] PERMISSIONS_WRITE = {"com.android.browser.permission.WRITE_HISTORY_BOOKMARKS"};

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
    public BookmarkItem createItem() {
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
