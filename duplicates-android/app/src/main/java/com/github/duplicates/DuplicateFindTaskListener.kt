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
 * Listener for find task progress events.
 *
 * @author moshe.w
 */
interface DuplicateFindTaskListener<I : DuplicateItem, VH : DuplicateViewHolder<I>> : DuplicateTaskListener<I> {

    /**
     * Notification that the task has found a possible match where the similarity is above 75%.
     *
     * @param task       the task.
     * @param item1      the first item.
     * @param item2      the second item.
     * @param match      the match percentage.
     * @param difference the array of differences.
     */
    fun <L : DuplicateFindTaskListener<I, VH>, T : DuplicateFindTask<I, VH, L>> onDuplicateTaskMatch(task: T, item1: I, item2: I, match: Float, difference: BooleanArray)
}
