package tdd.vendingMachine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author kdkz
 */
public class Configuration {

    void loadConfig(String env){

        try {
            InputStream inputStream = new FileInputStream(env + ".properties");

            Properties properties = new Properties();
            properties.load(inputStream);

            System.setProperties(properties);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
