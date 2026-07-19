package com.aurum.trader.ai;

public final class CHOCHDetector {
    public static boolean detect(double change, double confirmation) {
        return change > 0.6 && confirmation > 0.55;
    }
}
