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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Duplicate items database.
 */
@Database(
    entities = [
        DuplicateItemPairEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DuplicatesDatabase : RoomDatabase() {
    abstract fun pairDao(): DuplicateItemPairDao

    companion object {
        @Volatile
        private var instance: DuplicatesDatabase? = null

        fun getDatabase(context: Context): DuplicatesDatabase {
            if (instance == null) {
                synchronized(DuplicatesDatabase::class.java) {
                    if (instance == null) {
                        // Create database here
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            DuplicatesDatabase::class.java,
                            "dup.db"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return instance!!
        }
    }
}