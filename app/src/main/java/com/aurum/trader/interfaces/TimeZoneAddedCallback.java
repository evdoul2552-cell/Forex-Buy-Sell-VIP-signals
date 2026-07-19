package com.aurum.trader.interfaces;


public interface TimeZoneAddedCallback {

    public static final class DefaultImpls {
        public static void timeZoneAdded(TimeZoneAddedCallback timeZoneAddedCallback) {
        }
    }

    void timeZoneAdded();
}
