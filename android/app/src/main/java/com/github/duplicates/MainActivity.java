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

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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
public class MainActivity extends Activity implements DuplicateTaskListener {

    /**
     * Activity id for requesting location permissions.
     */
    protected static final int ACTIVITY_PERMISSIONS = 2;

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.search)
    ImageButton spinnerAction;
    @BindView(R.id.statusBar)
    View statusBar;
    @BindView(R.id.counter)
    TextView counter;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(android.R.id.list)
    RecyclerView list;

    private DuplicateTask task;
    private DuplicateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        spinner.setAdapter(new MainSpinnerAdapter());
        searchStopped();
    }

    @OnClick(R.id.search)
    void search() {
        if ((task != null) && !task.isCancelled()) {
            task.cancel(true);
        } else {
            MainSpinnerItem item = (MainSpinnerItem) spinner.getSelectedItem();
            if (checkPermissions(item)) {
                DuplicateTask task = createTask(item);
                this.task = task;
                if (task != null) {
                    this.adapter = task.createAdapter();
                    list.setAdapter(adapter);
                    task.execute();
                } else {
                    searchStopped();
                }
            }
        }
    }

    private void searchStarted() {
        spinnerAction.setImageResource(android.R.drawable.ic_media_pause);
        counter.setText(getString(R.string.counter, 0));
        statusBar.setVisibility(View.VISIBLE);
        if (adapter != null) {
            adapter.clear();
        }
    }

    private void searchStopped() {
        spinnerAction.setImageResource(android.R.drawable.ic_menu_search);
        statusBar.setVisibility(View.GONE);
        task = null;
    }

    @Nullable
    private DuplicateTask createTask(MainSpinnerItem item) {
        Context context = this;
        DuplicateTaskListener listener = this;

        switch (item) {
//            case ALARMS:
//                return new AlarmTask(context, listener);
//            case BOOKMARKS:
//                return new BookmarkTask(context, listener);
//            case CALENDAR:
//                return new CalendarTask(context, listener);
//            case CALL_LOG:
//                return new CallLogTask(context, listener);
//            case CONTACTS:
//                return new ContactTask(context, listener);
            case MESSAGES:
                return new MessageTask(context, listener);
        }
        return null;
    }

    @Override
    public void onDuplicateTaskStarted(DuplicateTask task) {
        if (task == this.task) {
            searchStarted();
        }
    }

    @Override
    public void onDuplicateTaskFinished(DuplicateTask task) {
        if (task == this.task) {
            searchStopped();
        }
    }

    @Override
    public void onDuplicateTaskCancelled(DuplicateTask task) {
        if (task == this.task) {
            searchStopped();
        }
    }

    @Override
    public void onDuplicateTaskProgress(DuplicateTask task, int count) {
        if (task == this.task) {
            counter.setText(getString(R.string.counter, count));
        }
    }

    @Override
    public void onDuplicateTaskMatch(DuplicateTask task, DuplicateItem item1, DuplicateItem item2, float match) {
        if (task == this.task) {
            adapter.add(item1, item2, match);
        }
    }

    protected boolean checkPermissions(MainSpinnerItem item) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkPermissionsImpl(item);
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected boolean checkPermissionsImpl(MainSpinnerItem item) {
        String permission = null;
        switch (item) {
//            case ALARMS:
//            break;
//            case BOOKMARKS:
//            break;
//            case CALENDAR:
//            break;
//            case CALL_LOG:
//            break;
//            case CONTACTS:
//            break;
            case MESSAGES:
                permission = Manifest.permission.READ_SMS;
                break;
        }
        if ((permission != null) && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permission}, ACTIVITY_PERMISSIONS);
            return false;
        }
        return true;
    }
}
