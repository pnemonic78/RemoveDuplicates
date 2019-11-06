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
import java.util.*
import java.util.concurrent.CancellationException

/**
 * Task to delete duplicates.
 *
 * @author moshe.w
 */
abstract class DuplicateDeleteTask<I : DuplicateItem, L : DuplicateDeleteTaskListener<I>>(context: Context, listener: L) : DuplicateTask<I, DuplicateItemPair<I>, Any, Unit, L>(context, listener) {

    private val pairs = ArrayList<DuplicateItemPair<I>>()

    override fun onPreExecute() {
        super.onPreExecute()
        pairs.clear()
    }

    override fun doInBackground(vararg params: DuplicateItemPair<I>) {
        pairs.addAll(listOf(*params))
        publishProgress(pairs.size)
        // Sort by descending id to avoid "index out of bounds" when displaying the list.
        pairs.sort()
        pairs.reverse()
        try {
            provider.deletePairs(pairs)
        } catch (ignore: CancellationException) {
        }
    }

    override fun onProgressUpdate(vararg progress: Any) {
        val listener = this.listener
        listener.onDuplicateTaskProgress(this, progress[0] as Int)
        if (progress.size > 1) {
            val arg1 = progress[1]
            if (arg1 is DuplicateItem) {
                val item = arg1 as I
                listener.onDuplicateTaskItemDeleted(this, item)
            } else {
                val pair = arg1 as DuplicateItemPair<I>
                listener.onDuplicateTaskPairDeleted(this, pair)
            }
        }
    }

    override fun onItemFetched(provider: DuplicateProvider<I>, count: Int, item: I) {
        // Nothing to do.
    }

    override fun onItemDeleted(provider: DuplicateProvider<I>, count: Int, item: I) {
        publishProgress(count, item)
    }

    override fun onPairDeleted(provider: DuplicateProvider<I>, count: Int, pair: DuplicateItemPair<I>) {
        publishProgress(count, pair)
    }

    override fun getPermissions(): Array<String>? {
        return provider.getDeletePermissions()
    }
}
