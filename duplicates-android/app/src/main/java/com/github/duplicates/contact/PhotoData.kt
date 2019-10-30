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

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.ContactsContract.CommonDataKinds.Photo
import android.text.TextUtils
import android.util.Base64

/**
 * Contact photograph data.
 *
 * @author moshe.w
 */
class PhotoData : ContactData() {

    private var _photo: Bitmap? = null
    val photo: Bitmap?
        get() {
            if (_photo == null) {
                val data15 = data15
                if (!TextUtils.isEmpty(data15)) {
                    val blob = Base64.decode(data15, Base64.DEFAULT)
                    if (blob != null) {
                        _photo = BitmapFactory.decodeByteArray(blob, 0, blob.size)
                    }
                }
            }
            return _photo
        }

    val photoFileId: Long
        get() = data14?.toLong() ?: 0L

    override var data15: String?
        get() = super.data15
        set(data15) {
            super.data15 = data15
            _photo = null
        }

    override val isEmpty: Boolean
        get() = data15 == null

    init {
        mimeType = Photo.CONTENT_ITEM_TYPE
    }

    override fun toString(): String {
        return data15 ?: super.toString()
    }

    override fun containsAny(s: CharSequence): Boolean {
        return false
    }
}
