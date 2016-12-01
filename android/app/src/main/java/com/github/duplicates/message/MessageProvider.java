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
package com.github.duplicates.message;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import com.github.duplicates.DuplicateProvider;
import com.github.duplicates.DuplicateProviderListener;

import java.util.List;

/**
 * Provide duplicate messages.
 *
 * @author moshe.w
 */
public class MessageProvider extends DuplicateProvider<MessageItem> {

    private final DuplicateProvider<MessageItem> delegate;

    public MessageProvider(Context context) {
        super(context);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            delegate = new JellybeanMessageProvider(context);
        } else {
            delegate = new KitkatMessageProvider(context);
        }
    }

    @Override
    protected Uri getCursorUri() {
        return null;
    }

    @Override
    public List<MessageItem> getItems() {
        return delegate.getItems();
    }

    @Override
    public void fetchItems() {
        delegate.fetchItems();
    }

    @Override
    public MessageItem createItem() {
        return delegate.createItem();
    }

    @Override
    public void populateItem(Cursor cursor, MessageItem item) {
        delegate.populateItem(cursor, item);
    }

    @Override
    public void setListener(DuplicateProviderListener<MessageItem, DuplicateProvider<MessageItem>> listener) {
        delegate.setListener(listener);
    }

    @Override
    public DuplicateProviderListener<MessageItem, DuplicateProvider<MessageItem>> getListener() {
        return delegate.getListener();
    }
}
