package tdd.vendingMachine.machine.state;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdd.vendingMachine.machine.Machine;
import tdd.vendingMachine.machine.cli.util.CommandLabelDecorator;
import tdd.vendingMachine.machine.cli.util.DisplayDecorator;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.shelve.entity.Shelve;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static tdd.vendingMachine.machine.cli.util.CommandLinePrinter.EMPTY_LINE;
import static tdd.vendingMachine.machine.state.InteractionState.QUIT;

@Service
class PickingShelveState extends AbstractState implements State {

	private Machine machine;

	@Autowired
	public PickingShelveState(Machine machine) {
		this.machine = machine;
	}

	@Override
	public List<String> getDescription() {
		final List<String> description = Lists.newArrayList();
		List<String> shelves = getShelves();
		description.addAll(DisplayDecorator.decorate(shelves));
		description.add(EMPTY_LINE);
		description.addAll(getActions());
		description.add(QUIT);
		return description;
	}

	@Override
	public void executeCommand(String command, InteractionState interactionState) {
		try {
			Integer index = Integer.valueOf(command);
			machine.setActiveShelveIndex(index);
			interactionState.changeState(InteractionState.StateName.PAYING);
		} catch (NumberFormatException e) {
			showInvalidCommandMessage(command, interactionState);
		}
	}

	private List<String> getShelves() {
		List<String> shelves = machine.getShelves()
			.stream()
			.map(this::describeShelve)
			.collect(Collectors.toList());

		List<String> shelves2 = Lists.newArrayList();

		IntStream.range(0, shelves.size()).forEach(index -> {
			shelves2.add("Shelve no. " + String.valueOf(index) + " | " + shelves.get(index));
		});

		return shelves2;
	}

	private List<String> getActions() {
		List<Shelve> shelves = machine.getShelves();
		List<String> actions = Lists.newArrayList();

		IntStream.range(0, shelves.size()).forEach(index -> {
			String indexAsString = String.valueOf(index);
			actions.add(CommandLabelDecorator.keyLegend(indexAsString, "pick shelve no. " + indexAsString));
		});

		return actions;
	}

	private String describeShelve(Shelve shelve) {
		Product product = shelve.getProduct();
		return "Product: " + product.getName();
	}

}
