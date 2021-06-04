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
package com.github.duplicates.message

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.android.removeduplicates.databinding.SameMessageBinding
import com.github.duplicates.DuplicateAdapter

/**
 * List adapter for duplicate messages.
 *
 * @author moshe.w
 */
class MessageAdapter : DuplicateAdapter<MessageItem, MessageViewHolder>() {

    override fun createCardViewHolder(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup,
        cardView: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        val binding = SameMessageBinding.inflate(inflater, cardView, true)
        return MessageViewHolder(parent, binding, this)
    }
}
