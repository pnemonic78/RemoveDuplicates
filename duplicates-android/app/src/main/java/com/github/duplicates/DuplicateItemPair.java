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

import androidx.annotation.NonNull;

/**
 * Item that is a possible duplicate of two items.
 *
 * @author moshe.w
 */
public class DuplicateItemPair<T extends DuplicateItem> implements Comparable<DuplicateItemPair<T>> {

    /**
     * Percentage for two items to be considered a very good match.
     */
    public static final float MATCH_GREAT = 0.95f;

    private T item1;
    private T item2;
    private float match;
    private boolean[] difference;

    public DuplicateItemPair() {
    }

    public DuplicateItemPair(T item1, T item2) {
        this(item1, item2, 1f, null);
    }

    public DuplicateItemPair(T item1, T item2, float match, boolean[] difference) {
        this.item1 = item1;
        this.item2 = item2;
        this.match = match;
        this.difference = difference;

        if (match >= MATCH_GREAT) {
            item2.isChecked = true;
        }
    }

    public T getItem1() {
        return item1;
    }

    public T getItem2() {
        return item2;
    }

    public float getMatch() {
        return match;
    }

    public boolean[] getDifference() {
        return difference;
    }

    public long getId() {
        long id1 = getItem1().id;
        long id2 = getItem2().id;
        return ((id1 & 0xFFFFFFFFL) << 32) | (id2 & 0xFFFFFFFFL);
    }

    @Override
    public int compareTo(@NonNull DuplicateItemPair<T> that) {
        long thisId1 = this.item1.id;
        long thatId1 = that.item1.id;
        int c = Long.compare(thisId1, thatId1);
        if (c != 0) {
            return c;
        }
        long thisId2 = this.item2.id;
        long thatId2 = that.item2.id;
        return Long.compare(thisId2, thatId2);
    }
}
