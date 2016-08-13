package tdd.vendingMachine.machine.purchase;

import com.google.common.collect.Maps;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import tdd.vendingMachine.machine.Machine;
import tdd.vendingMachine.machine.cli.util.CommandLinePrinter;
import tdd.vendingMachine.machine.purchase.enums.PurchaseStatus;
import tdd.vendingMachine.money.change.ChangeStorage;
import tdd.vendingMachine.money.coin.entity.Coin;
import tdd.vendingMachine.money.coin.factory.CoinFactory;
import tdd.vendingMachine.money.factory.MoneyFactory;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.shelve.entity.Shelve;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class PurchaseFacadeTest {

	private Machine machine;

	private ChangeStorage changeStorage;

	private CommandLinePrinter commandLinePrinter;

	private PurchaseFacade purchaseFacade;

	private Product product;

	Map<Coin, Integer> insertedCoins;

	@Before
	public void setup() {
		machine = mock(Machine.class);
		changeStorage = mock(ChangeStorage.class);
		commandLinePrinter = mock(CommandLinePrinter.class);
		purchaseFacade = new PurchaseFacade(machine, changeStorage, commandLinePrinter);
	}

	@Test
	public void returns_inserted_coins() {
		insertedCoins = Maps.newLinkedHashMap();
		insertedCoins.put(CoinFactory.create01(), 1);
		when(changeStorage.getInsertedCoins()).thenReturn(insertedCoins);

		Map<Coin, Integer> result = purchaseFacade.returnInsertedCoins();

		verify(changeStorage).returnInsertedCoins();
		Assertions.assertThat(result.get(CoinFactory.create01())).isEqualTo(1);
	}

	@Test
	public void inserts_coin() {
		Map<Coin, Integer> ownedCoins = Maps.newLinkedHashMap();
		ownedCoins.put(CoinFactory.create01(), 1);
		ownedCoins.put(CoinFactory.create02(), 2);
		when(changeStorage.getOwnedCoins()).thenReturn(ownedCoins);

		purchaseFacade.insertCoin(1);

		verify(changeStorage).insertCoin(CoinFactory.create02());
		verify(commandLinePrinter).print("Inserted USD 0.20");
	}

	@Test
	public void gets_INSUFFICIENT_FUNDS_status() {
		when(changeStorage.getInsertedCoins()).thenReturn(Maps.newLinkedHashMap());
		product = mock(Product.class);
		when(product.getPrice()).thenReturn(MoneyFactory.USD(1));
		Shelve shelve = mock(Shelve.class);
		when(shelve.getProduct()).thenReturn(product);
		when(machine.getActiveShelve()).thenReturn(shelve);

		Assertions.assertThat(purchaseFacade.getPurchaseStatus()).isEqualByComparingTo(PurchaseStatus.INSUFFICIENT_FUNDS);
	}

	@Test
	public void gets_NONBUYABLE_NO_CHANGE_status() {
		mock_NONBUYABLE_NO_CHANGE_status();

		Assertions.assertThat(purchaseFacade.getPurchaseStatus()).isEqualByComparingTo(PurchaseStatus.INSUFFICIENT_CHANGE);
	}

	@Test
	public void gets_BUYABLE_status() {
		mock_BUYABLE_status();

		Assertions.assertThat(purchaseFacade.getPurchaseStatus()).isEqualByComparingTo(PurchaseStatus.PURCHASABLE);
	}

	@Test
	public void gets_available_coins() {
		Map<Coin, Integer> ownedCoins = Maps.newLinkedHashMap();
		ownedCoins.put(CoinFactory.create01(), 1);
		ownedCoins.put(CoinFactory.create02(), 2);
		when(changeStorage.getOwnedCoins()).thenReturn(ownedCoins);

		List<Coin> availableCoins = purchaseFacade.getAvailableCoin();

		Assertions.assertThat(availableCoins).hasSize(2);
		Assertions.assertThat(availableCoins.get(0)).isEqualTo(CoinFactory.create01());
		Assertions.assertThat(availableCoins.get(1)).isEqualTo(CoinFactory.create02());
	}

	@Test
	public void nothing_is_bought_when_status_is_not_PURCHASABLE() {
		mock_NONBUYABLE_NO_CHANGE_status();

		purchaseFacade.buy();

		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

		verify(commandLinePrinter).print(argumentCaptor.capture());
		Assertions.assertThat(argumentCaptor.getValue()).containsSequence("Cannot buy.");
	}

	@Test
	public void buyable_product_is_bought_and_change_is_returned_using_inserted_coins() {
		mock_BUYABLE_status();
		final String productName = "productName";
		when(product.getName()).thenReturn(productName);
		when(product.getPrice()).thenReturn(MoneyFactory.USD(1));

		purchaseFacade.buy();

		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> argumentCaptorMap = ArgumentCaptor.forClass(Map.class);

		verify(commandLinePrinter).print(argumentCaptor.capture());
		Assertions.assertThat(argumentCaptor.getValue()).containsSequence("Purchased", "productName", "1.00");

		verify(changeStorage).setInsertedCoins(argumentCaptorMap.capture());
		Map<Coin, Integer> map = argumentCaptorMap.getValue();
		Assertions.assertThat(map.get(CoinFactory.create10())).isEqualTo(1);
	}

	@Test
	public void buyable_product_is_bought_and_change_is_returned_using_both_storages() {
		mock_BUYABLE_status();
		insertedCoins.clear();
		insertedCoins.put(CoinFactory.create05(), 2);

		final Map<Coin, Integer> ownedCoins = Maps.newLinkedHashMap();
		ownedCoins.put(CoinFactory.create02(), 10);
		when(changeStorage.getOwnedCoins()).thenReturn(ownedCoins);

		final String productName = "productName";
		when(product.getName()).thenReturn(productName);
		when(product.getPrice()).thenReturn(MoneyFactory.USD(.8));

		purchaseFacade.buy();

		ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> ownedCoinsArgumentCaptor = ArgumentCaptor.forClass(Map.class);
		ArgumentCaptor<Map> insertedCoinsArgumentCaptor = ArgumentCaptor.forClass(Map.class);

		verify(commandLinePrinter).print(stringArgumentCaptor.capture());
		Assertions.assertThat(stringArgumentCaptor.getValue()).containsSequence("Purchased", "productName", ".8");

		verify(changeStorage).setOwnedCoins(ownedCoinsArgumentCaptor.capture());
		verify(changeStorage).setInsertedCoins(insertedCoinsArgumentCaptor.capture());
		Map<Coin, Integer> ownedCoinsValue = ownedCoinsArgumentCaptor.getValue();
		Map<Coin, Integer> insertedCoinsValue = insertedCoinsArgumentCaptor.getValue();
		Assertions.assertThat(ownedCoinsValue.get(CoinFactory.create02())).isEqualTo(5);
		Assertions.assertThat(ownedCoinsValue.get(CoinFactory.create05())).isEqualTo(2);
		Assertions.assertThat(insertedCoinsValue.get(CoinFactory.create02())).isEqualTo(1);
	}

	private void mock_NONBUYABLE_NO_CHANGE_status() {
		insertedCoins = Maps.newLinkedHashMap();
		insertedCoins.put(CoinFactory.create02(), 3);
		when(changeStorage.getInsertedCoins()).thenReturn(insertedCoins);
		product = mock(Product.class);
		when(product.getPrice()).thenReturn(MoneyFactory.USD(.5));
		Shelve shelve = mock(Shelve.class);
		when(shelve.getProduct()).thenReturn(product);
		when(machine.getActiveShelve()).thenReturn(shelve);
	}

	private void mock_BUYABLE_status() {
		insertedCoins = Maps.newLinkedHashMap();
		insertedCoins.put(CoinFactory.create10(), 2);
		when(changeStorage.getInsertedCoins()).thenReturn(insertedCoins);
		product = mock(Product.class);
		when(product.getPrice()).thenReturn(MoneyFactory.USD(1));
		Shelve shelve = mock(Shelve.class);
		when(shelve.getProduct()).thenReturn(product);
		when(machine.getActiveShelve()).thenReturn(shelve);
	}

}
