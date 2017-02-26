package tdd.vendingMachine.domain;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class VendingMachineConfigurationTest {

    @Test
    public void should_provide_default_config_values_since_given_file_does_not_exist() {
        String configFile = "inexistent.file";
        int expectedCapacity = Integer.parseInt(VendingMachineConfiguration.SHELF_CAPACITY_DEFAULT);
        int expectedCount = Integer.parseInt(VendingMachineConfiguration.SHELF_COUNT_DEFAULT);

        VendingMachineConfiguration vendingMachineConfiguration = new VendingMachineConfiguration(configFile);

        Assert.assertEquals(expectedCapacity, vendingMachineConfiguration.getProductShelfCapacity());
        Assert.assertEquals(expectedCount, vendingMachineConfiguration.getProductShelfCount());
    }

    @Test
    public void should_provide_values_in_default_file_when_no_file_is_given() throws IOException {
        Properties prop = new Properties();
        prop.load(this.getClass().getClassLoader().getResourceAsStream(VendingMachineConfiguration.DEFAULT_CONFIG_FILE));
        int capacityInConfigFile = Integer.parseInt(prop.getProperty(VendingMachineConfiguration.SHELF_CAPACITY_KEY));
        int countInConfigFile = Integer.parseInt(prop.getProperty(VendingMachineConfiguration.SHELF_COUNT_KEY));

        VendingMachineConfiguration vendingMachineConfiguration = new VendingMachineConfiguration();

        Assert.assertEquals(capacityInConfigFile, vendingMachineConfiguration.getProductShelfCapacity());
        Assert.assertEquals(countInConfigFile, vendingMachineConfiguration.getProductShelfCount());
    }

    @Test
    public void should_provide_properties_from_given_existent_file() throws IOException {
        String configFile = "config_test.properties";
        Properties prop = new Properties();
        prop.load(this.getClass().getClassLoader().getResourceAsStream(configFile));
        int capacityInConfigFile = Integer.parseInt(prop.getProperty(VendingMachineConfiguration.SHELF_CAPACITY_KEY));
        int countInConfigFile = Integer.parseInt(prop.getProperty(VendingMachineConfiguration.SHELF_COUNT_KEY));

        VendingMachineConfiguration vendingMachineConfiguration = new VendingMachineConfiguration(configFile);

        Assert.assertEquals(capacityInConfigFile, vendingMachineConfiguration.getProductShelfCapacity());
        Assert.assertEquals(countInConfigFile, vendingMachineConfiguration.getProductShelfCount());
    }
}
