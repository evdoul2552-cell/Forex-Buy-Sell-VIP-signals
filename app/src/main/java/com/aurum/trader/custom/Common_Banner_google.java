package com.aurum.trader.custom;

import android.content.Context;
import android.widget.LinearLayout;

public class Common_Banner_google {
    public void GoogleBannerAds(Context context, LinearLayout adViewBanner) {
        if (adViewBanner != null) {
            adViewBanner.setVisibility(LinearLayout.GONE);
        }
    }
}
