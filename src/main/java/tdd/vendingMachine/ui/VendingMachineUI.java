package tdd.vendingMachine.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tdd.vendingMachine.model.Product;
import tdd.vendingMachine.service.IDisplayService;
import tdd.vendingMachine.service.IDisplayService.MessageType;
import tdd.vendingMachine.service.IDropService;
import tdd.vendingMachine.service.IMoneyService;
import tdd.vendingMachine.service.IVendingMachineStateService;
import tdd.vendingMachine.service.exception.CoinNotSupportedException;
import tdd.vendingMachine.service.exception.InvalidShelfException;
import tdd.vendingMachine.service.exception.NoMoneyForChangeException;

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
            displayService.print("Invalid shelfNo : " + shelfNo, MessageType.INFO);
        }

        try {
            stateService.selectShelf(shelfNo);

            Product product = stateService.getSelectedShelfProduct();
            displayService.print(product.getPrice().toPlainString(), MessageType.INFO);
        } catch (InvalidShelfException ise) {
            // TODO : add log4j
            ise.printStackTrace();
            displayService.print("Invalid shelfNo : " + shelfNo, MessageType.INFO);
        }
    }

    @Override
    public void putCoin(float denomination) {
        try {
            moneyService.putCoin(denomination);
            BigDecimal puttedSum = moneyService.getPuttedSum();
            displayService.print(stateService.getSelectedShelfProduct().getPrice().subtract(puttedSum)
                    .setScale(2, RoundingMode.HALF_UP).toPlainString(), MessageType.INFO);
        } catch (CoinNotSupportedException cnse) {
            // TODO : add log4j
            cnse.printStackTrace();
            dropService.addToDrop(denomination);
        }
    }

    //TODO: transaction
    @Override
    public void confirm() {
        BigDecimal puttedSum = moneyService.getPuttedSum();
        BigDecimal productPrice = stateService.getSelectedShelfProduct().getPrice();

        if (puttedSum.compareTo(productPrice) < 0) {
            return;
        }

        if (!moneyService.hasChange(productPrice)) {
            displayService.print("No change.", MessageType.WARN);
            dropService.addToDrop(moneyService.releasePuttedCoins());            
        } else {
            try {
                dropService.addToDrop(moneyService.getChange(productPrice));
                dropService.addToDrop(stateService.prepareReleaseSelectedProduct());
            } catch (NoMoneyForChangeException e) {
                //should never happen
                e.printStackTrace();
                displayService.print("Machine error. Call support.", MessageType.ERROR);
                dropService.addToDrop(moneyService.releasePuttedCoins());                
            }
        }
        
        dropService.confirm();
        moneyService.confirm();
        stateService.confirm();
    }

    @Override
    public void cancel() {
        dropService.addToDrop(moneyService.releasePuttedCoins());
        
        stateService.cancel();        
        dropService.confirm();
    }
}
