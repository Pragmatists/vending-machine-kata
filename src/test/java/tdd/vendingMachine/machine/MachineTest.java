package tdd.vendingMachine.machine;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.money.change.ChangeStorage;
import tdd.vendingMachine.product.factory.ProductFactory;
import tdd.vendingMachine.shelve.entity.Shelve;

import static org.mockito.Mockito.mock;

public class MachineTest {

	private Machine machine;

	private ChangeStorage changeStorage;

	@Before
	public void setup() {
		changeStorage = mock(ChangeStorage.class);
		machine = new Machine(changeStorage);
	}

	@Test
	public void three_shelves_are_initialized() {
		Assertions.assertThat(machine.getShelves()).hasSize(3);
	}

	@Test
	public void shelve_can_be_accessed_by_index() {
		Assertions.assertThat(machine.getShelve(0)).isInstanceOf(Shelve.class);
	}

	@Test
	public void active_shelve_can_be_set() {
		machine.setActiveShelveIndex(1);

		Assertions.assertThat(machine.getActiveShelve().getProduct().getName())
			.isEqualTo(ProductFactory.createChocolateBar().getName());
	}

}
