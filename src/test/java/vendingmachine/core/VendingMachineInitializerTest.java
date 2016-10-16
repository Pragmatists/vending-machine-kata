package vendingmachine.core;

import java.io.File;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import vendingmachine.core.VendingMachineInitializer;

public class VendingMachineInitializerTest {

	private static String GOOD_VENDING_MACHINE_CONFIG_FILEPATH = new File("src\\test\\resources\\xml\\VendingMachineConfigExample.xml").getAbsolutePath();
	private static String NOT_EXISTING_VENDING_MACHINE_CONFIG_FILEPATH = "abcdefg.xml";
	private static String NOT_CONFORMING_TO_XSD_VENDING_MACHINE_CONFIG_FILEPATH = new File("src\\test\\resources\\xml\\VendingMachineConfigNotConformingToXsd.xml")
			.getAbsolutePath();

	@Test
	public void test_init_ok() throws Exception {
		Assertions.assertThat(VendingMachineInitializer.init(GOOD_VENDING_MACHINE_CONFIG_FILEPATH)).isTrue();
	}

	@Test
	public void test_init_configFileNotExisting() throws Exception {
		Assertions.assertThat(VendingMachineInitializer.init(NOT_EXISTING_VENDING_MACHINE_CONFIG_FILEPATH)).isFalse();
	}

	@Test
	public void test_init_configFileNotConformingToXsd() throws Exception {
		Assertions.assertThat(VendingMachineInitializer.init(NOT_CONFORMING_TO_XSD_VENDING_MACHINE_CONFIG_FILEPATH)).isFalse();
	}

}
