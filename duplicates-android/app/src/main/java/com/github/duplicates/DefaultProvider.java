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

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Default provider that does nothing.
 *
 * @author moshe.w
 */
public class DefaultProvider<T extends DuplicateItem> extends DuplicateProvider<T> {

    public DefaultProvider(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return null;
    }

    @Override
    public T createItem(Cursor cursor) {
        return null;
    }

    @Override
    public void populateItem(Cursor cursor, T item) {
    }

    @Override
    public String[] getReadPermissions() {
        return null;
    }

    @Override
    public String[] getDeletePermissions() {
        return null;
    }
}
