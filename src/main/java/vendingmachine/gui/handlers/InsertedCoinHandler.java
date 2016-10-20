package vendingmachine.gui.handlers;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JTextPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vendingmachine.calc.ReturningChangePossibilityChecker;
import vendingmachine.calc.ReturningChangePossibilityResult;
import vendingmachine.enums.CoinDenomination;
import vendingmachine.enums.ShelfSelection;
import vendingmachine.model.Product;
import vendingmachine.model.VendingMachine;
import vendingmachine.model.VendingMachineCoinsStore;
import vendingmachine.model.VendingMachineConstants;
import vendingmachine.utils.MessagesHelper;

public class InsertedCoinHandler {

	private static final Logger LOG = LogManager.getLogger(InsertedCoinHandler.class);

	@Inject
	private VendingMachine vendingMachine;

	@Inject
	private ReturningChangePossibilityChecker returningChangePossibilityChecker;

	public void handle(JTextPane textFieldDisplay, JButton btnCancel, CoinDenomination coinDenomination) {
		LOG.info("Inserted coin: " + coinDenomination.getValue());
		btnCancel.setEnabled(true);
		vendingMachine.getCoinsStore().incrementCoinQuantity(coinDenomination);
		ShelfSelection currentlySelectedShelf = vendingMachine.getSelectedShelf();
		BigDecimal sumOfInsertedCoins = vendingMachine.getCoinsStore().calculateSumOfMoneyFromCurrentSessionCoinsQuantities();
		Product[][] vendingMachineProducts = vendingMachine.getProducts();
		BigDecimal priceOfSelectedProduct = vendingMachineProducts[currentlySelectedShelf.getCode()][0].getPrice();
		int compareResult = priceOfSelectedProduct.compareTo(sumOfInsertedCoins);
		if (compareResult == 0) {
			// inserted exact price of selected product
			handleInsertedSumOfMoneyEqualsProductPrice(textFieldDisplay, btnCancel, currentlySelectedShelf, vendingMachineProducts, priceOfSelectedProduct);
		} else if (compareResult < 0) {
			// inserted more money than price of selected product
			handleInsertedSumOfMoneyIsBiggerThanProductPrice(textFieldDisplay, btnCancel, sumOfInsertedCoins, priceOfSelectedProduct);
		} else {
			// inserted less money than price of selected product
			handleInsertedSumOfMoneyIsLessThanProductPrice(textFieldDisplay, btnCancel, currentlySelectedShelf, sumOfInsertedCoins, vendingMachineProducts, priceOfSelectedProduct);
		}

	}

	private void handleInsertedSumOfMoneyEqualsProductPrice(JTextPane textFieldDisplay, JButton btnCancel, ShelfSelection currentlySelectedShelf,
			Product[][] vendingMachineProducts, BigDecimal priceOfSelectedProduct) {
		LOG.info("Inserted exact amount of money to buy a product {} [Required price: {}]", vendingMachineProducts[currentlySelectedShelf.getCode()][0].getName(),
				priceOfSelectedProduct);
		textFieldDisplay.setText("Dispending product: " + vendingMachineProducts[currentlySelectedShelf.getCode()][0].getName());
		vendingMachine.getCoinsStore().clearCurrentSessionCoinsStore();
		btnCancel.setEnabled(false);
		dispenseProductFromShelf(currentlySelectedShelf, vendingMachineProducts);
	}

	private void dispenseProductFromShelf(ShelfSelection currentlySelectedShelf, Product[][] vendingMachineProducts) {
		for (int i = 0; i < VendingMachineConstants.PRODUCTS_PER_SHELF_NR; ++i) {
			if (vendingMachineProducts[currentlySelectedShelf.getCode()][i].getQuantity() > 0) {
				LOG.info("Dispensing product from shelf {}, slot {}", (currentlySelectedShelf.getCode() + 1), (i + 1));
				int oldProductQuanity = vendingMachineProducts[currentlySelectedShelf.getCode()][i].getQuantity();
				vendingMachineProducts[currentlySelectedShelf.getCode()][i].setQuantity(oldProductQuanity - 1);
				break;
			}
		}
	}

	private void handleInsertedSumOfMoneyIsBiggerThanProductPrice(JTextPane textFieldDisplay, JButton btnCancel, BigDecimal sumOfInsertedCoins, BigDecimal priceOfSelectedProduct) {
		BigDecimal moneyToReturn = sumOfInsertedCoins.subtract(priceOfSelectedProduct);
		ReturningChangePossibilityResult returningChangePossibilityResult = returningChangePossibilityChecker.check(moneyToReturn, vendingMachine.getCoinsStore());
		if (returningChangePossibilityResult.isReturningChangePossible()) {
			VendingMachineCoinsStore coinsStore = vendingMachine.getCoinsStore();
			LOG.info("Coins store before change \n{}", coinsStore);
			int[] coinsToReturn = returningChangePossibilityResult.getCoinsToReturn();
			for (int i = 0; i < coinsToReturn.length; ++i) {
				coinsStore.subtractCoinQuantity(CoinDenomination.getByCode(i), coinsToReturn[i]);
			}
			dispenseProductFromShelf(vendingMachine.getSelectedShelf(), vendingMachine.getProducts());
			textFieldDisplay.setText("Dispensing product: " + vendingMachine.getProducts()[vendingMachine.getSelectedShelf().getCode()][0].getName() + " and returning coins: "
					+ MessagesHelper.createReturningCoinsVector(coinsToReturn));
			vendingMachine.getCoinsStore().clearCurrentSessionCoinsStore();
			btnCancel.setEnabled(false);
			LOG.info("Coins store after change \n{}", coinsStore);
		} else {
			LOG.info("Vending machine does not have coins to give change... Returning inserted money");
			VendingMachineCoinsStore coinsStore = vendingMachine.getCoinsStore();
			LOG.info("Coins store before change \n{}", coinsStore);
			int[] coinsToReturn = vendingMachine.getCoinsStore().getCurrentSessionCoinsQuantities();
			for (int i = 0; i < coinsToReturn.length; ++i) {
				coinsStore.subtractCoinQuantity(CoinDenomination.getByCode(i), coinsToReturn[i]);
			}
			textFieldDisplay
					.setText("Vending machine does not have coins to give change. Returning all inserted money: " + MessagesHelper.createReturningCoinsVector(coinsToReturn));
			vendingMachine.getCoinsStore().clearCurrentSessionCoinsStore();
			btnCancel.setEnabled(false);
			LOG.info("Coins store after change \n{}", coinsStore);
		}
	}

	private void handleInsertedSumOfMoneyIsLessThanProductPrice(JTextPane textFieldDisplay, JButton btnCancel, ShelfSelection currentlySelectedShelf, BigDecimal sumOfInsertedCoins,
			Product[][] vendingMachineProducts, BigDecimal priceOfSelectedProduct) {
		LOG.info("Inserted not enough money to by a product {} [Required price: {}, Inserted sum of money: {}]",
				vendingMachineProducts[currentlySelectedShelf.getCode()][0].getName(), priceOfSelectedProduct, sumOfInsertedCoins);
		LOG.info("Coins store: " + vendingMachine.getCoinsStore());
		textFieldDisplay.setText("Money needed to be inserted to buy selected product: " + priceOfSelectedProduct.subtract(sumOfInsertedCoins));
	}

}
