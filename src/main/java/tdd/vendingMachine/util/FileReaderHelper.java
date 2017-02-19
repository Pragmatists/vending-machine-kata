package tdd.vendingMachine.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import tdd.vendingMachine.dto.ProductImport;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Agustin on 2/19/2017.
 * @since 1.0
 * Utility Class used to read plain files to load products and cash into the vending machine
 */
public class FileReaderHelper {

    private static final Logger logger = Logger.getLogger(FileReaderHelper.class);

    /**
     * Given a CSV file representing imports build a list.
     * @param inputStream the stream containing the csv representing product imports.
     * @return an optional value containing empty in case of failure or the requested list of import objects.
     */
    public static Optional<List<ProductImport>> retrieveProductsImportFromFileStream(InputStream inputStream) {
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream))){
            String line;
            String prodImport[] = StringUtils.split(bf.readLine(), ',');
            List<ProductImport> list = new ArrayList<>(asInt(prodImport[0]));
            while((line = bf.readLine())!=null) {
                prodImport = StringUtils.split(line, ',');
                list.add(new ProductImport(prodImport[0], asDouble(prodImport[1]), asInt(prodImport[2])));
            }
            return Optional.of(list);
        }catch (Exception e) {
            logger.error(e);
            return Optional.empty();
        }
    }

    private static double asDouble(String input) {
        return Double.parseDouble(input);
    }

    private static int asInt(String input) {
        return Integer.parseInt(input);
    }
}
