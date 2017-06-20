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
package com.github.duplicates.contact;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.github.duplicates.DuplicateProvider;

import static android.provider.BaseColumns._ID;
import static com.github.provider.Browser.BookmarkColumns.CREATED;
import static com.github.provider.Browser.BookmarkColumns.DATE;
import static com.github.provider.Browser.BookmarkColumns.FAVICON;
import static com.github.provider.Browser.BookmarkColumns.TITLE;
import static com.github.provider.Browser.BookmarkColumns.URL;
import static com.github.provider.Browser.BookmarkColumns.VISITS;

/**
 * Provide duplicate contacts.
 *
 * @author moshe.w
 */
public class ContactProvider extends DuplicateProvider<ContactItem> {

    private static String[] PERMISSIONS_READ = {Manifest.permission.READ_CONTACTS};
    private static String[] PERMISSIONS_WRITE = {Manifest.permission.WRITE_CONTACTS};

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

    public ContactProvider(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return ContactsContract.Contacts.CONTENT_URI;
    }

    @Override
    protected String[] getCursorProjection() {
        return PROJECTION;
    }

    @Override
    public ContactItem createItem() {
        return new ContactItem();
    }

    @Override
    public void populateItem(Cursor cursor, ContactItem item) {
        item.setId(cursor.getLong(INDEX_ID));
    }

    @Override
    public boolean deleteItem(ContentResolver cr, ContactItem item) {
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
