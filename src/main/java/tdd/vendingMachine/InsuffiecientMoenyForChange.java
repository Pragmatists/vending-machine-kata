package tdd.vendingMachine;

/**
 * Created by dzalunin on 2017-01-25.
 */
public class InsuffiecientMoenyForChange extends Exception {

    public InsuffiecientMoenyForChange() {
    }

    public InsuffiecientMoenyForChange(String message) {
        super(message);
    }

    public InsuffiecientMoenyForChange(String message, Throwable cause) {
        super(message, cause);
    }

    public InsuffiecientMoenyForChange(Throwable cause) {
        super(cause);
    }

    public InsuffiecientMoenyForChange(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
