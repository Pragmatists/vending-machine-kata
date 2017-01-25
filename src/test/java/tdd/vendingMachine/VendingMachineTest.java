package tdd.vendingMachine;

import org.junit.Test;

import java.util.EnumMap;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class VendingMachineTest {

    @Test
    public void testSelectPrdductSuccess() throws ShelveAlreadyIsBusy {
        Shelves shelves = new Shelves();
        Charger charger = new Charger();
        Display display = new DisplayImpl();
        shelves.supplyProduct(1, Product.COCA_COLA, 10);
        charger.supplyMoney(Denomination.TWO, 10);
        VendingMachine vendingMachine = new VendingMachine(charger, shelves, display);

        boolean status = vendingMachine.selectShelve(1);
        assertThat(status).isTrue();
        assertThat(display.getLastMessage()).isEqualTo(Display.PRODUCT_PRICE_MSG);
    }

    @Test
    public void testSelectShelveFailure() throws Exception {
        Shelves shelves = new Shelves();
        Display display = new DisplayImpl();
        Charger charger = new Charger();
        charger.supplyMoney(Denomination.TWO, 10);
        VendingMachine vendingMachine = new VendingMachine(charger, shelves, display);

        boolean status = vendingMachine.selectShelve(1);
        assertThat(status).isFalse();
        assertThat(display.getLastMessage()).isEqualTo(Display.WELCOME_MSG);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuyProductIllegalStateException() throws Exception {
        Charger charger = new Charger();
        Shelves shelves = new Shelves();
        Display display = new DisplayImpl();
        VendingMachine vendingMachine = new VendingMachine(charger, shelves, display);
        vendingMachine.buyProduct(Denomination.FIFTY_CENTS);
    }

    @Test
    public void testBuyProductNotEnoughMoney() throws Exception {
        Charger charger = new Charger();
        Shelves shelves = new Shelves();
        int shelveNo = 1;
        shelves.supplyProduct(shelveNo, Product.COCA_COLA, 10);

        Display display = new DisplayImpl();
        VendingMachine vendingMachine = new VendingMachine(charger, shelves, display);

        vendingMachine.selectShelve(shelveNo);
        Result result = vendingMachine.buyProduct(Denomination.FIFTY_CENTS);
        long remains = vendingMachine.remainsToPay(Product.COCA_COLA.getPrice());
        assertThat(result).isNull();
        assertThat(display.getLastMessage()).isEqualTo(Display.REMAINS_TO_PAY_MSG + remains);
    }

    @Test
    public void testBuyProductSuccess() throws ShelveAlreadyIsBusy, InsuffiecientMoenyForChange {
        Charger charger = new Charger();
        charger.supplyMoney(Denomination.TWO, 1);

        Shelves shelves = new Shelves();
        int shelveNo = 1;
        shelves.supplyProduct(shelveNo, Product.COCA_COLA, 1);

        Display display = new DisplayImpl();
        VendingMachine vendingMachine = new VendingMachine(charger, shelves, display);
        vendingMachine.selectShelve(shelveNo);
        Result result = vendingMachine.buyProduct(Denomination.FIVE);
        EnumMap<Denomination, Integer> actualChange = result.getChange();
        Product actualProduct = result.getProduct();

        EnumMap<Denomination, Integer> expectedChange = new EnumMap<>(Denomination.class);
        expectedChange.put(Denomination.TWO, 1);

        assertThat(actualProduct).isEqualTo(Product.COCA_COLA);
        assertThat(actualChange).isEqualTo(expectedChange);
    }

    @Test
    public void testBuyProductInsufficientMoenyForChange() throws ShelveAlreadyIsBusy {
        Charger charger = new Charger();

        Shelves shelves = new Shelves();
        int shelveNo = 1;
        shelves.supplyProduct(shelveNo, Product.COCA_COLA, 1);

        Display display = new DisplayImpl();
        VendingMachine vendingMachine = new VendingMachine(charger, shelves, display);
        vendingMachine.selectShelve(shelveNo);
        try {
            vendingMachine.buyProduct(Denomination.FIVE);
        } catch (InsuffiecientMoenyForChange insuffiecientMoenyForChange) {
            assertThat(display.getLastMessage()).isEqualTo(Display.WARNING_MSG);
        }
    }

    @Test
    public void testCancelSuccess() throws Exception {
        Charger charger = new Charger();
        Shelves shelves = new Shelves();
        int shelveNo = 1;
        shelves.supplyProduct(shelveNo, Product.KROPLA_BESKIDU, 2);
        Display display = new DisplayImpl();
        VendingMachine vendingMachine = new VendingMachine(charger, shelves, display);
        vendingMachine.selectShelve(shelveNo);
        Result result = vendingMachine.buyProduct(Denomination.TEN_CENTS);
        assertThat(result).isNull();
        result = vendingMachine.buyProduct(Denomination.TWENTY_CENTS);
        assertThat(result).isNull();
        result = vendingMachine.buyProduct(Denomination.FIFTY_CENTS);
        assertThat(result).isNull();

        EnumMap<Denomination, Integer> actualChange = vendingMachine.cancel();
        assertThat(display.getLastMessage()).isEqualTo(Display.WELCOME_MSG);
        EnumMap<Denomination, Integer> expectedChange = new EnumMap<Denomination, Integer>(Denomination.class);
        expectedChange.put(Denomination.TEN_CENTS, 1);
        expectedChange.put(Denomination.TWENTY_CENTS, 1);
        expectedChange.put(Denomination.FIFTY_CENTS, 1);
        assertThat(actualChange).isEqualTo(expectedChange);
    }
}







