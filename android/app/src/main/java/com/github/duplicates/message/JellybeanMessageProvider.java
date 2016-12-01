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

import com.github.duplicates.DuplicateProvider;

/**
 * Provide duplicate messages for Jellybean versions and older.
 *
 * @author moshe.w
 */
public class JellybeanMessageProvider extends DuplicateProvider<MessageItem> {

    private static final String[] PROJECTION = null;

    public JellybeanMessageProvider(Context context) {
        super(context);
    }

    @Override
    protected Uri getCursorUri() {
        return Uri.parse("content://sms/");
    }

    @Override
    protected String[] getCursorProjection() {
        return PROJECTION;
    }

    @Override
    public MessageItem createItem() {
        return new MessageItem();
    }

    @Override
    public void populateItem(Cursor cursor, MessageItem item) {
        //TODO implement me!
    }
}