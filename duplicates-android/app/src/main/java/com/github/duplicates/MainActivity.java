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
import android.widget.ViewSwitcher;

import com.github.android.removeduplicates.R;
import com.github.duplicates.alarm.AlarmDeleteTask;
import com.github.duplicates.alarm.AlarmFindTask;
import com.github.duplicates.bookmark.BookmarkDeleteTask;
import com.github.duplicates.bookmark.BookmarkFindTask;
import com.github.duplicates.call.CallLogDeleteTask;
import com.github.duplicates.call.CallLogFindTask;
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

    private static final int CHILD_LIST = 0;
    private static final int CHILD_EMPTY = 1;

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
    @BindView(R.id.list_switcher)
    ViewSwitcher listSwitcher;
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
        searchStopped(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ((task != null) && !task.isCancelled()) {
            task.cancel();
        }
    }

    @OnClick(R.id.search)
    void searchClicked() {
        if ((task != null) && !task.isCancelled()) {
            task.cancel();
        } else {
            MainSpinnerItem item = (MainSpinnerItem) spinner.getSelectedItem();
            DuplicateFindTask task = createFindTask(item);
            this.task = task;
            if (task != null) {
                this.adapter = task.createAdapter();
                adapter.setHasStableIds(true);
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
        listSwitcher.setDisplayedChild(CHILD_LIST);
        invalidateOptionsMenu();
    }

    private void searchStopped(boolean cancelled) {
        spinner.setEnabled(true);
        spinnerAction.setImageResource(android.R.drawable.ic_menu_search);
        statusBar.setVisibility(View.GONE);
        task = null;
        if (!cancelled) {
            invalidateOptionsMenu();
        }
        if ((adapter != null) && (adapter.getItemCount() > 0)) {
            listSwitcher.setDisplayedChild(CHILD_LIST);
        } else {
            listSwitcher.setDisplayedChild(CHILD_EMPTY);
        }
    }

    @Nullable
    private DuplicateFindTask createFindTask(MainSpinnerItem item) {
        Context context = this;
        DuplicateTaskListener listener = this;

        switch (item) {
            case ALARMS:
                return new AlarmFindTask(context, listener);
            case BOOKMARKS:
                return new BookmarkFindTask(context, listener);
//            case CALENDAR:
//                return new CalendarFindTask(context, listener);
            case CALL_LOG:
                return new CallLogFindTask(context, listener);
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
            case ALARMS:
                return new AlarmDeleteTask(context, listener);
            case BOOKMARKS:
                return new BookmarkDeleteTask(context, listener);
//            case CALENDAR:
//                return new CalendarDeleteTask(context, listener);
            case CALL_LOG:
                return new CallLogDeleteTask(context, listener);
//            case CONTACTS:
//                return new ContactDeleteTask(context, listener);
            case MESSAGES:
                return new MessageDeleteTask(context, listener);
        }
        return null;
    }

    @Override
    public void onDuplicateTaskStarted(DuplicateTask task) {
        if (task instanceof DuplicateFindTask) {
            searchStarted();
        } else if (task instanceof DuplicateDeleteTask) {
            deleteStarted();
        }
    }

    @Override
    public void onDuplicateTaskFinished(DuplicateTask task) {
        if (task instanceof DuplicateFindTask) {
            searchStopped(false);
            invalidateOptionsMenu();
        } else if (task instanceof DuplicateDeleteTask) {
            deleteStopped(false);
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onDuplicateTaskCancelled(DuplicateTask task) {
        if (task instanceof DuplicateFindTask) {
            searchStopped(true);
        } else if (task instanceof DuplicateDeleteTask) {
            deleteStopped(true);
        }
    }

    @Override
    public void onDuplicateTaskProgress(DuplicateTask task, int count) {
        if (task == this.task) {
            counter.setText(getString(R.string.counter, count));
        }
    }

    @Override
    public void onDuplicateTaskMatch(DuplicateTask task, DuplicateItem item1, DuplicateItem item2, float match, boolean[] difference) {
        if (task == this.task) {
            adapter.add(item1, item2, match, difference);
        }
    }

    @Override
    public void onDuplicateTaskItemDeleted(DuplicateTask task, DuplicateItem item) {
        if (task == this.task) {
            adapter.remove(item);
        }
    }

    @Override
    public void onDuplicateTaskPairDeleted(DuplicateTask task, DuplicateItemPair pair) {
        if (task == this.task) {
            adapter.remove(pair);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (task != null) {
            task.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        if ((task != null) && !task.isCancelled()) {
            task.cancel();
        } else if ((adapter != null) && (adapter.getItemCount() > 0)) {
            MainSpinnerItem item = (MainSpinnerItem) spinner.getSelectedItem();
            DuplicateDeleteTask task = createDeleteTask(item);
            this.task = task;
            if (task != null) {
                task.start(this, adapter.getCheckedPairs());
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
        listSwitcher.setDisplayedChild(CHILD_LIST);
    }

    private void deleteStopped(boolean cancelled) {
        spinnerAction.setImageResource(android.R.drawable.ic_menu_search);
        statusBar.setVisibility(View.GONE);
        task = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (task != null) {
            task.onActivityResult(this, requestCode, resultCode, data);
        }
    }
}
