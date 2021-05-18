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
package com.github.duplicates.call

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.android.removeduplicates.databinding.SameCallBinding
import com.github.duplicates.DuplicateAdapter

/**
 * List adapter for duplicate calls.
 *
 * @author moshe.w
 */
class CallLogAdapter : DuplicateAdapter<CallLogItem, CallLogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val context: Context = parent.context
        val binding = SameCallBinding.inflate(LayoutInflater.from(context), parent, false)
        return CallLogViewHolder(binding, this)
    }
}
