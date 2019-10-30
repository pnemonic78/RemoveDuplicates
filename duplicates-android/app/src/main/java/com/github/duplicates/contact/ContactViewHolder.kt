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
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.github.android.removeduplicates.BuildConfig
import com.github.android.removeduplicates.R
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
class ContactViewHolder(itemView: View, onCheckedChangeListener: OnItemCheckedChangeListener<ContactItem>) : DuplicateViewHolder<ContactItem>(itemView, onCheckedChangeListener) {

    @BindView(R.id.match)
    lateinit var match: TextView

    @BindView(R.id.checkbox1)
    lateinit var checkbox1: CheckBox
    @BindView(R.id.icon1)
    lateinit var icon1: ImageView
    @BindView(R.id.account1)
    lateinit var account1: TextView
    @BindView(R.id.name1)
    lateinit var name1: TextView
    @BindView(R.id.email1)
    lateinit var email1: TextView
    @BindView(R.id.event1)
    lateinit var event1: TextView
    @BindView(R.id.im1)
    lateinit var im1: TextView
    @BindView(R.id.phone1)
    lateinit var phone1: TextView

    @BindView(R.id.checkbox2)
    lateinit var checkbox2: CheckBox
    @BindView(R.id.icon2)
    lateinit var icon2: ImageView
    @BindView(R.id.account2)
    lateinit var account2: TextView
    @BindView(R.id.name2)
    lateinit var name2: TextView
    @BindView(R.id.email2)
    lateinit var email2: TextView
    @BindView(R.id.event2)
    lateinit var event2: TextView
    @BindView(R.id.im2)
    lateinit var im2: TextView
    @BindView(R.id.phone2)
    lateinit var phone2: TextView

    init {
        ButterKnife.bind(this, itemView)
    }

    override fun bindHeader(context: Context, pair: DuplicateItemPair<ContactItem>) {
        match.text = context.getString(R.string.match, percentFormatter.format(pair.match.toDouble()))
    }

    override fun bindItem1(context: Context, item: ContactItem) {
        checkbox1.isChecked = item.isChecked
        checkbox1.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        if (item.photoThumbnailUri == null) {
            icon1.setImageResource(R.drawable.ic_contact_picture)
        } else {
            icon1.setImageURI(item.photoThumbnailUri)
        }
        account1.text = context.getString(R.string.contacts_account, item.accountName, item.accountType)
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
            icon2.setImageResource(R.drawable.ic_contact_picture)
        } else {
            icon2.setImageURI(item.photoThumbnailUri)
        }
        account2.text = context.getString(R.string.contacts_account, item.accountName, item.accountType)
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

    @OnClick(R.id.checkbox1)
    internal fun checkbox1Clicked() {
        onCheckedChangeListener?.onItemCheckedChangeListener(item1, checkbox1.isChecked)
    }

    @OnClick(R.id.checkbox2)
    internal fun checkbox2Clicked() {
        onCheckedChangeListener?.onItemCheckedChangeListener(item2, checkbox2.isChecked)
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
