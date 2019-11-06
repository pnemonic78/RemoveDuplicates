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
interface DuplicateTaskListener<I : DuplicateItem> {

    /**
     * Notification that the task has started executing.
     *
     * @param task the task.
     */
    fun <Params, Progress, Result, L : DuplicateTaskListener<I>, T : DuplicateTask<I, Params, Progress, Result, L>> onDuplicateTaskStarted(task: T)

    /**
     * Notification that the task has finished executing.
     *
     * @param task the task.
     */
    fun <Params, Progress, Result, L : DuplicateTaskListener<I>, T : DuplicateTask<I, Params, Progress, Result, L>> onDuplicateTaskFinished(task: T)

    /**
     * Notification that the task has been cancelled.
     *
     * @param task the task.
     */
    fun <Params, Progress, Result, L : DuplicateTaskListener<I>, T : DuplicateTask<I, Params, Progress, Result, L>> onDuplicateTaskCancelled(task: T)

    /**
     * Notification that the task has progressed.
     *
     * @param task  the task.
     * @param count the number of items processed.
     */
    fun <Params, Progress, Result, L : DuplicateTaskListener<I>, T : DuplicateTask<I, Params, Progress, Result, L>> onDuplicateTaskProgress(task: T, count: Int)
}
