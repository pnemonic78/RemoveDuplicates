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
package com.github.duplicates;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * Provider that wraps functionality to a delegate provider.
 *
 * @author moshe.w
 */
public abstract class WrapperProvider<T extends DuplicateItem> extends DuplicateProvider<T> {

    protected final DuplicateProvider<T> delegate;

    public WrapperProvider(Context context) {
        super(context);
        DuplicateProvider<T> candidate = createDelegate(context);
        delegate = (candidate != null) ? candidate : new DefaultProvider<T>(context);
    }

    /**
     * Create the delegate provider.
     *
     * @param context the context.
     * @return the provider - {@code null} to use teh default provider.
     */
    protected abstract DuplicateProvider<T> createDelegate(Context context);

    @Override
    public void setListener(DuplicateProviderListener<T, DuplicateProvider<T>> listener) {
        delegate.setListener(listener);
    }

    @Override
    public DuplicateProviderListener<T, DuplicateProvider<T>> getListener() {
        return delegate.getListener();
    }

    @Override
    protected Uri getContentUri() {
        return null;
    }

    @Override
    public T createItem(Cursor cursor) {
        return delegate.createItem(cursor);
    }

    @Override
    public List<T> getItems() throws CancellationException {
        return delegate.getItems();
    }

    @Override
    public void fetchItems() throws CancellationException {
        delegate.fetchItems();
    }

    @Override
    public void populateItem(Cursor cursor, T item) {
        delegate.populateItem(cursor, item);
    }

    @Override
    public void deleteItems(Collection<T> items) throws CancellationException {
        delegate.deleteItems(items);
    }

    @Override
    public boolean deleteItem(T item) {
        return delegate.deleteItem(item);
    }

    @Override
    public boolean deleteItem(ContentResolver cr, T item) {
        return delegate.deleteItem(cr, item);
    }

    @Override
    public void onPreExecute() {
        delegate.onPreExecute();
    }

    @Override
    public void onPostExecute() {
        delegate.onPostExecute();
    }

    @Override
    public String[] getReadPermissions() {
        return delegate.getReadPermissions();
    }

    @Override
    public String[] getDeletePermissions() {
        return delegate.getDeletePermissions();
    }

    @Override
    public void cancel() {
        super.cancel();
        delegate.cancel();
    }
}
