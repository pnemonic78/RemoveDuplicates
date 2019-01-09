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

/**
 * Listener for task progress events.
 *
 * @author moshe.w
 */
public interface DuplicateTaskListener<T extends DuplicateTask, I extends DuplicateItem> {

    /**
     * Notification that the task has started executing.
     *
     * @param task the task.
     */
    void onDuplicateTaskStarted(T task);

    /**
     * Notification that the task has finished executing.
     *
     * @param task the task.
     */
    void onDuplicateTaskFinished(T task);

    /**
     * Notification that the task has been cancelled.
     *
     * @param task the task.
     */
    void onDuplicateTaskCancelled(T task);

    /**
     * Notification that the task has progressed.
     *
     * @param task  the task.
     * @param count the number of items processed.
     */
    void onDuplicateTaskProgress(T task, int count);

    /**
     * Notification that the task has found a possible match where the similarity is above 75%.
     *
     * @param task       the task.
     * @param item1      the first item.
     * @param item2      the second item.
     * @param match      the match percentage.
     * @param difference the array of differences.
     */
    void onDuplicateTaskMatch(T task, I item1, I item2, float match, boolean[] difference);

    /**
     * Notification that the task has deleted a duplicate item.
     *
     * @param task the task.
     * @param item the item.
     */
    void onDuplicateTaskItemDeleted(T task, I item);

    /**
     * Notification that the task has deleted a duplicate item.
     *
     * @param task the task.
     * @param pair the pair of items.
     */
    void onDuplicateTaskPairDeleted(T task, DuplicateItemPair<I> pair);
}
