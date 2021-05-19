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

import android.graphics.Bitmap
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.text.TextUtils.isEmpty
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Comparator to determine if it is a duplicate.
 *
 * @author moshe.w
 */
abstract class DuplicateComparator<T : DuplicateItem> : Comparator<T> {

    protected val locale: Locale = Locale.getDefault()

    override fun compare(lhs: T, rhs: T): Int {
        return compare(lhs.id, rhs.id)
    }

    /**
     * How similar are the two items?
     *
     * @param lhs        the left-hand-side item.
     * @param rhs        the right-hand-side item.
     * @param difference the array of differences.
     * @return the match as a percentage between `0.0` (dissimilar) and `1.0` (identical) inclusive.
     */
    abstract fun match(lhs: T, rhs: T, difference: BooleanArray?): Float

    /**
     * How different are the two items?
     *
     * @param lhs the first item.
     * @param rhs the other item.
     * @return the array of differences.
     */
    abstract fun difference(lhs: T, rhs: T): BooleanArray

    /**
     * How similar are the two items?
     *
     * @param lhs the first item.
     * @param rhs the other item.
     * @return the match as a percentage between `0.0` (dissimilar) and [.MATCH_SAME] (identical) inclusive.
     * @see .match
     * @see .difference
     */
    fun match(lhs: T, rhs: T): Float {
        return match(lhs, rhs, difference(lhs, rhs))
    }

    protected fun matchTitle(lhs: String?, rhs: String?, different: Float): Float {
        val s1 = lhs?.trim() ?: ""
        val s2 = rhs?.trim() ?: ""
        if (compareIgnoreCase(s1, s2) == 0) {
            return MATCH_SAME
        }
        if (isEmpty(s1) || isEmpty(s2)) {
            return different
        }
        val tokens1 = s1.split(" ,;".toRegex())
        val tokens2 = s2.split(" ,;".toRegex())
        val lengthMin = min(tokens1.size, tokens2.size)
        val lengthMax = max(tokens1.size, tokens2.size)
        var matches = 0
        for (i in 0 until lengthMin) {
            if (compareIgnoreCase(tokens1[i], tokens2[i]) == 0) {
                matches++
            }
        }
        return different + (MATCH_SAME - different) * matches / lengthMax
    }

    companion object {

        const val SAME = 0
        const val LHS = +1
        const val RHS = -1

        const val MATCH_SAME = 1f

        fun <T : Comparable<T>> compare(lhs: T?, rhs: T?): Int {
            if (lhs === rhs) {
                return SAME
            }
            return if (lhs == null) RHS else if (rhs == null) LHS else lhs.compareTo(rhs)
        }

        fun <T : Comparable<T>> isDifferent(lhs: T?, rhs: T?): Boolean {
            return compare(lhs, rhs) != SAME
        }

        fun compare(lhs: ByteArray?, rhs: ByteArray?): Int {
            if (lhs === rhs) {
                return SAME
            }
            if (lhs == null) {
                return RHS
            }
            if (rhs == null) {
                return LHS
            }
            val l1 = lhs.size
            val l2 = rhs.size
            val c = l1 - l2
            return if (c == 0 && Arrays.equals(lhs, rhs)) {
                SAME
            } else {
                c
            }
        }

        fun isDifferent(lhs: ByteArray?, rhs: ByteArray?): Boolean {
            return compare(lhs, rhs) != SAME
        }

        fun compare(lhs: Bitmap?, rhs: Bitmap?): Int {
            if (lhs === rhs) {
                return SAME
            }
            if (lhs == null) {
                return RHS
            }
            if (rhs == null) {
                return LHS
            }
            val w1 = lhs.width
            val w2 = rhs.width
            var c = w1 - w2
            if (c != 0) {
                return c
            }
            val h1 = lhs.height
            val h2 = rhs.height
            c = h1 - h2
            return if (c == 0 && lhs.sameAs(rhs)) {
                SAME
            } else {
                c
            }
        }

        fun isDifferent(lhs: Bitmap?, rhs: Bitmap?): Boolean {
            return compare(lhs, rhs) != SAME
        }

        fun compare(lhs: Uri?, rhs: Uri?): Int {
            if (lhs === rhs) {
                return SAME
            }
            if (lhs == null) {
                return RHS
            }
            if (rhs == null) {
                return LHS
            }
            var s1 = lhs.toString()
            if (s1.endsWith("/")) {
                s1 = s1.substring(0, s1.length - 1)
            }
            var s2 = rhs.toString()
            if (s2.endsWith("/")) {
                s2 = s2.substring(0, s2.length - 1)
            }
            return s1.compareTo(s2)
        }

        fun isDifferent(lhs: Uri?, rhs: Uri?): Boolean {
            return compare(lhs, rhs) != SAME
        }

        fun <C : Comparable<C>> compare(lhs: Collection<C>?, rhs: Collection<C>?): Int {
            if (lhs === rhs) {
                return SAME
            }
            if (lhs == null) {
                return RHS
            }
            if (rhs == null) {
                return LHS
            }
            var c = lhs.size - rhs.size
            if (c != 0) {
                return c
            }
            val size = lhs.size
            val l1 = if (lhs is List<C>) lhs else ArrayList(lhs)
            val l2 = if (rhs is List<C>) rhs else ArrayList(rhs)
            for (i in 0 until size) {
                c = compare(l1[i], l2[i])
                if (c != SAME) {
                    return c
                }
            }
            return SAME
        }

        fun <C : Comparable<C>> isDifferent(lhs: Collection<C>?, rhs: Collection<C>?): Boolean {
            return compare(lhs, rhs) != SAME
        }

        fun compareIgnoreCase(lhs: String?, rhs: String?): Int {
            if (lhs === rhs) {
                return SAME
            }
            return if (lhs == null) RHS else if (rhs == null) LHS else lhs.compareTo(
                rhs,
                ignoreCase = true
            )
        }

        fun isDifferentIgnoreCase(lhs: String?, rhs: String?): Boolean {
            return compareIgnoreCase(lhs, rhs) != SAME
        }

        fun comparePhoneNumber(lhs: String?, rhs: String?): Int {
            val c = compareIgnoreCase(lhs, rhs)
            if (c == SAME) {
                return c
            }
            return if (PhoneNumberUtils.compare(lhs, rhs)) SAME else c
        }

        fun isDifferentPhoneNumber(lhs: String?, rhs: String?): Boolean {
            return comparePhoneNumber(lhs, rhs) != SAME
        }

        fun compareTime(lhs: Long, rhs: Long, delta: Long = 0L): Int {
            return if (abs(lhs - rhs) <= delta) SAME else compare(lhs, rhs)
        }

        fun isDifferentTime(lhs: Long, rhs: Long, delta: Long = 0L): Boolean {
            return compareTime(lhs, rhs, delta) != SAME
        }
    }
}
