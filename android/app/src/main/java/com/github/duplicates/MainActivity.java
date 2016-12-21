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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.android.removeduplicates.R;
import com.github.duplicates.message.MessageDeleteTask;
import com.github.duplicates.message.MessageFindTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main activity.
 *
 * @author moshe.w
 */
public class MainActivity extends Activity implements DuplicateTaskListener {

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

    private DuplicateFindTask finderTask;
    private DuplicateDeleteTask deleteTask;
    private DuplicateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        spinner.setAdapter(new MainSpinnerAdapter());
        searchStopped(false);
    }

    @OnClick(R.id.search)
    void searchClicked() {
        if ((finderTask != null) && !finderTask.isCancelled()) {
            finderTask.cancel(true);
        } else if ((deleteTask != null) && !deleteTask.isCancelled()) {
            deleteTask.cancel(true);
        } else {
            MainSpinnerItem item = (MainSpinnerItem) spinner.getSelectedItem();
            DuplicateFindTask task = createFindTask(item);
            this.finderTask = task;
            if (task != null) {
                this.adapter = task.createAdapter();
                list.setAdapter(adapter);
                task.start(this);
            } else {
                searchStopped(false);
            }
        }
    }

    private void searchStarted() {
        spinner.setEnabled(false);
        spinnerAction.setImageResource(android.R.drawable.ic_media_pause);
        counter.setText(getString(R.string.counter, 0));
        statusBar.setVisibility(View.VISIBLE);
        if (adapter != null) {
            adapter.clear();
        }
        invalidateOptionsMenu();
    }

    private void searchStopped(boolean cancelled) {
        spinner.setEnabled(true);
        spinnerAction.setImageResource(android.R.drawable.ic_menu_search);
        statusBar.setVisibility(View.GONE);
        finderTask = null;
        if (!cancelled) {
            invalidateOptionsMenu();
        }
    }

    @Nullable
    private DuplicateFindTask createFindTask(MainSpinnerItem item) {
        Context context = this;
        DuplicateTaskListener listener = this;

        switch (item) {
//            case ALARMS:
//                return new AlarmFindTask(context, listener);
//            case BOOKMARKS:
//                return new BookmarkFindTask(context, listener);
//            case CALENDAR:
//                return new CalendarFindTask(context, listener);
//            case CALL_LOG:
//                return new CallLogFindTask(context, listener);
//            case CONTACTS:
//                return new ContactFindTask(context, listener);
            case MESSAGES:
                return new MessageFindTask(context, listener);
        }
        return null;
    }

    @Nullable
    private DuplicateDeleteTask createDeleteTask(MainSpinnerItem item) {
        Context context = this;
        DuplicateTaskListener listener = this;

        switch (item) {
//            case ALARMS:
//                return new AlarmDeleteTask(context, listener);
//            case BOOKMARKS:
//                return new BookmarkDeleteTask(context, listener);
//            case CALENDAR:
//                return new CalendarDeleteTask(context, listener);
//            case CALL_LOG:
//                return new CallLogDeleteTask(context, listener);
//            case CONTACTS:
//                return new ContactDeleteTask(context, listener);
            case MESSAGES:
                return new MessageDeleteTask(context, listener);
        }
        return null;
    }

    @Override
    public void onDuplicateTaskStarted(DuplicateTask task) {
        if (task == finderTask) {
            searchStarted();
        } else if (task == deleteTask) {
            deleteStarted();
        }
    }

    @Override
    public void onDuplicateTaskFinished(DuplicateTask task) {
        if (task == finderTask) {
            searchStopped(false);
            invalidateOptionsMenu();
        } else if (task == deleteTask) {
            deleteStopped(false);
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onDuplicateTaskCancelled(DuplicateTask task) {
        if (task == finderTask) {
            searchStopped(true);
        } else if (task == deleteTask) {
            deleteStopped(true);
        }
    }

    @Override
    public void onDuplicateTaskProgress(DuplicateTask task, int count) {
        if (task == finderTask) {
            counter.setText(getString(R.string.counter, count));
        } else if (task == deleteTask) {
            counter.setText(getString(R.string.counter, count));
        }
    }

    @Override
    public void onDuplicateTaskMatch(DuplicateTask task, DuplicateItem item1, DuplicateItem item2, float match) {
        if (task == finderTask) {
            adapter.add(item1, item2, match);
        }
    }

    @Override
    public void onDuplicateTaskItemDeleted(DuplicateTask task, DuplicateItem item) {
        if (task == deleteTask) {
            adapter.remove(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (finderTask != null) {
            finderTask.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (deleteTask != null) {
            deleteTask.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if ((adapter != null) && (adapter.getItemCount() > 0)) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                deleteItems();
                return true;
            case R.id.menu_select_all:
                selectAllItems();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteItems() {
        if ((deleteTask != null) && !deleteTask.isCancelled()) {
            deleteTask.cancel(true);
        } else if ((adapter != null) && (adapter.getItemCount() > 0)) {
            MainSpinnerItem item = (MainSpinnerItem) spinner.getSelectedItem();
            DuplicateDeleteTask task = createDeleteTask(item);
            this.deleteTask = task;
            if (task != null) {
                task.start(this, adapter.getCheckedItems());
            } else {
                searchStopped(false);
            }
        }
    }

    private void selectAllItems() {
        if ((adapter != null) && (adapter.getItemCount() > 0)) {
            adapter.selectAll();
        }
    }

    private void deleteStarted() {
        spinnerAction.setImageResource(android.R.drawable.ic_media_pause);
        counter.setText(getString(R.string.counter, 0));
        statusBar.setVisibility(View.VISIBLE);
    }

    private void deleteStopped(boolean cancelled) {
        spinnerAction.setImageResource(android.R.drawable.ic_menu_search);
        statusBar.setVisibility(View.GONE);
        deleteTask = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (finderTask != null) {
            finderTask.onActivityResult(this, requestCode, resultCode, data);
        } else if (deleteTask != null) {
            deleteTask.onActivityResult(this, requestCode, resultCode, data);
        }
    }
}
