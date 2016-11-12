package tdd.vendingMachine.domain.display;

public class Display {

    private String message;

    public String getMessage() {
        return message;
    }

    public Display setMessage(String message) {
        this.message = message;

        return this;
    }
}
