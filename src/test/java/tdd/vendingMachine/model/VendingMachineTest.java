package tdd.vendingMachine.model;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import tdd.vendingMachine.model.VendingMachine;

public class VendingMachineTest {

    @Test
    public void just_a_stupid_passing_test_to_ensure_that_tests_are_run() {
        Assertions.assertThat(new VendingMachine()).isNotNull();
    }
}
