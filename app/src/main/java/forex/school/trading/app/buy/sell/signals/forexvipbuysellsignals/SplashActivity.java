package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals;

import static forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.topbrokers.TopBrokerActivity.getHttpRequest;
import static forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.topbrokers.TopBrokerActivity.getJsonParser;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.pref.SPManager;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.topbrokers.Config;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.topbrokers.HttpRequest;

public final class SplashActivity extends AppCompatActivity {
    private Thread failedThread;
    private final Lazy handler$delegate = LazyKt.lazy(new Function0<Handler>() {
        @Override
        public Handler invoke() {
            return new Handler(Looper.getMainLooper());
        }
    });
    private DatabaseHandler databaseHandler;
    private static Map<String, HttpRequest> requests = new ConcurrentHashMap();
    public static String TAG = "msg";

    public final Thread getFailedThread() {
        return this.failedThread;
    }

    public final Handler getHandler() {
        return (Handler) this.handler$delegate.getValue();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Context context = this;
//        Utils.Companion.setLang(context);
        setContentView(R.layout.activity_splash);
        SPManager.INSTANCE.init(context);

        databaseHandler = DatabaseHandler.getInstance();
        init(databaseHandler);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SparseArray<MarketSymbolObject> symbols = getSymbols();
                    initMarketSymbols(symbols);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        getHandler().removeCallbacksAndMessages((Object) null);
    }

    public static SparseArray<MarketSymbolObject> getSymbols() {
        String str = Config.URL_SERVER + "/get-market-symbols.json";
        Log.d(TAG, str);
        SparseArray<MarketSymbolObject> sparseArray = new SparseArray<>();
        JsonParser jsonParser = null;
        try {
            HttpRequest httpRequest = getHttpRequest(str);
            ObjectMapper objectMapper = new ObjectMapper();
            jsonParser = getJsonParser(httpRequest, objectMapper, str);
            jsonParser.nextToken();
            while (jsonParser.nextToken() == JsonToken.START_OBJECT) {
                MarketSymbolObject marketSymbolObject = (MarketSymbolObject) objectMapper.readValue(jsonParser, MarketSymbolObject.class);
                sparseArray.append(marketSymbolObject.oid, marketSymbolObject);
            }
        } catch (Exception e) {
            Log.e(TAG, "getSymbols", e);
        }
        return sparseArray;
    }

    public static void init(DatabaseHandler databaseHandler) {
        if (forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.Utils.isOnline()) {
            ConfigSettings serverConfig = getServerConfig();
            long j = serverConfig.serverTime;
            if (j != -1) {
                Calendar instance = Calendar.getInstance();
                long j2 = (long) (instance.get(15) + instance.get(16));
                UserProperties.userOffset = j2;
                UserProperties.minuteTimeDiff = ((int) (instance.getTime().getTime() / 60000)) - ((int) (new Date(j - j2).getTime() / 60000));
                databaseHandler.updateConfig(new ConfigObject("server_timeframe", String.valueOf(UserProperties.minuteTimeDiff)));
            } else {
                UserProperties.minuteTimeDiff = databaseHandler.getConfigAsInt("server_timeframe", 0);
            }
        } else {
            UserProperties.minuteTimeDiff = databaseHandler.getConfigAsInt("server_timeframe", 0);
        }
    }

    public static ConfigSettings getServerConfig() {
        ConfigSettings configSettings = new ConfigSettings();
        try {
            HttpRequest httpRequest = HttpRequest.get(Config.URL_SERVER_CONFIG);
            requests.put(Config.URL_SERVER_CONFIG, httpRequest);
            httpRequest.acceptGzipEncoding().uncompress(true);
            JSONObject jSONObject = new JSONObject(httpRequest.body());
            configSettings.serverTime = jSONObject.getLong("serverTime");
        } catch (Exception e) {
            Log.e(TAG, "getServerConfig", e);
        } catch (Throwable th) {
            throw th;
        }
        return configSettings;
    }

    private void initMarketSymbols(SparseArray<MarketSymbolObject> sparseArray) throws Exception {
        boolean z;
        int i;
        ArrayList<MarketSymbolObject> allMarketSymbolsByUserOrder = this.databaseHandler.getAllMarketSymbolsByUserOrder();
        boolean z2 = allMarketSymbolsByUserOrder.size() == 0;
        int size = sparseArray.size();
        if (size == 0 && allMarketSymbolsByUserOrder.size() != 0) {
            z = false;
        } else if (size != 0 || !z2) {
            z = true;
        } else {
            throw new Exception("market symbols not initial properly");
        }
        if (z) {
            this.databaseHandler.deleteAllMarketSymbols();
            if (z2) {
                int size2 = Definitions.DEFAULT_MARKET_SYMBOLS.size();
                i = 0;
                for (int i2 = 0; i2 < size2; i2++) {
                    MarketSymbolObject marketSymbolObject = sparseArray.get(Definitions.DEFAULT_MARKET_SYMBOLS.get(i2).intValue());
                    if (marketSymbolObject != null) {
                        marketSymbolObject.order = i2;
                        marketSymbolObject.activated = true;
                        i = i2;
                    }
                }
            } else {
                i = 0;
            }
            int size3 = allMarketSymbolsByUserOrder.size();
            ArrayList arrayList = new ArrayList();
            for (int i3 = 0; i3 < size3; i3++) {
                MarketSymbolObject marketSymbolObject2 = allMarketSymbolsByUserOrder.get(i3);
                if (sparseArray.get(marketSymbolObject2.oid) != null) {
                    marketSymbolObject2.order = i;
                    i++;
                    arrayList.add(marketSymbolObject2);
                }
            }
            this.databaseHandler.addMarketSymbols(arrayList);
            ArrayList arrayList2 = new ArrayList();
            SparseArray<MarketSymbolObject> allMarketSymbols = this.databaseHandler.getAllMarketSymbols();
            int size4 = sparseArray.size();
            for (int i4 = 0; i4 < size4; i4++) {
                MarketSymbolObject valueAt = sparseArray.valueAt(i4);
                if (allMarketSymbols.get(valueAt.oid) == null) {
                    valueAt.order = i;
                    if (!z2) {
                        valueAt.activated = false;
                    }
                    i++;
                    arrayList2.add(valueAt);
                }
            }
            this.databaseHandler.addMarketSymbols(arrayList2);
            SparseArray<MarketSymbolObject> allMarketSymbols2 = this.databaseHandler.getAllMarketSymbols();
            int size5 = sparseArray.size();
            for (int i5 = 0; i5 < size5; i5++) {
                MarketSymbolObject valueAt2 = sparseArray.valueAt(i5);
                if (allMarketSymbols2.get(valueAt2.oid) == null) {
                    this.databaseHandler.deleteMarketNotificationBySymbols(valueAt2.oid);
                }
            }
        }
    }


    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(SplashActivity.this, "No Internet connection! Please Connect Internet..", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isNotificationPermissionGranted()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        Thread failedThread = getFailedThread();
                        if (failedThread != null) {
                            failedThread.stop();
                        }
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                        getHandler().removeCallbacksAndMessages((Object) null);
                    } catch (Exception unused) {
                    }
                }
            }).start();
        } else {
            requestNotificationPermission();
        }

    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Thread failedThread = getFailedThread();
                    if (failedThread != null) {
                        failedThread.stop();
                    }
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    getHandler().removeCallbacksAndMessages((Object) null);
                } catch (Exception unused) {
                }
            }
        }).start();
    }

    private boolean isNotificationPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS") == 0;
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.POST_NOTIFICATIONS"}, 1);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Thread failedThread = getFailedThread();
                    if (failedThread != null) {
                        failedThread.stop();
                    }
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    getHandler().removeCallbacksAndMessages((Object) null);
                } catch (Exception unused) {
                }
            }
        }).start();
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1) {
            if (isNotificationPermissionGranted()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            Thread failedThread = getFailedThread();
                            if (failedThread != null) {
                                failedThread.stop();
                            }
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                            getHandler().removeCallbacksAndMessages((Object) null);
                        } catch (Exception unused) {
                        }
                    }
                }).start();
            } else {
                requestNotificationPermission();
            }
        } else {
            finish();
        }
        super.onActivityResult(i, i2, intent);
    }


}
