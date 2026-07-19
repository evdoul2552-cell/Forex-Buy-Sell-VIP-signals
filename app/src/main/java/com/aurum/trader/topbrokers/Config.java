package com.aurum.trader.topbrokers;


public class Config {
    public static String URL_SERVER = null;
    public static String URL_SERVER_CONFIG = null;
    public static String URL_SERVER_STATIC = null;
    public static boolean initial = false;

    public static void init() {
        URL_SERVER = "https://mobile.mfbcdn.net";
        URL_SERVER_STATIC = "https://static.mfbcdn.net";
        URL_SERVER_CONFIG = URL_SERVER + "/mobile/get-config.json";
        initial = true;
    }
}
