package tdd.vendingMachine.machine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdd.vendingMachine.machine.cli.util.AnsiColorDecorator;
import tdd.vendingMachine.machine.cli.util.CommandLinePrinter;
import tdd.vendingMachine.machine.state.InteractionState;

import java.util.List;

@Service
public class MachineFacade {

	private CommandLinePrinter commandLinePrinter;

	private InteractionState interactionState;

	@Autowired
	public MachineFacade(CommandLinePrinter commandLinePrinter, InteractionState interactionState) {
		this.commandLinePrinter = commandLinePrinter;
		this.interactionState = interactionState;
	}

	public List<String> getState() {
		return interactionState.getDescription();
	}

	public void executeCommand(String command) {
		if (command.equals("q")) {
			commandLinePrinter.print(AnsiColorDecorator.green("Exiting."));
			commandLinePrinter.exit(0);
		}

		interactionState.executeCommand(command);
	}

}
