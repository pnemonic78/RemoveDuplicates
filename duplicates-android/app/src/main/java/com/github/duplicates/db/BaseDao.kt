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

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Base entity DAO.
 */
@Dao
interface BaseDao<E> {

    /**
     * Insert an entity in the database. If the entity already exists, replace it.
     *
     * @param entity the entity to be inserted.
     * @return the entity id.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: E): Long

    /**
     * Insert entities in the database. If an entity already exists, replace it.
     *
     * @param entities the entities to be inserted.
     * @return the entity ids.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: Array<E>): LongArray

    /**
     * Insert entities in the database. If an entity already exists, replace it.
     *
     * @param entities the entities to be inserted.
     * @return the entity ids.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: Collection<E>): LongArray

    /**
     * Update an entity.
     *
     * @param entity the entity to be updated.
     * @return the number of entities updated. This should always be 1.
     */
    @Update
    fun update(entity: E): Int

    /**
     * Update entities.
     *
     * @param entities the entities to be updated.
     * @return the number of entities updated.
     */
    @Update
    fun update(entities: Array<E>): Int

    /**
     * Update entities.
     *
     * @param entities the entities to be updated.
     * @return the number of entities updated.
     */
    @Update
    fun update(entities: Collection<E>): Int

    /**
     * Delete an entity.
     * @param entity the entity to be deleted.
     * @return the number of entities deleted.
     */
    @Delete
    fun delete(entity: E): Int

    /**
     * Delete entities.
     * @param entities the entities to be deleted.
     * @return the number of entities deleted.
     */
    @Delete
    fun delete(entities: Array<E>): Int

    /**
     * Delete entities.
     * @param entities the entities to be deleted.
     * @return the number of entities deleted.
     */
    @Delete
    fun delete(entities: Collection<E>): Int
}