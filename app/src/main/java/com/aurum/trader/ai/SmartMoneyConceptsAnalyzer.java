package com.aurum.trader.ai;

public final class SmartMoneyConceptsAnalyzer {
    public static String analyze(double trend, double support, double resistance) {
        if (trend > 0.6 && support > 0.5) {
            return "Bullish structure with strong demand zone";
        }
        if (trend < -0.6 && resistance > 0.5) {
            return "Bearish structure with strong supply zone";
        }
        return "Balanced structure; watch for confirmation";
    }
}
