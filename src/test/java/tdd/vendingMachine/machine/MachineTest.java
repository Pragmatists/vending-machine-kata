package tdd.vendingMachine.machine;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.shelve.entity.Shelve;

public class MachineTest {

	private Machine machine;

	@Before
	public void setup() {
		 machine = new Machine();
	}

	@Test
	public void three_shelves_are_initialized() {
		Assertions.assertThat(machine.getShelves()).hasSize(3);
	}

	@Test
	public void shelve_can_be_accessed_by_index() {
		Assertions.assertThat(machine.getShelve(0)).isInstanceOf(Shelve.class);
	}

}
