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

/**
 * Contact data.
 *
 * @author moshe.w
 */
open class ContactData {

    var id: Long = 0
    var contactId: Long = 0
    var rawContactId: Long = 0
    var data1: String? = null
    var data2: String? = null
    var data3: String? = null
    var data4: String? = null
    var data5: String? = null
    var data6: String? = null
    var data7: String? = null
    var data8: String? = null
    var data9: String? = null
    var data10: String? = null
    var data11: String? = null
    var data12: String? = null
    var data13: String? = null
    var data14: String? = null
    open var data15: String? = null
    var dataVersion: Int = 0
    var mimeType: String? = null

    open val isEmpty: Boolean
        get() = data1 == null

    override fun hashCode(): Int {
        return id.toInt()
    }

    override fun toString(): String {
        return data1 ?: ""
    }

    open fun containsAny(s: CharSequence, ignoreCase: Boolean = false): Boolean {
        return (data1?.contains(s, ignoreCase) ?: false)
            || (data2?.contains(s, ignoreCase) ?: false)
            || (data3?.contains(s, ignoreCase) ?: false)
            || (data4?.contains(s, ignoreCase) ?: false)
            || (data5?.contains(s, ignoreCase) ?: false)
            || (data6?.contains(s, ignoreCase) ?: false)
            || (data7?.contains(s, ignoreCase) ?: false)
            || (data8?.contains(s, ignoreCase) ?: false)
            || (data9?.contains(s, ignoreCase) ?: false)
            || (data10?.contains(s, ignoreCase) ?: false)
            || (data11?.contains(s, ignoreCase) ?: false)
            || (data12?.contains(s, ignoreCase) ?: false)
            || (data13?.contains(s, ignoreCase) ?: false)
            || (data14?.contains(s, ignoreCase) ?: false)
            || (data15?.contains(s, ignoreCase) ?: false)
    }

    protected fun parseInt(s: String?): Int {
        return s?.toInt() ?: 0
    }

    protected operator fun contains(s: String): Boolean {
        return containsAny(s, true)
    }
}
