package tdd.vendingMachine.machine;

import com.google.common.collect.Lists;
import lombok.Setter;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdd.vendingMachine.money.change.ChangeStorage;
import tdd.vendingMachine.product.factory.ProductFactory;
import tdd.vendingMachine.shelve.entity.Shelve;

import java.util.List;

@Service
public class Machine {

	private List<Shelve> shelves = Lists.newArrayList();

	private ChangeStorage changeStorage;

	@Setter
	private Integer activeShelveIndex;

	@Autowired
	public Machine(ChangeStorage changeStorage) {
		this.changeStorage = changeStorage;
		createShelves();
	}

	private void createShelves() {
		shelves.add(Shelve.of(ProductFactory.createCocaCola(), RandomUtils.nextInt(1, 4)));
		shelves.add(Shelve.of(ProductFactory.createChocolateBar(), RandomUtils.nextInt(1, 4)));
		shelves.add(Shelve.of(ProductFactory.createMineralWater(), RandomUtils.nextInt(1, 4)));
	}

	public Shelve getShelve(Integer index) {
		return shelves.get(index);
	}

	public Shelve getActiveShelve() {
		return getShelve(activeShelveIndex);
	}

	public List<Shelve> getShelves() {
		return Lists.newArrayList(shelves);
	}

}
