package tdd.vendingMachine.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tdd.vendingMachine.model.Product;
import tdd.vendingMachine.service.IDisplayService;
import tdd.vendingMachine.service.IDropService;
import tdd.vendingMachine.service.IMoneyService;
import tdd.vendingMachine.service.IMoneyService.SupportedCoins;
import tdd.vendingMachine.service.IStateService;
import tdd.vendingMachine.service.exception.CoinNotSupportedException;
import tdd.vendingMachine.service.exception.InvalidShelfException;

@Component
public class VendingMachineUI implements IVendingMachineUI {

    @Autowired
    private IDisplayService displayService;

    @Autowired
    private IStateService stateService;

    @Autowired
    private IMoneyService moneyService;
    
    @Autowired
    private IDropService dropService;

    @Override
    public void selectShelf(Integer shelfNo) {
        if (shelfNo < 1) {
            displayService.print("Invalid shelfNo : " + shelfNo);
        }

        try {
            Product product = stateService.getProductOnShelf(shelfNo);
            displayService.print(product.getPrice().toPlainString());
        } catch (InvalidShelfException ise) {
            // TODO : add log4j
            ise.printStackTrace();
            displayService.print("Invalid shelfNo : " + shelfNo);
        }
    }

    @Override
    public void putCoin(float denomination) {
        SupportedCoins supportedCoin;
        try {
            supportedCoin = moneyService.getCoinType(denomination);
        } catch (CoinNotSupportedException cnse) {
            // TODO : add log4j
            cnse.printStackTrace();
            dropService.dropUnknownDenomination(denomination);
        }

    }

    /**
     * Not supported coin is returned
     * @param denomination
     */
    private void dropPuttedDenomination(float denomination) { }
}
