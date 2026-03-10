package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kotlin.jvm.internal.Intrinsics;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.pref.SPManager;

public final class GlobalMethods {
    public static final GlobalMethods INSTANCE = new GlobalMethods();

    private GlobalMethods() {
    }

    public final String getNewDate(String str, String str2) {
        Date date;
//        Log.e("getNewDate", "getNewDate: " + str);
        if (Intrinsics.areEqual((Object) str, (Object) "") || Intrinsics.areEqual((Object) str, (Object) "-")) {
            return "-";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aaa", Locale.getDefault());
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("d-MMM hh:mm aaa", Locale.getDefault());
        if (isDateValid(str)) {
            Date date2 = null;
            try {
                date = simpleDateFormat.parse(str);
            } catch (Exception e) {
                e.printStackTrace();
                date = null;
            }
            float floatValueFromPref = SPManager.INSTANCE.getFloatValueFromPref(SPManager.SP_timeZone, 0.0f);
            float f = ((float) 60) * floatValueFromPref;
//            Log.d("timezone_test", "getNewDate: myTimeZone: " + floatValueFromPref);
//            Log.d("timezone_test", "getNewDate: minute: " + f);
            if (date != null) {
                date2 = new Date(date.getTime() + (((long) (((int) f) * 60)) * 1000));
            }
//            Log.d("time_mill", "getNewDate: plus: " + date.getTime());
//            Log.d("time_mill_z", "getNewDate: plus: " + (date.getTime() + (((long) (((int) f) * 60)) * 1000)));
            if (date2 == null) {
                return "-";
            }
            return simpleDateFormat2.format(date2);
        } else if (!Intrinsics.areEqual((Object) str2, (Object) "details")) {
            return str;
        } else {
            return str + " (GMT +0)";
        }
    }


    private final boolean isDateValid(String str) {
        try {
            DateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aaa", Locale.getDefault());
            simpleDateFormat.setLenient(false);
            simpleDateFormat.parse(str);
            return true;
        } catch (ParseException unused) {
            return false;
        }
    }
}
