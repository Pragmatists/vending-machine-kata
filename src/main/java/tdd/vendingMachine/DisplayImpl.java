package tdd.vendingMachine;

/**
 * Created by dzalunin on 2017-01-25.
 */
public class DisplayImpl implements Display {

    private String msg;

    @Override
    public void show(String msg) {
        this.msg = msg;
    }

    @Override
    public String getLastMessage() {
        return msg;
    }
}
