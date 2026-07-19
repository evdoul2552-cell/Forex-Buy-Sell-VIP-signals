package com.aurum.trader.custom;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Common_native_google {
    public boolean isOnline() {
        return true;
    }

    public void GoogleloadNativeAds(AppCompatActivity context, LinearLayout linearLayout) {
        if (linearLayout != null) {
            linearLayout.setVisibility(LinearLayout.GONE);
        }
    }
}
