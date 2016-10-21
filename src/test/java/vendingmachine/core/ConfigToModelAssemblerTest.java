package vendingmachine.core;

import java.io.File;
import java.math.BigDecimal;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import vendingmachine.config.xsd.classes.ProductsInVendingMachine;
import vendingmachine.model.Product;
import vendingmachine.model.VendingMachine;
import vendingmachine.model.VendingMachineConstants;

public class ConfigToModelAssemblerTest {

	private static final File GOOD_VENDING_MACHINE_CONFIG_FILE = new File("src\\test\\resources\\xml\\VendingMachineConfigExample.xml");

	private ConfigToModelAssembler configToModelAssembler = new ConfigToModelAssembler();

	@Test
	public void test_assemble() throws Exception {

		ProductsInVendingMachine productsInVendingMachine = loadConfigFromFile();

		VendingMachine vendingMachine = new VendingMachine();
		Product[][] products = vendingMachine.getProducts();
		configToModelAssembler.assemble(productsInVendingMachine, products);

		assertShelf(products, 0, new BigDecimal("3.0"), "Coke 0.33l", 1, 2, 3, 3, 2, 1);
		assertShelf(products, 1, new BigDecimal("5.0"), "Coke 0.5l", 4, 3, 2, 2, 1, 3);
		assertShelf(products, 2, new BigDecimal("3"), "Snickers", 2, 2, 3, 4, 5, 2);
		assertShelf(products, 3, new BigDecimal("2.5"), "7 Days", 3, 2, 4, 1, 2, 3);

	}

	private void assertShelf(Product[][] products, int shelfNr, BigDecimal productTypePrice, String productTypeName, int productQuantityInFirstColumn,
			int productQuantityInSecondColumn, int productQuantityInThirdColumn, int productQuantityInFourthColumn, int productQuantityInFifthColumn,
			int productQuantityInSixthColumn) {

		for (int i = 0; i < VendingMachineConstants.PRODUCTS_PER_SHELF_NR; ++i) {
			Assertions.assertThat(products[shelfNr][i].getPrice()).isEqualTo(productTypePrice);
			Assertions.assertThat(products[shelfNr][i].getName()).isEqualTo(productTypeName);
		}

		Assertions.assertThat(products[shelfNr][0].getQuantity()).isEqualTo(productQuantityInFirstColumn);
		Assertions.assertThat(products[shelfNr][1].getQuantity()).isEqualTo(productQuantityInSecondColumn);
		Assertions.assertThat(products[shelfNr][2].getQuantity()).isEqualTo(productQuantityInThirdColumn);
		Assertions.assertThat(products[shelfNr][3].getQuantity()).isEqualTo(productQuantityInFourthColumn);
		Assertions.assertThat(products[shelfNr][4].getQuantity()).isEqualTo(productQuantityInFifthColumn);
		Assertions.assertThat(products[shelfNr][5].getQuantity()).isEqualTo(productQuantityInSixthColumn);

	}

	private ProductsInVendingMachine loadConfigFromFile() throws Exception {

		JAXBContext jaxbContext = JAXBContext.newInstance(ProductsInVendingMachine.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		ProductsInVendingMachine productsInVendingMachine = (ProductsInVendingMachine) jaxbUnmarshaller.unmarshal(GOOD_VENDING_MACHINE_CONFIG_FILE);
		return productsInVendingMachine;

	}

}
