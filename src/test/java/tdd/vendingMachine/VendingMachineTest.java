package tdd.vendingMachine;

import static tdd.vendingMachine.message.VendingMachineMessages.*;
import static tdd.vendingMachine.money.Coin.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import tdd.vendingMachine.listener.VendingMachineChangeListener;
import tdd.vendingMachine.listener.VendingMachineDispenserListener;
import tdd.vendingMachine.listener.VendingMachineDisplayListener;
import tdd.vendingMachine.money.Money;
import tdd.vendingMachine.product.StandardVendingProduct;
import tdd.vendingMachine.product.VendingProduct;

public class VendingMachineTest {

	@Mock
	private VendingMachineDisplayListener displayMock;

	@Mock
	private VendingMachineChangeListener changeMock;

	@Mock
	private VendingMachineDispenserListener dispenserMock;

	private VendingMachine testMachine;

	private static List<VendingProduct> testProducts = new ArrayList<VendingProduct>();
	private static List<Money> testMoney = new ArrayList<Money>();

	@BeforeClass
	public static void initTestData() {
		testProducts.add(new StandardVendingProduct(5.5f, "Cola drink 0.25l"));
		testProducts.add(new StandardVendingProduct(3f, "Chocolate bar"));
		testProducts.add(new StandardVendingProduct(1.1f, "mineral water 0.33l"));

		testMoney.add(COIN_0_1);
		testMoney.add(COIN_1);
		testMoney.add(COIN_0_2);
		testMoney.add(COIN_0_2);
	}

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
		testMachine = new StandardVendingMachine();
		testMachine.addProductsToStorage(testProducts);
		testMachine.addMoneyToStorage(testMoney);
		testMachine.addVendingMachineDisplayListener(displayMock);
		testMachine.addVendingMachineChangeListener(changeMock);
		testMachine.addVendingMachineDispenserListener(dispenserMock);
	}

	@Test
	public void testChooseShelf() {
		testMachine.chooseProductShelf(0);
		Mockito.verify(displayMock).newMessageShown(PRODUCT_PRICE + "5.5");
		Mockito.verifyZeroInteractions(changeMock);
		Mockito.verifyZeroInteractions(dispenserMock);
	}

	@Test
	public void testPutCoin() {
		testMachine.chooseProductShelf(1);
		testMachine.putMoney(COIN_1);
		Mockito.verify(displayMock).newMessageShown(REMAINING_AMOUNT + "2.0");
		Mockito.verifyZeroInteractions(changeMock);
		Mockito.verifyZeroInteractions(dispenserMock);
	}

	@Test
	public void testPurchaseNoChange() {
		testMachine.chooseProductShelf(2);
		testMachine.putMoney(COIN_1);
		testMachine.putMoney(COIN_0_1);
		Mockito.verify(displayMock).newMessageShown(PRODUCT_PURCHASED);
		Mockito.verifyZeroInteractions(changeMock);
		Mockito.verify(dispenserMock).productDelivered(testProducts.get(2));
	}

	@Test
	public void testPurchaseWithChange() {
		testMachine.chooseProductShelf(2);
		testMachine.putMoney(COIN_1);
		testMachine.putMoney(COIN_0_5);
		// change is 0.4, we need to return 2x0.2 coin
		Mockito.verify(displayMock).newMessageShown(PRODUCT_PURCHASED);
		Mockito.verify(changeMock, Mockito.times(2)).changeReturned(COIN_0_2);
		Mockito.verify(dispenserMock).productDelivered(testProducts.get(2));
	}

	@Test
	public void testCancelPurchase() {
		testMachine.chooseProductShelf(1);
		testMachine.putMoney(COIN_0_1);
		testMachine.putMoney(COIN_2);
		testMachine.cancelPurchase();
		Mockito.verify(displayMock).newMessageShown(PURCHASE_CANCELLED);
		Mockito.verify(changeMock).changeReturned(COIN_0_1);
		Mockito.verify(changeMock).changeReturned(COIN_2);
		Mockito.verifyZeroInteractions(dispenserMock);
	}

	@Test
	public void testPurchaseCannotReturnChange() {
		testMachine.chooseProductShelf(2);
		testMachine.putMoney(COIN_5);
		// change is 3.9, there is no available coins
		Mockito.verify(displayMock).newMessageShown(CHANGE_NOT_AVAILABLE);
		Mockito.verify(changeMock).changeReturned(COIN_5);
		Mockito.verifyZeroInteractions(dispenserMock);
	}
	
	@Test
	public void testComplexPurchase() {
		testMachine.chooseProductShelf(0);
		testMachine.putMoney(COIN_2);
		testMachine.putMoney(COIN_1);
		testMachine.putMoney(COIN_0_5);
		testMachine.putMoney(COIN_0_1);
		testMachine.putMoney(COIN_0_1);
		testMachine.putMoney(COIN_0_2);
		testMachine.putMoney(COIN_5);
		// inserted 8.9, product costs 5.5, need to return total 3.4
		Mockito.verify(displayMock).newMessageShown(PRODUCT_PURCHASED);
		Mockito.verify(changeMock).changeReturned(COIN_2);
		Mockito.verify(changeMock).changeReturned(COIN_1);
		Mockito.verify(changeMock, Mockito.times(2)).changeReturned(COIN_0_2);
		Mockito.verify(dispenserMock).productDelivered(testProducts.get(0));
	}
}
