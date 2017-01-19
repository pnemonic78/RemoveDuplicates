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
import android.os.Build;

import com.github.duplicates.DelegateProvider;
import com.github.duplicates.DuplicateProvider;

/**
 * Provide duplicate messages.
 *
 * @author moshe.w
 */
public class MessageProvider extends DelegateProvider<MessageItem> {

    public MessageProvider(Context context) {
        super(context);
    }

    @Override
    protected DuplicateProvider<MessageItem> createDelegate(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return new JellybeanMessageProvider(context);
        }
        return new KitkatMessageProvider(context);
    }
}
