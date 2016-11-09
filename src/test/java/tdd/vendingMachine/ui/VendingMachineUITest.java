package tdd.vendingMachine.ui;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import tdd.vendingMachine.model.builder.ProductBuilder;
import tdd.vendingMachine.service.IDisplayService;
import tdd.vendingMachine.service.IStateService;
import tdd.vendingMachine.service.exception.InvalidShelfException;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineUITest {

    @Mock
    private IDisplayService displayService;
    
    @Mock
    private IStateService stateService;

    @InjectMocks
    @Autowired
    private VendingMachineUI vendingMachineUI;
    
    @Test
    public void select_shelf_should_display_product_price_test() throws InvalidShelfException {
        // given
        final String priceAsString = "123.45";
        when(stateService.getProductOnShelf(1)).thenReturn(new ProductBuilder().withPrice(new BigDecimal(priceAsString)).build());
        // when
        vendingMachineUI.selectShelf(1);
        // then
        verify(displayService, times(1)).print(priceAsString);
    }

    @Test
    public void select_shelf_without_product_test() throws InvalidShelfException {
        // given
        final int shelfNo = 10;
        when(stateService.getProductOnShelf(shelfNo)).thenThrow(new InvalidShelfException());
        // when
        vendingMachineUI.selectShelf(shelfNo);
        // then
        verify(displayService, times(1)).print("Invalid shelfNo : " + shelfNo);
    }
}
