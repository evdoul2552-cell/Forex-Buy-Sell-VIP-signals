package com.aurum.trader.ai;

public final class EMATrendAnalyzer {
    public static String analyze(double fastEma, double slowEma) {
        if (fastEma > slowEma) {
            return "Bullish EMA alignment";
        }
        if (fastEma < slowEma) {
            return "Bearish EMA alignment";
        }
        return "Neutral EMA alignment";
    }
}
