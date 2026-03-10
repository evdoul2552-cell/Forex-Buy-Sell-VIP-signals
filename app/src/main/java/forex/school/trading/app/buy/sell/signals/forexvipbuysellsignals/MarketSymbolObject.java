package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketSymbolObject {
    private static final String PARAM_DECIMALS = "d";
    private static final String PARAM_OID = "o";
    private static final String PARAM_SYMBOL_NAME = "s";
    public boolean activated = false;
    @JsonProperty("d")
    public int decimals;
    public String indicators = "";
    @JsonProperty("o")
    public int oid;
    public int order = -1;
    @JsonProperty(PARAM_SYMBOL_NAME)
    public String symbol;

    public MarketSymbolObject() {
    }

    public MarketSymbolObject(int i, int i2, boolean z) {
        this.oid = i;
        this.order = i2;
    }
}
