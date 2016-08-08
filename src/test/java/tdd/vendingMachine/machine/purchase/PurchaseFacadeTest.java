package tdd.vendingMachine.machine.purchase;

import com.google.common.collect.Maps;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
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

	@Before
	public void setup() {
		machine = mock(Machine.class);
		changeStorage = mock(ChangeStorage.class);
		commandLinePrinter = mock(CommandLinePrinter.class);
		purchaseFacade = new PurchaseFacade(machine, changeStorage, commandLinePrinter);
	}

	@Test
	public void returns_inserted_coins() {
		Map<Coin, Integer> insertedCoins = Maps.newLinkedHashMap();
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
		Product product = mock(Product.class);
		when(product.getPrice()).thenReturn(MoneyFactory.USD(1));
		Shelve shelve = mock(Shelve.class);
		when(shelve.getProduct()).thenReturn(product);
		when(machine.getActiveShelve()).thenReturn(shelve);

		Assertions.assertThat(purchaseFacade.getPurchaseStatus()).isEqualByComparingTo(PurchaseStatus.INSUFFICIENT_FUNDS);
	}

	@Test
	public void gets_NONBUYABLE_NO_CHANGE_status() {
		Map<Coin, Integer> insertedCoins = Maps.newLinkedHashMap();
		insertedCoins.put(CoinFactory.create02(), 3);
		when(changeStorage.getInsertedCoins()).thenReturn(insertedCoins);
		Product product = mock(Product.class);
		when(product.getPrice()).thenReturn(MoneyFactory.USD(.5));
		Shelve shelve = mock(Shelve.class);
		when(shelve.getProduct()).thenReturn(product);
		when(machine.getActiveShelve()).thenReturn(shelve);

		Assertions.assertThat(purchaseFacade.getPurchaseStatus()).isEqualByComparingTo(PurchaseStatus.INSUFFICIENT_CHANGE);
	}

	@Test
	public void gets_BUYABLE_status() {
		Map<Coin, Integer> insertedCoins = Maps.newLinkedHashMap();
		insertedCoins.put(CoinFactory.create10(), 1);
		when(changeStorage.getInsertedCoins()).thenReturn(insertedCoins);
		Product product = mock(Product.class);
		when(product.getPrice()).thenReturn(MoneyFactory.USD(1));
		Shelve shelve = mock(Shelve.class);
		when(shelve.getProduct()).thenReturn(product);
		when(machine.getActiveShelve()).thenReturn(shelve);

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

}
