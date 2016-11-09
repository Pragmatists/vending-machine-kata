package tdd.vendingMachine.ui;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import tdd.vendingMachine.model.Product;
import tdd.vendingMachine.model.builder.ProductBuilder;
import tdd.vendingMachine.service.IDisplayService;
import tdd.vendingMachine.service.IDropService;
import tdd.vendingMachine.service.IMoneyService;
import tdd.vendingMachine.service.IVendingMachineStateService;
import tdd.vendingMachine.service.exception.CoinNotSupportedException;
import tdd.vendingMachine.service.exception.InvalidShelfException;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineUITest {

    @Mock
    private IDisplayService displayService;

    @Mock
    private IVendingMachineStateService stateService;
    
    @Mock
    private IMoneyService moneyService;
    
    @Mock
    private IDropService dropService;

    @InjectMocks
    private VendingMachineUI vendingMachineUI;

    @Test
    public void select_shelf_should_display_product_price_test() throws InvalidShelfException {
        // given
        final String priceAsString = "123.45";
        when(stateService.getSelectedShelfProduct())
                .thenReturn(new ProductBuilder().withPrice(new BigDecimal(priceAsString)).build());
        // when
        vendingMachineUI.selectShelf(1);
        // then
        verify(displayService, times(1)).print(priceAsString);
    }

    @Test
    public void select_shelf_without_product_test() throws InvalidShelfException {
        // given
        final int shelfNo = 10;
        doThrow(new InvalidShelfException()).when(stateService).selectShelf(shelfNo);
        // when
        vendingMachineUI.selectShelf(shelfNo);
        // then
        verify(displayService, times(1)).print("Invalid shelfNo : " + shelfNo);
    }

    @Test
    public void put_supported_coin() throws CoinNotSupportedException {
        // given
        Product product = mock(Product.class);
        final float denomination = 2.2f;
        final float price = 22f;
        when(moneyService.getPuttedSum()).thenReturn(new BigDecimal(denomination));
        when(stateService.getSelectedShelfProduct()).thenReturn(product);
        when(product.getPrice()).thenReturn(new BigDecimal(price));
        
        // when
        vendingMachineUI.putCoin(denomination);
        // then
        verifyZeroInteractions(dropService);
        verify(displayService, times(1)).print("19.80");
    }
    
    @Test
    public void put_unsupported_coin() throws CoinNotSupportedException {
        // given
        final float denomination = 2f;
        final String excMsg = "exc msg";
        doThrow(new CoinNotSupportedException(excMsg)).when(moneyService).putCoin(denomination);
        // when
        vendingMachineUI.putCoin(denomination);
        // then
        verify(dropService, times(1)).dropUnknownDenomination(denomination);
    }
}
