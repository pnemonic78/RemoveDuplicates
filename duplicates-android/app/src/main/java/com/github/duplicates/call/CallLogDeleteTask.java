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

import android.content.Context;

import com.github.duplicates.DuplicateDeleteTask;
import com.github.duplicates.DuplicateTaskListener;

/**
 * Task to find duplicate calls.
 *
 * @author moshe.w
 */
public class CallLogDeleteTask extends DuplicateDeleteTask<CallLogItem> {

    public CallLogDeleteTask(Context context, DuplicateTaskListener listener) {
        super(context, listener);
    }

    @Override
    protected CallLogProvider createProvider(Context context) {
        return new CallLogProvider(context);
    }
}
