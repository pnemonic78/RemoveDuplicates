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
 * Listener for task progress events.
 *
 * @author moshe.w
 */
interface DuplicateTaskListener<I : DuplicateItem, T : DuplicateTask<I, *, *, *>> {

    /**
     * Notification that the task has started executing.
     *
     * @param task the task.
     */
    fun onDuplicateTaskStarted(task: T)

    /**
     * Notification that the task has finished executing.
     *
     * @param task the task.
     */
    fun onDuplicateTaskFinished(task: T)

    /**
     * Notification that the task has been cancelled.
     *
     * @param task the task.
     */
    fun onDuplicateTaskCancelled(task: T)

    /**
     * Notification that the task has progressed.
     *
     * @param task  the task.
     * @param count the number of items processed.
     */
    fun onDuplicateTaskProgress(task: T, count: Int)

    /**
     * Notification that the task has found a possible match where the similarity is above 75%.
     *
     * @param task       the task.
     * @param item1      the first item.
     * @param item2      the second item.
     * @param match      the match percentage.
     * @param difference the array of differences.
     */
    fun onDuplicateTaskMatch(task: T, item1: I, item2: I, match: Float, difference: BooleanArray)

    /**
     * Notification that the task has deleted a duplicate item.
     *
     * @param task the task.
     * @param item the item.
     */
    fun onDuplicateTaskItemDeleted(task: T, item: I)

    /**
     * Notification that the task has deleted a duplicate item.
     *
     * @param task the task.
     * @param pair the pair of items.
     */
    fun onDuplicateTaskPairDeleted(task: T, pair: DuplicateItemPair<I>)
}
