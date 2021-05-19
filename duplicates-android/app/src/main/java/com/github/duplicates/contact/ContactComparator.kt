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

import android.telephony.PhoneNumberUtils
import android.text.TextUtils
import com.github.duplicates.DuplicateComparator
import java.util.HashSet

/**
 * Compare duplicate contacts.
 *
 * @author moshe.w
 */
class ContactComparator : DuplicateComparator<ContactItem>() {

    override fun compare(lhs: ContactItem, rhs: ContactItem): Int {
        var c: Int

        c = compareData(lhs.emails, rhs.emails)
        if (c != SAME) {
            return c
        }
        c = compareData(lhs.events, rhs.events)
        if (c != SAME) {
            return c
        }
        c = compareData(lhs.ims, rhs.ims)
        if (c != SAME) {
            return c
        }
        c = compareData(lhs.names, rhs.names)
        if (c != SAME) {
            return c
        }
        c = compareData(lhs.phones, rhs.phones)
        return if (c != SAME) {
            c
        } else super.compare(lhs, rhs)
    }

    override fun difference(lhs: ContactItem, rhs: ContactItem): BooleanArray {
        val result = BooleanArray(5)

        result[EMAIL] = isDifferentData(lhs.emails, rhs.emails)
        result[EVENT] = isDifferentData(lhs.events, rhs.events)
        result[IM] = isDifferentData(lhs.ims, rhs.ims)
        result[NAME] = isDifferent(lhs.displayName, rhs.displayName)
        result[PHONE] = isDifferentData(lhs.phones, rhs.phones)

        return result
    }

    override fun match(lhs: ContactItem, rhs: ContactItem, difference: BooleanArray?): Float {
        var difference = difference
        if (difference == null) {
            difference = difference(lhs, rhs)
        }
        var match = MATCH_SAME

        if (difference[EMAIL]) {
            match *= matchEmails(lhs.emails, rhs.emails)
        }
        if (difference[EVENT]) {
            match *= matchEvents(lhs.events, rhs.events)
        }
        if (difference[IM]) {
            match *= matchIms(lhs.ims, rhs.ims)
        }
        if (difference[NAME]) {
            match *= matchNames(lhs.names, rhs.names)
        }
        if (difference[PHONE]) {
            match *= matchPhones(lhs.phones, rhs.phones)
        }

        return match
    }

    fun compareData(lhs: Collection<ContactData>, rhs: Collection<ContactData>): Int {
        if (lhs.isEmpty()) {
            return if (rhs.isEmpty()) SAME else RHS
        }
        if (rhs.isEmpty()) {
            return LHS
        }
        val set1 = HashSet<String>(lhs.size)
        for (datum in lhs) {
            set1.add(datum.toString().toLowerCase(locale))
        }
        val set2 = HashSet<String>(rhs.size)
        for (datum in rhs) {
            set2.add(datum.toString().toLowerCase(locale))
        }
        return compare(set1, set2)
    }

    fun isDifferentData(lhs: Collection<ContactData>, rhs: Collection<ContactData>): Boolean {
        return compareData(lhs, rhs) != SAME
    }

    protected fun matchEmails(lhs: Collection<EmailData>, rhs: Collection<EmailData>): Float {
        if (lhs.isEmpty()) {
            return if (rhs.isEmpty()) MATCH_SAME else MATCH_DATA
        }
        if (rhs.isEmpty()) {
            return MATCH_DATA
        }
        var s1: String?
        for (d1 in lhs) {
            s1 = d1.address
            for (d2 in rhs) {
                if (compareIgnoreCase(s1, d2.address) == SAME) {
                    return MATCH_SAME
                }
            }
        }
        return MATCH_DATA
    }

    protected fun matchEvents(lhs: Collection<EventData>, rhs: Collection<EventData>): Float {
        if (lhs.isEmpty()) {
            return if (rhs.isEmpty()) MATCH_SAME else MATCH_DATA
        }
        if (rhs.isEmpty()) {
            return MATCH_DATA
        }
        var s1: String?
        var t1: Int
        for (d1 in lhs) {
            s1 = d1.startDate
            t1 = d1.type
            for (d2 in rhs) {
                if (compare(s1, d2.startDate) == SAME && compare(t1, d2.type) == SAME) {
                    return MATCH_SAME
                }
            }
        }
        return MATCH_DATA
    }

    protected fun matchIms(lhs: Collection<ImData>, rhs: Collection<ImData>): Float {
        if (lhs.isEmpty()) {
            return if (rhs.isEmpty()) MATCH_SAME else MATCH_DATA
        }
        if (rhs.isEmpty()) {
            return MATCH_DATA
        }
        var s1: String?
        for (d1 in lhs) {
            s1 = d1.data
            for (d2 in rhs) {
                if (compareIgnoreCase(s1, d2.data) == SAME) {
                    return MATCH_SAME
                }
            }
        }
        return MATCH_DATA
    }

    protected fun matchNames(
        lhs: Collection<StructuredNameData>,
        rhs: Collection<StructuredNameData>
    ): Float {
        if (lhs.isEmpty() || rhs.isEmpty()) {
            return MATCH_NAME
        }
        var s1: String?
        var g1: String?
        var m1: String?
        var f1: String?
        var g2: String?
        var m2: String?
        var f2: String?
        var g1Empty: Boolean
        var f1Empty: Boolean
        var g2Empty: Boolean
        var f2Empty: Boolean
        for (d1 in lhs) {
            s1 = d1.displayName
            g1 = d1.givenName
            g1Empty = TextUtils.isEmpty(g1)
            m1 = d1.middleName
            if (g1Empty) {
                g1 = m1
                g1Empty = TextUtils.isEmpty(g1)
            }
            f1 = d1.familyName
            f1Empty = TextUtils.isEmpty(f1)
            for (d2 in rhs) {
                if (compareIgnoreCase(s1, d2.displayName) == SAME) {
                    return MATCH_SAME
                }
                g2 = d2.givenName
                g2Empty = TextUtils.isEmpty(g2)
                m2 = d2.middleName
                if (g2Empty) {
                    g2 = m2
                    g2Empty = TextUtils.isEmpty(g2)
                }
                f2 = d2.familyName
                f2Empty = TextUtils.isEmpty(f2)
                // if (g1 = g2 and f1 = f2) or (g1 = g2 and either f1 is empty or f2 is empty) or (f1 = f2 and either g1 is empty or g2 is empty)
                if (compareIgnoreCase(g1, g2) == SAME) {
                    if (compareIgnoreCase(f1, f2) == SAME) {
                        return MATCH_SAME
                    }
                    if (f1Empty || f2Empty) {
                        return MATCH_SAME
                    }
                } else if (compareIgnoreCase(f1, f2) == SAME) {
                    if (g1Empty || g2Empty) {
                        return MATCH_SAME
                    }
                }
            }
        }
        return MATCH_NAME
    }

    protected fun matchPhones(lhs: Collection<PhoneData>, rhs: Collection<PhoneData>): Float {
        if (lhs.isEmpty()) {
            return if (rhs.isEmpty()) MATCH_SAME else MATCH_DATA
        }
        if (rhs.isEmpty()) {
            return MATCH_DATA
        }
        var s1: String?
        for (d1 in lhs) {
            s1 = d1.number
            for (d2 in rhs) {
                if (PhoneNumberUtils.compare(s1, d2.number)) {
                    return MATCH_SAME
                }
            }
        }
        return MATCH_DATA
    }

    companion object {

        const val EMAIL = 0
        const val EVENT = 1
        const val IM = 2
        const val NAME = 3
        const val PHONE = 4

        private const val MATCH_DATA = 0.85f
        private const val MATCH_NAME = 0.8f
    }
}
