package com.aurum.trader;

import android.content.res.Resources;
import android.util.SparseArray;
import android.util.SparseIntArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Definitions {
    public static final List<Integer> DEFAULT_MARKET_SYMBOLS = Arrays.asList(1, 2, 11, 3, 5, 7, 28, 29, 17, 4, 14, 10, 51, 25, 6, 8, 9, 107, 20, 24, 13, 12, 46, 27, 48, 47, 26, 103, 50, 49);
    public static final String DEFAULT_SYMBOL = "USD";
    public static int PARAM_MARKET_UPDATE_REALTIME = 0;
    public static final String PARAM_OID = "oid";
    public static final Map<Integer, Integer> SCREENS = new HashMap();
    public static final String SYMBOL_JPY = "JPY";
    public static String TRANSLATION_AS_EXPECTED = "As expected";
    public static String TRANSLATION_BETTER_EXPECTED = "Better than expected";
    public static String TRANSLATION_DAYS_AGO = "Days Ago";
    public static String TRANSLATION_DAYS_SHORT = "d";
    public static String TRANSLATION_DAY_AGO = "Day Ago";
    public static String TRANSLATION_DONE = "Done";
    public static String TRANSLATION_HOURS_AGO = "Hours Ago";
    public static String TRANSLATION_HOUR_AGO = "Hour Ago";
    public static String TRANSLATION_HOUR_SHORT = "h";
    public static String TRANSLATION_MINUTE_SHORT = "m";
    public static String TRANSLATION_MIN_AGO = "Min Ago";
    public static String TRANSLATION_SEC_AGO = "Seconds Ago";
    public static String TRANSLATION_WORSE_EXPECTED = "Worse than expected";

    public static final Map<String, String> currencySymbols = new HashMap();
    public static final SparseIntArray marketUnits = new SparseIntArray();
    public static final Map<Integer, Integer> marketUpdateTimeframe = new Hashtable();
    public static final SparseArray<String> timeframesMinutesToStr = new SparseArray<>();
    public static final SparseArray<String> timeframesStr = new SparseArray<>();

    static {
        marketUpdateTimeframe.put(Integer.valueOf(PARAM_MARKET_UPDATE_REALTIME), Integer.valueOf(PARAM_MARKET_UPDATE_REALTIME));
        marketUpdateTimeframe.put(1, 10);
        marketUpdateTimeframe.put(2, 30);
        marketUpdateTimeframe.put(3, 60);
        marketUpdateTimeframe.put(4, 300);
        SCREENS.put(0, 0);
        SCREENS.put(3, 3);
        SCREENS.put(4, 4);
        SCREENS.put(5, 5);
        SCREENS.put(6, 6);
        SCREENS.put(7, 7);
        initTimeframes();
        initMarketUnits();
    }

    private static void initMarketUnits() {
        marketUnits.append(1, 1);
        marketUnits.append(2, 5);
        marketUnits.append(3, 15);
        marketUnits.append(4, 30);
        marketUnits.append(5, 60);
        marketUnits.append(6, 240);
        marketUnits.append(7, 1440);
        marketUnits.append(8, 10080);
        marketUnits.append(9, 43200);
    }

    private static void initTimeframes() {
        timeframesStr.put(1, "M1");
        timeframesStr.put(2, "M5");
        timeframesStr.put(3, "M15");
        timeframesStr.put(4, "M30");
        timeframesStr.put(5, "H1");
        timeframesStr.put(6, "H4");
        timeframesStr.put(7, "D1");
        timeframesStr.put(8, "W1");
        timeframesStr.put(9, "MN");
        timeframesMinutesToStr.put(1, "M1");
        timeframesMinutesToStr.put(5, "M5");
        timeframesMinutesToStr.put(15, "M15");
        timeframesMinutesToStr.put(30, "M30");
        timeframesMinutesToStr.put(60, "H1");
        timeframesMinutesToStr.put(240, "H4");
        timeframesMinutesToStr.put(1440, "D1");
        timeframesMinutesToStr.put(10080, "W1");
        timeframesMinutesToStr.put(43200, "MN");
    }


    public static void initStaticMaps(Resources resources) {
        TRANSLATION_DONE = resources.getString(R.string.done);
        TRANSLATION_SEC_AGO = resources.getString(R.string.secondsago1);
        TRANSLATION_MIN_AGO = resources.getString(R.string.minago1);
        TRANSLATION_HOUR_AGO = resources.getString(R.string.hourago1);
        TRANSLATION_HOURS_AGO = resources.getString(R.string.hourago2);
        TRANSLATION_DAYS_AGO = resources.getString(R.string.daysago1);
        TRANSLATION_DAY_AGO = resources.getString(R.string.dayago);
        TRANSLATION_BETTER_EXPECTED = resources.getString(R.string.betterthanexpected);
        TRANSLATION_WORSE_EXPECTED = resources.getString(R.string.worththanexpected);
        TRANSLATION_AS_EXPECTED = resources.getString(R.string.asaspected);
        TRANSLATION_HOUR_SHORT = resources.getString(R.string.hourshort);
        TRANSLATION_DAYS_SHORT = resources.getString(R.string.dayshort);
        TRANSLATION_MINUTE_SHORT = resources.getString(R.string.minshort);

        currencySymbols.put(DEFAULT_SYMBOL, "$");
        currencySymbols.put("CAD", "$");
        currencySymbols.put("EUR", "€");
        currencySymbols.put("NZD", "$");
        currencySymbols.put("CHF", "CHF");
        currencySymbols.put("GBP", "£");
        currencySymbols.put("SGD", "$");
        currencySymbols.put(SYMBOL_JPY, "¥");
        currencySymbols.put("AUD", "$");
    }


}
