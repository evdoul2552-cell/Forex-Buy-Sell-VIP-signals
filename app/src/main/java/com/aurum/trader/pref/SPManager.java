package com.aurum.trader.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import kotlin.jvm.internal.Intrinsics;

public final class SPManager {
    public static final SPManager INSTANCE = new SPManager();
    private static final String SP_APP = "spAppLiveBuySell";
    public static final String SP_IS_SUBSCRIBED = "isSubscribed";
    public static final String SP_activeAnim = "activeAnim";
    public static final String SP_firstTimeOpen = "firstTimeOpen";
    public static final String SP_isVipEnabled = "isVipEnabled";
    public static final String SP_timeZone = "timeZone";
    private static SharedPreferences sp;
    private static SharedPreferences.Editor spEditor;

    private SPManager() {
    }

    public final void init(Context context) {
        SharedPreferences sharedPreferences;
        SharedPreferences sharedPreferences2 = null;
        if (context != null) {
            try {
                sharedPreferences = context.getSharedPreferences(SP_APP, 0);
            } catch (Exception e) {
                Log.e("SPManager", "SPManager: " + e.getMessage());
                return;
            }
        } else {
            sharedPreferences = null;
        }
        sp = sharedPreferences;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sp");
        } else {
            sharedPreferences2 = sharedPreferences;
        }
        SharedPreferences.Editor edit = sharedPreferences2.edit();
        spEditor = edit;
    }

    public final void saveSPString(String str, String str2) {
        SharedPreferences.Editor editor = spEditor;
        SharedPreferences.Editor editor2 = null;
        if (editor == null) {
            Intrinsics.throwUninitializedPropertyAccessException("spEditor");
            editor = null;
        }
        editor.putString(str, str2);
        SharedPreferences.Editor editor3 = spEditor;
        if (editor3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("spEditor");
        } else {
            editor2 = editor3;
        }
        editor2.commit();
    }

    public final void saveSPInt(String str, int i) {
        SharedPreferences.Editor editor = spEditor;
        SharedPreferences.Editor editor2 = null;
        if (editor == null) {
            Intrinsics.throwUninitializedPropertyAccessException("spEditor");
            editor = null;
        }
        editor.putInt(str, i);
        SharedPreferences.Editor editor3 = spEditor;
        if (editor3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("spEditor");
        } else {
            editor2 = editor3;
        }
        editor2.commit();
    }

    public final void saveSPLong(String str, long j) {
        SharedPreferences.Editor editor = spEditor;
        SharedPreferences.Editor editor2 = null;
        if (editor == null) {
            Intrinsics.throwUninitializedPropertyAccessException("spEditor");
            editor = null;
        }
        editor.putLong(str, j);
        SharedPreferences.Editor editor3 = spEditor;
        if (editor3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("spEditor");
        } else {
            editor2 = editor3;
        }
        editor2.commit();
    }

    public final void saveSPFloat(String str, float f) {
        SharedPreferences.Editor editor = spEditor;
        SharedPreferences.Editor editor2 = null;
        if (editor == null) {
            Intrinsics.throwUninitializedPropertyAccessException("spEditor");
            editor = null;
        }
        editor.putFloat(str, f);
        SharedPreferences.Editor editor3 = spEditor;
        if (editor3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("spEditor");
        } else {
            editor2 = editor3;
        }
        editor2.commit();
    }

    public final void saveSPBoolean(String str, boolean z) {
        SharedPreferences.Editor editor = spEditor;
        SharedPreferences.Editor editor2 = null;
        if (editor == null) {
            Intrinsics.throwUninitializedPropertyAccessException("spEditor");
            editor = null;
        }
        editor.putBoolean(str, z);
        SharedPreferences.Editor editor3 = spEditor;
        if (editor3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("spEditor");
        } else {
            editor2 = editor3;
        }
        editor2.commit();
    }

    public final long getLongValueFromPref(String str, long j) {
        SharedPreferences sharedPreferences = sp;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sp");
            sharedPreferences = null;
        }
        return sharedPreferences.getLong(str, j);
    }

    public final int getIntValueFromPref(String str, int i) {
        SharedPreferences sharedPreferences = sp;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sp");
            sharedPreferences = null;
        }
        return sharedPreferences.getInt(str, i);
    }

    public final float getFloatValueFromPref(String str, float f) {
        SharedPreferences sharedPreferences = sp;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sp");
            sharedPreferences = null;
        }
        return sharedPreferences.getFloat(str, f);
    }

    public final String getStringEmptyValueFromPref(String str) {
        SharedPreferences sharedPreferences = sp;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sp");
            sharedPreferences = null;
        }
        return sharedPreferences.getString(str, "");
    }

    public final boolean getSpBoolean(String str, boolean z) {
        SharedPreferences sharedPreferences = sp;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sp");
            sharedPreferences = null;
        }
        return sharedPreferences.getBoolean(str, z);
    }

    public final void clearAll() {
        SharedPreferences sharedPreferences = sp;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sp");
            sharedPreferences = null;
        }
        sharedPreferences.edit().clear().commit();
    }
}
