/*
 * Copyright 2021, Moshe Waisberg
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
package com.github.duplicates.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.github.duplicates.DuplicateItemType

/**
 * Pair entity DAO.
 */
@Dao
interface DuplicateItemPairDao : BaseDao<DuplicateItemPairEntity> {

    /**
     * Select all pairs from the table.
     *
     * @return all pairs.
     */
    @Query("SELECT * FROM pair")
    fun queryAll(): List<DuplicateItemPairEntity>

    /**
     * Select all pairs from the table.
     *
     * @return all pairs.
     */
    @Transaction
    @Query("SELECT * FROM pair")
    fun queryAllWithTasksLive(): LiveData<List<DuplicateItemPairEntity>>

    /**
     * Select all pairs from the table.
     *
     * @return all pairs.
     */
    @Query("SELECT * FROM pair WHERE type=:type")
    fun queryAll(type: DuplicateItemType): List<DuplicateItemPairEntity>

    /**
     * Delete all pairs.
     */
    @Query("DELETE FROM pair")
    fun deleteAll(): Int

    /**
     * Delete all pairs.
     */
    @Query("DELETE FROM pair WHERE type=:type")
    fun deleteAll(type: DuplicateItemType): Int


    /**
     * Delete all items.
     */
    @Query("DELETE FROM pair WHERE (type=:type) AND ((id1=:id) OR (id2=:id))")
    fun deleteAll(type: DuplicateItemType, id: Long)

    /**
     * Update the item1's checked status.
     */
    @Query("UPDATE pair SET checked1=:checked WHERE (type=:type) AND (id1=:id)")
    fun updateItemChecked1(type: DuplicateItemType, id: Long, checked: Boolean): Int

    /**
     * Update the item2's checked status.
     */
    @Query("UPDATE pair SET checked2=:checked WHERE (type=:type) AND (id2=:id)")
    fun updateItemChecked2(type: DuplicateItemType, id: Long, checked: Boolean): Int
}