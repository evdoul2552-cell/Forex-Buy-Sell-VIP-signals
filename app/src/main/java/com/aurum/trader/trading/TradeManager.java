package com.aurum.trader.trading;

public final class TradeManager {
    public static String buy(String symbol, double lotSize, double price) {
        return "BUY " + symbol + " @" + price + " lot=" + lotSize;
    }

    public static String sell(String symbol, double lotSize, double price) {
        return "SELL " + symbol + " @" + price + " lot=" + lotSize;
    }

    public static String close(String symbol) {
        return "CLOSE " + symbol;
    }

    public static String partialClose(String symbol, double percent) {
        return "PARTIAL CLOSE " + symbol + " " + percent + "%";
    }

    public static String modifySL(String symbol, double price) {
        return "MODIFY SL " + symbol + " -> " + price;
    }

    public static String modifyTP(String symbol, double price) {
        return "MODIFY TP " + symbol + " -> " + price;
    }

    public static String trailingStop(String symbol, double distance) {
        return "TRAILING STOP " + symbol + " d=" + distance;
    }

    public static String breakeven(String symbol) {
        return "BREAKEVEN " + symbol;
    }
}
