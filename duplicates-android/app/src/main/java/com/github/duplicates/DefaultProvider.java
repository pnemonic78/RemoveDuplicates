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
package com.github.duplicates;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Default provider that does nothing.
 *
 * @author moshe.w
 */
public class DefaultProvider<T extends DuplicateItem> extends DuplicateProvider<T> {

    public DefaultProvider(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return null;
    }

    @Override
    public T createItem(Cursor cursor) {
        return null;
    }

    @Override
    public void populateItem(Cursor cursor, T item) {
    }

    @Override
    public String[] getReadPermissions() {
        return null;
    }

    @Override
    public String[] getDeletePermissions() {
        return null;
    }
}
