package tdd.vendingMachine.machine.state;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
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
import java.util.Map;
import java.util.Optional;

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
			interactionState.changeState(InteractionState.StateName.CANCEL);
		} else if (command.equals("b")) {
			purchaseFacade.buy();
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

		descriptionLegend.addAll(getAvailableCoinsDescription());

		descriptionLegend.addAll(Lists.newArrayList(
			CommandLabelDecorator.keyLegend("c", "cancel"),
			QUIT
		));

		return descriptionLegend;
	}

	private List<String> getAvailableCoinsDescription() {
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
		int pad = 18;
		List<String> description = Lists.newArrayList(
			StringUtils.rightPad("Buying", pad) + AnsiColorDecorator.green(product.getName()),
			StringUtils.rightPad("Available amount:", pad) + getProductQuantity(),
			StringUtils.rightPad("Price:", pad) + product.getPrice().getAmount(),
			StringUtils.rightPad("Inserted:", pad) + MoneyUtil.sum(changeStorage.getInsertedCoins()).getAmount(),
			EMPTY,
			getBuyDescription(),
			EMPTY
		);
		description.addAll(getOwnedAndInsertedCoinsDescription());
		return DisplayDecorator.decorate(description);
	}

	private String getProductQuantity() {
		int productQuantity = machine.getActiveShelve().getQuantity();
		return productQuantity == 0 ? AnsiColorDecorator.red("0") : String.valueOf(productQuantity);
	}

	private String getBuyDescription() {
		switch (purchaseFacade.getPurchaseStatus()) {
			case PURCHASABLE:
				return AnsiColorDecorator.green("You can buy now!");
			case INSUFFICIENT_CHANGE:
				return AnsiColorDecorator.red("Cannot buy - machine is unable to return change.");
			case NO_PRODUCT:
				return AnsiColorDecorator.red("Cannot buy - no more product in machine.");
			default:
				return AnsiColorDecorator.yellow("Cannot buy - insufficient founds. Insert more coins.");
		}
	}

	private List<String> getOwnedAndInsertedCoinsDescription() {
		List<Triple<Coin, Integer, Integer>> triples = getDescriptionTriples();
		List<String> descriptions = Lists.newArrayList();
		String nominalHeader = "Coin nominal";
		String ownedCoinsHeader = "Coins in machine";
		String insertedCoinsHeader = "Inserted coins";
		String header = nominalHeader + " | " + ownedCoinsHeader + " | " + insertedCoinsHeader;
		descriptions.add(header);
		triples.forEach(consumer -> {
			String nominal = consumer.getLeft().getNominal().toString();
			String ownedCoinsAmount = consumer.getMiddle().toString();
			String insertedCoinsAmount = consumer.getRight().toString();
			descriptions.add(StringUtils.rightPad(nominal, nominalHeader.length()) + " | " +
				StringUtils.rightPad(ownedCoinsAmount, ownedCoinsHeader.length()) + " | " +
				StringUtils.rightPad(insertedCoinsAmount, insertedCoinsHeader.length()));
		});
		return descriptions;
	}

	private List<Triple<Coin, Integer, Integer>> getDescriptionTriples() {
		List<Coin> availableCoins = purchaseFacade.getAvailableCoin();
		Map<Coin, Integer> insertedCoins = changeStorage.getInsertedCoins();
		Map<Coin, Integer> ownedCoins = changeStorage.getOwnedCoins();
		List<Triple<Coin, Integer, Integer>> triples = Lists.newArrayList();
		availableCoins.forEach(consumer ->
			triples.add(Triple.of(consumer,
				Optional.ofNullable(ownedCoins.get(consumer)).orElse(0),
				Optional.ofNullable(insertedCoins.get(consumer)).orElse(0))));
		return triples;
	}

}
