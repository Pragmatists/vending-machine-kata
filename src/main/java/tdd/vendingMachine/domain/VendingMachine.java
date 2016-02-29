package tdd.vendingMachine.domain;


import tdd.vendingMachine.external_interface.HardwareInterface;

public class VendingMachine {

    private static final String WELCOME_MESSAGE = "Welcome! Please choose product:";

    private HardwareInterface hardwareInterface;

    private Money[] pricesPerShelves;

    private PaymentRegistrar paymentRegistrar;

    private Integer chosenShelfNumber;

    public VendingMachine(HardwareInterface hardwareInterface, Money[] pricesPerShelves) {
        this.hardwareInterface = hardwareInterface;
        this.pricesPerShelves = pricesPerShelves;
        this.paymentRegistrar = new PaymentRegistrar();
        this.hardwareInterface.displayMessage(WELCOME_MESSAGE);
    }

    public void acceptChoice(int shelfNumber) {
        this.chosenShelfNumber = shelfNumber;
        if (this.chosenShelfNumber == 0 || shelfNumber > pricesPerShelves.length) {
            hardwareInterface.displayMessage("Invalid shelf choice. Please try again.");
        } else {
            Money productPrice = pricesPerShelves[shelfNumber - 1];
            hardwareInterface.displayMessage("Price: " + productPrice);
            paymentRegistrar.setAmountToBeCollected(productPrice);
        }
    }

    public void acceptCoin(Coin coin) {
        if (chosenShelfNumber == null) return;

        paymentRegistrar.register(coin.getDenomination());
        hardwareInterface.displayMessage("Remaining: " + paymentRegistrar.tellHowMuchMoreNeedsToBeCollected().toString());

        if (paymentRegistrar.hasSufficientMoneyBeenCollected()) {
            sellProduct();
        }
    }

    private void sellProduct() {
        hardwareInterface.disposeProduct(chosenShelfNumber);
        hardwareInterface.displayMessage(WELCOME_MESSAGE);
        paymentRegistrar.reset();
    }

    public void cancel() {
        hardwareInterface.disposeInsertedCoins();
        hardwareInterface.displayMessage(WELCOME_MESSAGE);
        paymentRegistrar.reset();
    }
}
