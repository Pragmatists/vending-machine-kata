package tdd.vendingMachine;


import tdd.vendingMachine.domain.Coins;
import tdd.vendingMachine.domain.Money;
import tdd.vendingMachine.domain.ProductWithChange;

public class DispenseProductWithChangeState extends VendingMachineState {

    private static final String FINISHED_MESSAGE = "Zakończono pracę, odbierz produkt.";

    protected DispenseProductWithChangeState(VendingMachine vendingMachine) {
        super(vendingMachine);
        vendingMachine.display.displayText(FINISHED_MESSAGE);
    }

    @Override
    protected ProductWithChange dispense() {
        Money moneyToChange = calculateMoneyToChange();
        Coins bestFitOfCoinsToChange = takeBestFitCoinsToChange(moneyToChange);
        return returnProductWithChange(bestFitOfCoinsToChange);
    }

    private Money calculateMoneyToChange() {
        return vendingMachine.calculator.calculateMoneyToChange(vendingMachine.credit, vendingMachine.shelfs.getProductPrice());
    }

    private Coins takeBestFitCoinsToChange(Money moneyToChange) {
        Coins bestFitOfCoinsToChange = vendingMachine.calculator.calculateBestFitOfCoinsToChange(vendingMachine.coinBin, moneyToChange);
        vendingMachine.coinBin.take(bestFitOfCoinsToChange);
        return bestFitOfCoinsToChange;
    }

    private ProductWithChange returnProductWithChange(Coins bestFitOfCoinsToChange) {
        ProductWithChange productWithChange = new ProductWithChange(vendingMachine.shelfs.takeProduct(), bestFitOfCoinsToChange);
        vendingMachine.currentState = new MachineReadyInitialState(vendingMachine);
        return productWithChange;
    }

}
