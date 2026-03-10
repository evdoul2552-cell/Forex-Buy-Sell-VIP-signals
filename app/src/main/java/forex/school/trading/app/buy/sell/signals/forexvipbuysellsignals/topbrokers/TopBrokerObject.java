package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.topbrokers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


@JsonIgnoreProperties(ignoreUnknown = true)
public class TopBrokerObject implements Serializable {
    private static final String PARAM_ACCEPTING_US_CLIENTS = "auc";
    private static final String PARAM_ACCOUNTS_FOR_MONEY_MANAGERS = "afmm";
    private static final String PARAM_ACCOUNT_CURRENCY_TYPE = "ac";
    private static final String PARAM_ADDRESS = "ad";
    private static final String PARAM_API = "api";
    private static final String PARAM_AVAILABILITY_TYPE = "at";
    private static final String PARAM_BONUSES = "bon";
    private static final String PARAM_BROKER_NAME = "bn";
    private static final String PARAM_BROKER_OID = "bo";
    private static final String PARAM_BROKER_STATUS = "bs";
    private static final String PARAM_BROKER_TYPE = "bt";
    private static final String PARAM_COMMISSION = "comi";
    private static final String PARAM_COMMISSION_COMMENT = "comicmnt";
    private static final String PARAM_CONTESTS = "cont";
    private static final String PARAM_COUNTRY = "c";
    private static final String PARAM_DECIMALS = "deci";
    private static final String PARAM_DEMO_ACCOUNT = "da";
    private static final String PARAM_ECN_SPREAD_TYPE = "ecnS";
    private static final String PARAM_EMAIL = "em";
    private static final String PARAM_FAX = "fa";
    private static final String PARAM_FUND_WITHD_METHOD_TYPE = "fwm";
    private static final String PARAM_GENERAL_INFORMATION = "gi";
    private static final String PARAM_HEDGING = "hedg";
    private static final String PARAM_IMAGE_NAME = "in";
    private static final String PARAM_INSTRUMENT_TYPE = "it";
    private static final String PARAM_INTEREST_ON_MARGIN = "iom";
    private static final String PARAM_LOGO_URL = "lu";
    private static final String PARAM_MANAGE_ACCOUNT = "mnga";
    private static final String PARAM_MAX_LEVERAGE = "maxl";
    private static final String PARAM_MAX_LEVERAGE_COMMENT = "maxlcmnt";
    private static final String PARAM_MAX_LOT_SIZE = "maxlot";
    private static final String PARAM_MAX_LOT_SIZE_COMMENT = "maxlotcmnt";
    private static final String PARAM_MINIMUM_DEPOSIT = "md";
    private static final String PARAM_MINIMUM_DEPOSIT_COMMENT = "mdcmnt";
    private static final String PARAM_MIN_LOT_SIZE = "mlot";
    private static final String PARAM_MIN_LOT_SIZE_COMMENT = "mlotcmnt";
    private static final String PARAM_MOBILE_APP_URL = "mau";
    private static final String PARAM_MOBILE_TRADING = "mt";
    private static final String PARAM_NUMBER_OF_EMPLOYEES = "noe";
    private static final String PARAM_OCO = "oco";
    private static final String PARAM_OFFICE_COUNTRY_TYPE = "pct";
    private static final String PARAM_OID = "o";
    private static final String PARAM_ONE_CLICK_TRADING = "oct";
    private static final String PARAM_OPERATION_SINCE = "os";
    private static final String PARAM_PHONE = "ph";
    private static final String PARAM_REGULATION_TYPE = "rt";
    private static final String PARAM_SCALPING_ALLOW = "scalA";
    private static final String PARAM_SEGREGATED_ACCOUNTS = "sega";
    private static final String PARAM_SUPPORTED_LANGUAGE = "sl";
    private static final String PARAM_SWAP_FREE_ACCOUNT = "sfa";
    private static final String PARAM_TRADING_OVER_PHONE = "tof";
    private static final String PARAM_TRADING_PLATFORMS = "tp";
    private static final String PARAM_TRADING_PLATFORMS_TIMEZONE = "tpt";
    private static final String PARAM_TRAILING_STOPS = "tlst";
    private static final String PARAM_WEB_BASE_TRADING = "wbt";
    @JsonProperty(PARAM_ACCEPTING_US_CLIENTS)
    public boolean acceptUSClients = false;
    @JsonProperty("ac")
    public String accountCurrencyType;
    @JsonProperty(PARAM_ACCOUNTS_FOR_MONEY_MANAGERS)
    public String accountForMoneyManagers = "";
    @JsonProperty(PARAM_ADDRESS)
    public String address = "";
    @JsonProperty(PARAM_API)
    public boolean api = false;
    @JsonProperty(PARAM_AVAILABILITY_TYPE)
    public String availability = "";
    @JsonProperty(PARAM_BONUSES)
    public boolean bonuses = false;
    @JsonProperty(PARAM_BROKER_NAME)
    public String brokerName;
    @JsonProperty(PARAM_BROKER_OID)
    public int brokerOid = 0;
    @JsonProperty(PARAM_BROKER_STATUS)
    public String brokerStatus = "";
    @JsonProperty(PARAM_BROKER_TYPE)
    public String brokerType;
    @JsonProperty(PARAM_COMMISSION)
    public double commission = 999.0d;
    @JsonProperty(PARAM_COMMISSION_COMMENT)
    public String commissionComment = "";
    @JsonProperty(PARAM_CONTESTS)
    public boolean contests = false;
    @JsonProperty(PARAM_COUNTRY)
    public String country;
    @JsonProperty(PARAM_DECIMALS)
    public String decimals = "";
    @JsonProperty(PARAM_ECN_SPREAD_TYPE)
    public String ecnSpreadType = "";
    @JsonProperty("em")
    public String email = "";
    @JsonProperty(PARAM_FAX)
    public String fax = "";
    @JsonProperty(PARAM_FUND_WITHD_METHOD_TYPE)
    public String fundWithMethodType = "";
    @JsonProperty(PARAM_GENERAL_INFORMATION)
    public String generalInformation = "";
    @JsonProperty(PARAM_DEMO_ACCOUNT)
    public boolean hasDemoAccount;
    @JsonProperty(PARAM_HEDGING)
    public boolean hedging = false;
    public String imageUrl;
    @JsonProperty(PARAM_INSTRUMENT_TYPE)
    public String instrumentType = "";
    @JsonProperty(PARAM_INTEREST_ON_MARGIN)
    public boolean interestedOnMargin = false;
    @JsonProperty(PARAM_MANAGE_ACCOUNT)
    public boolean manageAccount = false;
    @JsonProperty(PARAM_MAX_LEVERAGE)
    public int maxLeverage = -1;
    @JsonProperty(PARAM_MAX_LEVERAGE_COMMENT)
    public String maxLeverageComment = "";
    @JsonProperty(PARAM_MAX_LOT_SIZE_COMMENT)
    public String maxLotComment = "";
    @JsonProperty(PARAM_MAX_LOT_SIZE)
    public double maxLotSize = 999.0d;
    @JsonProperty(PARAM_MINIMUM_DEPOSIT_COMMENT)
    public String minDepositComment = "";
    @JsonProperty(PARAM_MIN_LOT_SIZE_COMMENT)
    public String minLotComment = "";
    @JsonProperty(PARAM_MIN_LOT_SIZE)
    public double minLotSize = 999.0d;
    @JsonProperty(PARAM_MINIMUM_DEPOSIT)
    public double minimumDeposit;
    @JsonProperty(PARAM_MOBILE_TRADING)
    public boolean mobileTrading = false;
    @JsonProperty(PARAM_MOBILE_APP_URL)
    public String mobileUrl = "";
    @JsonProperty(PARAM_NUMBER_OF_EMPLOYEES)
    public int numberOfEmployers = -1;
    @JsonProperty(PARAM_OCO)
    public boolean oco = false;
    @JsonProperty(PARAM_OFFICE_COUNTRY_TYPE)
    public String officeCountryType = "";
    @JsonProperty("o")
    public int oid;
    @JsonProperty(PARAM_ONE_CLICK_TRADING)
    public boolean oneClickTrading = false;
    @JsonProperty(PARAM_OPERATION_SINCE)
    public String operationSince;
    @JsonProperty("ph")
    public String phone = "";
    @JsonProperty(PARAM_REGULATION_TYPE)
    public String regulationType = "";
    @JsonProperty(PARAM_SCALPING_ALLOW)
    public String scalpingAllow = "";
    @JsonProperty(PARAM_SEGREGATED_ACCOUNTS)
    public boolean segregated = false;
    @JsonProperty(PARAM_SUPPORTED_LANGUAGE)
    public String supportedLanguages;
    @JsonProperty(PARAM_SWAP_FREE_ACCOUNT)
    public boolean swapFreeAccount = false;
    @JsonProperty(PARAM_TRADING_OVER_PHONE)
    public boolean tradingOverPhone = false;
    @JsonProperty(PARAM_TRADING_PLATFORMS)
    public String tradingPlatforms;
    @JsonProperty(PARAM_TRADING_PLATFORMS_TIMEZONE)
    public String tradingPlatformsTimezone;
    @JsonProperty(PARAM_TRAILING_STOPS)
    public boolean trailingStops = false;
    public String url;
    @JsonProperty(PARAM_WEB_BASE_TRADING)
    public boolean webBaseTrading = false;


    @JsonProperty(PARAM_IMAGE_NAME)
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty(PARAM_IMAGE_NAME)
    public void setImageUrl(String imageUrl) {
        this.imageUrl = String.format("%s/images/brokers/%s", Config.URL_SERVER_STATIC, imageUrl);
        ;
    }

    @JsonProperty(PARAM_LOGO_URL)
    public void setUrl(String str) {
        try {
            this.url = URLDecoder.decode(str, HttpRequest.CHARSET_UTF8);
        } catch (UnsupportedEncodingException unused) {
            this.url = str;
        }
    }

}
