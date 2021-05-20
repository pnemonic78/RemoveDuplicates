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
abstract class DuplicateDeleteTask<I : DuplicateItem, L : DuplicateDeleteTaskListener<I>>(
    context: Context,
    listener: L
) : DuplicateTask<I, DuplicateItemPair<I>, Any, Unit, L>(context, listener) {

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

    override fun onItemFetched(provider: DuplicateProvider<I>, count: Int, item: I) {
        // Nothing to do.
    }

    override fun onItemDeleted(provider: DuplicateProvider<I>, count: Int, item: I) {
        listener.onDuplicateTaskItemDeleted(this, item)
        publishProgress(count, item)
    }

    override fun onPairDeleted(
        provider: DuplicateProvider<I>,
        count: Int,
        pair: DuplicateItemPair<I>
    ) {
        listener.onDuplicateTaskPairDeleted(this, pair)
        publishProgress(count, pair)
    }

    override fun getPermissions(): Array<String>? {
        return provider.getDeletePermissions()
    }
}
