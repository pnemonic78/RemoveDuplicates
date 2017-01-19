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

import android.content.Context;

import com.github.duplicates.DuplicateDeleteTask;
import com.github.duplicates.DuplicateTaskListener;

/**
 * Task to find duplicate alarms.
 *
 * @author moshe.w
 */
public class AlarmDeleteTask extends DuplicateDeleteTask<AlarmItem> {

    public AlarmDeleteTask(Context context, DuplicateTaskListener listener) {
        super(context, listener);
    }

    @Override
    protected AlarmProvider createProvider(Context context) {
        return new AlarmProvider(context);
    }
}