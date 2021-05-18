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
package com.github.duplicates.contact

import android.content.Context
import android.view.ViewGroup
import com.github.android.removeduplicates.BuildConfig
import com.github.android.removeduplicates.R
import com.github.android.removeduplicates.databinding.SameContactBinding
import com.github.duplicates.DuplicateItemPair
import com.github.duplicates.DuplicateViewHolder
import com.github.duplicates.contact.ContactComparator.Companion.EMAIL
import com.github.duplicates.contact.ContactComparator.Companion.EVENT
import com.github.duplicates.contact.ContactComparator.Companion.IM
import com.github.duplicates.contact.ContactComparator.Companion.NAME
import com.github.duplicates.contact.ContactComparator.Companion.PHONE
import java.util.*

/**
 * View holder of a duplicate contact.
 *
 * @author moshe.w
 */
class ContactViewHolder(
    itemView: ViewGroup,
    binding: SameContactBinding,
    onCheckedChangeListener: OnItemCheckedChangeListener<ContactItem>? = null
) : DuplicateViewHolder<ContactItem>(itemView, onCheckedChangeListener) {

    private val match = binding.match

    private val checkbox1 = binding.item1.checkbox
    private val icon1 = binding.item1.icon
    private val account1 = binding.item1.account
    private val name1 = binding.item1.name
    private val email1 = binding.item1.email
    private val event1 = binding.item1.event
    private val im1 = binding.item1.im
    private val phone1 = binding.item1.phone

    private val checkbox2 = binding.item2.checkbox
    private val icon2 = binding.item2.icon
    private val account2 = binding.item2.account
    private val name2 = binding.item2.name
    private val email2 = binding.item2.email
    private val event2 = binding.item2.event
    private val im2 = binding.item2.im
    private val phone2 = binding.item2.phone

    init {
        checkbox1.setOnClickListener {
            onCheckedChangeListener?.onItemCheckedChangeListener(item1, checkbox1.isChecked)
        }
        checkbox2.setOnClickListener {
            onCheckedChangeListener?.onItemCheckedChangeListener(item2, checkbox2.isChecked)
        }
    }

    override fun bindHeader(context: Context, pair: DuplicateItemPair<ContactItem>) {
        match.text =
            context.getString(R.string.match, percentFormatter.format(pair.match.toDouble()))
    }

    override fun bindItem1(context: Context, item: ContactItem) {
        checkbox1.isChecked = item.isChecked
        checkbox1.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        if (item.photoThumbnailUri == null) {
            icon1.setImageResource(R.drawable.ic_person_outline)
        } else {
            icon1.setImageURI(item.photoThumbnailUri)
        }
        account1.text =
            context.getString(R.string.contacts_account, item.accountName, item.accountType)
        name1.text = item.displayName
        email1.text = formatData(item.emails)
        event1.text = formatData(item.events)
        im1.text = formatData(item.ims)
        phone1.text = formatData(item.phones)
    }

    override fun bindItem2(context: Context, item: ContactItem) {
        checkbox2.isChecked = item.isChecked
        checkbox2.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        if (item.photoThumbnailUri == null) {
            icon2.setImageResource(R.drawable.ic_person_outline)
        } else {
            icon2.setImageURI(item.photoThumbnailUri)
        }
        account2.text =
            context.getString(R.string.contacts_account, item.accountName, item.accountType)
        name2.text = item.displayName
        email2.text = formatData(item.emails)
        event2.text = formatData(item.events)
        im2.text = formatData(item.ims)
        phone2.text = formatData(item.phones)
    }

    override fun bindDifference(context: Context, pair: DuplicateItemPair<ContactItem>) {
        val difference = pair.difference

        bindDifference(email1, email2, difference[EMAIL])
        bindDifference(event1, event2, difference[EVENT])
        bindDifference(im1, im2, difference[IM])
        bindDifference(name1, name2, difference[NAME])
        bindDifference(phone1, phone2, difference[PHONE])
    }

    private fun formatData(data: Collection<ContactData>): CharSequence? {
        if (data.isEmpty()) {
            return null
        }
        val unique: MutableSet<String> = HashSet(data.size)
        for (datum in data) {
            unique.add(datum.toString())
        }
        val s = StringBuilder()
        for (u in unique) {
            if (s.isNotEmpty()) {
                s.append("; ")
            }
            s.append(u)
        }
        return s
    }
}
