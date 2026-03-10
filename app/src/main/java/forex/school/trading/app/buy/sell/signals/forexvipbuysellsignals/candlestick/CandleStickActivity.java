package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.candlestick;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.R;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.custom.Common_Banner_google;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.topbrokers.BrokerDetailsActivity;
import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class CandleStickActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    LinearLayoutManager linearLayoutManager;
    TypedArray img, names;
    ImageView back;
    public static InterstitialAd interstitialAd;
    public static boolean adIsLoading;
    public KProgressHUD hud;
    public static final String TAG = CandleStickActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candlestick);

        LinearLayout banner = (LinearLayout) findViewById(R.id.adView);
        Common_Banner_google common_bannerad = new Common_Banner_google();
        common_bannerad.GoogleBannerAds(getApplicationContext(), banner);

        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Loading AD").setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f).show();

        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        InterstitialAd.load(this, getResources().getString(R.string.INTRESTITIAL_GGL_AD), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                CandleStickActivity.this.interstitialAd = interstitialAd;
                adIsLoading = false;
                Log.e(TAG, "onAdLoaded");
                if (interstitialAd != null) {
                    interstitialAd.show(CandleStickActivity.this);
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
                        CandleStickActivity.this.interstitialAd = null;
                        Log.e("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        CandleStickActivity.this.interstitialAd = null;
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

        img = getResources().obtainTypedArray(R.array.candle_img);
        names = getResources().obtainTypedArray(R.array.candle_name);

        recyclerView = findViewById(R.id.recyclerView);
        homeAdapter = new HomeAdapter(CandleStickActivity.this);
        linearLayoutManager = new LinearLayoutManager(CandleStickActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(homeAdapter);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
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