package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


public class Utils {
    public static String TAG = Utils.class.getName();


    public static void close(Thread thread) {
        if (thread != null) {
            try {
                thread.interrupt();
            } catch (Exception unused) {
            }
        }
    }


    public static double round(double d, int i) {
        if (i >= 0) {
            return new BigDecimal(d).setScale(i, RoundingMode.HALF_UP).doubleValue();
        }
        throw new IllegalArgumentException();
    }

    public static boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static void saveProperties(String str, int i) {
        try {
            SharedPreferences.Editor edit = MyApplication.getContext().getSharedPreferences("Forex", 0).edit();
            edit.putInt(str, i);
            edit.commit();
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
    }

    public static void saveProperties(String str, String str2) {
        try {
            SharedPreferences.Editor edit = MyApplication.getContext().getSharedPreferences("Forex", 0).edit();
            edit.putString(str, str2);
            edit.commit();
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
    }


    public static int getProperties(String str, int i) {
        return MyApplication.getContext().getSharedPreferences("Forex", 0).getInt(str, i);
    }

    public static String getProperties(String str, String str2) {
        return MyApplication.getContext().getSharedPreferences("Forex", 0).getString(str, str2);
    }

    public static String getString(int i) {
        return MyApplication.getContext().getString(i);
    }


    public static List<String> getSymbolsArray(SparseArray<MarketSymbolObject> sparseArray) {
        if (sparseArray == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            arrayList.add(sparseArray.valueAt(i).symbol);
        }
        return arrayList;
    }

    public static void closeKeyboard(AppCompatActivity appCompatActivity) {
        try {
            View currentFocus = appCompatActivity.getCurrentFocus();
            if (currentFocus != null) {
                ((InputMethodManager) appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
    }


}
