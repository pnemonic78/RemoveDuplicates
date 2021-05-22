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
import com.github.duplicates.db.DuplicateItemPairEntity
import com.github.duplicates.db.DuplicatesDatabase
import java.util.*
import java.util.concurrent.*
import kotlin.math.abs

/**
 * Task to find duplicates.
 *
 * @author moshe.w
 */
abstract class DuplicateFindTask<I : DuplicateItem, VH : DuplicateViewHolder<I>, L : DuplicateFindTaskListener<I, VH>>(
    private val itemType: DuplicateItemType,
    context: Context,
    listener: L
) : DuplicateTask<I, Any, Any, List<I>, L>(context, listener) {

    private var comparator: DuplicateComparator<I>? = null
    private val items = ArrayList<I>()
    private lateinit var db: DuplicatesDatabase

    abstract fun createAdapter(): DuplicateAdapter<I, VH>

    abstract fun createComparator(): DuplicateComparator<I>

    override fun onPreExecute() {
        super.onPreExecute()
        items.clear()
        this.comparator = createComparator()
    }

    override fun doInBackground(vararg params: Any): List<I> {
        db = DuplicatesDatabase.getDatabase(context)
        if (params.isNotEmpty()) {
            val param0 = params[0]
            if ((param0 is Boolean) && param0) {
                doFindDatabase(db)
                return items
            }
        }
        doFind(db)
        return items
    }

    private fun doFind(db: DuplicatesDatabase) {
        clearDatabaseTable(db)
        try {
            provider.fetchItems(this)
        } catch (ignore: CancellationException) {
        }
    }

    private fun doFindDatabase(db: DuplicatesDatabase) {
        val dao = db.pairDao()
        val entities = dao.queryAll(itemType)
        val comparator = this.comparator ?: return
        try {
            for (entity in entities) {
                val item1 = provider.fetchItem(entity.id1) ?: return
                val item2 = provider.fetchItem(entity.id2) ?: return
                item1.isChecked = entity.isChecked1
                item2.isChecked = entity.isChecked2
                val difference = comparator.difference(item1, item2)
                val match = comparator.match(item1, item2, difference)

                // Did the data change in the interim?
                if ((abs(match - entity.match) >= 0.01f)) continue

                listener.onDuplicateTaskMatch(this, item1, item2, match, difference)
                if (items.add(item1) && items.add(item2)) {
                    publishProgress(items.size)
                }

                if (isCancelled) {
                    break
                }
            }
        } catch (ignore: CancellationException) {
        }
    }

    override fun onItemFetched(provider: DuplicateProvider<I>, count: Int, item: I) {
        val size = items.size

        // Maybe the item already exists in the list?
        var item1: I
        for (i in size - 1 downTo 0) {
            item1 = items[i]
            if (item === item1) {
                return
            }
        }

        // Is it a duplicate?
        var bestMatch = 0f
        var bestItem: I? = null
        var bestDifference: BooleanArray? = null
        var difference: BooleanArray
        var match: Float
        val comparator = this.comparator ?: return

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
            item1 = bestItem
            match = bestMatch
            difference = bestDifference!!
            listener.onDuplicateTaskMatch(this, item1, item, match, difference)
            insertDatabase(item1, item, match)
        }

        if (items.add(item)) {
            publishProgress(size + 1)
        }
    }

    override fun onItemDeleted(provider: DuplicateProvider<I>, count: Int, item: I) {
        // Nothing to do.
    }

    override fun onPairDeleted(
        provider: DuplicateProvider<I>,
        count: Int,
        pair: DuplicateItemPair<I>
    ) {
        // Nothing to do.
    }

    override fun getPermissions(): Array<String>? {
        return provider.getReadPermissions()
    }

    private fun clearDatabaseTable(db: DuplicatesDatabase) {
        val dao = db.pairDao()
        dao.deleteAll(itemType)
    }

    private fun insertDatabase(item1: I, item2: I, match: Float) {
        val isChecked2 = item2.isChecked or (match >= DuplicateItemPair.MATCH_GREAT)
        val dao = db.pairDao()
        val entity = DuplicateItemPairEntity(
            type = itemType,
            match = match,
            id1 = item1.id,
            isChecked1 = item1.isChecked,
            id2 = item2.id,
            isChecked2 = isChecked2
        )
        dao.insert(entity)
    }

    companion object {

        /**
         * Percentage for two items to be considered a good match.
         */
        const val MATCH_GOOD = 0.7f
    }
}
