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
import android.content.Context
import android.database.Cursor
import android.net.Uri
import java.util.concurrent.CancellationException

/**
 * Provider that wraps functionality to a delegate provider.
 *
 * @author moshe.w
 */
abstract class WrapperProvider<T : DuplicateItem>(private val context: Context) : DuplicateProvider<T>(context) {

    private var _delegate: DuplicateProvider<T>? = null
    private val delegate: DuplicateProvider<T>
        get() {
            if (_delegate == null) {
                _delegate = createDelegate(context)
                this.listener = _delegate!!.listener
            }
            return _delegate!!
        }

    /**
     * Create the delegate provider.
     *
     * @param context the context.
     * @return the provider.
     */
    protected abstract fun createDelegate(context: Context): DuplicateProvider<T>

    override fun setListener(listener: DuplicateProviderListener<T, DuplicateProvider<T>>) {
        super.setListener(listener)
        delegate.setListener(listener)
    }

    override fun getContentUri(): Uri? {
        return delegate.getContentUri()
    }

    override fun getCursorProjection(): Array<String>? {
        return delegate.getCursorProjection()
    }

    override fun getCursorSelection(): String? {
        return delegate.getCursorSelection()
    }

    override fun getCursorOrder(): String? {
        return delegate.getCursorOrder()
    }

    override fun createItem(cursor: Cursor): T? {
        return delegate.createItem(cursor)
    }

    @Throws(CancellationException::class)
    override fun getItems(): List<T> {
        return delegate.getItems()
    }

    @Throws(CancellationException::class)
    override fun fetchItems(listener: DuplicateProviderListener<T, DuplicateProvider<T>>) {
        delegate.fetchItems(listener)
    }

    override fun populateItem(cursor: Cursor, item: T) {
        delegate.populateItem(cursor, item)
    }

    @Throws(CancellationException::class)
    override fun deleteItems(items: Collection<T>?) {
        delegate.deleteItems(items)
    }

    override fun deleteItem(item: T): Boolean {
        return delegate.deleteItem(item)
    }

    override fun deleteItem(cr: ContentResolver, item: T): Boolean {
        return delegate.deleteItem(cr, item)
    }

    @Throws(CancellationException::class)
    override fun deletePairs(pairs: Collection<DuplicateItemPair<T>>) {
        delegate.deletePairs(pairs)
    }

    override fun onPreExecute() {
        delegate.onPreExecute()
    }

    override fun onPostExecute() {
        delegate.onPostExecute()
    }

    override fun getReadPermissions(): Array<String>? {
        return delegate.getReadPermissions()
    }

    override fun getDeletePermissions(): Array<String>? {
        return delegate.getDeletePermissions()
    }

    override fun cancel() {
        super.cancel()
        delegate.cancel()
    }

    override fun isCancelled(): Boolean {
        return delegate.isCancelled()
    }
}
