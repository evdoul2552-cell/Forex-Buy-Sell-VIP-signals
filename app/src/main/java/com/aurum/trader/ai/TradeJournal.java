package com.aurum.trader.ai;

import java.util.ArrayList;
import java.util.List;

public final class TradeJournal {
    private final List<String> entries = new ArrayList<>();

    public void log(String entry) {
        entries.add(entry);
    }

    public List<String> getEntries() {
        return entries;
    }
}
