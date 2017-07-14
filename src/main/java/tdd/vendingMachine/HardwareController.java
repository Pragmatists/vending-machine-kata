package tdd.vendingMachine;

/**
 * @author Yevhen Sukhomud
 */
public interface HardwareController {

    void processSelection(int index);

    void processPayment(Integer money);

    void cancel();

}
