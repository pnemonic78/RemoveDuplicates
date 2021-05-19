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

import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.github.duplicates.MainSpinnerItem

@Entity(tableName = "pair")
@TypeConverters(DuplicateConverters::class)
data class DuplicateItemPairEntity(
    @ColumnInfo(name = BaseColumns._ID)
    @PrimaryKey
    var id: Long,
    @ColumnInfo(name = "type")
    val type: MainSpinnerItem,
    @ColumnInfo(name = "match")
    val match: Float = 0f,
    @ColumnInfo(name = "id1")
    val id1: Long,
    @ColumnInfo(name = "checked1")
    var isChecked1: Boolean,
    @ColumnInfo(name = "id2")
    val id2: Long,
    @ColumnInfo(name = "checked2")
    var isChecked2: Boolean
)