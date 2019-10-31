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

import android.content.Context
import com.github.duplicates.DuplicateComparator.Companion.MATCH_SAME
import java.util.*
import java.util.concurrent.CancellationException

/**
 * Task to find duplicates.
 *
 * @author moshe.w
 */
abstract class DuplicateFindTask<T : DuplicateItem, VH : DuplicateViewHolder<T>>(context: Context, listener: DuplicateTaskListener<T, DuplicateTask<T, Any, Any, List<T>>>) : DuplicateTask<T, Any, Any, List<T>>(context, listener) {

    private var comparator: DuplicateComparator<T>? = null
    private val items = ArrayList<T>()

    abstract fun createAdapter(): DuplicateAdapter<T, VH>

    abstract fun createComparator(): DuplicateComparator<T>

    override fun onPreExecute() {
        super.onPreExecute()
        items.clear()
        this.comparator = createComparator()
    }

    override fun doInBackground(vararg params: Any): List<T> {
        try {
            provider.fetchItems(this)
        } catch (ignore: CancellationException) {
        }
        return items
    }

    override fun onProgressUpdate(vararg progress: Any) {
        val listener = this.listener
        if (progress.size == 1) {
            listener.onDuplicateTaskProgress(this, progress[0] as Int)
        } else {
            val item1 = progress[1] as T
            val item2 = progress[2] as T
            val match = progress[3] as Float
            val difference = progress[4] as BooleanArray
            listener.onDuplicateTaskMatch(this, item1, item2, match, difference)
        }
    }

    override fun onItemFetched(provider: DuplicateProvider<T>, count: Int, item: T) {
        val size = items.size

        // Maybe the item already exists in the list?
        var item1: T
        for (i in size - 1 downTo 0) {
            item1 = items[i]
            if (item === item1) {
                return
            }
        }

        // Is it a duplicate?
        var bestMatch = 0f
        var bestItem: T? = null
        var bestDifference: BooleanArray? = null
        var difference: BooleanArray
        var match: Float
        val comparator = this.comparator!!

        // Most likely that a matching item is a neighbour, so count backwards.
        for (i in size - 1 downTo 0) {
            item1 = items[i]
            difference = comparator.difference(item1, item)
            match = comparator.match(item1, item, difference)
            if (match >= MATCH_GOOD && match > bestMatch) {
                bestMatch = match
                bestDifference = difference
                bestItem = item1
                if (match == MATCH_SAME) {
                    break
                }
            }
        }
        if (bestItem != null) {
            publishProgress(size, bestItem, item, bestMatch, bestDifference)
        }

        if (items.add(item)) {
            publishProgress(size + 1)
        }
    }

    override fun onItemDeleted(provider: DuplicateProvider<T>, count: Int, item: T) {
        // Nothing to do.
    }

    override fun onPairDeleted(provider: DuplicateProvider<T>, count: Int, pair: DuplicateItemPair<T>) {
        // Nothing to do.
    }

    override fun getPermissions(): Array<String>? {
        return provider.getReadPermissions()
    }

    companion object {

        /**
         * Percentage for two items to be considered a good match.
         */
        const val MATCH_GOOD = 0.71f
    }
}
