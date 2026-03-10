package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.custom;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;

import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.R;


public class Common_Banner_google {


    public void GoogleBannerAds(Context context,LinearLayout adViewBanner){
        AdManagerAdView adView = new AdManagerAdView(context);
        adView.setVisibility(View.VISIBLE);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(context.getResources().getString(R.string.NATIVE_BANNER_GGL));
        adView.loadAd(new AdManagerAdRequest.Builder().build());
        adViewBanner.setVisibility(View.VISIBLE);
        adViewBanner.addView(adView);
    }
}
