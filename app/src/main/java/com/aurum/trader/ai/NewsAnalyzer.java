package com.aurum.trader.ai;

public final class NewsAnalyzer {
    public static String analyze(String headline) {
        if (headline == null || headline.isEmpty()) {
            return "Neutral";
        }
        String normalized = headline.toLowerCase();
        if (normalized.contains("inflation") || normalized.contains("rate") || normalized.contains("hawkish")) {
            return "High impact";
        }
        return "Medium impact";
    }
}
