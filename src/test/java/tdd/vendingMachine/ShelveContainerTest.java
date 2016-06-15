package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.impl.BasicShelf;
import tdd.vendingMachine.impl.ShelveContainer;

import static org.assertj.core.api.Assertions.assertThat;

public class ShelveContainerTest {

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_adding_null_shelf() {
        new ShelveContainer().add(null);
    }

    @Test
    public void should_get_shelve_at_index() {
        assertThat(new ShelveContainer()
            .add(new BasicShelf(null, null))
            .get(0)).isNotNull();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void should_throw_exception_when_get_from_invalid_index() {
        new ShelveContainer().get(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void should_be_able_to_get_readonly_shelves_collection() {
        new ShelveContainer()
            .add(new BasicShelf(null, null))
            .getReadonlyShelves().add(new BasicShelf(null, null));
    }
}
