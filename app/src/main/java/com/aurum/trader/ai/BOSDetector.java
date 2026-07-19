package com.aurum.trader.ai;

public final class BOSDetector {
    public static boolean detect(double breakout, double confirmation) {
        return breakout > 0.55 && confirmation > 0.5;
    }
}
