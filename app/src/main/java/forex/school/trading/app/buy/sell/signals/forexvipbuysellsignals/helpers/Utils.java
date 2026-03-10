package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.lang.AppLangSessionManager;

public final class Utils {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private final Context context;

    public Utils(Context context2) {
        this.context = context2;
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final void setLocale(Context context, String str) {
            if (Intrinsics.areEqual((Object) str, (Object) "")) {
                str = "en";
            }
            Locale locale = new Locale(str);
            Resources resources = context.getResources();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, displayMetrics);
        }

        public final void setLang(Context context) {
            if (!Intrinsics.areEqual((Object) AppLangSessionManager.getLanguage(context), (Object) "")) {
                String language = AppLangSessionManager.getLanguage(context);
                setLocale(context, language);
            }
            if (Intrinsics.areEqual((Object) "ar", (Object) AppLangSessionManager.getLanguage(context)) || Intrinsics.areEqual((Object) "fa", (Object) AppLangSessionManager.getLanguage(context))) {
                ((Activity) context).getWindow().getDecorView().setLayoutDirection(0);
            } else {
                ((Activity) context).getWindow().getDecorView().setLayoutDirection(0);
            }
        }
    }
}
