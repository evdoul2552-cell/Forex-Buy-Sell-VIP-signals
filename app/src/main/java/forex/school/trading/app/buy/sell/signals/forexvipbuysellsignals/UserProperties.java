package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseIntArray;

import java.util.Calendar;

public class UserProperties {
    public static boolean APP_RUN_ON_BACKGROUND = false;
    public static String FX_SESSION = "";
    public static String USER_ID = "";
    public static boolean initial = false;
    public static final SparseIntArray marketNotificationMap = new SparseIntArray();
    public static int minuteTimeDiff;
    public static SparseIntArray newsReadByUser = new SparseIntArray();
    public static final SparseIntArray outlookNotificationMap = new SparseIntArray();
    public static long userOffset;

    public static void init(DatabaseHandler databaseHandler, Context context) {
        marketNotificationMap.clear();

        FX_SESSION = databaseHandler.getConfigAsString("fxSession", "");
        Calendar instance = Calendar.getInstance();
        userOffset = (long) (instance.get(15) + instance.get(16));
        minuteTimeDiff = databaseHandler.getConfigAsInt("server_timeframe", 0);
        initial = true;
    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(FX_SESSION);
    }
}
