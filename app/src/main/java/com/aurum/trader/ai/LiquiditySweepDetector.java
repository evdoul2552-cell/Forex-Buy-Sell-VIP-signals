package com.aurum.trader.ai;

public final class LiquiditySweepDetector {
    public static boolean detect(double sweepScore) {
        return sweepScore > 0.65;
    }
}
