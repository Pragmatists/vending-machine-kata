package tdd.vendingMachine;

/**
 * @author Yevhen Sukhomud
 */
public interface HardwareController {

    void processSelectionProductFlow(int num);

    void processTransferringCoinToAccount(double coin);

    void cancel();

}
