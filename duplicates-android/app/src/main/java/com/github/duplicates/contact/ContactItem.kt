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

import android.net.Uri
import androidx.core.net.toUri
import com.github.duplicates.DuplicateItem
import java.util.*

/**
 * Duplicate contact item.
 *
 * @author moshe.w
 */
class ContactItem : DuplicateItem() {

    var lookup: String? = null
    var accountName: String? = null
    var accountType: String? = null
    var displayName: String? = null
    var photoThumbnailUri: Uri? = null
    var emails: MutableCollection<EmailData> = ArrayList()
    var events: MutableCollection<EventData> = ArrayList()
    var ims: MutableCollection<ImData> = ArrayList()
    var phones: MutableCollection<PhoneData> = ArrayList()
    var photos: MutableCollection<PhotoData> = ArrayList()
    var names: MutableCollection<StructuredNameData> = ArrayList()

    fun setPhotoThumbnailUri(photoThumbnailPath: String?) {
        photoThumbnailUri = photoThumbnailPath?.toUri()
    }

    override fun toString(): String {
        return "$id: $displayName"
    }

    override fun contains(s: CharSequence): Boolean {
        var result = displayName?.contains(s) ?: false
        if (!result) {
            for (data in names) {
                result = result or data.containsAny(s)
            }
        }
        if (!result) {
            for (data in emails) {
                result = result or data.containsAny(s)
            }
        }
        if (!result) {
            for (data in events) {
                result = result or data.containsAny(s)
            }
        }
        if (!result) {
            for (data in phones) {
                result = result or data.containsAny(s)
            }
        }
        return result
    }
}
