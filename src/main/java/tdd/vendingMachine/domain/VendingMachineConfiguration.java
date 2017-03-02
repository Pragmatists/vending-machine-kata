package tdd.vendingMachine.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 * Class representing the VendingMachine configuration, which read from properties file or a given file in the
 * resources main folder. In case of failure reading from file loads the default specified values.
 */
public class VendingMachineConfiguration {


    private static final Logger logger = Logger.getLogger(VendingMachineConfiguration.class);

    /**
     * Indicates whether the properties where loaded from file successfully
     */
    private static boolean ready;

    /**
     * The properties file to read from
     */
    private final Properties prop;

    /**
     * The amount of shelves available for the machine
     */
    private final int productShelfCount;

    /**
     * The shelf capacity for the machine
     */
    private final int productShelfCapacity;


    protected static final String DEFAULT_CONFIG_FILE = "config.properties";
    protected static final String SHELF_COUNT_KEY = "vendingmachine.productShelfCount";
    protected static final String SHELF_COUNT_DEFAULT = "10";
    protected static final String SHELF_CAPACITY_KEY = "vendingmachine.productShelfCapacity";
    protected static final String SHELF_CAPACITY_DEFAULT = "10";
    protected static final String COIN_SHELF_CAPACITY_KEY = "vendingmachine.coinShelfCapacity";
    protected static final String COIN_SHELF_CAPACITY_DEFAULT = "100";
    private int coinShelfCapacity;

    /**
     * Default constructor initializes properties from default file
     */
    public VendingMachineConfiguration() {
        this.prop = init(null);
        this.productShelfCount = Integer.parseInt(retrieveProperty(SHELF_COUNT_KEY, SHELF_COUNT_DEFAULT));
        this.productShelfCapacity = Integer.parseInt(retrieveProperty(SHELF_CAPACITY_KEY, SHELF_CAPACITY_DEFAULT));
        this.coinShelfCapacity = Integer.parseInt(retrieveProperty(COIN_SHELF_CAPACITY_KEY, COIN_SHELF_CAPACITY_DEFAULT));
    }

    /**
     * Constructor providing a configuration file
     * @param configFile a string representing the file's name inside the resources folder.
     */
    public VendingMachineConfiguration(String configFile) {
        this.prop = init(configFile);
        this.productShelfCount = Integer.parseInt(retrieveProperty(SHELF_COUNT_KEY, SHELF_COUNT_DEFAULT));
        this.productShelfCapacity = Integer.parseInt(retrieveProperty(SHELF_CAPACITY_KEY, SHELF_CAPACITY_DEFAULT));
        this.coinShelfCapacity = Integer.parseInt(retrieveProperty(COIN_SHELF_CAPACITY_KEY, COIN_SHELF_CAPACITY_DEFAULT));
    }

    private String retrieveProperty(String key, String valueDefault) {
        return ready ? prop.getProperty(key, valueDefault) : valueDefault;
    }

    /**
     * This method attempts to read the properties from configuration file in case of failure the ready property
     * indicates defaults must be used.
     * @return the
     */
    private static Properties init(String file) {
        Properties prop = new Properties();
        try {
            String loadFromFile = StringUtils.isEmpty(file) ? DEFAULT_CONFIG_FILE : file;
            InputStream resourceAsStream = VendingMachineConfiguration.class.getClassLoader()
                .getResourceAsStream(loadFromFile);
            prop.load(resourceAsStream);
            ready = true;
        } catch (IOException | NullPointerException e) {
            logger.error("Failed loading properties from the given file: " + file);
            ready = false;
        }
        return prop;
    }

    public int getProductShelfCount() {
        return productShelfCount;
    }

    public int getProductShelfCapacity() {
        return productShelfCapacity;
    }

    public int getCoinShelfCapacity() {
        return coinShelfCapacity;
    }
}
