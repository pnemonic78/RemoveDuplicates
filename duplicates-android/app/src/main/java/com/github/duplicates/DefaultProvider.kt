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

import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.github.duplicates.db.DuplicateItemPairDao

/**
 * Default provider that does nothing.
 *
 * @author moshe.w
 */
class DefaultProvider<T : DuplicateItem>(context: Context) : DuplicateProvider<T>(context) {

    override fun getContentUri(): Uri? {
        return null
    }

    override fun createItem(cursor: Cursor): T? {
        return null
    }

    override fun populateItem(cursor: Cursor, item: T) {}

    override fun getReadPermissions(): Array<String>? {
        return null
    }

    override fun getDeletePermissions(): Array<String>? {
        return null
    }

    override fun clearDatabaseTable(dao: DuplicateItemPairDao) {
        dao.deleteAll()
    }
}
