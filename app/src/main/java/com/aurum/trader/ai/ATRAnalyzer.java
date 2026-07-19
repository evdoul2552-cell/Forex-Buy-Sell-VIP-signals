package com.aurum.trader.ai;

public final class ATRAnalyzer {
    public static double estimateRange(double atr, double price) {
        return Math.max(1.0, atr / Math.max(price, 1.0));
    }
}
