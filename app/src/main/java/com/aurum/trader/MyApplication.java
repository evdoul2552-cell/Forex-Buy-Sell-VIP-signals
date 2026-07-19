package com.aurum.trader;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.BuildConfig;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import com.aurum.trader.topbrokers.CacheManagerLocal;
import com.aurum.trader.topbrokers.Config;


public class MyApplication extends Application {
    public static double APPLICATION_VERSION = 999.0d;
    public static boolean IS_RTL_JELLY_BEAN = false;
    public static final String LANG = "lang";
    public static boolean LTR = true;
    private static final String TAG = MyApplication.class.getName();
    private static Map<String, String> analyticsNames = new Hashtable();
    private static Context context;
    public static CookieManager cookieManager = new CookieManager();
    public static Configuration defaultConfiguration;
    private static Properties properties;
    private Activity currentActivity;
    private Locale locale = null;
    private int sponsoredBrokerOid = 0;

    public void setLocale(String str) {
        if (str.equals("")) {
            str = "en";
        }
        Log.d("Support", str + "");
        Locale locale = new Locale(str);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);
    }


    public int getSponsoredBrokerOid() {
        return this.sponsoredBrokerOid;
    }

    public void onCreate() {
        super.onCreate();
        context = this;
        CookieHandler.setDefault(cookieManager);
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
        }

        setLocale("en");
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("all").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.e("TAG", "onCreate: subscribeToTopic");
                } else {
                    Log.e("TAG", "onCreate: subscribeToTopic failed");
                }
            }
        });


        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            public void onActivityCreated(Activity activity, Bundle bundle) {
                currentActivity = activity;
            }

            public void onActivityStarted(Activity activity) {
                currentActivity = activity;
            }

            public void onActivityResumed(Activity activity) {
                currentActivity = activity;
            }

            public void onActivityPaused(Activity activity) {
                if (currentActivity == activity) {
                    currentActivity = null;
                }
            }

            public void onActivityStopped(Activity activity) {
                if (currentActivity == activity) {
                    currentActivity = null;
                }
            }

            public void onActivityDestroyed(Activity activity) {
                if (currentActivity == activity) {
                    currentActivity = null;
                }
            }
        });
        CacheManagerLocal.init(this);
        Config.init();
        Definitions.initStaticMaps(getResources());
//        Definitions.init(instance.getAllCalendarSymbols());
    }


    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.locale != null) {
            getBaseContext().getResources().updateConfiguration(defaultConfiguration, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context2) {
        context = context2;
    }


    public static MyApplication getInstance() {
        return (MyApplication) context.getApplicationContext();
    }


}
