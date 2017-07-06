package tdd.vendingMachine;

/**
 * @author Yevhen Sukhomud
 */
public class CoinHolder implements MoneyHolder {

    private HardwareController hardwareController;

    public CoinHolder(HardwareController hardwareController) {
        this.hardwareController = hardwareController;
    }

    @Override
    public void insert(double money) {
        hardwareController.processPayment(money);
    }

}
