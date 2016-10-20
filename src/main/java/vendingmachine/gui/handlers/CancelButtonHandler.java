package vendingmachine.gui.handlers;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JTextPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vendingmachine.enums.CoinDenomination;
import vendingmachine.model.VendingMachine;
import vendingmachine.model.VendingMachineCoinsStore;
import vendingmachine.utils.MessagesHelper;

@Dependent
public class CancelButtonHandler {

	private static final Logger LOG = LogManager.getLogger(InsertedCoinHandler.class);

	@Inject
	private VendingMachine vendingMachine;

	public void handle(JTextPane textPaneDisplay, JButton btnCancel) {
		LOG.info("Cancel button pressed - returning inserted money");
		VendingMachineCoinsStore coinsStore = vendingMachine.getCoinsStore();
		LOG.info("Coins store before change \n{}", coinsStore);
		int[] coinsToReturn = vendingMachine.getCoinsStore().getCurrentSessionCoinsQuantities();
		for (int i = 0; i < coinsToReturn.length; ++i) {
			coinsStore.subtractCoinQuantity(CoinDenomination.getByCode(i), coinsToReturn[i]);
		}
		textPaneDisplay.setText("Returning all inserted money: " + MessagesHelper.createReturningCoinsVector(coinsToReturn));
		vendingMachine.getCoinsStore().clearCurrentSessionCoinsStore();
		btnCancel.setEnabled(false);
		LOG.info("Coins store after change \n{}", coinsStore);
	}

}
