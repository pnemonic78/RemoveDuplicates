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
package com.github.duplicates.bookmark

import android.text.format.DateUtils.MINUTE_IN_MILLIS
import com.github.duplicates.DuplicateComparator

/**
 * Compare duplicate bookmarks.
 *
 * @author moshe.w
 */
class BookmarkComparator : DuplicateComparator<BookmarkItem>() {

    override fun compare(lhs: BookmarkItem, rhs: BookmarkItem): Int {
        var c: Int

        c = compare(lhs.created, rhs.created)
        if (c != SAME) return c
        c = compare(lhs.date, rhs.date)
        if (c != SAME) return c
        c = compare(lhs.favIcon, rhs.favIcon)
        if (c != SAME) return c
        c = compare(lhs.title, rhs.title)
        if (c != SAME) return c
        c = compare(lhs.uri, rhs.uri)
        if (c != SAME) return c
        c = compare(lhs.visits, rhs.visits)
        return if (c != SAME) c else super.compare(lhs, rhs)
    }

    override fun difference(lhs: BookmarkItem, rhs: BookmarkItem): BooleanArray {
        val result = BooleanArray(6)

        result[CREATED] = isDifferentTime(lhs.created, rhs.created, MINUTE_IN_MILLIS)
        result[DATE] = isDifferentTime(lhs.date, rhs.date, MINUTE_IN_MILLIS)
        result[FAVICON] = isDifferent(lhs.favIcon, rhs.favIcon)
        result[TITLE] = isDifferentIgnoreCase(lhs.title, rhs.title)
        result[URL] = isDifferent(lhs.uri, rhs.uri)
        result[VISITS] = isDifferent(lhs.visits, rhs.visits)

        return result
    }

    override fun match(lhs: BookmarkItem, rhs: BookmarkItem, difference: BooleanArray?): Float {
        val different = difference ?: difference(lhs, rhs)
        var match = MATCH_SAME

        if (different[URL]) {
            match *= 0.8f
        }
        if (different[TITLE]) {
            match *= matchTitle(lhs.title, rhs.title, 0.9f)
        }
        if (different[CREATED]) {
            match *= 0.95f
        }
        if (different[FAVICON]) {
            match *= 0.95f
        }
        if (different[DATE]) {
            match *= 0.975f
        }
        if (different[VISITS]) {
            match *= 0.975f
        }

        return match
    }

    companion object {

        const val URL = 0
        const val TITLE = 1
        const val CREATED = 2
        const val FAVICON = 3
        const val DATE = 4
        const val VISITS = 5
    }
}
