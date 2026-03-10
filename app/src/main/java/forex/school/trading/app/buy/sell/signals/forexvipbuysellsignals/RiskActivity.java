package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import io.github.rupinderjeet.kprogresshud.KProgressHUD;

import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.custom.Common_Banner_google;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.topbrokers.BrokerDetailsActivity;

public class RiskActivity extends AppCompatActivity {
    ImageView imageBack;
    public static InterstitialAd interstitialAd;
    public static boolean adIsLoading;
    public KProgressHUD hud;
    public static final String TAG = RiskActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk);

        LinearLayout banner = (LinearLayout) findViewById(R.id.adView);
        Common_Banner_google common_bannerad = new Common_Banner_google();
        common_bannerad.GoogleBannerAds(getApplicationContext(), banner);

        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Loading AD").setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f).show();

        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        InterstitialAd.load(this, getResources().getString(R.string.INTRESTITIAL_GGL_AD), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                RiskActivity.this.interstitialAd = interstitialAd;
                adIsLoading = false;
                Log.e(TAG, "onAdLoaded");
                if (interstitialAd != null) {
                    interstitialAd.show(RiskActivity.this);
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
                        RiskActivity.this.interstitialAd = null;
                        Log.e("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        RiskActivity.this.interstitialAd = null;
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

        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}