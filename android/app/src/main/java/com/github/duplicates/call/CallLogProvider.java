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
package com.github.duplicates.call;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import com.github.duplicates.DuplicateProvider;
import com.github.duplicates.DuplicateProviderListener;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * Provide duplicate calls.
 *
 * @author moshe.w
 */
public class CallLogProvider extends DuplicateProvider<CallLogItem> {

    private static String[] PERMISSIONS_READ = {"android.permission.READ_CALL_LOG"};
    private static String[] PERMISSIONS_WRITE = {"android.permission.WRITE_CALL_LOG"};

    public CallLogProvider(Context context) {
        super(context);
    }

    @Override
    public void setListener(DuplicateProviderListener<CallLogItem, DuplicateProvider<CallLogItem>> listener) {
        //TODO implement me!
    }

    @Override
    public DuplicateProviderListener<CallLogItem, DuplicateProvider<CallLogItem>> getListener() {
        return null;
    }

    @Override
    protected Uri getContentUri() {
        return CallLog.CONTENT_URI;
    }

    @Override
    public CallLogItem createItem() {
        return new CallLogItem();
    }

    @Override
    public List<CallLogItem> getItems() throws CancellationException {
        return null;
    }

    @Override
    public void fetchItems() throws CancellationException {
        //TODO implement me!
    }

    @Override
    public void populateItem(Cursor cursor, CallLogItem item) {
        //TODO implement me!
    }

    @Override
    public void deleteItems(Collection<CallLogItem> items) throws CancellationException {
        //TODO implement me!
    }

    @Override
    public boolean deleteItem(CallLogItem item) {
        return false;
    }

    @Override
    public boolean deleteItem(ContentResolver cr, CallLogItem item) {
        return false;
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
