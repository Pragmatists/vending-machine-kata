package tdd.vendingMachine.dto;

import org.junit.Assert;
import org.junit.Test;
import tdd.vendingMachine.util.Constants;

/**
 * @author Agustin on 2/19/2017.
 * @since 1.0
 */
public class CashImportTest {

    @Test
    public void should_validate_creation_productImport() {
        String label = "0.5";
        int amount = 4;
        CashImport cashImport = new CashImport(label, amount);
        Assert.assertEquals(amount, cashImport.getAmount(), Constants.ACCURACY);
        Assert.assertEquals(label, cashImport.getLabel());
    }

}
