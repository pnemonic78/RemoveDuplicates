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

import android.text.TextUtils

import android.provider.ContactsContract.CommonDataKinds.Im

/**
 * Contact instant-messaging data.
 *
 * @author moshe.w
 */
class ImData : ContactData() {

    val data: String?
        get() = data1

    val type: Int
        get() = parseInt(data2)

    val label: String?
        get() = data3

    val protocol: Int
        get() = parseInt(data5)

    val customProtocol: String?
        get() = data6

    init {
        mimeType = Im.CONTENT_ITEM_TYPE
    }

    override fun toString(): String {
        val label = label
        return data!! + if (TextUtils.isEmpty(label)) "" else " " + label!!
    }

    override fun containsAny(s: CharSequence, ignoreCase: Boolean): Boolean {
        return (label?.contains(s, ignoreCase) ?: false)
    }
}
