package com.aurum.trader.topbrokers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import io.github.rupinderjeet.kprogresshud.KProgressHUD;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.aurum.trader.MyApplication;
import com.aurum.trader.R;
import com.aurum.trader.custom.Common_Banner_google;


public class TopBrokerActivity extends AppCompatActivity {
    private static Map<String, HttpRequest> requests = new ConcurrentHashMap();
    public static String TAG = TopBrokerActivity.class.getName();
    private View content;
    private boolean isCreated = false;
    private TopBrokersAsyncTask newsAsyncTask;
    private ListView newsListView;
    private ProgressBar progressBar;
    private boolean restartPreferences = false;
    private TopBrokersAdapter topBrokersAdapter;
    Context context;
    AppCompatActivity activity;
    ImageView back, setting;
    TextView title_name;
    public static MyApplication myApplication;
    public static InterstitialAd interstitialAd;
    public static boolean adIsLoading;
    public KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_broker);

        myApplication = MyApplication.getInstance();

        LinearLayout banner = (LinearLayout) findViewById(R.id.adView);
        Common_Banner_google common_bannerad = new Common_Banner_google();
        common_bannerad.GoogleBannerAds(getApplicationContext(), banner);

        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Loading AD").setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f).show();

        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        InterstitialAd.load(this, getResources().getString(R.string.INTRESTITIAL_GGL_AD), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                TopBrokerActivity.this.interstitialAd = interstitialAd;
                adIsLoading = false;
                Log.e(TAG, "onAdLoaded");
                if (interstitialAd != null) {
                    interstitialAd.show(TopBrokerActivity.this);
                    if (hud != null) {
                        hud.dismiss();
                    }
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        if (hud != null) {
                            hud.dismiss();
                        }
                        TopBrokerActivity.this.interstitialAd = null;
                        Log.e("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        TopBrokerActivity.this.interstitialAd = null;
                        Log.e("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        Log.e("TAG", "The ad was shown.");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e(TAG, loadAdError.getMessage());
                interstitialAd = null;
                adIsLoading = false;
                if (hud != null) {
                    hud.dismiss();
                }
                String error = String.format(java.util.Locale.US, "domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
//                Toast.makeText(SimNetwork_StartActivity.this, "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        title_name = findViewById(R.id.title_name);
        title_name.setText("Top Brokers");

        context = getApplicationContext();
        View findViewById = findViewById(R.id.content);
        this.content = findViewById;
        this.newsListView = (ListView) findViewById.findViewById(R.id.topBrokersListView);
        this.content.setVisibility(View.GONE);
        this.newsListView.setEmptyView(this.content.findViewById(R.id.empty));
        ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.progressBarSpinner);
        this.progressBar = progressBar2;
        progressBar2.setVisibility(View.VISIBLE);

    }


    public static SparseArray<TopBrokerObject> getTopBrokers(HttpStatus httpStatus, String str) {
        int i;
        String str2 = "https://panthitech.github.io/Forex-Calender-Calculator/top-broker/top-brokers.json?" + str;
        Log.d("NAME", str2);
        SparseArray<TopBrokerObject> sparseArray = new SparseArray<>();
        JsonParser jsonParser = null;
        try {
            HttpRequest httpRequest = getHttpRequest(str2);
            ObjectMapper objectMapper = new ObjectMapper();
            JSONObject jSONObject = new JSONObject(getJson(httpRequest, str2));
            int i2 = 0;
            jsonParser = objectMapper.getJsonFactory().createJsonParser(jSONObject.getString("brokerManageArray"));
            jsonParser.nextToken();
            while (jsonParser.nextToken() == JsonToken.START_OBJECT) {
                sparseArray.put(i2, (TopBrokerObject) objectMapper.readValue(jsonParser, TopBrokerObject.class));
                i2++;
            }
            httpStatus.setOK();
        } catch (Exception e) {
            Log.e("NAME", "getTopBrokers", e);
            httpStatus.setError();
        } catch (Throwable th) {
//            close(str2);
//            close(String.valueOf((JsonParser) null));
            throw th;
        }
//        close(str2);
//        close(String.valueOf(jsonParser));
        return sparseArray;
    }

    public static HttpRequest getHttpRequest(String str) {
        return getHttpRequest(str, 15000);
    }

    public static HttpRequest getHttpRequest(String str, int i) {
        if (!isOnline()) {
            return null;
        }
        HttpRequest httpRequest = HttpRequest.get(str);
        requests.put(str, httpRequest);
        httpRequest.connectTimeout(i);
        httpRequest.readTimeout(i);
        httpRequest.acceptGzipEncoding().uncompress(true);
        return httpRequest;
    }

    public static boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static String getJson(HttpRequest httpRequest, String str) {
        if (httpRequest == null) {
            return CacheManagerLocal.getValue(str);
        }
        String text = IOUtils.toText(httpRequest.reader());
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        CacheManagerLocal.insert(str, text);
        return text;
    }


    public static void updateBrokerStats(HttpStatus httpStatus, String str, int i) {
        String str2 = "https://mobile.mfbcdn.net/link-track-redirect.html?type=brokerMngOpenMobile&oid=" + i;
        Log.d("NAME", str2);
        try {
            getJsonParser(getHttpRequest(str2, 120000), new ObjectMapper(), str);
            httpStatus.setOK();
        } catch (Exception e) {
            Log.e("NAME", "submitRequest", e);
            httpStatus.setError();
        } catch (Throwable th) {
//            close(str2);
            throw th;
        }
//        close(str2);
    }

    public static JsonParser getJsonParser(HttpRequest httpRequest, ObjectMapper objectMapper, String str) {
        try {
            return objectMapper.getJsonFactory().createJsonParser(getJson(httpRequest, str));
        } catch (Exception e) {
            Log.e("NAME", "getJsonParser", e);
            return null;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (!this.isCreated) {
            this.isCreated = true;
            TopBrokersAsyncTask topBrokersAsyncTask = new TopBrokersAsyncTask(this);
            this.newsAsyncTask = topBrokersAsyncTask;
            topBrokersAsyncTask.execute(new String[0]);
        }
    }

    public class TopBrokersAsyncTask extends AsyncTask<String, String, SparseArray<TopBrokerObject>> {
        private HttpStatus httpStatus = new HttpStatus();
        private TopBrokerActivity newsFragment;

        public TopBrokersAsyncTask(TopBrokerActivity topBrokersFragment) {
            this.newsFragment = topBrokersFragment;
        }


        public SparseArray<TopBrokerObject> doInBackground(String... strArr) {
            HttpStatus httpStatus2 = this.httpStatus;
            TopBrokerActivity topBrokersFragment = TopBrokerActivity.this;
            return getTopBrokers(httpStatus2, topBrokersFragment.getUrlParams(TopBrokerActivity.this));
        }


        public void onPostExecute(SparseArray<TopBrokerObject> sparseArray) {
            int i = 0;
            while (true) {
                try {
                    if (i >= sparseArray.size()) {
                        break;
                    }
                    TopBrokerObject topBrokerObject = sparseArray.get(i);
                    if (topBrokerObject.oid == myApplication.getSponsoredBrokerOid()) {
                        sparseArray.remove(i);
                        sparseArray.put(0, topBrokerObject);
                        break;
                    }
                    i++;
                } catch (Exception e) {
                    Log.e(TopBrokerActivity.TAG, "error", e);
                    return;
                }
            }
            topBrokersAdapter = new TopBrokersAdapter(context, this.newsFragment, sparseArray);
            newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(TopBrokerActivity.this, BrokerDetailsActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("position", position);
                    intent.putExtra("queries", asList(sparseArray));
                    startActivity(intent);
                }
            });
            newsListView.setAdapter((ListAdapter) topBrokersAdapter);
            content.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            super.onPostExecute((SparseArray<TopBrokerObject>) sparseArray);
        }
    }

    public static <C> ArrayList<C> asList(SparseArray<C> sparseArray) {
        if (sparseArray == null) {
            return null;
        }
        ArrayList<C> arrayList = new ArrayList<>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            arrayList.add(sparseArray.valueAt(i));
        }
        return arrayList;
    }


    public ListView getList() {
        return this.newsListView;
    }

    public String getUrlParams(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("brokerFilter_preferences", 0);
        HashSet hashSet = new HashSet();
        StringBuilder sb = new StringBuilder();
        Set<String> stringSet = sharedPreferences.getStringSet("broker_types", hashSet);
        sb.append("&brokerType=");
        sb.append(TextUtils.join(",", stringSet));
        Set<String> stringSet2 = sharedPreferences.getStringSet("countries", hashSet);
        sb.append("&country=");
        sb.append(TextUtils.join(",", stringSet2));
        Set<String> stringSet3 = sharedPreferences.getStringSet("international_offices", hashSet);
        sb.append("&officeCountry=");
        sb.append(TextUtils.join(",", stringSet3));
        Set<String> stringSet4 = sharedPreferences.getStringSet("regulation_types", hashSet);
        sb.append("&regulation=");
        sb.append(TextUtils.join(",", stringSet4));
        Set<String> stringSet5 = sharedPreferences.getStringSet("broker_status", hashSet);
        sb.append("&brokerStatus=");
        sb.append(TextUtils.join(",", stringSet5));
        if (sharedPreferences.contains("acceptingUSClients")) {
            boolean z = sharedPreferences.getBoolean("acceptingUSClients", false);
            sb.append("&clients=");
            sb.append(z);
        }
        Set<String> stringSet6 = sharedPreferences.getStringSet("account_currency", hashSet);
        sb.append("&currency=");
        sb.append(TextUtils.join(",", stringSet6));
        Set<String> stringSet7 = sharedPreferences.getStringSet("fundOrDepositMethod", hashSet);
        sb.append("&fundOrDepositMethod=");
        sb.append(TextUtils.join(",", stringSet7));
        if (sharedPreferences.contains("swap_free_account")) {
            boolean z2 = sharedPreferences.getBoolean("swap_free_account", false);
            sb.append("&swapFree=");
            sb.append(z2);
        }
        if (sharedPreferences.contains("segregated")) {
            boolean z3 = sharedPreferences.getBoolean("segregated", false);
            sb.append("&segregated=");
            sb.append(z3);
        }
        if (sharedPreferences.contains("marginInterest")) {
            boolean z4 = sharedPreferences.getBoolean("marginInterest", false);
            sb.append("&marginInterest=");
            sb.append(z4);
        }
        if (sharedPreferences.contains("managers")) {
            boolean z5 = sharedPreferences.getBoolean("managers", false);
            sb.append("&managers=");
            sb.append(z5);
        }
        if (sharedPreferences.contains("demo")) {
            boolean z6 = sharedPreferences.getBoolean("demo", false);
            sb.append("&demo=");
            sb.append(z6);
        }
        if (sharedPreferences.contains("demo_expiring")) {
            boolean z7 = sharedPreferences.getBoolean("demo_expiring", false);
            sb.append("&demoExpiring=");
            sb.append(z7);
        }
        if (sharedPreferences.contains("mobile")) {
            boolean z8 = sharedPreferences.getBoolean("mobile", false);
            sb.append("&mobile=");
            sb.append(z8);
        }
        if (sharedPreferences.contains("web")) {
            boolean z9 = sharedPreferences.getBoolean("web", false);
            sb.append("&web=");
            sb.append(z9);
        }
        if (sharedPreferences.contains("api")) {
            boolean z10 = sharedPreferences.getBoolean("api", false);
            sb.append("&api=");
            sb.append(z10);
        }
        if (sharedPreferences.contains("oco")) {
            boolean z11 = sharedPreferences.getBoolean("oco", false);
            sb.append("&oco=");
            sb.append(z11);
        }
        if (sharedPreferences.contains("over_phone")) {
            boolean z12 = sharedPreferences.getBoolean("over_phone", false);
            sb.append("&overPhone=");
            sb.append(z12);
        }
        if (sharedPreferences.contains("hedge")) {
            boolean z13 = sharedPreferences.getBoolean("hedge", false);
            sb.append("&hedge=");
            sb.append(z13);
        }
        if (sharedPreferences.contains("trailing")) {
            boolean z14 = sharedPreferences.getBoolean("trailing", false);
            sb.append("&trailing=");
            sb.append(z14);
        }
        if (sharedPreferences.contains("one_click")) {
            boolean z15 = sharedPreferences.getBoolean("one_click", false);
            sb.append("&oneClickTrading=");
            sb.append(z15);
        }
        if (sharedPreferences.contains("bonuses")) {
            boolean z16 = sharedPreferences.getBoolean("bonuses", false);
            sb.append("&bonuses=");
            sb.append(z16);
        }
        if (sharedPreferences.contains("contests")) {
            boolean z17 = sharedPreferences.getBoolean("contests", false);
            sb.append("&contests=");
            sb.append(z17);
        }
        if (sharedPreferences.contains("centsScalping")) {
            boolean z18 = sharedPreferences.getBoolean("centsScalping", false);
            sb.append("&centsScalping=");
            sb.append(z18);
        }
        Set<String> stringSet8 = sharedPreferences.getStringSet("other_trading_instruments", hashSet);
        sb.append("&instruments=");
        sb.append(TextUtils.join(",", stringSet8));
        Set<String> stringSet9 = sharedPreferences.getStringSet("broker_languages", hashSet);
        sb.append("&brokerLanguage=");
        sb.append(TextUtils.join(",", stringSet9));
        Set<String> stringSet10 = sharedPreferences.getStringSet("broker_availability", hashSet);
        sb.append("&availability=");
        sb.append(TextUtils.join(",", stringSet10));
        Set<String> stringSet11 = sharedPreferences.getStringSet("trading_platforms", hashSet);
        sb.append("&platforms=");
        sb.append(TextUtils.join(",", stringSet11));
        Set<String> stringSet12 = sharedPreferences.getStringSet("trading_platforms_timezones", hashSet);
        sb.append("&timeZone=");
        sb.append(TextUtils.join(",", stringSet12));
        if (sharedPreferences.contains("commission")) {
            sb.append("&commission=");
            sb.append((double) getSharedPrefernces(sharedPreferences, "commission", 0.0f));
        }
        if (sharedPreferences.contains("minimal_deposit")) {
            sb.append("&minDeposit=");
            sb.append((double) getSharedPrefernces(sharedPreferences, "minimal_deposit", 0.0f));
        }
        if (sharedPreferences.contains("max_leverage")) {
            sb.append("&maxLeverage=");
            sb.append((double) getSharedPrefernces(sharedPreferences, "max_leverage", 0.0f));
        }
        if (sharedPreferences.contains("min_lot")) {
            sb.append("&minLot=");
            sb.append((double) getSharedPrefernces(sharedPreferences, "min_lot", 0.0f));
        }
        if (sharedPreferences.contains("max_lot")) {
            sb.append("&maxLot=");
            sb.append((double) getSharedPrefernces(sharedPreferences, "max_lot", 0.0f));
        }
        Set<String> stringSet13 = sharedPreferences.getStringSet("ecnSpreads", hashSet);
        sb.append("&ecnSpreads=");
        sb.append(TextUtils.join(",", stringSet13));
        return sb.toString();
    }


    public static float getSharedPrefernces(SharedPreferences sharedPreferences, String str, float f) {
        try {
            return sharedPreferences.getFloat(str, f);
        } catch (Exception unused) {
            return f;
        }
    }


    public static void addSharedPreferences(Map<String, ?> map, SharedPreferences.Editor editor) {
        try {
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                Object value = entry.getValue();
                String key = entry.getKey();
                if (value instanceof String) {
                    editor.putString(key, (String) value);
                } else if (value instanceof Set) {
                    editor.putStringSet(key, (Set) value);
                } else if (value instanceof Integer) {
                    editor.putInt(key, ((Integer) value).intValue());
                } else if (value instanceof Long) {
                    editor.putLong(key, ((Long) value).longValue());
                } else if (value instanceof Float) {
                    editor.putFloat(key, ((Float) value).floatValue());
                } else if (value instanceof Boolean) {
                    editor.putBoolean(key, ((Boolean) value).booleanValue());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
    }

    public void setRestartPreferences(boolean z) {
        this.restartPreferences = z;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        IOUtils.close(this.newsAsyncTask);
        super.onDestroy();
    }
}