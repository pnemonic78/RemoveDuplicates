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
package com.github.duplicates.alarm;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;

import com.github.duplicates.DefaultProvider;
import com.github.duplicates.DuplicateProvider;
import com.github.duplicates.DuplicateProviderListener;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * Provide duplicate alarms.
 *
 * @author moshe.w
 */
public class AlarmProvider extends DuplicateProvider<AlarmItem> {

    private static Boolean hasSamsungProvider;
    private final DuplicateProvider<AlarmItem> delegate;

    public AlarmProvider(Context context) {
        super(context);

        if (hasSamsungProvider == null) {
            PackageManager pm = context.getPackageManager();
            try {
                PackageInfo packageInfo = pm.getPackageInfo(SamsungAlarmProvider.PACKAGE, PackageManager.GET_PROVIDERS);
                hasSamsungProvider = (packageInfo.providers != null) && (packageInfo.providers.length > 0);
            } catch (PackageManager.NameNotFoundException e) {
                hasSamsungProvider = Boolean.FALSE;
                e.printStackTrace();
            }
        }

        DuplicateProvider<AlarmItem> candidate;
        if (hasSamsungProvider) {
            candidate = new SamsungAlarmProvider(context);
        } else {
            candidate = new DefaultProvider<>(context);
        }
        delegate = candidate;
    }

    @Override
    public void setListener(DuplicateProviderListener<AlarmItem, DuplicateProvider<AlarmItem>> listener) {
        delegate.setListener(listener);
    }

    @Override
    public DuplicateProviderListener<AlarmItem, DuplicateProvider<AlarmItem>> getListener() {
        return delegate.getListener();
    }

    @Override
    protected Uri getContentUri() {
        return null;
    }

    @Override
    public AlarmItem createItem() {
        return delegate.createItem();
    }

    @Override
    public List<AlarmItem> getItems() throws CancellationException {
        return delegate.getItems();
    }

    @Override
    public void fetchItems() throws CancellationException {
        delegate.fetchItems();
    }

    @Override
    public void populateItem(Cursor cursor, AlarmItem item) {
        delegate.populateItem(cursor, item);
    }

    @Override
    public void deleteItems(Collection<AlarmItem> items) throws CancellationException {
        delegate.deleteItems(items);
    }

    @Override
    public boolean deleteItem(AlarmItem item) {
        return delegate.deleteItem(item);
    }

    @Override
    public boolean deleteItem(ContentResolver cr, AlarmItem item) {
        return delegate.deleteItem(cr, item);
    }

    @Override
    public void onPreExecute() {
        delegate.onPreExecute();
    }

    @Override
    public void onPostExecute() {
        delegate.onPostExecute();
    }

    @Override
    public String[] getReadPermissions() {
        return delegate.getReadPermissions();
    }

    @Override
    public String[] getDeletePermissions() {
        return delegate.getDeletePermissions();
    }

    @Override
    public void cancel() {
        super.cancel();
        delegate.cancel();
    }
}
