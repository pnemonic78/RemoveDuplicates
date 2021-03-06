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
package com.github.duplicates.contact

import com.github.duplicates.DuplicateComparator.Companion.SAME
import com.github.duplicates.DuplicateComparator.Companion.compareIgnoreCase

/**
 * Compare contacts by name and ID.
 *
 * @author moshe.w
 */
class ContactNameComparator : ContactIdComparator() {

    override fun compare(lhs: ContactItem, rhs: ContactItem): Int {
        val c = compareIgnoreCase(lhs.displayName, rhs.displayName)
        return if (c != SAME) c else super.compare(lhs, rhs)
    }
}
