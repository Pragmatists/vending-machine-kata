package tdd.vendingMachine.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tdd.vendingMachine.model.Product;
import tdd.vendingMachine.service.IDisplayService;
import tdd.vendingMachine.service.IDropService;
import tdd.vendingMachine.service.IMoneyService;
import tdd.vendingMachine.service.IVendingMachineStateService;
import tdd.vendingMachine.service.exception.CoinNotSupportedException;
import tdd.vendingMachine.service.exception.InvalidShelfException;

@Component
public class VendingMachineUI implements IVendingMachineUI {

    @Autowired
    private IDisplayService displayService;

    @Autowired
    private IVendingMachineStateService stateService;

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
            stateService.selectShelf(shelfNo);

            Product product = stateService.getSelectedShelfProduct();
            displayService.print(product.getPrice().toPlainString());
        } catch (InvalidShelfException ise) {
            // TODO : add log4j
            ise.printStackTrace();
            displayService.print("Invalid shelfNo : " + shelfNo);
        }
    }

    @Override
    public void putCoin(float denomination) {
        try {
            moneyService.putCoin(denomination);
            BigDecimal puttedSum = moneyService.getPuttedSum();
            displayService.print(stateService.getSelectedShelfProduct().getPrice().subtract(puttedSum)
                    .setScale(2, RoundingMode.HALF_UP).toPlainString());
        } catch (CoinNotSupportedException cnse) {
            // TODO : add log4j
            cnse.printStackTrace();
            dropService.dropUnknownDenomination(denomination);
        }

    }
}
