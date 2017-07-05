package tdd.vendingMachine;

/**
 * @author Yevhen Sukhomud
 */
public interface HardwareController {

    void processSelectionProductFlow(int index);

    void processTransferringPayment(double money);

    void cancel();

}
