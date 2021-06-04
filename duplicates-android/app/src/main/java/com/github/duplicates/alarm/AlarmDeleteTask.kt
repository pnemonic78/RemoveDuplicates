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
package com.github.duplicates.alarm

import android.content.Context
import com.github.duplicates.DuplicateDeleteTask
import com.github.duplicates.DuplicateDeleteTaskListener

/**
 * Task to find duplicate alarms.
 *
 * @author moshe.w
 */
class AlarmDeleteTask<L : DuplicateDeleteTaskListener<AlarmItem>>(context: Context, listener: L) :
    DuplicateDeleteTask<AlarmItem, L>(context, listener) {

    override fun createProvider(context: Context): AlarmProvider {
        return AlarmProvider(context)
    }
}
