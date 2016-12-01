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

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Provider of duplicate items.
 *
 * @author moshe.w
 */
public abstract class DuplicateProvider<T extends DuplicateItem> {

    private final Context context;
    private DuplicateProviderListener<T, DuplicateProvider<T>> listener;

    protected DuplicateProvider(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return context;
    }

    /**
     * Get the items from the system provider.
     *
     * @return the list of items.
     */
    public List<T> getItems() {
        List<T> items = new ArrayList<>();
        Context context = getContext();
        ContentResolver cr = context.getContentResolver();

        Cursor cursor = cr.query(getCursorUri(), getCursorProjection(), null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                T item;
                do {
                    item = createItem();
                    populateItem(cursor, item);
                    items.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return items;
    }

    /**
     * Fetch the items from the system provider into the listener.
     *
     * @return the list of items.
     */
    public void fetchItems() {
        DuplicateProviderListener<T, DuplicateProvider<T>> listener = getListener();
        if (listener == null) {
            return;
        }
        Context context = getContext();
        ContentResolver cr = context.getContentResolver();

        Cursor cursor = cr.query(getCursorUri(), getCursorProjection(), null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                T item;
                do {
                    item = createItem();
                    populateItem(cursor, item);
                    listener.onItemFetched(this, item);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    protected abstract Uri getCursorUri();

    protected String[] getCursorProjection() {
        return null;
    }

    public abstract T createItem();

    public abstract void populateItem(Cursor cursor, T item);

    public DuplicateProviderListener<T, DuplicateProvider<T>> getListener() {
        return listener;
    }

    public void setListener(DuplicateProviderListener<T, DuplicateProvider<T>> listener) {
        this.listener = listener;
    }
}