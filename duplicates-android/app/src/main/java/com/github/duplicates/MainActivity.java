/*
 * Copyright 2016, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.duplicates;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.github.android.removeduplicates.BuildConfig;
import com.github.android.removeduplicates.R;
import com.github.duplicates.alarm.AlarmDeleteTask;
import com.github.duplicates.alarm.AlarmFindTask;
import com.github.duplicates.bookmark.BookmarkDeleteTask;
import com.github.duplicates.bookmark.BookmarkFindTask;
import com.github.duplicates.calendar.CalendarDeleteTask;
import com.github.duplicates.calendar.CalendarFindTask;
import com.github.duplicates.call.CallLogDeleteTask;
import com.github.duplicates.call.CallLogFindTask;
import com.github.duplicates.contact.ContactDeleteTask;
import com.github.duplicates.contact.ContactFindTask;
import com.github.duplicates.message.MessageDeleteTask;
import com.github.duplicates.message.MessageFindTask;
import com.github.util.LogTree;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Main activity.
 *
 * @author moshe.w
 */
public class MainActivity extends Activity implements DuplicateTaskListener, SearchView.OnQueryTextListener {

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
    private MainSpinnerItem spinnerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.plant(new LogTree(BuildConfig.DEBUG));

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
        spinnerAction.setEnabled(false);
        if ((task != null) && !task.isCancelled()) {
            task.cancel();
        } else {
            spinnerItem = (MainSpinnerItem) spinner.getSelectedItem();
            DuplicateFindTask task = createFindTask(spinnerItem);
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
        spinnerAction.setEnabled(true);
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
        spinnerAction.setEnabled(true);
        statusBar.setVisibility(View.GONE);
        task = null;
        if ((adapter != null) && (adapter.getItemCount() > 0)) {
            listSwitcher.setDisplayedChild(CHILD_LIST);
        } else {
            listSwitcher.setDisplayedChild(CHILD_EMPTY);
        }
        invalidateOptionsMenu();
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
            case CALENDAR:
                return new CalendarFindTask(context, listener);
            case CALL_LOG:
                return new CallLogFindTask(context, listener);
            case CONTACTS:
                return new ContactFindTask(context, listener);
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
            case CALENDAR:
                return new CalendarDeleteTask(context, listener);
            case CALL_LOG:
                return new CallLogDeleteTask(context, listener);
            case CONTACTS:
                return new ContactDeleteTask(context, listener);
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
        } else if (task instanceof DuplicateDeleteTask) {
            deleteStopped(false);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

            SearchView searchView = (SearchView) menu.findItem(R.id.menu_filter).getActionView();
            searchView.setOnQueryTextListener(this);
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
            case R.id.menu_select_none:
                selectNoItems();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteItems() {
        if ((task != null) && !task.isCancelled()) {
            task.cancel();
        } else if ((adapter != null) && (adapter.getItemCount() > 0) && (spinnerItem != null)) {
            DuplicateDeleteTask task = createDeleteTask(spinnerItem);
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

    private void selectNoItems() {
        if ((adapter != null) && (adapter.getItemCount() > 0)) {
            adapter.selectNone();
        }
    }

    private void deleteStarted() {
        spinnerAction.setImageResource(android.R.drawable.ic_media_pause);
        spinnerAction.setEnabled(true);
        counter.setText(getString(R.string.counter, 0));
        statusBar.setVisibility(View.VISIBLE);
        listSwitcher.setDisplayedChild(CHILD_LIST);
    }

    private void deleteStopped(boolean cancelled) {
        spinnerAction.setImageResource(android.R.drawable.ic_menu_search);
        spinnerAction.setEnabled(true);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (adapter != null) {
            adapter.filter(query);
            return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (adapter != null) {
            adapter.filter(query);
            return true;
        }
        return false;
    }
}