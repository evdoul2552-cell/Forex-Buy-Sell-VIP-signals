package com.aurum.trader.ai;

public final class FairValueGapDetector {
    public static boolean detect(double gapSize) {
        return gapSize > 0.6;
    }
}
