package com.aurum.trader.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public final class TimeZoneDataItem {
    @SerializedName("abbr")
    private final String abbr;
    @SerializedName("isdst")
    private final boolean isdst;
    @SerializedName("offset")
    private final float offset;
    @SerializedName("text")
    private final String text;
    @SerializedName("utc")
    private final List<String> utc;
    @SerializedName("value")
    private final String value;

    public static /* synthetic */ TimeZoneDataItem copy$default(TimeZoneDataItem timeZoneDataItem, String str, boolean z, float f, String str2, List<String> list, String str3, int i, Object obj) {
        if ((i & 1) != 0) {
            str = timeZoneDataItem.abbr;
        }
        if ((i & 2) != 0) {
            z = timeZoneDataItem.isdst;
        }
        boolean z2 = z;
        if ((i & 4) != 0) {
            f = timeZoneDataItem.offset;
        }
        float f2 = f;
        if ((i & 8) != 0) {
            str2 = timeZoneDataItem.text;
        }
        String str4 = str2;
        if ((i & 16) != 0) {
            list = timeZoneDataItem.utc;
        }
        List<String> list2 = list;
        if ((i & 32) != 0) {
            str3 = timeZoneDataItem.value;
        }
        return timeZoneDataItem.copy(str, z2, f2, str4, list2, str3);
    }

    public final String component1() {
        return this.abbr;
    }

    public final boolean component2() {
        return this.isdst;
    }

    public final float component3() {
        return this.offset;
    }

    public final String component4() {
        return this.text;
    }

    public final List<String> component5() {
        return this.utc;
    }

    public final String component6() {
        return this.value;
    }

    public final TimeZoneDataItem copy(String str, boolean z, float f, String str2, List<String> list, String str3) {
        return new TimeZoneDataItem(str, z, f, str2, list, str3);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TimeZoneDataItem)) {
            return false;
        }
        TimeZoneDataItem timeZoneDataItem = (TimeZoneDataItem) obj;
        return Intrinsics.areEqual((Object) this.abbr, (Object) timeZoneDataItem.abbr) && this.isdst == timeZoneDataItem.isdst && Float.compare(this.offset, timeZoneDataItem.offset) == 0 && Intrinsics.areEqual((Object) this.text, (Object) timeZoneDataItem.text) && Intrinsics.areEqual((Object) this.utc, (Object) timeZoneDataItem.utc) && Intrinsics.areEqual((Object) this.value, (Object) timeZoneDataItem.value);
    }

    public int hashCode() {
        int hashCode = this.abbr.hashCode() * 31;
        boolean z = this.isdst;
        if (z) {
            z = true;
        }
        return ((((((((hashCode + (z ? 1 : 0)) * 31) + Float.floatToIntBits(this.offset)) * 31) + this.text.hashCode()) * 31) + this.utc.hashCode()) * 31) + this.value.hashCode();
    }

    public String toString() {
        return "TimeZoneDataItem(abbr=" + this.abbr + ", isdst=" + this.isdst + ", offset=" + this.offset + ", text=" + this.text + ", utc=" + this.utc + ", value=" + this.value + ')';
    }

    public TimeZoneDataItem(String str, boolean z, float f, String str2, List<String> list, String str3) {
        this.abbr = str;
        this.isdst = z;
        this.offset = f;
        this.text = str2;
        this.utc = list;
        this.value = str3;
    }

    public final String getAbbr() {
        return this.abbr;
    }

    public final boolean getIsdst() {
        return this.isdst;
    }

    public final float getOffset() {
        return this.offset;
    }

    public final String getText() {
        return this.text;
    }

    public final List<String> getUtc() {
        return this.utc;
    }

    public final String getValue() {
        return this.value;
    }
}
