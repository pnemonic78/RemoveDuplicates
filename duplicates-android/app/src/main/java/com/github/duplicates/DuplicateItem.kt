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

import com.github.duplicates.DuplicateComparator.Companion.compare

/**
 * Item that is a duplicate of some other item.
 *
 * @author moshe.w
 */
abstract class DuplicateItem : Comparable<DuplicateItem> {

    var id: Long = 0
    var isChecked: Boolean = false
    var isError: Boolean = false

    override fun compareTo(other: DuplicateItem): Int {
        return compare(this.id, other.id)
    }

    override fun hashCode(): Int {
        return id.toInt()
    }

    abstract operator fun contains(s: CharSequence): Boolean
}
