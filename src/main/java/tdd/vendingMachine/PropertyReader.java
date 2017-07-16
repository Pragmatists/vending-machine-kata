package tdd.vendingMachine;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Yevhen Sukhomud
 */
public class PropertyReader {

    private static final String APPLICATION_PROPERTY_PATH = "/application.property";
    private static final String VALID_DENOMINATIONS_PROPERTY = "valid.denominations";
    private Properties prop = new Properties();
    private Set<Integer> acceptableDenominations = new HashSet<>();

    public PropertyReader() throws IOException {
        prop.load(getClass().getResourceAsStream(APPLICATION_PROPERTY_PATH));
        String[] acceptable = prop.getProperty(VALID_DENOMINATIONS_PROPERTY).split(",");
        acceptableDenominations.addAll(
            Arrays.stream(acceptable)
                .map(Integer::valueOf)
                .collect(Collectors.toSet()));
    }

    public Set<Integer> readAcceptableDenominations() {
        return new HashSet<>(acceptableDenominations);
    }

}
