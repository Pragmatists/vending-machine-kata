package tdd.vendingMachine;

/**
 * @author Yevhen Sukhomud
 */
public class SccmController implements HardwareController {

    private Display display;
    private Inventory inventory;
    private Account account;
    private Bucket bucket;

    public SccmController(Display display, Inventory inventory, Account account, Bucket bucket) {
        this.display = display;
        this.inventory = inventory;
        this.account = account;
        this.bucket = bucket;
    }

    @Override
    public void processSelectionProductFlow(int num) {

    }

    @Override
    public void processTransferringPayment(double money) {

    }

    @Override
    public void cancel() {

    }

}
