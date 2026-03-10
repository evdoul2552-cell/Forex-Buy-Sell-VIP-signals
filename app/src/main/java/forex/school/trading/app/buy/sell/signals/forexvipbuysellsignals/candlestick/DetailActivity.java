package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.candlestick;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import io.github.rupinderjeet.kprogresshud.KProgressHUD;

import java.io.File;
import java.io.FileOutputStream;

import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.R;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.SignalDetailsActivity;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.custom.Common_Banner_google;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.custom.Common_native_google;

public class DetailActivity extends AppCompatActivity {
    ImageView detail_iamge;
    TextView detail_name, detail_desc;

    ImageView back, share;

    public static InterstitialAd interstitialAd;
    public static boolean adIsLoading;
    private String TAG = getClass().getSimpleName();
    public KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        LinearLayout banner = (LinearLayout) findViewById(R.id.adView);
        Common_Banner_google common_bannerad = new Common_Banner_google();
        common_bannerad.GoogleBannerAds(getApplicationContext(), banner);

        LinearLayout fl_adplaceholder = (LinearLayout) findViewById(R.id.fl_adplaceholder);
        Common_native_google commonNativeGoogle = new Common_native_google();
        commonNativeGoogle.GoogleloadNativeAds(this, fl_adplaceholder);

        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Loading AD").setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f).show();

        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        InterstitialAd.load((Context) DetailActivity.this, getResources().getString(R.string.INTRESTITIAL_GGL_AD), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                DetailActivity.this.interstitialAd = interstitialAd;
                adIsLoading = false;
                Log.e(TAG, "onAdLoaded");
                if (interstitialAd != null) {
                    interstitialAd.show(DetailActivity.this);
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
                        DetailActivity.this.interstitialAd = null;
                        Log.e("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        DetailActivity.this.interstitialAd = null;
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

        detail_iamge = findViewById(R.id.detail_iamge);
//        detail_iamge.setImageBitmap(bitmap);
        int position = getIntent().getIntExtra("pos", 0);
        String path = getIntent().getStringExtra("bitmap");
        Glide.with(this).load(path).into(detail_iamge);

        String name = getIntent().getStringExtra("name");
        String desc = getIntent().getStringExtra("desc");

        detail_desc = findViewById(R.id.detail_desc);
        detail_desc.setText(desc);

        detail_name = findViewById(R.id.detail_name);
        detail_name.setText(name);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp(DetailActivity.this);
            }
        });

    }


    void shareApp(Context context) {
        Uri urishare;
        Drawable mDrawable = detail_iamge.getDrawable();
        Bitmap decodeResource = ((BitmapDrawable) mDrawable).getBitmap();
//        Bitmap decodeResource = BitmapFactory.decodeFile(context.getResources(), R.id.detail_iamge);
        File file = new File(context.getExternalCacheDir() + "/image.png");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            decodeResource.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.TEXT", "\n" + detail_name.getText().toString() +
                    "\n\n" + detail_desc.getText().toString() +
                    "\n\nDownload the Amazing MyApplication " + context.getString(R.string.app_name) + "  : \n" + "https://play.google.com/store/apps/details?id=" + context.getPackageName());
            if (Build.VERSION.SDK_INT >= 23) {
                urishare = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            } else {
                urishare = Uri.fromFile(file);
            }
            intent.putExtra("android.intent.extra.STREAM", urishare);
            context.startActivity(Intent.createChooser(intent, "Share Image using"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}