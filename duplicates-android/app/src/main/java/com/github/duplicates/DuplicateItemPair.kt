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

import com.github.duplicates.DuplicateComparator.Companion.SAME

/**
 * Item that is a possible duplicate of two items.
 *
 * @author moshe.w
 */
class DuplicateItemPair<T : DuplicateItem>(val item1: T, val item2: T, val match: Float = 1f, val difference: BooleanArray) : Comparable<DuplicateItemPair<T>> {

    val id: Long
        get() {
            val id1 = item1.id
            val id2 = item2.id
            return id1 and 0xFFFFFFFFL shl 32 or (id2 and 0xFFFFFFFFL)
        }

    init {
        if (match >= MATCH_GREAT) {
            item2.isChecked = true
        }
    }

    override fun compareTo(other: DuplicateItemPair<T>): Int {
        val thisId1 = this.item1.id
        val thatId1 = other.item1.id
        val c = thisId1.compareTo(thatId1)
        if (c != SAME) {
            return c
        }
        val thisId2 = this.item2.id
        val thatId2 = other.item2.id
        return thisId2.compareTo(thatId2)
    }

    companion object {

        /**
         * Percentage for two items to be considered a very good match.
         */
        const val MATCH_GREAT = 0.99f
    }
}
