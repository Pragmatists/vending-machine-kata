package vendingmachine.main;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.jboss.weld.environment.se.events.ContainerInitialized;

import vendingmachine.core.VendingMachineInitializer;
import vendingmachine.gui.VendingMachineMainWindow;

@ApplicationScoped
public class Main {

	private static final Logger LOG = LogManager.getLogger(VendingMachineMainWindow.class);

	@Inject
	private VendingMachineInitializer vendingMachineInitializer;

	@Inject
	private VendingMachineMainWindow vendingMachineMainWindow;

	public void main(@Observes ContainerInitialized event, @Parameters List<String> args) {
		if (args.size() != 1) {
			LOG.info("Application should be given one parameter: path to configration file. Quitting...");
			System.exit(1);
		} else {
			if (vendingMachineInitializer.init(args.get(0))) {
				vendingMachineMainWindow.launchGUI();
			}
		}
	}
}
