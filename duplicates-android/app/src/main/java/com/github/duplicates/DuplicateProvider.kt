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

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.annotation.MainThread
import com.github.duplicates.db.DuplicateItemPairDao
import com.github.duplicates.db.DuplicatesDatabase
import timber.log.Timber
import java.util.*
import java.util.concurrent.*

/**
 * Provider of duplicate items.
 *
 * @author moshe.w
 */
abstract class DuplicateProvider<T : DuplicateItem> protected constructor(private val context: Context) {

    open var listener: DuplicateProviderListener<T, DuplicateProvider<T>>? = null

    /**
     * Are current operations cancelled?
     *
     * @return `true` if cancelled.
     */
    open var isCancelled: Boolean = false
        protected set

    abstract fun getContentUri(): Uri?

    open fun getCursorProjection(): Array<String>? = null

    open fun getCursorSelection(): String? = null

    open fun getCursorOrder(): String? = null

    abstract fun createItem(cursor: Cursor): T?

    abstract fun populateItem(cursor: Cursor, item: T)

    /**
     * Get the items from the system provider.
     *
     * @return the list of items.
     * @throws CancellationException if the provider has been cancelled.
     */
    @Throws(CancellationException::class)
    open fun getItems(): List<T> {
        if (isCancelled) {
            throw CancellationException()
        }
        val items = ArrayList<T>()
        val cr = context.contentResolver

        val contentUri = getContentUri() ?: return items
        val cursor = cr.query(contentUri, getCursorProjection(), null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                var item: T?
                do {
                    if (isCancelled) {
                        break
                    }
                    item = createItem(cursor)
                    if (item != null) {
                        populateItem(cursor, item)
                        items.add(item)
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        }

        return items
    }

    /**
     * Fetch the items from the system provider into the listener.
     *
     * @param listener the listener.
     * @throws CancellationException if the provider has been cancelled.
     */
    @Throws(CancellationException::class)
    open fun fetchItems(listener: DuplicateProviderListener<T, DuplicateProvider<T>>) {
        if (isCancelled) {
            throw CancellationException()
        }
        val cr = context.contentResolver

        val contentUri = getContentUri() ?: return
        val cursor = cr.query(
            contentUri,
            getCursorProjection(),
            getCursorSelection(),
            null,
            getCursorOrder()
        )
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    val provider: DuplicateProvider<T> = this
                    var item: T?
                    var count = 0
                    do {
                        if (isCancelled) {
                            break
                        }
                        item = createItem(cursor)
                        if (item != null) {
                            populateItem(cursor, item)
                            listener.onItemFetched(provider, ++count, item)
                        }
                    } while (cursor.moveToNext())
                }
            } catch (e: RuntimeException) {
                Timber.e(e, "Error fetching items: %s", e.message)
            } finally {
                cursor.close()
            }
        }
    }

    /**
     * Delete the items from the system provider.
     *
     * @param items the list of items.
     * @throws CancellationException if the provider has been cancelled.
     */
    @Throws(CancellationException::class)
    open fun deleteItems(items: Collection<T>) {
        if (items.isEmpty()) {
            return
        }
        if (isCancelled) {
            throw CancellationException()
        }
        val listener = this.listener ?: return
        val cr = context.contentResolver

        var count = 0
        for (item in items) {
            if (isCancelled) {
                break
            }
            if (deleteItem(cr, item)) {
                listener.onItemDeleted(this, ++count, item)
            }
        }
    }

    /**
     * Delete an item from the system provider.
     *
     * @param item the item.
     */
    open fun deleteItem(item: T): Boolean {
        return if (isCancelled) {
            false
        } else {
            deleteItem(context.contentResolver, item)
        }
    }

    /**
     * Delete an item from the system provider.
     *
     * @param cr   the content resolver.
     * @param item the item.
     */
    open fun deleteItem(cr: ContentResolver, item: T): Boolean {
        val contentUri = getContentUri()
        try {
            item.isError = false
            return (contentUri != null) && cr.delete(
                ContentUris.withAppendedId(
                    contentUri,
                    item.id
                ), null, null
            ) > 0
        } catch (e: IllegalArgumentException) {
            item.isError = true
            Timber.e(e, "deleteItem: %s: %s", item, e.message)
        }
        return false
    }

    /**
     * Delete the pairs from the system provider.
     *
     * @param pairs the list of item pairs.
     * @throws CancellationException if the provider has been cancelled.
     */
    @Throws(CancellationException::class)
    open fun deletePairs(pairs: Collection<DuplicateItemPair<T>>) {
        if (isCancelled) {
            throw CancellationException()
        }
        val listener = this.listener ?: return
        val cr = context.contentResolver

        var count = 0
        var item1: T
        var item2: T
        var deleted1: Boolean
        var deleted2: Boolean
        for (pair in pairs) {
            if (isCancelled) {
                break
            }
            item1 = pair.item1
            item2 = pair.item2
            deleted1 = item1.isChecked && deleteItem(cr, item1)
            deleted2 = item2.isChecked && deleteItem(cr, item2)
            if (deleted1 || deleted2) {
                listener.onPairDeleted(this, ++count, pair)
            }
        }
    }

    /**
     * Execute some code before task does background work.
     */
    @MainThread
    open fun onPreExecute() {}

    /**
     * Execute some code after the task did background work.
     */
    @MainThread
    open fun onPostExecute() {}

    /**
     * Get the permissions necessary for reading content from the system provider.
     *
     * @return the array of permissions.
     */
    abstract fun getReadPermissions(): Array<String>?

    /**
     * Get the permissions necessary for deleting content from the system provider.
     *
     * @return the array of permissions.
     */
    abstract fun getDeletePermissions(): Array<String>?

    /**
     * Cancel all operations.
     */
    open fun cancel() {
        isCancelled = true
    }

    /**
     * Get the non-`null` string column.
     *
     * @param cursor the database cursor.
     * @param index  the column index with string value.
     * @return the string - empty otherwise.
     */
    protected fun empty(cursor: Cursor, index: Int): String {
        return if (cursor.isNull(index)) "" else cursor.getString(index)
    }

    fun clearDatabase() {
        val dao = DuplicatesDatabase.getDatabase(context).pairDao()
        clearDatabaseTable(dao)
    }

    protected abstract fun clearDatabaseTable(dao: DuplicateItemPairDao)
}
