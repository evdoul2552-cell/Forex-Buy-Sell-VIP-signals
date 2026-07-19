package com.aurum.trader.models;

import java.util.ArrayList;

public final class TimeZoneData extends ArrayList<TimeZoneDataItem> {
    public /* bridge */ boolean contains(TimeZoneDataItem timeZoneDataItem) {
        return super.contains(timeZoneDataItem);
    }

    public final /* bridge */ boolean contains(Object obj) {
        if (!(obj instanceof TimeZoneDataItem)) {
            return false;
        }
        return contains((TimeZoneDataItem) obj);
    }

    public /* bridge */ int getSize() {
        return super.size();
    }

    public /* bridge */ int indexOf(TimeZoneDataItem timeZoneDataItem) {
        return super.indexOf(timeZoneDataItem);
    }

    public final /* bridge */ int indexOf(Object obj) {
        if (!(obj instanceof TimeZoneDataItem)) {
            return -1;
        }
        return indexOf((TimeZoneDataItem) obj);
    }

    public /* bridge */ int lastIndexOf(TimeZoneDataItem timeZoneDataItem) {
        return super.lastIndexOf(timeZoneDataItem);
    }

    public final /* bridge */ int lastIndexOf(Object obj) {
        if (!(obj instanceof TimeZoneDataItem)) {
            return -1;
        }
        return lastIndexOf((TimeZoneDataItem) obj);
    }

    public final /* bridge */ TimeZoneDataItem remove(int i) {
        return removeAt(i);
    }

    public /* bridge */ boolean remove(TimeZoneDataItem timeZoneDataItem) {
        return super.remove(timeZoneDataItem);
    }

    public final /* bridge */ boolean remove(Object obj) {
        if (!(obj instanceof TimeZoneDataItem)) {
            return false;
        }
        return remove((TimeZoneDataItem) obj);
    }

    public /* bridge */ TimeZoneDataItem removeAt(int i) {
        return (TimeZoneDataItem) super.remove(i);
    }

    public final /* bridge */ int size() {
        return getSize();
    }
}
