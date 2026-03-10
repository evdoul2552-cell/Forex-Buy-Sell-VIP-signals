package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.topbrokers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.MyApplication;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.R;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.candlestick.DetailActivity;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.custom.Common_Banner_google;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.custom.Common_native_google;


public class BrokerDetailsActivity extends AppCompatActivity {
    public static final String ARG_OBJECT = "arg_object";
    public static final String TAG = BrokerDetailsActivity.class.getName();
    public static int TITLE_STATIC = 0;
    public static String lastTitle = "";
    public static int titleStatus = -1;
    private TopBrokerObject currentEvent;
    private View openLiveAccount;
    private SparseArray<PlaceholderFragment> placeholderFragments = new SparseArray<>();
    ImageView back, share;
    TextView title_name;
    public static InterstitialAd interstitialAd;
    public static boolean adIsLoading;
    public KProgressHUD hud;

    @Override
    // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, androidx.fragment.app.FragmentActivity, panthi.tech.app.forex.trading.signal.forexcalendercalculator.objects.CustomActionBarActivity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_broker_details);

        LinearLayout banner = (LinearLayout) findViewById(R.id.adView);
        Common_Banner_google common_bannerad = new Common_Banner_google();
        common_bannerad.GoogleBannerAds(getApplicationContext(), banner);

        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Loading AD").setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f).show();

        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        InterstitialAd.load((Context) BrokerDetailsActivity.this, getResources().getString(R.string.INTRESTITIAL_GGL_AD), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                BrokerDetailsActivity.this.interstitialAd = interstitialAd;
                adIsLoading = false;
                Log.e(TAG, "onAdLoaded");
                if (interstitialAd != null) {
                    interstitialAd.show(BrokerDetailsActivity.this);
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
                        BrokerDetailsActivity.this.interstitialAd = null;
                        Log.e("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        BrokerDetailsActivity.this.interstitialAd = null;
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

        final MutableInteger mutableInteger = new MutableInteger(getIntent().getIntExtra("position", 0));
        final ArrayList arrayList = (ArrayList) getIntent().getSerializableExtra("queries");
        TopBrokerObject topBrokerObject = (TopBrokerObject) arrayList.get(mutableInteger.value);
        this.currentEvent = topBrokerObject;
        String str = topBrokerObject.brokerName;
        lastTitle = str;
        titleStatus = TITLE_STATIC;

        title_name = findViewById(R.id.title_name);
        title_name.setText(str);

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
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                String string = getResources().getString(R.string.share_broker, currentEvent.url, "https://play.google.com/store/apps/details?id=panthi.tech.app.candle.stick.pattern.forex.trading.candlestickforextradinglearning");
                intent.putExtra("android.intent.extra.SUBJECT", getString(R.string.share_brokerheader, new Object[]{currentEvent.brokerName}));
                intent.putExtra("android.intent.extra.TEXT", "Broker Name : " + title_name.getText().toString() + "\n\n" + string);
                startActivity(Intent.createChooser(intent, getString(R.string.sharevia)));
            }
        });


        BrokerSectionsPagerAdapter brokerSectionsPagerAdapter = new BrokerSectionsPagerAdapter(getSupportFragmentManager(), this.placeholderFragments, arrayList);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(brokerSectionsPagerAdapter);
        viewPager.setCurrentItem(mutableInteger.value);
        View findViewById = findViewById(R.id.openLiveAccount);
        this.openLiveAccount = findViewById;
        findViewById.setOnClickListener(new View.OnClickListener() {
            /* class panthi.tech.app.forex.trading.signal.forexcalendercalculator.activities.topBrokers.BrokerDetailsActivity.AnonymousClass1 */

            public void onClick(View view) {
                HttpStatus httpStatus = new HttpStatus();
                String str = !TextUtils.isEmpty(BrokerDetailsActivity.this.currentEvent.mobileUrl) ? BrokerDetailsActivity.this.currentEvent.mobileUrl : BrokerDetailsActivity.this.currentEvent.url;
                TopBrokerActivity.updateBrokerStats(httpStatus, str, BrokerDetailsActivity.this.currentEvent.brokerOid);
                openWebView(str);
            }
        });
        new AtomicFloat().f = viewPager.getX();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /* class panthi.tech.app.forex.trading.signal.forexcalendercalculator.activities.topBrokers.BrokerDetailsActivity.AnonymousClass2 */

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                if (BrokerDetailsActivity.this.placeholderFragments.get(mutableInteger.value) != null) {
//                    MyApplication.sendAnalyticsScreenView((AppCompatActivity) BrokerDetailsActivity.this, BrokerDetailsActivity.TAG);
                    BrokerDetailsActivity.this.currentEvent = (TopBrokerObject) arrayList.get(i);
                    String str = BrokerDetailsActivity.this.currentEvent.brokerName;
                    mutableInteger.value = i;
                    title_name.setText(str);
                    BrokerDetailsActivity.lastTitle = str;
                }
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
                if (i == 0) {
                    title_name.setText(BrokerDetailsActivity.lastTitle);
                }
            }
        });
    }

    public static void openWebView(String str) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            intent.setFlags(268435456);
            MyApplication.getContext().startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
    }


    public static class BrokerSectionsPagerAdapter extends FragmentPagerAdapter {
        private SparseArray<PlaceholderFragment> placeholderFragments;
        private ArrayList<TopBrokerObject> qs;
        private int sections = 0;

        public BrokerSectionsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public BrokerSectionsPagerAdapter(FragmentManager fragmentManager, SparseArray<PlaceholderFragment> sparseArray, ArrayList<TopBrokerObject> arrayList) {
            super(fragmentManager);
            this.sections = arrayList.size();
            this.qs = arrayList;
            this.placeholderFragments = sparseArray;
        }

        @Override // androidx.fragment.app.FragmentPagerAdapter
        public Fragment getItem(int i) {
            PlaceholderFragment newInstance = PlaceholderFragment.newInstance(i, this.qs.get(i));
            this.placeholderFragments.put(i, newInstance);
            return newInstance;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return this.sections;
        }

        @Override
        // androidx.viewpager.widget.PagerAdapter, androidx.fragment.app.FragmentPagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            super.destroyItem(viewGroup, i, obj);
        }

        public Fragment getFragment(int i) {
            return this.placeholderFragments.get(i);
        }
    }

    public static void setZebraBackgroundColorParent(View view, int i) {
        try {
            if (i % 2 == 0) {
                ((ViewGroup) view.getParent()).setBackgroundResource(R.color.zebra1);
            } else {
                ((ViewGroup) view.getParent()).setBackgroundResource(R.color.zebra2);
            }
        } catch (Exception unused) {
        }
    }

    public static void setZebraBackgroundColor(View view, int i) {
        if (i % 2 == 0) {
            view.setBackgroundResource(R.color.zebra1);
        } else {
            view.setBackgroundResource(R.color.zebra2);
        }
    }


    public static String returnYesNo(boolean z) {
        return z ? "Yes" : "No";
    }

    public static String formatDoubleToStr(double d) {
        String replaceAll = String.format(Locale.US, "%f", Double.valueOf(d)).replaceAll("[0]*$", "");
        return (replaceAll.length() <= 0 || replaceAll.charAt(replaceAll.length() - 1) != '.') ? replaceAll : replaceAll.substring(0, replaceAll.length() - 1);
    }


    public static class PlaceholderFragment extends Fragment {
        public TextView accountCurrency;
        public TextView address;
        public TextView api;
        public TextView availability;
        public TextView bonuses;
        private TopBrokerObject brokerObject;
        public TextView brokerStatus;
        public TextView brokerType;
        public TextView commission;
        public TextView contests;
        public TextView country;
        public TextView decimals;
        public TextView demoAccount;
        public TextView email;
        public TextView fax;
        public TextView fwMethods;
        public TextView generalInformationBrokerText;
        public TextView generalInformationBrokerTitle;
        public TextView hedgingAllows;
        public TextView interestOfMargin;
        public TextView internationalOffices;
        public TextView languages;
        public TextView managedAccounts;
        public TextView maximalLeverage;
        public TextView maximumLotSize;
        public TextView minimumDeposit;
        public TextView minimumLotSize;
        public TextView mobileTrading;
        public TextView moneyManagers;
        public TextView numberOfEmployees;
        public TextView ocoOrders;
        public TextView oneClickTrading;
        public TextView operationSince;
        public TextView otherTradingInstruments;
        public TextView phone;
        public TextView regulation;
        public View rootView;
        public TextView scalpingAllowed;
        public TextView segregatedAccounts;
        public TextView spread;
        public TextView swapFreeAccounts;
        public TextView tradingOverPhone;
        public TextView tradingPlatforms;
        public TextView tradingPlatformsTimezone;
        public TextView trailingStops;
        public TextView usClients;
        public TextView webBasedTrading;
        Context context;

        public static PlaceholderFragment newInstance(int i, TopBrokerObject topBrokerObject) {
            PlaceholderFragment placeholderFragment = new PlaceholderFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("arg_object", topBrokerObject);
            placeholderFragment.setArguments(bundle);
            return placeholderFragment;
        }

        @Override // androidx.fragment.app.Fragment
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            if (this.brokerObject == null) {
                this.brokerObject = (TopBrokerObject) getArguments().getSerializable("arg_object");
            }
            View inflate = layoutInflater.inflate(R.layout.fragment_broker_details, viewGroup, false);
            this.rootView = inflate;
            context = getContext();
            TextView textView = (TextView) inflate.findViewById(R.id.brokerType);
            this.brokerType = textView;
            setZebraBackgroundColorParent(textView, 0);
            TextView textView2 = (TextView) this.rootView.findViewById(R.id.country);
            this.country = textView2;
            setZebraBackgroundColorParent(textView2, 1);
            TextView textView3 = (TextView) this.rootView.findViewById(R.id.operationSinceYear);
            this.operationSince = textView3;
            setZebraBackgroundColorParent(textView3, 2);
            TextView textView4 = (TextView) this.rootView.findViewById(R.id.numberOfEmployees);
            this.numberOfEmployees = textView4;
            setZebraBackgroundColorParent(textView4, 3);
            TextView textView5 = (TextView) this.rootView.findViewById(R.id.internationalOffices);
            this.internationalOffices = textView5;
            setZebraBackgroundColorParent(textView5, 4);
            TextView textView6 = (TextView) this.rootView.findViewById(R.id.regulation);
            this.regulation = textView6;
            setZebraBackgroundColorParent(textView6, 5);
            TextView textView7 = (TextView) this.rootView.findViewById(R.id.address);
            this.address = textView7;
            setZebraBackgroundColorParent(textView7, 6);
            TextView textView8 = (TextView) this.rootView.findViewById(R.id.brokerStatus);
            this.brokerStatus = textView8;
            setZebraBackgroundColorParent(textView8, 7);
            TextView textView9 = (TextView) this.rootView.findViewById(R.id.usClients);
            this.usClients = textView9;
            setZebraBackgroundColorParent(textView9, 8);
            TextView textView10 = (TextView) this.rootView.findViewById(R.id.accountcurrency);
            this.accountCurrency = textView10;
            setZebraBackgroundColorParent(textView10, 9);
            TextView textView11 = (TextView) this.rootView.findViewById(R.id.fwmethods);
            this.fwMethods = textView11;
            setZebraBackgroundColorParent(textView11, 10);
            TextView textView12 = (TextView) this.rootView.findViewById(R.id.swapfreeaccounts);
            this.swapFreeAccounts = textView12;
            setZebraBackgroundColorParent(textView12, 11);
            TextView textView13 = (TextView) this.rootView.findViewById(R.id.segregatedaccounts);
            this.segregatedAccounts = textView13;
            setZebraBackgroundColorParent(textView13, 12);
            TextView textView14 = (TextView) this.rootView.findViewById(R.id.interestofmargin);
            this.interestOfMargin = textView14;
            setZebraBackgroundColorParent(textView14, 13);
            TextView textView15 = (TextView) this.rootView.findViewById(R.id.managedaccounts);
            this.managedAccounts = textView15;
            setZebraBackgroundColorParent(textView15, 14);
            TextView textView16 = (TextView) this.rootView.findViewById(R.id.moneymanagers);
            this.moneyManagers = textView16;
            setZebraBackgroundColorParent(textView16, 15);
            TextView textView17 = (TextView) this.rootView.findViewById(R.id.phone);
            this.phone = textView17;
            setZebraBackgroundColorParent(textView17, 16);
            TextView textView18 = (TextView) this.rootView.findViewById(R.id.fax);
            this.fax = textView18;
            setZebraBackgroundColorParent(textView18, 17);
            TextView textView19 = (TextView) this.rootView.findViewById(R.id.email);
            this.email = textView19;
            setZebraBackgroundColorParent(textView19, 18);
            TextView textView20 = (TextView) this.rootView.findViewById(R.id.languages);
            this.languages = textView20;
            setZebraBackgroundColorParent(textView20, 19);
            TextView textView21 = (TextView) this.rootView.findViewById(R.id.availability);
            this.availability = textView21;
            setZebraBackgroundColorParent(textView21, 20);
            TextView textView22 = (TextView) this.rootView.findViewById(R.id.tradingplatforms);
            this.tradingPlatforms = textView22;
            setZebraBackgroundColorParent(textView22, 21);
            TextView textView23 = (TextView) this.rootView.findViewById(R.id.tradingplatformstimezone);
            this.tradingPlatformsTimezone = textView23;
            setZebraBackgroundColorParent(textView23, 22);
            TextView textView24 = (TextView) this.rootView.findViewById(R.id.demoaccounts);
            this.demoAccount = textView24;
            setZebraBackgroundColorParent(textView24, 23);
            TextView textView25 = (TextView) this.rootView.findViewById(R.id.mobiletrading);
            this.mobileTrading = textView25;
            setZebraBackgroundColorParent(textView25, 24);
            TextView textView26 = (TextView) this.rootView.findViewById(R.id.webbasedtrading);
            this.webBasedTrading = textView26;
            setZebraBackgroundColorParent(textView26, 25);
            TextView textView27 = (TextView) this.rootView.findViewById(R.id.api);
            this.api = textView27;
            setZebraBackgroundColorParent(textView27, 26);
            TextView textView28 = (TextView) this.rootView.findViewById(R.id.oco);
            this.ocoOrders = textView28;
            setZebraBackgroundColorParent(textView28, 27);
            TextView textView29 = (TextView) this.rootView.findViewById(R.id.tradingoverphone);
            this.tradingOverPhone = textView29;
            setZebraBackgroundColorParent(textView29, 28);
            TextView textView30 = (TextView) this.rootView.findViewById(R.id.hedgingallowed);
            this.hedgingAllows = textView30;
            setZebraBackgroundColorParent(textView30, 29);
            TextView textView31 = (TextView) this.rootView.findViewById(R.id.trailingstops);
            this.trailingStops = textView31;
            setZebraBackgroundColorParent(textView31, 30);
            TextView textView32 = (TextView) this.rootView.findViewById(R.id.oneclicktrading);
            this.oneClickTrading = textView32;
            setZebraBackgroundColorParent(textView32, 31);
            TextView textView33 = (TextView) this.rootView.findViewById(R.id.bonuses);
            this.bonuses = textView33;
            setZebraBackgroundColorParent(textView33, 32);
            TextView textView34 = (TextView) this.rootView.findViewById(R.id.contests);
            this.contests = textView34;
            setZebraBackgroundColorParent(textView34, 33);
            TextView textView35 = (TextView) this.rootView.findViewById(R.id.othertradinginstruments);
            this.otherTradingInstruments = textView35;
            setZebraBackgroundColorParent(textView35, 34);
            this.minimumDeposit = (TextView) this.rootView.findViewById(R.id.minimumdeposit);
            setZebraBackgroundColor(this.rootView.findViewById(R.id.minimumdepositlayout), 35);
            this.maximalLeverage = (TextView) this.rootView.findViewById(R.id.maximalleverage);
            setZebraBackgroundColor(this.rootView.findViewById(R.id.maximalleveragelayout), 36);
            this.minimumLotSize = (TextView) this.rootView.findViewById(R.id.minimumlotsize);
            setZebraBackgroundColor(this.rootView.findViewById(R.id.maximumlotsizelayout), 37);
            this.maximumLotSize = (TextView) this.rootView.findViewById(R.id.maximumlotsize);
            setZebraBackgroundColor(this.rootView.findViewById(R.id.maximumlotsizelayout), 38);
            this.commission = (TextView) this.rootView.findViewById(R.id.commission);
            setZebraBackgroundColor(this.rootView.findViewById(R.id.commissionlayout), 39);
            TextView textView36 = (TextView) this.rootView.findViewById(R.id.spread);
            this.spread = textView36;
            setZebraBackgroundColorParent(textView36, 40);
            TextView textView37 = (TextView) this.rootView.findViewById(R.id.decimals);
            this.decimals = textView37;
            setZebraBackgroundColorParent(textView37, 41);
            TextView textView38 = (TextView) this.rootView.findViewById(R.id.scalpingallowed);
            this.scalpingAllowed = textView38;
            setZebraBackgroundColorParent(textView38, 42);
            this.generalInformationBrokerTitle = (TextView) this.rootView.findViewById(R.id.generalInformationBrokerTitle);
            TextView textView39 = (TextView) this.rootView.findViewById(R.id.generalInformationBrokerText);
            this.generalInformationBrokerText = textView39;
            setZebraBackgroundColorParent(textView39, 43);
            this.brokerType.setText(this.brokerObject.brokerType);
            setZebraBackgroundColorParent(this.brokerType, 44);
            this.country.setText(this.brokerObject.country);
            setZebraBackgroundColorParent(this.country, 45);
            this.operationSince.setText(this.brokerObject.operationSince);
            setZebraBackgroundColorParent(this.operationSince, 46);
            this.numberOfEmployees.setText(String.valueOf(this.brokerObject.numberOfEmployers));
            setZebraBackgroundColorParent(this.numberOfEmployees, 47);
            this.internationalOffices.setText(this.brokerObject.officeCountryType);
            setZebraBackgroundColorParent(this.internationalOffices, 48);
            this.regulation.setText(this.brokerObject.regulationType);
            setZebraBackgroundColorParent(this.regulation, 49);
            this.address.setText(this.brokerObject.address);
            setZebraBackgroundColorParent(this.address, 50);
            this.brokerStatus.setText(String.valueOf(this.brokerObject.brokerStatus));
            setZebraBackgroundColorParent(this.brokerStatus, 51);
            this.usClients.setText(returnYesNo(this.brokerObject.acceptUSClients));
            setZebraBackgroundColorParent(this.usClients, 52);
            this.accountCurrency.setText(this.brokerObject.accountCurrencyType);
            setZebraBackgroundColorParent(this.accountCurrency, 53);
            this.fwMethods.setText(this.brokerObject.fundWithMethodType);
            setZebraBackgroundColorParent(this.fwMethods, 54);
            this.swapFreeAccounts.setText(returnYesNo(this.brokerObject.swapFreeAccount));
            setZebraBackgroundColorParent(this.swapFreeAccounts, 55);
            this.segregatedAccounts.setText(returnYesNo(this.brokerObject.segregated));
            setZebraBackgroundColorParent(this.segregatedAccounts, 56);
            this.interestOfMargin.setText(returnYesNo(this.brokerObject.interestedOnMargin));
            setZebraBackgroundColorParent(this.interestOfMargin, 57);
            this.managedAccounts.setText(returnYesNo(this.brokerObject.manageAccount));
            setZebraBackgroundColorParent(this.managedAccounts, 58);
            this.moneyManagers.setText(this.brokerObject.accountForMoneyManagers);
            setZebraBackgroundColorParent(this.moneyManagers, 59);
            this.phone.setText(this.brokerObject.phone);
            setZebraBackgroundColorParent(this.phone, 60);
            this.fax.setText(this.brokerObject.fax);
            setZebraBackgroundColorParent(this.fax, 61);
            this.email.setText(this.brokerObject.email);
            setZebraBackgroundColorParent(this.email, 62);
            this.languages.setText(this.brokerObject.supportedLanguages);
            setZebraBackgroundColorParent(this.languages, 63);
            this.availability.setText(this.brokerObject.availability);
            setZebraBackgroundColorParent(this.availability, 64);
            this.tradingPlatforms.setText(this.brokerObject.tradingPlatforms);
            setZebraBackgroundColorParent(this.tradingPlatforms, 65);
            this.tradingPlatformsTimezone.setText(this.brokerObject.tradingPlatformsTimezone);
            setZebraBackgroundColorParent(this.tradingPlatformsTimezone, 66);
            this.demoAccount.setText(returnYesNo(this.brokerObject.hasDemoAccount));
            setZebraBackgroundColorParent(this.demoAccount, 67);
            this.mobileTrading.setText(returnYesNo(this.brokerObject.mobileTrading));
            setZebraBackgroundColorParent(this.mobileTrading, 68);
            this.webBasedTrading.setText(returnYesNo(this.brokerObject.webBaseTrading));
            setZebraBackgroundColorParent(this.webBasedTrading, 69);
            this.api.setText(returnYesNo(this.brokerObject.api));
            setZebraBackgroundColorParent(this.api, 70);
            this.ocoOrders.setText(returnYesNo(this.brokerObject.oco));
            setZebraBackgroundColorParent(this.ocoOrders, 71);
            this.tradingOverPhone.setText(returnYesNo(this.brokerObject.tradingOverPhone));
            setZebraBackgroundColorParent(this.tradingOverPhone, 72);
            this.hedgingAllows.setText(returnYesNo(this.brokerObject.hedging));
            setZebraBackgroundColorParent(this.hedgingAllows, 73);
            this.trailingStops.setText(returnYesNo(this.brokerObject.trailingStops));
            setZebraBackgroundColorParent(this.trailingStops, 74);
            this.oneClickTrading.setText(returnYesNo(this.brokerObject.oneClickTrading));
            setZebraBackgroundColorParent(this.oneClickTrading, 75);
            this.bonuses.setText(returnYesNo(this.brokerObject.bonuses));
            setZebraBackgroundColorParent(this.bonuses, 76);
            this.contests.setText(returnYesNo(this.brokerObject.contests));
            setZebraBackgroundColorParent(this.contests, 77);
            this.otherTradingInstruments.setText(this.brokerObject.instrumentType);
            setZebraBackgroundColorParent(this.otherTradingInstruments, 78);
            this.minimumDeposit.setText(formatDoubleToStr(this.brokerObject.minimumDeposit));
            setZebraBackgroundColorParent(this.minimumDeposit, 79);
            this.maximalLeverage.setText(formatDoubleToStr((double) this.brokerObject.maxLeverage));
            setZebraBackgroundColorParent(this.maximalLeverage, 80);
            this.minimumLotSize.setText(formatDoubleToStr(this.brokerObject.minLotSize));
            setZebraBackgroundColorParent(this.minimumLotSize, 81);
            this.maximumLotSize.setText(formatDoubleToStr(this.brokerObject.maxLotSize));
            setZebraBackgroundColorParent(this.maximumLotSize, 82);
            this.commission.setText(formatDoubleToStr(this.brokerObject.commission));
            setZebraBackgroundColorParent(this.commission, 83);
            this.spread.setText(this.brokerObject.ecnSpreadType);
            setZebraBackgroundColorParent(this.spread, 84);
            this.decimals.setText(this.brokerObject.decimals);
            setZebraBackgroundColorParent(this.decimals, 85);
            this.scalpingAllowed.setText(this.brokerObject.scalpingAllow);
            setZebraBackgroundColorParent(this.scalpingAllowed, 86);
            this.generalInformationBrokerTitle.setText(getString(R.string.generalinformationbroker, this.brokerObject.brokerName));
            this.generalInformationBrokerText.setText(this.brokerObject.generalInformation);
            setZebraBackgroundColorParent(this.generalInformationBrokerText, 87);
            TextView textView40 = (TextView) this.rootView.findViewById(R.id.minimumdepositcomment);
            textView40.setText(Html.fromHtml("<i>" + this.brokerObject.minDepositComment + "</a>"));
            if (TextUtils.isEmpty(this.brokerObject.minDepositComment)) {
                textView40.setVisibility(View.GONE);
            }
            TextView textView41 = (TextView) this.rootView.findViewById(R.id.minimumlotsizecomment);
            textView41.setText(Html.fromHtml("<i>" + this.brokerObject.minLotComment + "</i>"));
            if (TextUtils.isEmpty(this.brokerObject.minLotComment)) {
                textView41.setVisibility(View.GONE);
            }
            TextView textView42 = (TextView) this.rootView.findViewById(R.id.maximumlotsizecomment);
            textView42.setText(Html.fromHtml("<i>" + this.brokerObject.maxLotComment + "</i>"));
            if (TextUtils.isEmpty(this.brokerObject.maxLotComment)) {
                textView42.setVisibility(View.GONE);
            }
            TextView textView43 = (TextView) this.rootView.findViewById(R.id.maximalleveragecomment);
            textView43.setText(Html.fromHtml("<i>" + this.brokerObject.maxLeverageComment + "</i>"));
            if (TextUtils.isEmpty(this.brokerObject.maxLeverageComment)) {
                textView43.setVisibility(View.GONE);
            }
            TextView textView44 = (TextView) this.rootView.findViewById(R.id.commissioncomment);
            textView44.setText(Html.fromHtml("<i>" + this.brokerObject.commissionComment + "</i>"));
            if (TextUtils.isEmpty(this.brokerObject.commissionComment)) {
                textView44.setVisibility(View.GONE);
            }

            LinearLayout fl_adplaceholder = (LinearLayout) rootView.findViewById(R.id.fl_adplaceholder);
            Common_native_google commonNativeGoogle = new Common_native_google();
            commonNativeGoogle.GoogleloadNativeAds((AppCompatActivity) context, fl_adplaceholder);

            LinearLayout fl_adplaceholder1 = (LinearLayout) rootView.findViewById(R.id.fl_adplaceholder1);
            Common_native_google commonNativeGoogle1 = new Common_native_google();
            commonNativeGoogle1.GoogleloadNativeAds((AppCompatActivity) context, fl_adplaceholder1);

            LinearLayout fl_adplaceholder2 = (LinearLayout) rootView.findViewById(R.id.fl_adplaceholder2);
            Common_native_google commonNativeGoogle2 = new Common_native_google();
            commonNativeGoogle2.GoogleloadNativeAds((AppCompatActivity) context, fl_adplaceholder2);

            ImageView brokerImage = inflate.findViewById(R.id.brokerImage);
            Picasso.get().load(brokerObject.imageUrl.replaceAll(" ", "%20")).error(R.drawable.exness).into(brokerImage);
            return this.rootView;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
