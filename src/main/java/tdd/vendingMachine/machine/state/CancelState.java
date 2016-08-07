package tdd.vendingMachine.machine.state;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdd.vendingMachine.machine.cli.util.CommandLinePrinter;
import tdd.vendingMachine.machine.cli.util.DisplayDecorator;
import tdd.vendingMachine.money.change.ChangeStorage;
import tdd.vendingMachine.money.coin.entity.Coin;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
class CancelState extends AbstractState implements State {

	private CommandLinePrinter commandLinePrinter;

	private ChangeStorage changeStorage;

	@Autowired
	public CancelState(CommandLinePrinter commandLinePrinter, ChangeStorage changeStorage) {
		this.commandLinePrinter = commandLinePrinter;
		this.changeStorage = changeStorage;
	}

	@Override
	public List<String> getDescription() {
		return Lists.newArrayList();
	}

	@Override
	public void executeCommand(String command, InteractionState interactionState) {
		commandLinePrinter.print(formatInsertedCoinsReturnDescription());
		interactionState.changeState(InteractionState.StateName.PICKING_SHELVE);
	}

	private List<String> formatInsertedCoinsReturnDescription() {
		Map<Coin, Integer> coins = changeStorage.getInsertedCoins();

		if (coins.isEmpty()) {
			return DisplayDecorator.decorate(Lists.newArrayList("No coins to return."));
		} else {
			return DisplayDecorator.decorate(coins.entrySet().stream().map(entry ->
				 "Returned " + entry.getValue() + " coin" + (entry.getValue() == 1 ? "" : "s" ) +
					" with nominal " + entry.getKey().getNominal().getAmount().toString() + "."
			).collect(Collectors.toList()));
		}
	}

}
