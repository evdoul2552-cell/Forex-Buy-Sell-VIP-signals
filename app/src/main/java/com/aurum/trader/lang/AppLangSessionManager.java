package com.aurum.trader.lang;

import android.content.Context;
import android.content.SharedPreferences;

public class AppLangSessionManager {
    public static final String KEY_APP_LANGUAGE = "lang";
    private static final String PREFER_NAME = "AppLangPref";
    public static int PRIVATE_MODE;
    public static SharedPreferences.Editor editor;
    public static SharedPreferences pref;

    public static void setLanguage(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        pref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        editor = edit;
        edit.putString(KEY_APP_LANGUAGE, str);
        editor.apply();
    }

    public static String getLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        pref = sharedPreferences;
        editor = sharedPreferences.edit();
        return pref.getString(KEY_APP_LANGUAGE, "");
    }
}
