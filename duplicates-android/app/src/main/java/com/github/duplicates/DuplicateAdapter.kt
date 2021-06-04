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
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.github.android.removeduplicates.databinding.SameItemBinding
import com.github.android.removeduplicates.databinding.SameItemShadowBinding
import com.github.duplicates.db.DuplicatesDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * List adapter for duplicate pairs.
 *
 * @author moshe.w
 */
abstract class DuplicateAdapter<T : DuplicateItem, VH : DuplicateViewHolder<T>> :
    RecyclerView.Adapter<VH>(), DuplicateViewHolder.OnItemCheckedChangeListener<T>, Filterable {

    private val pairsAll = ArrayList<DuplicateItemPair<T>>()
    private var pairs: MutableList<DuplicateItemPair<T>> = pairsAll
    private var filter: Filter? = null
    private var recyclerView: RecyclerView? = null

    init {
        @Suppress("LeakingThis")
        setHasStableIds(true)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val pair = pairs[position]
        holder.bind(pair)
    }

    override fun getItemCount(): Int {
        return pairs.size
    }

    override fun getItemId(position: Int): Long {
        val pair = pairs[position]
        return pair.id
    }

    /**
     * Clear the pairs.
     */
    fun clear() {
        pairsAll.clear()
        pairs.clear()
        notifyDataSetChangedWithClear()
    }

    /**
     * Add a pair.
     *
     * @param item1      the first item.
     * @param item2      the second item.
     * @param match      the matching percentage.
     * @param difference the array of differences.
     */
    fun add(item1: T, item2: T, match: Float = 1f, difference: BooleanArray) {
        val pair = DuplicateItemPair(item1, item2, match, difference)
        add(pair)
    }

    /**
     * Add a pair.
     *
     * @param pair      the pair.
     */
    fun add(pair: DuplicateItemPair<T>) {
        if (pair.item1.isChecked && pair.item2.isChecked) {
            return
        }
        if (pairs.add(pair)) {
            notifyItemInserted(pairs.size)
        }
    }

    /**
     * Remove an item and its pair.
     *
     * @param item the item.
     */
    fun removeItem(item: T) {
        var positions = findAllPairs(item)
        for (position in positions) {
            pairsAll.removeAt(position)
        }

        positions = findPairs(item)
        for (position in positions) {
            pairs.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     * Remove a pair.
     *
     * @param pair the item pair.
     */
    fun remove(pair: DuplicateItemPair<T>) {
        pairsAll.remove(pair)

        val position = pairs.indexOf(pair)
        if (position >= 0) {
            pairs.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     * Find all the pairs containing the item.
     *
     * @param item the item to find.
     * @return the list of indexes/positions - `null` otherwise.
     */
    private fun findAllPairs(item: T): List<Int> {
        return findPairs(item, pairsAll)
    }

    /**
     * Find the pairs containing the item.
     *
     * @param item  the item to find.
     * @param pairs the list of pairs with items.
     * @return the list of indexes/positions - `null` otherwise.
     */
    private fun findPairs(item: T, pairs: List<DuplicateItemPair<T>> = this.pairs): List<Int> {
        val positions = ArrayList<Int>()
        val size = pairs.size
        var pair: DuplicateItemPair<T>
        // Sort by descending index to avoid "index out of bounds" when displaying the list.
        for (i in size - 1 downTo 0) {
            pair = pairs[i]
            if (pair.item1 === item || pair.item2 === item) {
                positions.add(i)
            }
        }
        return positions
    }

    /**
     * Mark all the "duplicate" items as checked.
     */
    fun selectAll() {
        for (pair in pairs) {
            pair.item2.isChecked = true
        }
        notifyDataSetChanged()
    }

    /**
     * Mark all the items as unchecked.
     */
    fun selectNone() {
        for (pair in pairs) {
            pair.item1.isChecked = false
            pair.item2.isChecked = false
        }
        notifyDataSetChanged()
    }

    /**
     * Get the list of items that are checked for deletion.
     *
     * @return the list of items.
     */
    fun getCheckedItems(): Collection<T> {
        val items = TreeSet<T>()
        var item: T
        for (pair in pairs) {
            item = pair.item1
            if (item.isChecked) {
                items.add(item)
            }
            item = pair.item2
            if (item.isChecked) {
                items.add(item)
            }
        }
        return items
    }

    /**
     * Get the list of pairs that have checked items for deletion.
     *
     * @return the list of items.
     */
    fun getCheckedPairs(): Collection<DuplicateItemPair<T>> {
        val checked = ArrayList<DuplicateItemPair<T>>(pairs.size)
        for (pair in pairs) {
            if (pair.item1.isChecked || pair.item2.isChecked) {
                checked.add(pair)
            }
        }
        return checked
    }

    override fun onItemCheckedChangeListener(item: T, checked: Boolean) {
        item.isChecked = checked
        // update the database pair table `entity.isChecked = checked`
        updateDatabase(item)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        getFilter().filter(query)
    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = DuplicateAdapterFilter()
        }
        return filter!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView: ViewGroup
        val cardView: ViewGroup
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val binding = SameItemShadowBinding.inflate(inflater, parent, false)
            itemView = binding.root
            cardView = binding.item.root
        } else {
            val binding = SameItemBinding.inflate(inflater, parent, false)
            itemView = binding.root
            cardView = binding.root
        }
        return createCardViewHolder(context, inflater, itemView, cardView, viewType)
    }

    protected abstract fun createCardViewHolder(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup,
        cardView: ViewGroup,
        viewType: Int
    ): VH

    protected fun notifyDataSetChangedWithClear() {
        recyclerView?.recycledViewPool?.clear()
        notifyDataSetChanged()
    }

    private fun updateDatabase(item: T) {
        val context: Context = recyclerView?.context ?: return
        CoroutineScope(Dispatchers.IO).launch {
            val db = DuplicatesDatabase.getDatabase(context)
            val dao = db.pairDao()
            dao.updateItemChecked1(item.itemType, item.id, item.isChecked)
            dao.updateItemChecked2(item.itemType, item.id, item.isChecked)
        }
    }

    private inner class DuplicateAdapterFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filtered: MutableList<DuplicateItemPair<T>>

            if (TextUtils.isEmpty(constraint)) {
                filtered = pairsAll
            } else {
                filtered = ArrayList()
                for (pair in pairsAll) {
                    if (pair.item1.contains(constraint) || pair.item2.contains(constraint)) {
                        filtered.add(pair)
                    }
                }
            }

            val results = FilterResults()
            results.values = filtered
            results.count = filtered.size
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            pairs = results.values as MutableList<DuplicateItemPair<T>>
            notifyDataSetChangedWithClear()
        }
    }
}
