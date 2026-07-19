package com.aurum.trader.ai;

public final class MultiTimeframeAnalyzer {
    public static String analyze(double h1, double h4, double d1) {
        if (h1 > 0.5 && h4 > 0.5 && d1 > 0.5) {
            return "Aligned bullish multi-timeframe structure";
        }
        if (h1 < -0.5 && h4 < -0.5 && d1 < -0.5) {
            return "Aligned bearish multi-timeframe structure";
        }
        return "Mixed multi-timeframe outlook";
    }
}
