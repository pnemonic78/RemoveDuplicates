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
     * Percentage for two items to be considered a good match.
     */
    float MATCH_GOOD = 0.75f;
    /**
     * Percentage for two items to be considered a very good match.
     */
    float MATCH_GREAT = 0.85f;

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
     * @param task  the task.
     * @param item1 the first item.
     * @param item2 the second item.
     * @param match the match percentage.
     */
    void onDuplicateTaskMatch(T task, I item1, I item2, float match);

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
