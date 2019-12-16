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

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import com.github.duplicates.DuplicateItem
import java.io.ByteArrayOutputStream

/**
 * Duplicate bookmark item.
 *
 * @author moshe.w
 */
class BookmarkItem : DuplicateItem() {

    var created: Long = 0
    var date: Long = 0
    private var _favIcon: ByteArray? = null
    var favIcon: ByteArray?
        get() {
            if (_favIcon == null) {
                val icon = _icon
                if (icon != null) {
                    val out = ByteArrayOutputStream()
                    icon.compress(Bitmap.CompressFormat.PNG, 100, out)
                    _favIcon = out.toByteArray()
                }
            }
            return _favIcon
        }
        set(value) {
            _favIcon = value
            _icon = null
        }
    private var _icon: Bitmap? = null
    var icon: Bitmap?
        get() {
            if (_icon == null) {
                val favIcon = _favIcon
                if (favIcon != null) {
                    _icon = BitmapFactory.decodeByteArray(favIcon, 0, favIcon.size)
                }
            }
            return _icon
        }
        set(value) {
            _icon = value
            _favIcon = null
        }
    var title: String? = null
    var uri: Uri? = null
    var visits: Int = 0

    fun setUrl(url: String?) {
        uri = url?.toUri()
    }

    override fun contains(s: CharSequence): Boolean {
        return title?.contains(s, true) ?: false
    }
}
