package tdd.vendingMachine.builder;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import org.junit.Test;

import tdd.vendingMachine.VendingMachine;

public class VendingMachineBuilderTest {
    @Test
    public void build_always_not_null_test() {
        assertThat(new VendingMachineBuilder().build()).isNotNull();
    }

    @Test
    public void with_no_shelf_test() {
        // given
        final IBuilder<VendingMachine> vendingMachineBuilder = new VendingMachineBuilder();

        // when
        VendingMachine vendingMachine = vendingMachineBuilder.build();
        
        // then
        assertThat(vendingMachine.getShelfs()).isEmpty();
    }
    
    
    @Test
    public void with_one_shelf_test() {
        // given
        final IBuilder<VendingMachine> vendingMachineBuilder = new VendingMachineBuilder().withShelf(new ShelfBuilder());

        // when
        VendingMachine vendingMachine = vendingMachineBuilder.build();
        
        // then
        assertThat(vendingMachine.getShelfs()).hasSize(1);
    }
    
    
    @Test
    public void with_multiple_shelf_test() {
        // given
        final VendingMachineBuilder vendingMachineBuilder = new VendingMachineBuilder();
        
        IntStream.range(0, 10).forEach(nbr -> vendingMachineBuilder.withShelf(new ShelfBuilder().withProductCount(nbr)));

        // when
        VendingMachine vendingMachine = vendingMachineBuilder.build();
        
        // then
        assertThat(vendingMachine.getShelfs()).hasSize(10);
        assertThat(vendingMachine.getShelfs().get(5).getProductCount().intValue()).isEqualTo(5);
    }
}
