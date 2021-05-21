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
import android.widget.AdapterView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.github.android.removeduplicates.R
import com.github.android.removeduplicates.databinding.ActivityMainBinding
import com.github.duplicates.alarm.AlarmDeleteTask
import com.github.duplicates.alarm.AlarmFindTask
import com.github.duplicates.alarm.AlarmItem
import com.github.duplicates.alarm.AlarmViewHolder
import com.github.duplicates.bookmark.BookmarkDeleteTask
import com.github.duplicates.bookmark.BookmarkFindTask
import com.github.duplicates.bookmark.BookmarkItem
import com.github.duplicates.bookmark.BookmarkViewHolder
import com.github.duplicates.calendar.CalendarDeleteTask
import com.github.duplicates.calendar.CalendarFindTask
import com.github.duplicates.calendar.CalendarItem
import com.github.duplicates.calendar.CalendarViewHolder
import com.github.duplicates.call.CallLogDeleteTask
import com.github.duplicates.call.CallLogFindTask
import com.github.duplicates.call.CallLogItem
import com.github.duplicates.call.CallLogViewHolder
import com.github.duplicates.contact.ContactDeleteTask
import com.github.duplicates.contact.ContactFindTask
import com.github.duplicates.contact.ContactItem
import com.github.duplicates.contact.ContactViewHolder
import com.github.duplicates.message.MessageDeleteTask
import com.github.duplicates.message.MessageFindTask
import com.github.duplicates.message.MessageItem
import com.github.duplicates.message.MessageViewHolder

/**
 * Main activity.
 *
 * @author moshe.w
 */
class MainActivity<I : DuplicateItem, T : DuplicateTask<I, *, *, *, DuplicateTaskListener<I>>> :
    AppCompatActivity(),
    DuplicateFindTaskListener<I, DuplicateViewHolder<I>>,
    DuplicateDeleteTaskListener<I>,
    SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private var task: DuplicateTask<I, *, *, *, DuplicateTaskListener<I>>? = null
    private var adapter: DuplicateAdapter<I, DuplicateViewHolder<I>>? = null
    private var spinnerItem: MainSpinnerItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = MainSpinnerAdapter()
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                onSpinnerItemSelected(adapter.getItem(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding.spinnerAction.setOnClickListener { searchClicked() }
        searchStopped(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        task?.cancel()
    }

    private fun onSpinnerItemSelected(item: MainSpinnerItem) {
        //TODO("Load the previous find from the pair table")
    }

    private fun searchClicked() {
        binding.spinnerAction.isEnabled = false
        if (stopSearch()) return

        spinnerItem = binding.spinner.selectedItem as MainSpinnerItem
        startSearch(spinnerItem!!)
    }

    @Suppress("UNCHECKED_CAST")
    private fun startSearch(item: MainSpinnerItem) {
        val task = createFindTask(item)
        if (task != null) {
            this.task = task as DuplicateTask<I, *, *, *, DuplicateTaskListener<I>>
            this.adapter = task.createAdapter()
            adapter!!.setHasStableIds(true)
            binding.list.adapter = adapter
            task.start(this)
        } else {
            this.task = null
            searchStopped(false)
        }
    }

    private fun searchStarted() {
        binding.apply {
            spinner.isEnabled = false
            spinnerAction.setImageResource(android.R.drawable.ic_media_pause)
            spinnerAction.isEnabled = true
            counter.text = getString(R.string.counter, 0)
            statusBar.visibility = View.VISIBLE
            adapter?.clear()
            listSwitcher.displayedChild = CHILD_LIST
        }
        invalidateOptionsMenu()
    }

    private fun stopSearch(): Boolean {
        val taskActive = this.task ?: return false
        if (!taskActive.isCancelled) {
            return taskActive.cancel()
        }
        return false
    }

    private fun searchStopped(cancelled: Boolean) {
        task = null
        binding.apply {
            spinner.isEnabled = true
            spinnerAction.setImageResource(android.R.drawable.ic_menu_search)
            spinnerAction.isEnabled = true
            statusBar.visibility = View.GONE
            if (adapter != null && adapter!!.itemCount > 0) {
                listSwitcher.displayedChild = CHILD_LIST
            } else {
                listSwitcher.displayedChild = CHILD_EMPTY
            }
        }
        invalidateOptionsMenu()
    }

    @Suppress("UNCHECKED_CAST")
    private fun createFindTask(item: MainSpinnerItem): DuplicateFindTask<I, DuplicateViewHolder<I>, DuplicateFindTaskListener<I, DuplicateViewHolder<I>>>? {
        val context: Context = this
        val listener: DuplicateFindTaskListener<I, DuplicateViewHolder<I>> = this

        return when (item) {
            MainSpinnerItem.ALARMS -> AlarmFindTask(
                context,
                listener as DuplicateFindTaskListener<AlarmItem, AlarmViewHolder>
            )
            MainSpinnerItem.BOOKMARKS -> BookmarkFindTask(
                context,
                listener as DuplicateFindTaskListener<BookmarkItem, BookmarkViewHolder>
            )
            MainSpinnerItem.CALENDARS -> CalendarFindTask(
                context,
                listener as DuplicateFindTaskListener<CalendarItem, CalendarViewHolder>
            )
            MainSpinnerItem.CALL_LOGS -> CallLogFindTask(
                context,
                listener as DuplicateFindTaskListener<CallLogItem, CallLogViewHolder>
            )
            MainSpinnerItem.CONTACTS -> ContactFindTask(
                context,
                listener as DuplicateFindTaskListener<ContactItem, ContactViewHolder>
            )
            MainSpinnerItem.MESSAGES -> MessageFindTask(
                context,
                listener as DuplicateFindTaskListener<MessageItem, MessageViewHolder>
            )
        } as DuplicateFindTask<I, DuplicateViewHolder<I>, DuplicateFindTaskListener<I, DuplicateViewHolder<I>>>
    }

    @Suppress("UNCHECKED_CAST")
    private fun createDeleteTask(item: MainSpinnerItem): DuplicateDeleteTask<I, DuplicateDeleteTaskListener<I>>? {
        val context: Context = this
        val listener: DuplicateDeleteTaskListener<I> = this

        return when (item) {
            MainSpinnerItem.ALARMS -> AlarmDeleteTask(
                context,
                listener as DuplicateDeleteTaskListener<AlarmItem>
            )
            MainSpinnerItem.BOOKMARKS -> BookmarkDeleteTask(
                context,
                listener as DuplicateDeleteTaskListener<BookmarkItem>
            )
            MainSpinnerItem.CALENDARS -> CalendarDeleteTask(
                context,
                listener as DuplicateDeleteTaskListener<CalendarItem>
            )
            MainSpinnerItem.CALL_LOGS -> CallLogDeleteTask(
                context,
                listener as DuplicateDeleteTaskListener<CallLogItem>
            )
            MainSpinnerItem.CONTACTS -> ContactDeleteTask(
                context,
                listener as DuplicateDeleteTaskListener<ContactItem>
            )
            MainSpinnerItem.MESSAGES -> MessageDeleteTask(
                context,
                listener as DuplicateDeleteTaskListener<MessageItem>
            )
        } as DuplicateDeleteTask<I, DuplicateDeleteTaskListener<I>>
    }

    override fun <Params, Progress, Result, L : DuplicateTaskListener<I>, T : DuplicateTask<I, Params, Progress, Result, L>> onDuplicateTaskStarted(
        task: T
    ) {
        if (task is DuplicateFindTask<*, *, *>) {
            searchStarted()
        } else if (task is DuplicateDeleteTask<*, *>) {
            deleteStarted()
        }
    }

    override fun <Params, Progress, Result, L : DuplicateTaskListener<I>, T : DuplicateTask<I, Params, Progress, Result, L>> onDuplicateTaskFinished(
        task: T
    ) {
        if (task is DuplicateFindTask<*, *, *>) {
            searchStopped(false)
        } else if (task is DuplicateDeleteTask<*, *>) {
            deleteStopped(false)
        }
    }

    override fun <Params, Progress, Result, L : DuplicateTaskListener<I>, T : DuplicateTask<I, Params, Progress, Result, L>> onDuplicateTaskCancelled(
        task: T
    ) {
        if (task is DuplicateFindTask<*, *, *>) {
            searchStopped(true)
        } else if (task is DuplicateDeleteTask<*, *>) {
            deleteStopped(true)
        }
    }

    override fun <Params, Progress, Result, L : DuplicateTaskListener<I>, T : DuplicateTask<I, Params, Progress, Result, L>> onDuplicateTaskProgress(
        task: T,
        count: Int
    ) {
        if (task === this.task) {
            binding.counter.text = getString(R.string.counter, count)
        }
    }

    override fun <L : DuplicateFindTaskListener<I, DuplicateViewHolder<I>>, T : DuplicateFindTask<I, DuplicateViewHolder<I>, L>> onDuplicateTaskMatch(
        task: T,
        item1: I,
        item2: I,
        match: Float,
        difference: BooleanArray
    ) {
        if (task === this.task) {
            runOnUiThread { adapter?.add(item1, item2, match, difference) }
        }
    }

    override fun <L : DuplicateDeleteTaskListener<I>, T : DuplicateDeleteTask<I, L>> onDuplicateTaskItemDeleted(
        task: T,
        item: I
    ) {
        if (task === this.task) {
            runOnUiThread { adapter?.removeItem(item) }
        }
    }

    override fun <L : DuplicateDeleteTaskListener<I>, T : DuplicateDeleteTask<I, L>> onDuplicateTaskPairDeleted(
        task: T,
        pair: DuplicateItemPair<I>
    ) {
        if (task === this.task) {
            runOnUiThread { adapter?.remove(pair) }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        task?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        if (adapter != null && adapter!!.itemCount > 0) {
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

    @Suppress("UNCHECKED_CAST")
    private fun deleteItems() {
        if (task != null && !task!!.isCancelled) {
            task!!.cancel()
        } else if (adapter != null && adapter!!.itemCount > 0 && spinnerItem != null) {
            val task = createDeleteTask(spinnerItem!!)
            if (task != null) {
                this.task = task as DuplicateTask<I, *, *, *, DuplicateTaskListener<I>>
                val pairs = adapter!!.getCheckedPairs()
                val params = pairs.toTypedArray()
                task.start(this, *params)
            } else {
                this.task = null
                searchStopped(false)
            }
        }
    }

    private fun selectAllItems() {
        if (adapter != null && adapter!!.itemCount > 0) {
            adapter!!.selectAll()
        }
    }

    private fun selectNoItems() {
        if (adapter != null && adapter!!.itemCount > 0) {
            adapter!!.selectNone()
        }
    }

    private fun deleteStarted() {
        binding.apply {
            spinnerAction.setImageResource(android.R.drawable.ic_media_pause)
            spinnerAction.isEnabled = true
            counter.text = getString(R.string.counter, 0)
            statusBar.visibility = View.VISIBLE
            listSwitcher.displayedChild = CHILD_LIST
        }
    }

    private fun deleteStopped(cancelled: Boolean) {
        task = null
        binding.apply {
            spinnerAction.setImageResource(android.R.drawable.ic_menu_search)
            spinnerAction.isEnabled = true
            statusBar.visibility = View.GONE
        }
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
