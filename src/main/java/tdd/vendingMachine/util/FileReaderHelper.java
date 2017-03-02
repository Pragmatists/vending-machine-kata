package tdd.vendingMachine.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import tdd.vendingMachine.dto.CashImport;
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
            String prodImport[];
            List<ProductImport> list = new ArrayList<>();
            while((line = bf.readLine())!=null) {
                prodImport = StringUtils.split(line, ',');
                list.add(new ProductImport(prodImport[0], asInt(prodImport[1]), asInt(prodImport[2])));
            }
            return Optional.of(list);
        }catch (Exception e) {
            logger.error(e);
            return Optional.empty();
        }
    }

    /**
     * Given a CSV file representing cash imports to build a list.
     * @param inputStream the stream containing the csv representing cash imports.
     * @return an optional value containing empty in case of failure or the requested list of import objects.
     */
    public static Optional<List<CashImport>> retrieveCashImportFromFileStream(InputStream inputStream) {
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream))){
            String line;
            String cashImport[];
            List<CashImport> list = new ArrayList<>();
            while((line = bf.readLine())!=null) {
                cashImport = StringUtils.split(line, ',');
                list.add(new CashImport(cashImport[0], asInt(cashImport[1])));
            }
            return Optional.of(list);
        }catch (Exception e) {
            logger.error(e);
            return Optional.empty();
        }
    }

    private static int asInt(String input) {
        return Integer.parseInt(input);
    }
}
