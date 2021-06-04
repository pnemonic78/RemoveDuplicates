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

/**
 * Listener for deletion task progress events.
 *
 * @author moshe.w
 */
interface DuplicateDeleteTaskListener<I : DuplicateItem> : DuplicateTaskListener<I> {

    /**
     * Notification that the task has deleted a duplicate item.
     *
     * @param task the task.
     * @param item the item.
     */
    fun <L : DuplicateDeleteTaskListener<I>, T : DuplicateDeleteTask<I, L>> onDuplicateTaskItemDeleted(
        task: T,
        item: I
    )

    /**
     * Notification that the task has deleted a duplicate item.
     *
     * @param task the task.
     * @param pair the pair of items.
     */
    fun <L : DuplicateDeleteTaskListener<I>, T : DuplicateDeleteTask<I, L>> onDuplicateTaskPairDeleted(
        task: T,
        pair: DuplicateItemPair<I>
    )
}
