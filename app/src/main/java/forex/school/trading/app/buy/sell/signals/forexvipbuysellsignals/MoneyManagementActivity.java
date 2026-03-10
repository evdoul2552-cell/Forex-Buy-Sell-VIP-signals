package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals;


import static forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.topbrokers.TopBrokerActivity.getHttpRequest;
import static forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.topbrokers.TopBrokerActivity.getJsonParser;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import io.github.rupinderjeet.kprogresshud.KProgressHUD;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.custom.Common_Banner_google;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.topbrokers.Config;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.topbrokers.HttpRequest;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.topbrokers.HttpStatus;


public class MoneyManagementActivity extends AppCompatActivity {
    public static final String TAG = MoneyManagementActivity.class.getName();
    ImageView back, setting;
    TextView title_name;
    private Spinner accountCurrencySpinner;
    private EditText accountSizeEditText;
    private TextView amountAtRisk;
    private Button calculateButton;
    private double contractSize = 1.0d;
    private boolean contractSizeInserted = false;
    private Spinner currencyPair;
    private DatabaseHandler databaseHandler;
    private List<String> differentContractSize = Arrays.asList("XAGAUD", "XAGEUR", "XAGUSD", "XAUAUD", "XAUCHF", "XAUEUR", "XAUGBP", "XAUUSD", "XPDUSD", "XPTUSD", "XBRUSD", "XTIUSD", "XNGUSD");
    private boolean firstTime = true;
    private boolean firstTradeSizeCalculate = true;
    private TextView moneyTextView;
    public SparseArray<MarketSymbolObject> oidToCalendarSymbol;
    private TextView positionSwitchButton;
    private TextView positionUnits;
    private ProgressBar progressBarSpinner;
    private EditText riskByRatio;
    private boolean riskRatioChecked = true;
    private TextView riskTextView;
    private ScrollView scrollView;
    private TextView standardLots;
    private EditText stopLossInPips;
    private TextView symbolName;
    private LinearLayout symbolNameLayout;
    private TextView symbolPrice;
    private EditText tradeSize;
    private Spinner tradeSizeType;
    public boolean hasResult = false;
    public static InterstitialAd interstitialAd;
    public static boolean adIsLoading;
    public KProgressHUD hud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_management);

        LinearLayout banner = (LinearLayout) findViewById(R.id.adView);
        Common_Banner_google common_bannerad = new Common_Banner_google();
        common_bannerad.GoogleBannerAds(getApplicationContext(), banner);

        this.hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Loading AD").setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f).show();

        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        InterstitialAd.load(this, getResources().getString(R.string.INTRESTITIAL_GGL_AD), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                MoneyManagementActivity.this.interstitialAd = interstitialAd;
                adIsLoading = false;
                Log.e(TAG, "onAdLoaded");
                if (interstitialAd != null) {
                    interstitialAd.show(MoneyManagementActivity.this);
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
                        MoneyManagementActivity.this.interstitialAd = null;
                        Log.e("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        MoneyManagementActivity.this.interstitialAd = null;
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

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

        title_name = findViewById(R.id.title_name);
        title_name.setText("Position Size Calculator");

        this.scrollView = (ScrollView) findViewById(R.id.scrollView);
        new CreateFragment().execute(new Void[0]);

    }

    public void share() {
        if (this.hasResult) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", "Check Out Position Size Calculator");
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.calculatorshare1, "Positions Size"));
            sb.append("\n");
            sb.append(getString(R.string.currencies));
            sb.append(" : ");
            sb.append(this.currencyPair.getSelectedItem().toString());
            sb.append("\n");
            sb.append(getString(R.string.accountcurrency));
            sb.append(" : ");
            sb.append(this.accountCurrencySpinner.getSelectedItem().toString());
            sb.append("\n");
            sb.append(getString(R.string.accountsize));
            sb.append(" : ");
            sb.append(this.accountSizeEditText.getText().toString());
            sb.append("\n");
            sb.append(getString(R.string.stoplossinpips));
            sb.append(" : ");
            sb.append(this.stopLossInPips.getText().toString());
            sb.append("\n");
            if (this.riskRatioChecked) {
                sb.append(getString(R.string.riskratio));
                sb.append(" : ");
                sb.append(this.riskByRatio.getText().toString());
                sb.append("\n");
            } else {
                sb.append(getString(R.string.positionmoney, "").replace(", ", ""));
                sb.append(" : ");
                sb.append(this.riskByRatio.getText().toString());
                sb.append("\n");
            }
            sb.append(getString(R.string.calculatorshare2));
            sb.append(" " + getString(R.string.units) + " ");
            sb.append(this.positionUnits.getText().toString());
            intent.putExtra("android.intent.extra.TEXT", sb.toString() + "\n\n Download App On Play Store : https://play.google.com/store/apps/details?id=" + getPackageName());
            startActivity(Intent.createChooser(intent, "Share via"));
            return;
        }
        Toast.makeText(MyApplication.getContext(), MyApplication.getContext().getString(R.string.pleaseenterdata), Toast.LENGTH_LONG).show();
    }

    public class CreateFragment extends AsyncTask<Void, Void, Void> {
        List<String> symbols;

        public CreateFragment() {
        }


        public Void doInBackground(Void... voidArr) {
            databaseHandler = DatabaseHandler.getInstance();
            oidToCalendarSymbol = databaseHandler.getAllMarketSymbolsAsSparse();
            List<String> symbolsArray = Utils.getSymbolsArray(databaseHandler.getAllMarketSymbols());
            this.symbols = symbolsArray;
            Collections.sort(symbolsArray);
            initView();
            return null;
        }


        public void onPostExecute(Void r3) {
            initial(this.symbols);
            super.onPostExecute((Void) r3);
        }
    }

    public void initView() {
        this.riskByRatio = (EditText) findViewById(R.id.riskByRatio);
        this.accountSizeEditText = (EditText) findViewById(R.id.accountSize);
        this.stopLossInPips = (EditText) findViewById(R.id.stopLossInPips);
        this.symbolName = (TextView) findViewById(R.id.symbolName);
        this.symbolPrice = (TextView) findViewById(R.id.symbolPrice);
        this.amountAtRisk = (TextView) findViewById(R.id.amountAtRisk);
        this.positionUnits = (TextView) findViewById(R.id.positionUnits);
        this.standardLots = (TextView) findViewById(R.id.standardLots);
        this.moneyTextView = (TextView) findViewById(R.id.moneyTextView);
        this.riskTextView = (TextView) findViewById(R.id.riskTextView);
        this.currencyPair = (Spinner) findViewById(R.id.currencyPair);
        this.accountCurrencySpinner = (Spinner) findViewById(R.id.accountCurrency);
        this.symbolNameLayout = (LinearLayout) findViewById(R.id.symbolNameLayout);
        this.tradeSize = (EditText) findViewById(R.id.tradeSize);
        this.tradeSizeType = (Spinner) findViewById(R.id.tradeSizeType);
        this.progressBarSpinner = (ProgressBar) findViewById(R.id.progressBarSpinner);
    }

    public void initial(List<String> list) {
        int properties = Utils.getProperties(SharedDef.PARAM_CALCULATOR_CURRENCY_ACCOUNT, -1);
        if (properties == -1) {
            properties = Arrays.asList(getResources().getStringArray(R.array.account_currency)).indexOf(Definitions.DEFAULT_SYMBOL);
        }
        this.accountCurrencySpinner.setSelection(properties);
        this.accountCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (!firstTime) {
                    Utils.saveProperties(SharedDef.PARAM_CALCULATOR_CURRENCY_ACCOUNT, accountCurrencySpinner.getSelectedItemPosition());
                    TextView textView = moneyTextView;
                    textView.setText(getString(R.string.positionmoney, accountCurrencySpinner.getSelectedItem().toString()));
                    resetResults();
                    return;
                }
                firstTime = false;
            }
        });
        this.moneyTextView.setText(getString(R.string.positionmoney, this.accountCurrencySpinner.getSelectedItem().toString()));
        this.progressBarSpinner.setVisibility(View.GONE);
        final ArrayAdapter arrayAdapter = new ArrayAdapter(MoneyManagementActivity.this, (int) R.layout.simple_spinner_item_custom, list);
        arrayAdapter.setDropDownViewResource(17367049);
        this.currencyPair.setAdapter((SpinnerAdapter) arrayAdapter);
        int position = arrayAdapter.getPosition(Utils.getProperties(SharedDef.PARAM_CALCULATOR_CURRENCY_PAIR, "EURUSD"));
//        String obj = this.currencyPair.getSelectedItem().toString();
        if (this.firstTradeSizeCalculate) {
            calculateContractSize(currencyPair.getSelectedItem().toString());
            EditText editText = this.tradeSize;
            editText.setText(this.contractSize + "");
            this.firstTradeSizeCalculate = false;
        }
        this.currencyPair.setSelection(position);
        this.currencyPair.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean firsTime = true;

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (!this.firsTime) {
                    Utils.saveProperties(SharedDef.PARAM_CALCULATOR_CURRENCY_PAIR, (String) arrayAdapter.getItem(i));
                    resetResults();
                }
                this.firsTime = false;
                if (!contractSizeInserted) {
                    calculateContractSize(currencyPair.getSelectedItem().toString());
                    EditText editText = tradeSize;
                    editText.setText(contractSize + "");
                }
            }
        });
        this.tradeSize.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                contractSizeInserted = true;
                return false;
            }
        });
        TextView textView = (TextView) findViewById(R.id.positionSwitchButton);
        this.positionSwitchButton = textView;
        textView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                riskRatioChecked = !riskRatioChecked;
                resetResults();
                switchRatioMoneyField();
            }
        });
        Button button = (Button) findViewById(R.id.calculate);
        this.calculateButton = button;
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String obj = currencyPair.getSelectedItem().toString();
                String substring = obj.substring(0, 3);
                String substring2 = obj.substring(3, 6);
                if (TextUtils.isEmpty(substring) || TextUtils.isEmpty(substring2)) {
                    Toast.makeText(MyApplication.getContext(), MyApplication.getContext().getString(R.string.errorpositionsize), Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(accountSizeEditText.getText().toString())) {
                    Toast.makeText(MyApplication.getContext(), MyApplication.getContext().getString(R.string.pleaseenterallvalues), Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(stopLossInPips.getText().toString())) {
                    Toast.makeText(MyApplication.getContext(), MyApplication.getContext().getString(R.string.pleaseenterallvalues), Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(riskByRatio.getText().toString())) {
                    Toast.makeText(MyApplication.getContext(), MyApplication.getContext().getString(R.string.pleaseenterallvalues), Toast.LENGTH_LONG).show();
                } else {
                    calculateButton.setEnabled(false);
                    new CalculatePositionAsyncTask(MoneyManagementActivity.this, currencyPair.getSelectedItem().toString()).execute(new String[0]);
                }
            }
        });
        findViewById(R.id.cleanAll).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                cleanAll();
            }
        });
        this.tradeSizeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                double d;
                String obj = tradeSize.getText().toString();
                if (!obj.equals("")) {
                    d = i == 0 ? Double.parseDouble(obj) / 100000.0d : Double.parseDouble(obj) * 100000.0d;
                } else {
                    d = 1.0d;
                }
                DecimalFormat decimalFormat = new DecimalFormat("0.#####");
                EditText editText = tradeSize;
                editText.setText(decimalFormat.format(d) + "");
            }
        });
        this.calculateButton.setEnabled(true);
    }


    private void cleanAll() {
        this.accountSizeEditText.setText("");
        this.stopLossInPips.setText("");
        this.riskByRatio.setText("");
        resetResults();
    }

    public class CalculatePositionAsyncTask extends AsyncTask<String, Void, Void> {
        private HttpStatus httpStatus = new HttpStatus();
        Map<String, LastTickObject> lastTickObjects;
        private String symbol;

        public CalculatePositionAsyncTask(Context context, String str) {
            this.symbol = str;
        }


        public void onPreExecute() {
            progressBarSpinner.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }


        public Void doInBackground(String... strArr) {
            this.lastTickObjects = getSymbolPriceAsMap(this.httpStatus, oidToCalendarSymbol, 1, true);
            return null;
        }


        public void onPostExecute(Void r21) {
            String str;
            String str2;
            String str3;
            super.onPostExecute((Void) r21);
            try {
                progressBarSpinner.setVisibility(View.GONE);
                double parseDouble = Double.parseDouble(accountSizeEditText.getText().toString());
                double parseDouble2 = Double.parseDouble(riskByRatio.getText().toString());
                double parseDouble3 = Double.parseDouble(stopLossInPips.getText().toString());
                String obj = accountCurrencySpinner.getSelectedItem().toString();
                String str4 = this.symbol;
                String substring = str4.substring(0, 3);
                String substring2 = str4.substring(3, 6);
                int i = oidToCalendarSymbol.get(this.lastTickObjects.get(this.symbol).symbolOid).decimals;
                if (riskRatioChecked) {
                    parseDouble2 = (parseDouble * parseDouble2) / 100.0d;
                    str2 = substring;
                    str = substring2;
                    amountAtRisk.setText(String.format("%s%s", Definitions.currencySymbols.get(obj), NumberFormat.getNumberInstance(Locale.US).format(Utils.round(parseDouble2, 2))));
                } else {
                    str2 = substring;
                    str = substring2;
                    amountAtRisk.setText(NumberFormat.getNumberInstance(Locale.US).format(Utils.round((parseDouble2 / parseDouble) * 100.0d, 2)) + "%");
                }
                double d = (parseDouble2 / parseDouble3) * ((double) (i <= 3 ? 100 : 20000));
                if (obj.equals(str2)) {
                    d *= this.lastTickObjects.get(str4).close;
                    str3 = str;
                } else {
                    str3 = str;
                    if (!obj.equals(str3)) {
                        LastTickObject lastTickObject = this.lastTickObjects.get(str3 + obj);
                        if (lastTickObject != null) {
                            d /= lastTickObject.close;
                        } else {
                            if (this.lastTickObjects.get(obj + str3) != null) {
                                d *= this.lastTickObjects.get(obj + str3).close;
                            } else {
                                d = Double.NaN;
                            }
                        }
                    }
                }
                symbolNameLayout.setVisibility(View.VISIBLE);
                double d2 = 1.0d;
                String str5 = "";
                if (obj.equals(str3) || Double.isNaN(d)) {
                    symbolNameLayout.setVisibility(View.GONE);
                } else {
                    if (this.lastTickObjects.get(obj + str3) != null) {
                        d2 = this.lastTickObjects.get(obj + str3).close;
                        str5 = obj + str3;
                    } else {
                        if (this.lastTickObjects.get(str3 + obj) != null) {
                            d2 = this.lastTickObjects.get(str3 + obj).close;
                            str5 = str3 + obj;
                        }
                    }
                }
                symbolName.setText(str5);
                symbolPrice.setText(String.format("%s", Double.valueOf(d2)));
                if (!Double.isNaN(d)) {
                    positionUnits.setText(NumberFormat.getNumberInstance(Locale.US).format(Utils.round(d, 0)));
                    standardLots.setText(NumberFormat.getNumberInstance(Locale.US).format(Utils.round(d / getTradeSize(scrollView), 2)));
                } else {
                    positionUnits.setText("NaN");
                    standardLots.setText("NaN");
                }
                hasResult = true;
                Utils.closeKeyboard(MoneyManagementActivity.this);
                scrollView.post(new Runnable() {

                    public void run() {
                        scrollView.fullScroll(130);
                    }
                });
                new CreateFragment().execute(new Void[0]);
            } catch (Exception e) {
                Log.d(MoneyManagementActivity.TAG, "error", e);
            }
        }
    }

    public double getTradeSize(View view) {
        double parseDouble = Double.parseDouble(((EditText) view.findViewById(R.id.tradeSize)).getText().toString());
        return ((Spinner) view.findViewById(R.id.tradeSizeType)).getSelectedItemPosition() == 0 ? parseDouble * 100000.0d : parseDouble;
    }


    public static Map<String, LastTickObject> getSymbolPriceAsMap(HttpStatus httpStatus, SparseArray<MarketSymbolObject> sparseArray, int i, boolean z) {
        String str = Config.URL_SERVER + "/get-last-quotes.json?timeframe=" + i;
        Log.d(TAG, str);
        HashMap hashMap = new HashMap();
        JsonParser jsonParser = null;
        try {
            HttpRequest httpRequest = getHttpRequest(str);
            ObjectMapper objectMapper = new ObjectMapper();
            jsonParser = getJsonParser(httpRequest, objectMapper, str);
            jsonParser.nextToken();
            while (jsonParser.nextToken() == JsonToken.START_OBJECT) {
                LastTickObject lastTickObject = (LastTickObject) objectMapper.readValue(jsonParser, LastTickObject.class);
                MarketSymbolObject marketSymbolObject = sparseArray.get(lastTickObject.symbolOid);
                if (marketSymbolObject != null) {
                    lastTickObject.symbolName = marketSymbolObject.symbol;
                    hashMap.put(lastTickObject.symbolName, lastTickObject);
                }
            }
            httpStatus.setOK();
        } catch (Exception e) {
            Log.e(TAG, "getCalendar", e);
            httpStatus.setError();
        } catch (Throwable th) {
            throw th;
        }
        return hashMap;
    }


    private void calculateContractSize(String str) {
        int i = this.tradeSizeType.getSelectedItemPosition() == 0 ? 100000 : 1;
        if (!this.differentContractSize.contains(str)) {
            this.contractSize = 100000.0d;
        } else if (str.substring(0, 3).equals("XAG")) {
            this.contractSize = 5000.0d;
        } else if (str.substring(0, 3).equals("XNG")) {
            this.contractSize = 10000.0d;
        } else {
            this.contractSize = 100.0d;
        }
        this.contractSize /= (double) i;
    }


    private void resetResults() {
        this.symbolPrice.setText("");
        this.amountAtRisk.setText("");
        this.positionUnits.setText("");
        this.standardLots.setText("");
    }


    private void switchRatioMoneyField() {
        if (!this.riskRatioChecked) {
            this.riskTextView.setText(getString(R.string.positionmoney, this.accountCurrencySpinner.getSelectedItem().toString()));
            this.moneyTextView.setText(getString(R.string.riskratio));
            this.positionSwitchButton.setText(getString(R.string.swapwithrisk));
            new CalculatePositionAsyncTask(MoneyManagementActivity.this, this.currencyPair.getSelectedItem().toString()).execute(new String[0]);
            return;
        }
        this.moneyTextView.setText(getString(R.string.positionmoney, this.accountCurrencySpinner.getSelectedItem().toString()));
        this.riskTextView.setText(getString(R.string.riskratio));
        this.positionSwitchButton.setText(getString(R.string.swapwithmoney));
        new CalculatePositionAsyncTask(MoneyManagementActivity.this, this.currencyPair.getSelectedItem().toString()).execute(new String[0]);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}