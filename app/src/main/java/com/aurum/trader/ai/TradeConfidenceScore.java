package com.aurum.trader.ai;

public final class TradeConfidenceScore {
    public static int calculate(double trendScore, double supportScore, double resistanceScore, double volumeScore, double riskScore) {
        double weighted = (trendScore * 0.3) + (supportScore * 0.2) + (resistanceScore * 0.2) + (volumeScore * 0.15) + (riskScore * 0.15);
        return Math.max(0, Math.min(100, (int) Math.round(weighted)));
    }

    public static String label(int score) {
        if (score >= 80) return "STRONG BUY";
        if (score >= 60) return "BUY";
        if (score >= 40) return "HOLD";
        if (score >= 20) return "SELL";
        return "STRONG SELL";
    }
}
