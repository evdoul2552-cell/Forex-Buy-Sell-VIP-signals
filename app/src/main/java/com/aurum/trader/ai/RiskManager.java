package com.aurum.trader.ai;

public final class RiskManager {
    public static double calculatePositionSize(double balance, double riskPercent, double stopDistance) {
        if (stopDistance <= 0) return 0.01;
        return Math.max(0.01, Math.min(10.0, (balance * (riskPercent / 100.0)) / stopDistance));
    }
}
