package tdd.vendingMachine;

/**
 * Created by dzalunin on 2017-01-25.
 */
public class ShelveAlreadyIsBusy extends Exception {
    public ShelveAlreadyIsBusy() {
    }

    public ShelveAlreadyIsBusy(String message) {
        super(message);
    }

    public ShelveAlreadyIsBusy(String message, Throwable cause) {
        super(message, cause);
    }

    public ShelveAlreadyIsBusy(Throwable cause) {
        super(cause);
    }

    public ShelveAlreadyIsBusy(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
