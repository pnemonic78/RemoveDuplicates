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
package com.github.duplicates.contact;

import android.text.TextUtils;

/**
 * Contact data.
 *
 * @author moshe.w
 */
public class ContactData {

    private long id;
    private long contactId;
    private long rawContactId;
    private String data1;
    private String data2;
    private String data3;
    private String data4;
    private String data5;
    private String data6;
    private String data7;
    private String data8;
    private String data9;
    private String data10;
    private String data11;
    private String data12;
    private String data13;
    private String data14;
    private String data15;
    private int dataVersion;
    private String mimeType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public long getRawContactId() {
        return rawContactId;
    }

    public void setRawContactId(long rawContactId) {
        this.rawContactId = rawContactId;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    public String getData4() {
        return data4;
    }

    public void setData4(String data4) {
        this.data4 = data4;
    }

    public String getData5() {
        return data5;
    }

    public void setData5(String data5) {
        this.data5 = data5;
    }

    public String getData6() {
        return data6;
    }

    public void setData6(String data6) {
        this.data6 = data6;
    }

    public String getData7() {
        return data7;
    }

    public void setData7(String data7) {
        this.data7 = data7;
    }

    public String getData8() {
        return data8;
    }

    public void setData8(String data8) {
        this.data8 = data8;
    }

    public String getData9() {
        return data9;
    }

    public void setData9(String data9) {
        this.data9 = data9;
    }

    public String getData10() {
        return data10;
    }

    public void setData10(String data10) {
        this.data10 = data10;
    }

    public String getData11() {
        return data11;
    }

    public void setData11(String data11) {
        this.data11 = data11;
    }

    public String getData12() {
        return data12;
    }

    public void setData12(String data12) {
        this.data12 = data12;
    }

    public String getData13() {
        return data13;
    }

    public void setData13(String data13) {
        this.data13 = data13;
    }

    public String getData14() {
        return data14;
    }

    public void setData14(String data14) {
        this.data14 = data14;
    }

    public String getData15() {
        return data15;
    }

    public void setData15(String data15) {
        this.data15 = data15;
    }

    public int getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(int dataVersion) {
        this.dataVersion = dataVersion;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public int hashCode() {
        return (int) getId();
    }

    @Override
    public String toString() {
        return getData1();
    }

    public boolean isEmpty() {
        return getData1() == null;
    }

    public boolean containsAny(CharSequence s) {
        return contains(data1)
                || contains(data2)
                || contains(data3)
                || contains(data4)
                || contains(data5)
                || contains(data6)
                || contains(data7)
                || contains(data8)
                || contains(data9)
                || contains(data10)
                || contains(data11)
                || contains(data12)
                || contains(data13)
                || contains(data14)
                || contains(data15);
    }

    protected int parseInt(String s) {
        return TextUtils.isEmpty(s) ? 0 : Integer.parseInt(s);
    }

    protected boolean contains(String s) {
        return !TextUtils.isEmpty(s) && s.contains(s);
    }
}