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

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.github.android.removeduplicates.R;
import com.github.duplicates.message.MessageTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main activity.
 *
 * @author moshe.w
 */
public class MainActivity extends Activity {

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.search)
    ImageButton spinnerAction;
    @BindView(android.R.id.list)
    RecyclerView list;

    private DuplicateTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        spinner.setAdapter(new MainSpinnerAdapter());
    }

    @OnClick(R.id.search)
    void search() {
        if (task != null) {
            task.cancel(true);
            spinnerAction.setImageResource(android.R.drawable.ic_menu_search);
        } else {
            MainSpinnerItem item = (MainSpinnerItem) spinner.getSelectedItem();
            task = createTask(item);
            if (task != null) {
                spinnerAction.setImageResource(android.R.drawable.ic_media_pause);
                task.execute();
            } else {
                spinnerAction.setImageResource(android.R.drawable.ic_menu_search);
            }
        }
    }

    @Nullable
    private DuplicateTask createTask(MainSpinnerItem item) {
        switch (item) {
            case ALARMS:
                //TODO implement me!
                break;
            case BOOKMARKS:
                //TODO implement me!
                break;
            case CALENDAR:
                //TODO implement me!
                break;
            case CALL_LOG:
                //TODO implement me!
                break;
            case CONTACTS:
                //TODO implement me!
                break;
            case MESSAGES:
                return new MessageTask(this);
        }

//        return new DuplicateTask<DuplicateItem, Object, Void, List<DuplicateItem>>(this) {
//            @Override
//            protected DuplicateProvider<DuplicateItem> createProvider(Context context) {
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(List<DuplicateItem> items) {
//                spinnerAction.setImageResource(android.R.drawable.ic_menu_search);
//                task = null;
//            }
//        };
        return null;
    }
}
