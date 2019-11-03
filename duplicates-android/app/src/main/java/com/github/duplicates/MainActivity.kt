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
package com.github.duplicates

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.github.android.removeduplicates.BuildConfig
import com.github.android.removeduplicates.R
import com.github.duplicates.alarm.AlarmDeleteTask
import com.github.duplicates.alarm.AlarmFindTask
import com.github.duplicates.alarm.AlarmItem
import com.github.duplicates.bookmark.BookmarkDeleteTask
import com.github.duplicates.bookmark.BookmarkFindTask
import com.github.duplicates.bookmark.BookmarkItem
import com.github.duplicates.calendar.CalendarDeleteTask
import com.github.duplicates.calendar.CalendarFindTask
import com.github.duplicates.calendar.CalendarItem
import com.github.duplicates.call.CallLogDeleteTask
import com.github.duplicates.call.CallLogFindTask
import com.github.duplicates.call.CallLogItem
import com.github.duplicates.contact.ContactDeleteTask
import com.github.duplicates.contact.ContactFindTask
import com.github.duplicates.contact.ContactItem
import com.github.duplicates.message.MessageDeleteTask
import com.github.duplicates.message.MessageFindTask
import com.github.duplicates.message.MessageItem
import com.github.util.LogTree
import timber.log.Timber

/**
 * Main activity.
 *
 * @author moshe.w
 */
class MainActivity<I : DuplicateItem, T : DuplicateTask<I, *, *, *>> : AppCompatActivity(),
    DuplicateTaskListener<I, T>,
    SearchView.OnQueryTextListener {

    @BindView(R.id.spinner)
    lateinit var spinner: Spinner
    @BindView(R.id.search)
    lateinit var spinnerAction: ImageButton
    @BindView(R.id.statusBar)
    lateinit var statusBar: Group
    @BindView(R.id.counter)
    lateinit var counter: TextView
    @BindView(R.id.progress)
    lateinit var progressBar: ProgressBar
    @BindView(R.id.listSwitcher)
    lateinit var listSwitcher: ViewSwitcher
    @BindView(android.R.id.list)
    lateinit var list: RecyclerView

    private var task: DuplicateTask<I, *, *, *>? = null
    private var adapter: DuplicateAdapter<I, *>? = null
    private var spinnerItem: MainSpinnerItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(LogTree(BuildConfig.DEBUG))

        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        spinner.adapter = MainSpinnerAdapter()
        searchStopped(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (task != null && !task!!.isCancelled) {
            task!!.cancel()
        }
    }

    @OnClick(R.id.search)
    internal fun searchClicked() {
        spinnerAction.isEnabled = false
        val taskActive = this.task
        if (taskActive != null && !taskActive.isCancelled) {
            taskActive.cancel()
        } else {
            spinnerItem = spinner.selectedItem as MainSpinnerItem
            val task = createFindTask(spinnerItem!!)
            this.task = task
            if (task != null) {
                this.adapter = task.createAdapter()
                adapter!!.setHasStableIds(true)
                list.adapter = adapter
                task.start(this)
            } else {
                searchStopped(false)
            }
        }
    }

    private fun searchStarted() {
        spinner.isEnabled = false
        spinnerAction.setImageResource(android.R.drawable.ic_media_pause)
        spinnerAction.isEnabled = true
        counter.text = getString(R.string.counter, 0)
        statusBar.visibility = View.VISIBLE
        adapter?.clear()
        listSwitcher.displayedChild = CHILD_LIST
        invalidateOptionsMenu()
    }

    private fun searchStopped(cancelled: Boolean) {
        spinner.isEnabled = true
        spinnerAction.setImageResource(android.R.drawable.ic_menu_search)
        spinnerAction.isEnabled = true
        statusBar.visibility = View.GONE
        task = null
        if (adapter != null && adapter!!.getItemCount() > 0) {
            listSwitcher.displayedChild = CHILD_LIST
        } else {
            listSwitcher.displayedChild = CHILD_EMPTY
        }
        invalidateOptionsMenu()
    }

    @Suppress("UNCHECKED_CAST")
    private fun createFindTask(item: MainSpinnerItem): DuplicateFindTask<I, DuplicateViewHolder<I>>? {
        val context: Context = this
        val listener: DuplicateTaskListener<I, T> = this

        return when (item) {
            MainSpinnerItem.ALARMS -> AlarmFindTask(context, listener as DuplicateTaskListener<AlarmItem, DuplicateTask<AlarmItem, Any, Any, List<AlarmItem>>>)
            MainSpinnerItem.BOOKMARKS -> BookmarkFindTask(context, listener as DuplicateTaskListener<BookmarkItem, DuplicateTask<BookmarkItem, Any, Any, List<BookmarkItem>>>)
            MainSpinnerItem.CALENDAR -> CalendarFindTask(context, listener as DuplicateTaskListener<CalendarItem, DuplicateTask<CalendarItem, Any, Any, List<CalendarItem>>>)
            MainSpinnerItem.CALL_LOG -> CallLogFindTask(context, listener as DuplicateTaskListener<CallLogItem, DuplicateTask<CallLogItem, Any, Any, List<CallLogItem>>>)
            MainSpinnerItem.CONTACTS -> ContactFindTask(context, listener as DuplicateTaskListener<ContactItem, DuplicateTask<ContactItem, Any, Any, List<ContactItem>>>)
            MainSpinnerItem.MESSAGES -> MessageFindTask(context, listener as DuplicateTaskListener<MessageItem, DuplicateTask<MessageItem, Any, Any, List<MessageItem>>>)
        } as DuplicateFindTask<I, DuplicateViewHolder<I>>
    }

    @Suppress("UNCHECKED_CAST")
    private fun createDeleteTask(item: MainSpinnerItem): DuplicateDeleteTask<I>? {
        val context: Context = this
        val listener: DuplicateTaskListener<I, T> = this

        return when (item) {
            MainSpinnerItem.ALARMS -> AlarmDeleteTask(context, listener as DuplicateTaskListener<AlarmItem, DuplicateTask<AlarmItem, DuplicateItemPair<AlarmItem>, Any, Unit>>)
            MainSpinnerItem.BOOKMARKS -> BookmarkDeleteTask(context, listener as DuplicateTaskListener<BookmarkItem, DuplicateTask<BookmarkItem, DuplicateItemPair<BookmarkItem>, Any, Unit>>)
            MainSpinnerItem.CALENDAR -> CalendarDeleteTask(context, listener as DuplicateTaskListener<CalendarItem, DuplicateTask<CalendarItem, DuplicateItemPair<CalendarItem>, Any, Unit>>)
            MainSpinnerItem.CALL_LOG -> CallLogDeleteTask(context, listener as DuplicateTaskListener<CallLogItem, DuplicateTask<CallLogItem, DuplicateItemPair<CallLogItem>, Any, Unit>>)
            MainSpinnerItem.CONTACTS -> ContactDeleteTask(context, listener as DuplicateTaskListener<ContactItem, DuplicateTask<ContactItem, DuplicateItemPair<ContactItem>, Any, Unit>>)
            MainSpinnerItem.MESSAGES -> MessageDeleteTask(context, listener as DuplicateTaskListener<MessageItem, DuplicateTask<MessageItem, DuplicateItemPair<MessageItem>, Any, Unit>>)
        } as DuplicateDeleteTask<I>
    }

    override fun onDuplicateTaskStarted(task: T) {
        if (task is DuplicateFindTask<*, *>) {
            searchStarted()
        } else if (task is DuplicateDeleteTask<*>) {
            deleteStarted()
        }
    }

    override fun onDuplicateTaskFinished(task: T) {
        if (task is DuplicateFindTask<*, *>) {
            searchStopped(false)
        } else if (task is DuplicateDeleteTask<*>) {
            deleteStopped(false)
        }
    }

    override fun onDuplicateTaskCancelled(task: T) {
        if (task is DuplicateFindTask<*, *>) {
            searchStopped(true)
        } else if (task is DuplicateDeleteTask<*>) {
            deleteStopped(true)
        }
    }

    override fun onDuplicateTaskProgress(task: T, count: Int) {
        if (task === this.task) {
            counter.text = getString(R.string.counter, count)
        }
    }

    override fun onDuplicateTaskMatch(task: T, item1: I, item2: I, match: Float, difference: BooleanArray) {
        if (task === this.task) {
            adapter?.add(item1, item2, match, difference)
        }
    }

    override fun onDuplicateTaskItemDeleted(task: T, item: I) {
        if (task === this.task) {
            adapter?.removeItem(item)
        }
    }

    override fun onDuplicateTaskPairDeleted(task: T, pair: DuplicateItemPair<I>) {
        if (task === this.task) {
            adapter?.remove(pair)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val task = this.task
        if (task != null) {
            task.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        if (adapter != null && adapter!!.getItemCount() > 0) {
            menuInflater.inflate(R.menu.main, menu)

            val searchView = menu.findItem(R.id.menu_filter).actionView as SearchView
            searchView.setOnQueryTextListener(this)
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                deleteItems()
                return true
            }
            R.id.menu_select_all -> {
                selectAllItems()
                return true
            }
            R.id.menu_select_none -> {
                selectNoItems()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteItems() {
        if (task != null && !task!!.isCancelled) {
            task!!.cancel()
        } else if (adapter != null && adapter!!.getItemCount() > 0 && spinnerItem != null) {
            val task = createDeleteTask(spinnerItem!!)
            this.task = task
            if (task != null) {
                val pairs = adapter!!.getCheckedPairs()
                val params = pairs.toTypedArray()
                task.start(this, params)
            } else {
                searchStopped(false)
            }
        }
    }

    private fun selectAllItems() {
        if (adapter != null && adapter!!.getItemCount() > 0) {
            adapter!!.selectAll()
        }
    }

    private fun selectNoItems() {
        if (adapter != null && adapter!!.getItemCount() > 0) {
            adapter!!.selectNone()
        }
    }

    private fun deleteStarted() {
        spinnerAction.setImageResource(android.R.drawable.ic_media_pause)
        spinnerAction.isEnabled = true
        counter.text = getString(R.string.counter, 0)
        statusBar.visibility = View.VISIBLE
        listSwitcher.displayedChild = CHILD_LIST
    }

    private fun deleteStopped(cancelled: Boolean) {
        spinnerAction.setImageResource(android.R.drawable.ic_menu_search)
        spinnerAction.isEnabled = true
        statusBar.visibility = View.GONE
        task = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (task != null) {
            task!!.onActivityResult(this, requestCode, resultCode, data)
        }
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        if (adapter != null) {
            adapter!!.filter(query)
            return true
        }
        return false
    }

    override fun onQueryTextChange(query: String): Boolean {
        if (adapter != null) {
            adapter!!.filter(query)
            return true
        }
        return false
    }

    companion object {

        private const val CHILD_LIST = 0
        private const val CHILD_EMPTY = 1
    }
}
