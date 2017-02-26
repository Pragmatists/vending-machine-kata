package tdd.vendingMachine.dto;

import org.junit.Assert;
import org.junit.Test;
import tdd.vendingMachine.util.Constants;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.NoSuchElementException;

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
        Assert.assertEquals(amount, cashImport.getAmount());
        Assert.assertEquals(label, cashImport.getLabel());
    }

    @Test
    public void should_accumulate_import_given() {
        CashImport initial = new CashImport("label", 1);
        CashImport toAccumulateWith = new CashImport("label", 2);
        CashImport accumulate = initial.accumulate(toAccumulateWith);
        Assert.assertEquals(initial.getLabel(), accumulate.getLabel());
        Assert.assertEquals(toAccumulateWith.getLabel(), accumulate.getLabel());
        Assert.assertEquals(initial.getAmount() + toAccumulateWith.getAmount(), accumulate.getAmount());
    }

    @Test(expected = NoSuchElementException.class)
    public void should_fail_accumulate_given_cashImport_different_label() {
        CashImport initial = new CashImport("label", 1);
        CashImport toAccumulateWith = new CashImport("label_2", 2);
        initial.accumulate(toAccumulateWith);
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_null_label_given() {
        new CashImport(null, 0);
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_empty_label_given() {
        new CashImport("", 0);
    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_negative_amount_given() {
        new CashImport("label", -1);
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_null_import_given() {
        new CashImport("label", 1).accumulate(null);
    }
}
