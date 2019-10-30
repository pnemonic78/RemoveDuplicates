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

import android.provider.ContactsContract.CommonDataKinds.StructuredName

/**
 * Contact structured name data.
 *
 * @author moshe.w
 */
class StructuredNameData : ContactData() {

    val displayName: String?
        get() = data1

    val givenName: String?
        get() = data2

    val familyName: String?
        get() = data3

    val prefix: String?
        get() = data4

    val middleName: String?
        get() = data5

    val suffix: String?
        get() = data6

    val phoneticGivenName: String?
        get() = data7

    val phoneticMiddleName: String?
        get() = data8

    val phoneticFamilyName: String?
        get() = data9

    init {
        mimeType = StructuredName.CONTENT_ITEM_TYPE
    }

    override fun toString(): String {
        return displayName ?: super.toString()
    }
}
