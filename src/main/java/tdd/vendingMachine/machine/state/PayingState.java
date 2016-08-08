package tdd.vendingMachine.machine.state;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdd.vendingMachine.machine.Machine;
import tdd.vendingMachine.machine.cli.util.AnsiColorDecorator;
import tdd.vendingMachine.machine.cli.util.CommandLabelDecorator;
import tdd.vendingMachine.machine.cli.util.DisplayDecorator;
import tdd.vendingMachine.machine.purchase.PurchaseFacade;
import tdd.vendingMachine.machine.purchase.enums.PurchaseStatus;
import tdd.vendingMachine.money.change.ChangeStorage;
import tdd.vendingMachine.money.coin.entity.Coin;
import tdd.vendingMachine.money.util.MoneyUtil;
import tdd.vendingMachine.product.Product;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static tdd.vendingMachine.machine.state.InteractionState.QUIT;

@Service
class PayingState extends AbstractState implements State {

	private PurchaseFacade purchaseFacade;

	private Machine machine;

	private ChangeStorage changeStorage;

	@Autowired
	public PayingState(PurchaseFacade purchaseFacade, Machine machine, ChangeStorage changeStorage) {
		this.purchaseFacade = purchaseFacade;
		this.machine = machine;
		this.changeStorage = changeStorage;
	}

	@Override
	public List<String> getDescription() {
		List<String> description = Lists.newArrayList();
		description.addAll(getDescriptionDisplay());
		description.add(EMPTY);
		description.addAll(getDescriptionLegend());
		return description;
	}

	@Override
	public void executeCommand(String command, InteractionState interactionState) {
		if (command.equals("c")) {
			purchaseFacade.returnInsertedCoins();
			interactionState.changeState(InteractionState.StateName.CANCEL);
		} else if (command.equals("b")) {
			purchaseFacade.buyAndGetChange();
		} else {
			try {
				Integer index = Integer.valueOf(command);
				purchaseFacade.insertCoin(index);
			} catch (NumberFormatException e) {
				showInvalidCommandMessage(command, interactionState);
			}
		}
	}

	private List<String> getDescriptionLegend() {
		List<String> descriptionLegend = Lists.newArrayList();

		if (PurchaseStatus.PURCHASABLE.equals(purchaseFacade.getPurchaseStatus())) {
			descriptionLegend.add(CommandLabelDecorator.keyLegend("b", "buy"));
		} else {
			descriptionLegend.add(CommandLabelDecorator.keyLegendInvalid("b", "buy"));
		}

		descriptionLegend.addAll(getCoinsDescription());

		descriptionLegend.addAll(Lists.newArrayList(
			CommandLabelDecorator.keyLegend("c", "cancel"),
			QUIT
		));

		return descriptionLegend;
	}

	private List<String> getCoinsDescription() {
		List<Coin> availableCoins = purchaseFacade.getAvailableCoin();
		List<String> descriptions = Lists.newArrayList();
		for (int i = 0; i < availableCoins.size(); i++) {
			String coinValue = availableCoins.get(i).getNominal().getAmount().toString();
			descriptions.add(CommandLabelDecorator.keyLegend(String.valueOf(i), "insert coin with value " + coinValue));
		}
		return descriptions;
	}

	private List<String> getDescriptionDisplay() {
		Product product = machine.getActiveShelve().getProduct();
		return DisplayDecorator.decorate(Lists.newArrayList(
			"Buying:   " + AnsiColorDecorator.green(product.getName()),
			"Price:    " + product.getPrice().getAmount(),
			"Inserted: " + MoneyUtil.sum(changeStorage.getInsertedCoins()).getAmount(),
			EMPTY,
			getBuyDescripton()
		));
	}

	private String getBuyDescripton() {
		switch (purchaseFacade.getPurchaseStatus()) {
			case PURCHASABLE:
				return AnsiColorDecorator.green("You can buy now!");
			case INSUFFICIENT_CHANGE:
				return AnsiColorDecorator.red("Cannot buy - machine is unable to return change");
			default:
				return AnsiColorDecorator.yellow("Cannot buy insufficient founds. Insert more coins.");
		}
	}

}
