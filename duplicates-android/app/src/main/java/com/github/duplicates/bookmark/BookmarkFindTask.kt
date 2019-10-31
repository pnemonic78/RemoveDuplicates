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

import android.content.Context

import com.github.duplicates.DuplicateFindTask
import com.github.duplicates.DuplicateTask
import com.github.duplicates.DuplicateTaskListener

/**
 * Task to find duplicate bookmarks.
 *
 * @author moshe.w
 */
class BookmarkFindTask(context: Context, listener: DuplicateTaskListener<BookmarkItem, DuplicateTask<BookmarkItem, Any, Any, List<BookmarkItem>>>) : DuplicateFindTask<BookmarkItem, BookmarkViewHolder>(context, listener) {

    override fun createProvider(context: Context): BookmarkProvider {
        return BookmarkProvider(context)
    }

    override fun createAdapter(): BookmarkAdapter {
        return BookmarkAdapter()
    }

    override fun createComparator(): BookmarkComparator {
        return BookmarkComparator()
    }
}
