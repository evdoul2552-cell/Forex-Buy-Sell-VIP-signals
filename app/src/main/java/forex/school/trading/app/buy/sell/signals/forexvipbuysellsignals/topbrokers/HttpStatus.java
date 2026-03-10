package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.topbrokers;


public class HttpStatus {
    private String response;
    private int status;

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public String getResponse() {
        return this.response;
    }

    public void setResponse(String str) {
        this.response = str;
    }

    public boolean isOk() {
        return this.status == 200;
    }

    public void setOK() {
        this.status = 200;
    }

    public void setError() {
        this.status = 400;
    }
}
