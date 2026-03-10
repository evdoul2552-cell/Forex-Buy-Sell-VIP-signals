package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.interfaces;


public interface TimeZoneAddedCallback {

    public static final class DefaultImpls {
        public static void timeZoneAdded(TimeZoneAddedCallback timeZoneAddedCallback) {
        }
    }

    void timeZoneAdded();
}
