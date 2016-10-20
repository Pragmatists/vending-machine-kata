package vendingmachine.core;

import java.io.File;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import vendingmachine.model.VendingMachine;

public class VendingMachineInitializerTest {

	private static String GOOD_VENDING_MACHINE_CONFIG_FILEPATH = new File("src\\test\\resources\\xml\\VendingMachineConfigExample.xml").getAbsolutePath();
	private static String NOT_EXISTING_VENDING_MACHINE_CONFIG_FILEPATH = "abcdefg.xml";
	private static String NOT_CONFORMING_TO_XSD_VENDING_MACHINE_CONFIG_FILEPATH = new File("src\\test\\resources\\xml\\VendingMachineConfigNotConformingToXsd.xml")
			.getAbsolutePath();

	@InjectMocks
	private VendingMachineInitializer vendingMachineInitializer;

	@Mock
	private VendingMachine vendingMachine;

	@Mock
	private ConfigToModelAssembler configToModelAssembler;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void test_init_ok() throws Exception {
		Assertions.assertThat(vendingMachineInitializer.init(GOOD_VENDING_MACHINE_CONFIG_FILEPATH)).isTrue();
	}

	@Test
	public void test_init_configFileNotExisting() throws Exception {
		Assertions.assertThat(vendingMachineInitializer.init(NOT_EXISTING_VENDING_MACHINE_CONFIG_FILEPATH)).isFalse();
	}

	@Test
	public void test_init_configFileNotConformingToXsd() throws Exception {
		Assertions.assertThat(vendingMachineInitializer.init(NOT_CONFORMING_TO_XSD_VENDING_MACHINE_CONFIG_FILEPATH)).isFalse();
	}

}
