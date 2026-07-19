package com.aurum.trader;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.net.MailTo;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Locale;

import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import com.aurum.trader.candlestick.CandleStickActivity;
import com.aurum.trader.interfaces.TimeZoneAddedCallback;
import com.aurum.trader.lang.AppLangSessionManager;
import com.aurum.trader.models.SignalModel;
import com.aurum.trader.models.TimeZoneData;
import com.aurum.trader.nointernet.NoInternetDialog;
import com.aurum.trader.pref.SPManager;
import com.aurum.trader.topbrokers.TopBrokerActivity;

public final class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static MainActivity instance;
    private final String TAG = getClass().getName();
    public SignalsAdapter adapter;
    public int currentVersion;
    private DatabaseReference database;
    private boolean doubleBackToExitPressedOnce;
    private ArrayList<SignalModel> list;
    private final Lazy mainViewModel$delegate = LazyKt.lazy(new Function0<MainViewModel>() {
        @Override
        public MainViewModel invoke() {
            return new MainViewModel();
        }
    });
    private NoInternetDialog noInternetDialog;

    DrawerLayout drawerLayout;
    ImageView imageHome;
    ImageView imageMenu;
    RelativeLayout layoutToolbar;
    ProgressBar progressbar;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView textNotice;

    LinearLayout menuHome;
    LinearLayout menuSettings;
    LinearLayout menuPrivacyPolicy;
    LinearLayout menuRateThisApp;
    LinearLayout menuRiskDisclaimer;
    LinearLayout menuSupport;
    LinearLayout menuTimezoneSetting;
    LinearLayout menuTopBroker;
    LinearLayout menu_pos_calculator, menu_candle;
    Animation anim;
    LinearLayout applyNow;
    TextView text;

    /* access modifiers changed from: private */
    public final MainViewModel getMainViewModel() {
        return (MainViewModel) this.mainViewModel$delegate.getValue();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.e(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        Intent intentBackgroundService = new Intent(this,FirebaseMessageReceiver.class);
        startService(intentBackgroundService);

        drawerLayout = findViewById(R.id.drawer_layout);
        imageHome = findViewById(R.id.image_home);
        imageMenu = findViewById(R.id.image_menu);
        layoutToolbar = findViewById(R.id.layout_toolbar);
        progressbar = findViewById(R.id.progressbar);
        recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        textNotice = findViewById(R.id.text_notice);

        menuHome = findViewById(R.id.menu_home);
        menuSettings = findViewById(R.id.menu_settings);
        menuPrivacyPolicy = findViewById(R.id.menu_privacy_policy);
        menuRateThisApp = findViewById(R.id.menu_rate_this_app);
        menuRiskDisclaimer = findViewById(R.id.menu_risk_disclaimer);
        menuSupport = findViewById(R.id.menu_support);
        menuTimezoneSetting = findViewById(R.id.menu_timezone_setting);
        menuTopBroker = findViewById(R.id.menu_top_broker);
        menu_pos_calculator = findViewById(R.id.menu_pos_calculator);
        menu_candle = findViewById(R.id.menu_candle);

        applyNow = findViewById(R.id.applyNow);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        anim = alphaAnimation;
        alphaAnimation.setDuration(500);
        anim.setStartOffset(10);
        anim.setRepeatMode(2);
        anim.setRepeatCount(-1);
        text = findViewById(R.id.text);
        text.startAnimation(this.anim);
        applyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView("https://one.exnesstrack.org/a/ncf03hnukk");
            }
        });

        setLang();
        noInternetChecker();
        SPManager.INSTANCE.init(this);

        init();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMainViewModel().retrieveData("new_signal");
            }
        });
        initListeners();
        initializeObservers();
        getTimeZoneFirstTime();
    }


    private final void noInternetChecker() {
        this.noInternetDialog = new NoInternetDialog.Builder((Context) this).build();
    }

    public static void openWebView(String str) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            intent.setFlags(268435456);
            MyApplication.getContext().startActivity(intent);
        } catch (Exception e) {
            Log.e("WEB", "error", e);
        }
    }


    public void onDestroy() {
        super.onDestroy();
        NoInternetDialog noInternetDialog2 = this.noInternetDialog;
        noInternetDialog2.onDestroy();
    }


    private final void getTimeZoneFirstTime() {
        if (SPManager.INSTANCE.getSpBoolean(SPManager.SP_firstTimeOpen, true)) {
            showTimeZoneDialog();
        } else {
            retrieveData();
        }
    }


    private final void showTimeZoneDialog() {
        Context context = getApplicationContext();
        LifecycleOwner lifecycleOwner = this;
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.time_zone_dialog);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        Button button = dialog.findViewById(R.id.button);
        ImageView imageClose = dialog.findViewById(R.id.image_close);
        ProgressBar progressbar = dialog.findViewById(R.id.progressbar);
        RecyclerView timezoneDialogRv = dialog.findViewById(R.id.timezone_dialog_rv);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveData();
                dialog.dismiss();
                if (SPManager.INSTANCE.getSpBoolean(SPManager.SP_firstTimeOpen, true)) {
                    SPManager.INSTANCE.saveSPBoolean(SPManager.SP_firstTimeOpen, false);
                    Toast.makeText(context, "0.0 timezone added", Toast.LENGTH_LONG).show();
                }
            }
        });
        timezoneDialogRv.setHasFixedSize(true);
        timezoneDialogRv.setLayoutManager(new LinearLayoutManager(context));
        getMainViewModel().getAllTimeZone(context);
        getMainViewModel().getTimeZoneLiveData().observe(lifecycleOwner, new Observer<TimeZoneData>() {
            @Override
            public void onChanged(TimeZoneData timeZoneDataItems) {

                timezoneDialogRv.setAdapter(new TimeZoneAdapter(context, timeZoneDataItems, new TimeZoneAddedCallback() {
                    @Override
                    public void timeZoneAdded() {
                        TimeZoneAddedCallback.DefaultImpls.timeZoneAdded(this);
                        retrieveData();
                        dialog.dismiss();
                        SPManager.INSTANCE.saveSPBoolean(SPManager.SP_firstTimeOpen, false);
                    }
                }));
                progressbar.setVisibility(View.GONE);
            }
        });
        dialog.show();
    }


    public final void retrieveData() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                getMainViewModel().retrieveData("new_signal");
            }
        }, 1000);
    }


    private final void initializeObservers() {
        LifecycleOwner lifecycleOwner = this;
        getMainViewModel().getProgessLiveData().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean.booleanValue()) {
                    progressbar.setVisibility(View.VISIBLE);
                } else {
                    progressbar.setVisibility(View.GONE);

                }
            }
        });
        getMainViewModel().getSwipeRefreshLayoutLiveData().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                swipeRefreshLayout.setRefreshing(aBoolean.booleanValue());
            }
        });

        getMainViewModel().getDataListLiveData().observe(lifecycleOwner, new Observer<ArrayList<SignalModel>>() {
            @Override
            public void onChanged(ArrayList<SignalModel> signalModels) {
                progressbar.setVisibility(View.GONE);
                String stringEmptyValueFromPref = SPManager.INSTANCE.getStringEmptyValueFromPref(SPManager.SP_activeAnim);
                adapter = new SignalsAdapter(MainActivity.this, signalModels, stringEmptyValueFromPref);
                SignalsAdapter signalsAdapter = adapter;
                if (signalsAdapter != null) {
                    recyclerView.setAdapter(signalsAdapter);
                    signalsAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private final void init() {
        instance = this;
        initRecyclerview();
        this.list = new ArrayList<>();
    }

    private final void initListeners() {

        View.OnClickListener onClickListener = this;
        imageMenu.setOnClickListener(onClickListener);
        menuHome.setOnClickListener(onClickListener);
        menuSettings.setOnClickListener(onClickListener);
        menuTopBroker.setOnClickListener(onClickListener);
        menu_pos_calculator.setOnClickListener(onClickListener);
        menu_candle.setOnClickListener(onClickListener);
        menuRiskDisclaimer.setOnClickListener(onClickListener);
        menuRateThisApp.setOnClickListener(onClickListener);
        menuSupport.setOnClickListener(onClickListener);
        menuPrivacyPolicy.setOnClickListener(onClickListener);
        menuTimezoneSetting.setOnClickListener(onClickListener);
        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainViewModel().retrieveData("new_signal");
            }
        });
    }


    private final void initRecyclerview() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void onClick(View view) {
        if (view != null) {
            int id = view.getId();
            if (id != R.id.image_menu) {
                switch (id) {
                    case R.id.menu_home /*2131362151*/:
                        closeDrawer();
                        getMainViewModel().retrieveData("all");
                        return;
                    case R.id.menu_pos_calculator:
                        closeDrawer();
                        startActivity(new Intent(MainActivity.this, MoneyManagementActivity.class));
                        return;
                    case R.id.menu_candle:
                        closeDrawer();
                        startActivity(new Intent(MainActivity.this, CandleStickActivity.class));
                        return;
                    case R.id.menu_settings:
                        closeDrawer();
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        return;
                    case R.id.menu_privacy_policy /*2131362155*/:
                        closeDrawer();
                        String string4 = getString(R.string.privacy_policy_link);
                        webView(string4);
                        return;
                    case R.id.menu_rate_this_app /*2131362156*/:
                        closeDrawer();
                        rateApp();
                        return;
                    case R.id.menu_risk_disclaimer /*2131362158*/:
                        closeDrawer();
                        Intent intent = new Intent(this, RiskActivity.class);
                        startActivity(intent);
                        return;
                    case R.id.menu_support /*2131362159*/:
                        closeDrawer();
                        sendEmail();
                        return;
                    case R.id.menu_timezone_setting /*2131362160*/:
                        closeDrawer();
                        showTimeZoneDialog();
                        return;
                    case R.id.menu_top_broker /*2131362161*/:
                        closeDrawer();
                        startActivity(new Intent(MainActivity.this, TopBrokerActivity.class));
                        return;
                    default:
                        return;
                }
            } else {
                drawerLayout.openDrawer((int) GravityCompat.START);
            }
        }
    }


    public final void setLocale(String str) {
        Locale locale = new Locale(str);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);
    }


    public final void rateApp() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException unused) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private final void webView(String str) {
        closeDrawer();
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
    }

    private final void sendEmail() {
        Intent intent = new Intent("android.intent.action.VIEW");
        Intent type = intent.setType("message/rfc822");
        type.setData(Uri.parse(MailTo.MAILTO_SCHEME + getString(R.string.support_email))).putExtra("android.intent.extra.EMAIL", getString(R.string.support_email)).putExtra("android.intent.extra.SUBJECT", "").putExtra("android.intent.extra.TEXT", "").setPackage("com.google.android.gm");
        startActivity(intent);
    }

    private final void closeDrawer() {
        drawerLayout.closeDrawer((int) GravityCompat.START);
    }

    public void onBackPressed() {
        if (this.doubleBackToExitPressedOnce) {
            finishAffinity();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_LONG).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 5000);
    }


    public void onSignalClick(SignalModel signalModel) {
        Intent intent = new Intent(this, SignalDetailsActivity.class);
        intent.putExtra("object", signalModel);
        startActivity(intent);
    }

    private final void setLang() {
        Context context = this;
        if (!Intrinsics.areEqual((Object) AppLangSessionManager.getLanguage(context), (Object) "")) {
            setLocale("en");
        }
        if (Intrinsics.areEqual((Object) "ar", (Object) AppLangSessionManager.getLanguage(context)) || Intrinsics.areEqual((Object) "fa", (Object) AppLangSessionManager.getLanguage(context))) {
            getWindow().getDecorView().setLayoutDirection(0);
        } else {
            getWindow().getDecorView().setLayoutDirection(0);
        }
    }

}
