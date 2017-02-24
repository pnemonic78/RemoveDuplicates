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
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * Provider of duplicate items.
 *
 * @author moshe.w
 */
public abstract class DuplicateProvider<T extends DuplicateItem> {

    private final Context context;
    private DuplicateProviderListener<T, DuplicateProvider<T>> listener;
    private boolean cancelled;

    protected DuplicateProvider(Context context) {
        this.context = context;
    }

    public DuplicateProviderListener<T, DuplicateProvider<T>> getListener() {
        return listener;
    }

    public void setListener(DuplicateProviderListener<T, DuplicateProvider<T>> listener) {
        this.listener = listener;
    }

    protected Context getContext() {
        return context;
    }

    protected abstract Uri getContentUri();

    protected String[] getCursorProjection() {
        return null;
    }

    protected String getCursorSelection() {
        return null;
    }

    public abstract T createItem();

    public abstract void populateItem(Cursor cursor, T item);

    /**
     * Get the items from the system provider.
     *
     * @return the list of items.
     * @throws CancellationException if the provider has been cancelled.
     */
    public List<T> getItems() throws CancellationException {
        if (isCancelled()) {
            throw new CancellationException();
        }
        List<T> items = new ArrayList<>();
        Context context = getContext();
        ContentResolver cr = context.getContentResolver();

        Cursor cursor = cr.query(getContentUri(), getCursorProjection(), null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                T item;
                do {
                    if (isCancelled()) {
                        break;
                    }
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
     * @throws CancellationException if the provider has been cancelled.
     */
    public void fetchItems() throws CancellationException {
        if (isCancelled()) {
            throw new CancellationException();
        }
        DuplicateProviderListener<T, DuplicateProvider<T>> listener = getListener();
        if (listener == null) {
            return;
        }
        Context context = getContext();
        ContentResolver cr = context.getContentResolver();

        Uri contentUri = getContentUri();
        if (contentUri == null) {
            return;
        }
        Cursor cursor = cr.query(getContentUri(), getCursorProjection(), getCursorSelection(), null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                T item;
                int count = 0;
                do {
                    if (isCancelled()) {
                        break;
                    }
                    item = createItem();
                    populateItem(cursor, item);
                    listener.onItemFetched(this, ++count, item);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    /**
     * Delete the items from the system provider.
     *
     * @param items the list of items.
     * @throws CancellationException if the provider has been cancelled.
     */
    public void deleteItems(Collection<T> items) throws CancellationException {
        if (isCancelled()) {
            throw new CancellationException();
        }
        DuplicateProviderListener<T, DuplicateProvider<T>> listener = getListener();
        if (listener == null) {
            return;
        }
        Context context = getContext();
        ContentResolver cr = context.getContentResolver();

        int count = 0;
        for (T item : items) {
            if (isCancelled()) {
                break;
            }
            if (deleteItem(cr, item)) {
                listener.onItemDeleted(this, ++count, item);
            }
        }
    }

    /**
     * Delete an item from the system provider.
     *
     * @param item the item.
     */
    public boolean deleteItem(T item) {
        if (isCancelled()) {
            return false;
        }
        return deleteItem(getContext().getContentResolver(), item);
    }

    /**
     * Delete an item from the system provider.
     *
     * @param cr   the content resolver.
     * @param item the item.
     */
    public boolean deleteItem(ContentResolver cr, T item) {
        return cr.delete(ContentUris.withAppendedId(getContentUri(), item.getId()), null, null) > 0;
    }

    /**
     * Delete the pairs from the system provider.
     *
     * @param pairs the list of item pairs.
     * @throws CancellationException if the provider has been cancelled.
     */
    public void deletePairs(Collection<DuplicateItemPair<T>> pairs) throws CancellationException {
        if (isCancelled()) {
            throw new CancellationException();
        }
        DuplicateProviderListener<T, DuplicateProvider<T>> listener = getListener();
        if (listener == null) {
            return;
        }
        Context context = getContext();
        ContentResolver cr = context.getContentResolver();

        int count = 0;
        T item1, item2;
        boolean deleted1, deleted2;
        for (DuplicateItemPair<T> pair : pairs) {
            if (isCancelled()) {
                break;
            }
            item1 = pair.getItem1();
            item2 = pair.getItem2();
            deleted1 = item1.isChecked() && deleteItem(cr, item1);
            deleted2 = item2.isChecked() && deleteItem(cr, item2);
            if (deleted1 || deleted2) {
                listener.onPairDeleted(this, ++count, pair);
            }
        }
    }

    /**
     * Execute some code before task does background work.
     */
    public void onPreExecute() {
    }

    /**
     * Execute some code after the task did background work.
     */
    public void onPostExecute() {
    }

    /**
     * Get the permissions necessary for reading content from the system provider.
     *
     * @return the array of permissions.
     */
    public abstract String[] getReadPermissions();

    /**
     * Get the permissions necessary for deleting content from the system provider.
     *
     * @return the array of permissions.
     */
    public abstract String[] getDeletePermissions();

    /**
     * Cancel all operations.
     */
    public void cancel() {
        cancelled = true;
    }

    /**
     * Are current operations cancelled?
     *
     * @return {@code true} if cancelled.
     */
    public boolean isCancelled() {
        return cancelled;
    }
}
