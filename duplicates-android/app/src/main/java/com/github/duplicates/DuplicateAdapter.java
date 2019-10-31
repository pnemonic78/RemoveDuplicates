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

import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.android.removeduplicates.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * List adapter for duplicate pairs.
 *
 * @author moshe.w
 */
public abstract class DuplicateAdapter<T extends DuplicateItem, VH extends DuplicateViewHolder<T>> extends RecyclerView.Adapter<VH> implements
        DuplicateViewHolder.OnItemCheckedChangeListener<T>,
        Filterable {

    private final List<DuplicateItemPair<T>> pairsAll = new ArrayList<>();
    private List<DuplicateItemPair<T>> pairs = pairsAll;
    private Filter filter;

    public DuplicateAdapter() {
        setHasStableIds(true);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        DuplicateItemPair<T> pair = pairs.get(position);
        holder.bind(pair);
    }

    @Override
    public int getItemCount() {
        return pairs.size();
    }

    @Override
    public long getItemId(int position) {
        DuplicateItemPair<T> pair = pairs.get(position);
        return pair.getId();
    }

    /**
     * Clear the pairs.
     */
    public void clear() {
        pairsAll.clear();
        pairs.clear();
        notifyDataSetChanged();
    }

    /**
     * Add a pair.
     *
     * @param item1 the first item.
     * @param item2 the second item.
     */
    public void add(T item1, T item2) {
        add(item1, item2, 1f, null);
    }

    /**
     * Add a pair.
     *
     * @param item1      the first item.
     * @param item2      the second item.
     * @param match      the matching percentage.
     * @param difference the array of differences.
     */
    public void add(T item1, T item2, float match, boolean[] difference) {
        if (item1.isChecked && item2.isChecked) {
            return;
        }
        DuplicateItemPair<T> pair = new DuplicateItemPair<>(item1, item2, match, difference);
        if (pairs.add(pair)) {
            notifyItemInserted(pairs.size());
        }
    }

    /**
     * Remove an item and its pair.
     *
     * @param item the item.
     */
    public void remove(T item) {
        List<Integer> positions = findAllPairs(item);
        for (int position : positions) {
            pairsAll.remove(position);
        }

        positions = findPairs(item);
        for (int position : positions) {
            pairs.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Remove a pair.
     *
     * @param pair the item pair.
     */
    public void remove(DuplicateItemPair<T> pair) {
        pairsAll.remove(pair);

        int position = pairs.indexOf(pair);
        if (position >= 0) {
            pairs.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Find the pairs containing the item.
     *
     * @param item the item to find.
     * @return the list of indexes/positions - {@code null} otherwise.
     */
    @NonNull
    protected List<Integer> findPairs(T item) {
        return findPairs(item, pairs);
    }

    /**
     * Find all the pairs containing the item.
     *
     * @param item the item to find.
     * @return the list of indexes/positions - {@code null} otherwise.
     */
    @NonNull
    protected List<Integer> findAllPairs(T item) {
        return findPairs(item, pairsAll);
    }

    /**
     * Find the pairs containing the item.
     *
     * @param item  the item to find.
     * @param pairs the list of pairs with items.
     * @return the list of indexes/positions - {@code null} otherwise.
     */
    @NonNull
    protected List<Integer> findPairs(T item, List<DuplicateItemPair<T>> pairs) {
        List<Integer> positions = new ArrayList<>();
        int size = pairs.size();
        DuplicateItemPair<T> pair;
        // Sort by descending index to avoid "index out of bounds" when displaying the list.
        for (int i = size - 1; i >= 0; i--) {
            pair = pairs.get(i);
            if ((pair.item1 == item) || (pair.item2 == item)) {
                positions.add(i);
            }
        }
        return positions;
    }

    /**
     * Mark all the "duplicate" items as checked.
     */
    public void selectAll() {
        for (DuplicateItemPair<T> pair : pairs) {
            pair.item2.isChecked = true;
        }
        notifyDataSetChanged();
    }

    /**
     * Mark all the items as unchecked.
     */
    public void selectNone() {
        for (DuplicateItemPair<T> pair : pairs) {
            pair.item1.isChecked = false;
            pair.item2.isChecked = false;
        }
        notifyDataSetChanged();
    }

    /**
     * Get the list of items that are checked for deletion.
     *
     * @return the list of items.
     */
    @NonNull
    public Collection<T> getCheckedItems() {
        Set<T> items = new TreeSet<>();
        T item = null;
        for (DuplicateItemPair<T> pair : pairs) {
            item = pair.item1;
            if (item.isChecked) {
                items.add(item);
            }
            item = pair.item2;
            if (item.isChecked) {
                items.add(item);
            }
        }
        return items;
    }

    /**
     * Get the list of pairs that have checked items for deletion.
     *
     * @return the list of items.
     */
    @NonNull
    public Collection<DuplicateItemPair<T>> getCheckedPairs() {
        List<DuplicateItemPair<T>> checked = new ArrayList<>(pairs.size());
        for (DuplicateItemPair<T> pair : pairs) {
            if (pair.item1.isChecked || pair.item2.isChecked) {
                checked.add(pair);
            }
        }
        return checked;
    }

    @Override
    public void onItemCheckedChangeListener(T item, boolean checked) {
        item.isChecked = checked;
        notifyDataSetChanged();//FIXME Update only the affected rows!
    }

    public void filter(String query) {
        getFilter().filter(query);
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new DuplicateAdapterFilter();
        }
        return filter;
    }

    protected View createViewHolder(@LayoutRes int layoutId, ViewGroup parent, int viewType) {
        View itemView;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.same_item_shadow, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.same_item, parent, false);
        }
        ViewStub stub = itemView.findViewById(R.id.stub);
        stub.setLayoutResource(layoutId);
        stub.inflate();
        return itemView;
    }

    private class DuplicateAdapterFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<DuplicateItemPair<T>> filtered;

            if (TextUtils.isEmpty(constraint)) {
                filtered = pairsAll;
            } else {
                filtered = new ArrayList<>();
                for (DuplicateItemPair<T> pair : pairsAll) {
                    if (pair.item1.contains(constraint)
                            || pair.item2.contains(constraint)) {
                        filtered.add(pair);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filtered;
            results.count = filtered.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            pairs = (List<DuplicateItemPair<T>>) results.values;
            notifyDataSetChanged();
        }
    }
}
