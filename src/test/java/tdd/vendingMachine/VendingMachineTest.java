package tdd.vendingMachine;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class VendingMachineTest {

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenShelvesAreNull() throws Exception{
        new VendingMachine(null);
    }

    @Test
    public void shouldReturnShelves() throws Exception {
        //given
        List<Shelve> shelves = new ArrayList<>();
        VendingMachine vendingMachine = new VendingMachine(shelves);
        //when
        List<Shelve> returnedShelves = vendingMachine.getShelves();
        //then
        assertEquals(shelves, returnedShelves);
    }

}
