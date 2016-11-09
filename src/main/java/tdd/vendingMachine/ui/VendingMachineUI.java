package tdd.vendingMachine.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tdd.vendingMachine.model.Product;
import tdd.vendingMachine.service.IDisplayService;
import tdd.vendingMachine.service.IStateService;
import tdd.vendingMachine.service.exception.InvalidShelfException;

@Component
public class VendingMachineUI implements IVendingMachineUI {
    
    @Autowired
    private IDisplayService displayService;
    
    @Autowired
    private IStateService stateService;

    @Override
    public void selectShelf(Integer shelfNo) {
        if(shelfNo < 1) {
            displayService.print("Invalid shelfNo : " + shelfNo);
        }

        try {
            Product product = stateService.getProductOnShelf(shelfNo);
            displayService.print(product.getPrice().toPlainString());
        } catch (InvalidShelfException ise) {
            //TODO : add log4j
            ise.printStackTrace();
            displayService.print("Invalid shelfNo : " + shelfNo);
        }
    }
}
