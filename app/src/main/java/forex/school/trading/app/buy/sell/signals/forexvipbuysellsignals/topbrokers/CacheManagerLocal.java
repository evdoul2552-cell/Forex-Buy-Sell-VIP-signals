package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.topbrokers;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.iainconnor.objectcache.CacheManager;
import com.iainconnor.objectcache.DiskCache;
import com.iainconnor.objectcache.PutCallback;

import java.io.File;
import java.io.IOException;

public class CacheManagerLocal {
    public static String PARAM_NOTIFICATIONS = "notifications";
    public static CacheManager cacheManager;

    public static void init(Context context) {
        try {
            if (cacheManager == null) {
                String path = context.getCacheDir().getPath();
                cacheManager = CacheManager.getInstance(new DiskCache(new File(path + File.separator + context.getPackageName()), 1604, 10485760));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void insert(String str, String str2) {
        try {
            if (!TextUtils.isEmpty(str2)) {
                cacheManager.putAsync(str, str2, CacheManager.ExpiryTimes.ONE_WEEK.asSeconds(), true, new PutCallback() {

                    @Override // com.iainconnor.objectcache.PutCallback
                    public void onFailure(Exception exc) {
                    }

                    @Override // com.iainconnor.objectcache.PutCallback
                    public void onSuccess() {
                    }
                });
            }
        } catch (Exception unused) {
        }
    }

    public static String getValue(String str) {
        return (String) cacheManager.get(str, String.class, new TypeToken<String>() {
        }.getType());
    }
}
