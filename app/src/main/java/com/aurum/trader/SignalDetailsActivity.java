package com.aurum.trader;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import io.github.rupinderjeet.kprogresshud.KProgressHUD;

import java.io.Serializable;
import java.util.Locale;

import com.aurum.trader.custom.Common_Banner_google;
import kotlin.jvm.internal.Intrinsics;
import com.aurum.trader.helpers.GlobalMethods;
import com.aurum.trader.lang.AppLangSessionManager;
import com.aurum.trader.models.SignalModel;
import com.aurum.trader.nointernet.NoInternetDialog;
import com.aurum.trader.pref.SPManager;

public final class SignalDetailsActivity extends AppCompatActivity {
    private SignalModel data;
    private NoInternetDialog noInternetDialog;
    ImageView imageBack;
    ImageView imageFlag;
    ImageView imageUpOrDown;
    LinearLayout layoutToolbar;
    RelativeLayout rootView;
    TextView textComment;
    TextView textCurrencyDetails;
    TextView textLastUpdate;
    TextView textOpenPrice;
    TextView textOpeningTime;
    TextView textPosition;
    TextView textProfitOrLoss;
    TextView textStatus;
    TextView textStopLoss;
    TextView textTakeProfit1;
    TextView textTakeProfit2;
    TextView textTakeProfit3;
    TextView textTimeFrame, textTimeFrame1;
    TextView textTradeProbability;
    TextView textTradeResult;
    CardView buysell, calculate, chart;
    Animation anim;
    ImageView trade_result_img, image_share;
    public static InterstitialAd interstitialAd;
    public static boolean adIsLoading;
    private String TAG = getClass().getSimpleName();
    public KProgressHUD hud;

    public static String removeWords(String word, String remove) {
        return word.replace(remove, "");
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setLang();
        setContentView(R.layout.activity_signal_details);

        LinearLayout banner = (LinearLayout) findViewById(R.id.adView);
        Common_Banner_google common_bannerad = new Common_Banner_google();
        common_bannerad.GoogleBannerAds(getApplicationContext(), banner);

        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Loading AD").setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f).show();

        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        InterstitialAd.load((Context) SignalDetailsActivity.this, getResources().getString(R.string.INTRESTITIAL_GGL_AD), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                SignalDetailsActivity.this.interstitialAd = interstitialAd;
                adIsLoading = false;
                Log.e(TAG, "onAdLoaded");
                if (interstitialAd != null) {
                    interstitialAd.show(SignalDetailsActivity.this);
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
                        SignalDetailsActivity.this.interstitialAd = null;
                        Log.e("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        SignalDetailsActivity.this.interstitialAd = null;
                        if (hud != null) {
                            hud.dismiss();
                        }
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

        this.imageBack = findViewById(R.id.image_back);
        this.imageFlag = findViewById(R.id.image_flag);
        this.imageUpOrDown = findViewById(R.id.image_up_or_down);
        this.layoutToolbar = findViewById(R.id.layout_toolbar);
        this.textComment = findViewById(R.id.text_comment);
        this.textCurrencyDetails = findViewById(R.id.text_currency_details);
        this.textLastUpdate = findViewById(R.id.text_last_update);
        this.textOpenPrice = findViewById(R.id.text_open_price);
        this.textOpeningTime = findViewById(R.id.text_opening_time);
        this.textPosition = findViewById(R.id.text_position);
        this.textProfitOrLoss = findViewById(R.id.text_profit_or_loss);
        this.textStatus = findViewById(R.id.text_status);
        this.textStopLoss = findViewById(R.id.text_stop_loss);
        this.textTakeProfit1 = findViewById(R.id.text_take_profit_1);
        this.textTakeProfit2 = findViewById(R.id.text_take_profit_2);
        this.textTakeProfit3 = findViewById(R.id.text_take_profit_3);
        this.textTimeFrame = findViewById(R.id.text_time_frame);
        this.textTimeFrame1 = findViewById(R.id.text_time_frame1);
        this.textTradeProbability = findViewById(R.id.text_trade_probability);
        this.textTradeResult = findViewById(R.id.text_trade_result);
        buysell = findViewById(R.id.buysell);
        trade_result_img = findViewById(R.id.trade_result_img);
        image_share = findViewById(R.id.image_share);
        calculate = findViewById(R.id.calculate);

        noInternetChecker();
        SPManager.INSTANCE.init(this);
        Serializable serializableExtra = getIntent().getSerializableExtra("object");
        data = serializableExtra instanceof SignalModel ? (SignalModel) serializableExtra : null;
        setData();

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        image_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                String string = getResources().getString(R.string.app_name) + "\n\nCurrency Name : " + textCurrencyDetails.getText().toString() +
                        "\nDate : " + textOpeningTime.getText().toString() +
                        "\nAction Type : " + textPosition.getText().toString() +
                        "\nStatus : " + textStatus.getText().toString() +
                        "\nPossiblity : " + textTradeProbability.getText().toString() +
                        "\n\nOpen Price : " + textOpenPrice.getText().toString() +
                        "\nTake Profit 1 : " + textTakeProfit1.getText().toString() +
                        "\nTake Profit 2 : " + textTakeProfit2.getText().toString() +
                        "\nTake Profit 3 : " + textTakeProfit3.getText().toString() +
                        "\n\nStop Loss : " + textStopLoss.getText().toString() +
                        "\nProfit/Loss Status : " + textProfitOrLoss.getText().toString() +
                        "\nTrade Result : " + textTradeResult.getText().toString() +
                        "\n\nTime Frame : " + textTimeFrame.getText().toString() +
                        "\nLast Updates : " + textLastUpdate.getText().toString() +
                        "\nComment : " + textComment.getText().toString() +
                        "\n\n\n Download the Forex Signal Live Signals App In PlayStore : " + "https://play.google.com/store/apps/details?id=" + getPackageName();
                intent.putExtra("android.intent.extra.TEXT", string);
                startActivity(Intent.createChooser(intent, "Share Via"));
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignalDetailsActivity.this, MoneyManagementActivity.class));
            }
        });

        chart = findViewById(R.id.chart);
        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageName = "com.tradingview.tradingviewapp";
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                if (launchIntent != null) {
                    Uri data = Uri.parse("https://www.tradingview.com/symbols/" + textCurrencyDetails.getText().toString() + "/?exchange=FX&utm_source=androidapp&utm_medium=share");
                    launchIntent.setData(data);
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntent);
//                    Toast.makeText(SignalDetailsActivity.this, "App Installed", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://in.tradingview.com/chart/?symbol=" + textCurrencyDetails.getText().toString())));
                    Log.i("SampleLog", "Application is not currently installed.");
//                    Toast.makeText(SignalDetailsActivity.this, "There is no package available in android", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private final void setData() {
        String str;
        String str2 = "";
        String lowerCase;
        String lowerCase2;

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        anim = alphaAnimation;
        alphaAnimation.setDuration(500);
        anim.setStartOffset(10);
        anim.setRepeatMode(2);
        anim.setRepeatCount(-1);

        textPosition.setText(data.getAction());

        String currs = data.getCurrency().toString();
        String remove = " ";
        textCurrencyDetails.setText(removeWords(currs, remove));

        StringBuilder sb = new StringBuilder();
        String substring = data.getStatus().substring(0, 1);
        String upperCase = substring.toUpperCase(Locale.ROOT);
        sb.append(upperCase);
        String substring2 = data.getStatus().substring(1);
        String lowerCase3 = substring2.toLowerCase(Locale.ROOT);
        sb.append(lowerCase3);
        textStatus.setText(sb.toString());

        if (textStatus.getText().equals("Active")) {
            textStatus.startAnimation(anim);
            textStatus.setTextColor(getResources().getColor(R.color.green_light));
        }

        textOpenPrice.setText(data.getOpen_Price());
        textTakeProfit1.setText(data.getTake_profit());
        textTakeProfit2.setText(data.getTake_profit_2());
        textTakeProfit3.setText(data.getTake_Profit_3());

        textStopLoss.setText(data.getStop_Loss());

        GlobalMethods globalMethods = GlobalMethods.INSTANCE;
        textLastUpdate.setText(globalMethods.getNewDate(data.getLast_update(), "details"));
        textOpeningTime.setText(globalMethods.getNewDate(data.getTrade_Opening_Time(), "details"));

        textTimeFrame.setText(data.getTimeFrame());

        String curr = data.getTimeFrame().toString();
        String remove1 = "-";
        textTimeFrame1.setText("TimeFrame : " + removeWords(curr, remove1));

        textTradeProbability.setText(data.getProbability());

        if (Intrinsics.areEqual(data.getAction().toLowerCase(Locale.ROOT), "sell")) {
            imageUpOrDown.setImageResource(R.drawable.down_img);
            buysell.setCardBackgroundColor(ContextCompat.getColor(this, R.color.red));
        } else {
            imageUpOrDown.setImageResource(R.drawable.up_img);
            buysell.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green_light));
        }

        String lowerCase4 = data.getTrade_Result().toLowerCase(Locale.ROOT);
        if (!Intrinsics.areEqual(lowerCase4, "tp-1")) {
            String lowerCase5 = data.getTrade_Result().toLowerCase(Locale.ROOT);
            if (!Intrinsics.areEqual(lowerCase5, "tp-2")) {
                StringBuilder sb2 = new StringBuilder();
                String substring3 = data.getTrade_Result().substring(0, 1);
                String upperCase2 = substring3.toUpperCase(Locale.ROOT);
                sb2.append(upperCase2);
                String substring4 = data.getTrade_Result().substring(1);
                String lowerCase6 = substring4.toLowerCase(Locale.ROOT);
                sb2.append(lowerCase6);
                str = sb2.toString();
                textTradeResult.setText(str);


                if (!Intrinsics.areEqual(data.getTrade_Close(), "")) {
                    if (!Intrinsics.areEqual(data.getTrade_Close(), "-")) {
                        str2 = data.getTrade_Close();
                        textProfitOrLoss.setText(str2);

                        if (!Intrinsics.areEqual(this.data.getTrade_Close(), "")) {
                            if (!Intrinsics.areEqual(this.data.getTrade_Close(), "-")) {
                                String substring5 = this.data.getTrade_Close().substring(0, 1);
                                if (Intrinsics.areEqual(substring5, "+")) {
                                    textComment.setText(getString(R.string.successful_trade));
                                    textComment.setTextColor(getResources().getColor(R.color.white));
                                    textComment.setBackgroundColor(getResources().getColor(R.color.green_light));
                                } else {
                                    textComment.setText(getString(R.string.unsuccessful_trade));
                                    textComment.setTextColor(getResources().getColor(R.color.white));
                                    textComment.setBackgroundColor(getResources().getColor(R.color.red));
                                }
                            }
                        }


                        if (Intrinsics.areEqual(data.getTrade_Close(), "")) {
                        }
                        lowerCase2 = data.getTrade_Result().toLowerCase(Locale.ROOT);
                        if (!Intrinsics.areEqual(lowerCase2, "tp-1")) {
                            String lowerCase7 = data.getTrade_Result().toLowerCase(Locale.ROOT);
                        }
//                        textComment.setText("Break-even the trade and wait for next take profit");
                        selectImage();
                    }
                }
                textProfitOrLoss.setText(str2);
                lowerCase = data.getAction().toLowerCase(Locale.ROOT);
                if (Intrinsics.areEqual(lowerCase, "sell")) {
                }
                if (!Intrinsics.areEqual(data.getTrade_Close(), "")) {
                }
                if (Intrinsics.areEqual(data.getTrade_Close(), "")) {
                }
                lowerCase2 = data.getTrade_Result().toLowerCase(Locale.ROOT);
                if (!Intrinsics.areEqual(lowerCase2, "tp-1")) {
                }
//                textComment.setText("Break-even the trade and wait for next take profit");
                selectImage();
            }
        }

        str = data.getTrade_Result();
        textTradeResult.setText(str);
        if (str.equals("Tp-1") || str.equals("Tp-2") || str.equals("Waiting")) {
            textComment.setText("Break-even trade and wait for take profit");
            textComment.setTextColor(getResources().getColor(R.color.black));
            textComment.setBackgroundColor(getResources().getColor(R.color.white));
        }

        if (data.getTrade_Result().toLowerCase(Locale.ROOT).equals("take profit")) {
            textTradeResult.setTextColor(getResources().getColor(R.color.flat_blue_2));
            trade_result_img.setImageDrawable(getResources().getDrawable(R.drawable.profitt));
        } else if (data.getTrade_Result().toLowerCase(Locale.ROOT).equals("stop loss")) {
            textTradeResult.setTextColor(getResources().getColor(R.color.red));
            trade_result_img.setImageDrawable(getResources().getDrawable(R.drawable.losss));
        } else {
            textTradeResult.setTextColor(getResources().getColor(R.color.black));
            trade_result_img.setImageDrawable(getResources().getDrawable(R.drawable.waiting));
        }
        textProfitOrLoss.setText(str2);
        lowerCase = data.getAction().toLowerCase(Locale.ROOT);
        if (Intrinsics.areEqual(lowerCase, "sell")) {
        }
        if (!Intrinsics.areEqual(data.getTrade_Close(), "")) {
        }
        if (Intrinsics.areEqual(data.getTrade_Close(), "")) {
        }
        lowerCase2 = data.getTrade_Result().toLowerCase(Locale.ROOT);
        if (!Intrinsics.areEqual(lowerCase2, "tp-1")) {
        }
//        textComment.setText("Break-even the trade and wait for next take profit");
        selectImage();


    }


    private final void selectImage() {
        String lowerCase = data.getCurrency().toLowerCase(Locale.ROOT);
        switch (lowerCase.hashCode()) {
            case -1867650114:
                if (lowerCase.equals("nzd cad")) {
                    imageFlag.setImageResource(R.drawable.nzd_cad);
                    return;
                }
                return;
            case -1867649895:
                if (lowerCase.equals("nzd chf")) {
                    imageFlag.setImageResource(R.drawable.nzd_chf);
                    return;
                }
                return;
            case -1867642901:
                if (lowerCase.equals("nzd jpy")) {
                    imageFlag.setImageResource(R.drawable.nzd_jpy);
                    return;
                }
                return;
            case -1867632258:
                if (lowerCase.equals("nzd usd")) {
                    imageFlag.setImageResource(R.drawable.nzd_usd);
                    return;
                }
                return;
            case -1395466414:
                if (lowerCase.equals("eur aud")) {
                    imageFlag.setImageResource(R.drawable.eur_aud);
                    return;
                }
                return;
            case -1395465112:
                if (lowerCase.equals("eur cad")) {
                    imageFlag.setImageResource(R.drawable.eur_cad);
                    return;
                }
                return;
            case -1395464893:
                if (lowerCase.equals("eur chf")) {
                    imageFlag.setImageResource(R.drawable.eur_chf);
                    return;
                }
                return;
            case -1395461225:
                if (lowerCase.equals("eur gbp")) {
                    imageFlag.setImageResource(R.drawable.eur_gbp);
                    return;
                }
                return;
            case -1395457899:
                if (lowerCase.equals("eur jpy")) {
                    imageFlag.setImageResource(R.drawable.eur_jpy);
                    return;
                }
                return;
            case -1395453766:
                if (lowerCase.equals("eur nzd")) {
                    imageFlag.setImageResource(R.drawable.eur_nzd);
                    return;
                }
                return;
            case -1395447256:
                if (lowerCase.equals("eur usd")) {
                    imageFlag.setImageResource(R.drawable.eur_usd);
                    return;
                }
                return;
            case -663441834:
                if (lowerCase.equals("aud cad")) {
                    imageFlag.setImageResource(R.drawable.aud_cad);
                    return;
                }
                return;
            case -663441615:
                if (lowerCase.equals("aud chf")) {
                    imageFlag.setImageResource(R.drawable.aud_chf);
                    return;
                }
                return;
            case -663434621:
                if (lowerCase.equals("aud jpy")) {
                    imageFlag.setImageResource(R.drawable.aud_jpy);
                    return;
                }
                return;
            case -663430488:
                if (lowerCase.equals("aud nzd")) {
                    imageFlag.setImageResource(R.drawable.aud_nzd);
                    return;
                }
                return;
            case -663423978:
                if (lowerCase.equals("aud usd")) {
                    imageFlag.setImageResource(R.drawable.aud_usd);
                    return;
                }
                return;
            case -166259963:
                if (lowerCase.equals("gbp aud")) {
                    imageFlag.setImageResource(R.drawable.gbp_aud);
                    return;
                }
                return;
            case -166258661:
                if (lowerCase.equals("gbp cad")) {
                    imageFlag.setImageResource(R.drawable.gbp_cad);
                    return;
                }
                return;
            case -166258442:
                if (lowerCase.equals("gbp chf")) {
                    imageFlag.setImageResource(R.drawable.gbp_chf);
                    return;
                }
                return;
            case -166251448:
                if (lowerCase.equals("gbp jpy")) {
                    imageFlag.setImageResource(R.drawable.gbp_jpy);
                    return;
                }
                return;
            case -166247315:
                if (lowerCase.equals("gbp nzd")) {
                    imageFlag.setImageResource(R.drawable.gbp_nzd);
                    return;
                }
                return;
            case -166240805:
                if (lowerCase.equals("gbp usd")) {
                    imageFlag.setImageResource(R.drawable.gbp_usd);
                    return;
                }
                return;
            case -150495700:
                if (lowerCase.equals("usd cad")) {
                    imageFlag.setImageResource(R.drawable.usd_cad);
                    return;
                }
                return;
            case -150495481:
                if (lowerCase.equals("usd chf")) {
                    imageFlag.setImageResource(R.drawable.usd_chf);
                    return;
                }
                return;
            case -150488487:
                if (lowerCase.equals("usd jpy")) {
                    imageFlag.setImageResource(R.drawable.usd_jpy);
                    return;
                }
                return;
            case 538982727:
                if (lowerCase.equals("cad chf")) {
                    imageFlag.setImageResource(R.drawable.cad_chf);
                    return;
                }
                return;
            case 538989721:
                if (lowerCase.equals("cad jpy")) {
                    imageFlag.setImageResource(R.drawable.cad_jpy);
                    return;
                }
                return;
            case 741240820:
                if (lowerCase.equals("chf jpy")) {
                    imageFlag.setImageResource(R.drawable.chf_jpy);
                    return;
                }
                return;
            case 2012408338:
                if (lowerCase.equals("xau usd")) {
                    imageFlag.setImageResource(R.drawable.xau_usd);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private final void setLang() {
        Context context = this;
        if (!Intrinsics.areEqual((Object) AppLangSessionManager.getLanguage(context), (Object) "")) {
            setLocale(AppLangSessionManager.getLanguage(context));
        }
        if (Intrinsics.areEqual((Object) "ar", (Object) AppLangSessionManager.getLanguage(context)) || Intrinsics.areEqual((Object) "fa", (Object) AppLangSessionManager.getLanguage(context))) {
            getWindow().getDecorView().setLayoutDirection(0);
        } else {
            getWindow().getDecorView().setLayoutDirection(0);
        }
    }

    public final void setLocale(String str) {
        Locale locale = new Locale(str);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);
    }

    private final void noInternetChecker() {
        this.noInternetDialog = new NoInternetDialog.Builder((Context) this).build();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        NoInternetDialog noInternetDialog2 = this.noInternetDialog;
        noInternetDialog2.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
