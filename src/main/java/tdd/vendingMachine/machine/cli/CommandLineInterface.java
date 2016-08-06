package tdd.vendingMachine.machine.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import tdd.vendingMachine.machine.MachineFacade;
import tdd.vendingMachine.machine.cli.util.CommandLinePrinter;

import java.util.Scanner;

@Service
@Profile("!test")
public class CommandLineInterface implements CommandLineRunner {

	private MachineFacade machineFacade;

	private CommandLinePrinter commandLinePrinter;

	@Autowired
	public CommandLineInterface(MachineFacade machineFacade, CommandLinePrinter commandLinePrinter) {
		this.machineFacade = machineFacade;
		this.commandLinePrinter = commandLinePrinter;
	}

	@Override
	public void run(String... args) throws Exception {
		commandLinePrinter.print(machineFacade.getState());

		try (Scanner scanner = new Scanner(System.in)) {
			machineFacade.executeCommand(scanner.nextLine());
			run();
		}
	}

}
