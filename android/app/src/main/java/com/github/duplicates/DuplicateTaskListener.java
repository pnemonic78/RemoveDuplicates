/*
 * Source file of the Remove Duplicates project.
 * Copyright (c) 2016. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/2.0
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 *
 * https://github.com/pnemonic78/RemoveDuplicates
 *
 * Contributor(s):
 *   Moshe Waisberg
 *
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
     * Notification that the task has found a possible match where the similarity is above 80%.
     *
     * @param task  the task.
     * @param item1 the first item.
     * @param item2 the second item.
     */
    void onDuplicateTaskMatch(T task, I item1, I item2);
}
