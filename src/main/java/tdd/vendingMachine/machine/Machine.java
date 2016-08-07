package tdd.vendingMachine.machine;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import tdd.vendingMachine.money.ChangeStorage;
import tdd.vendingMachine.product.factory.ProductFactory;
import tdd.vendingMachine.shelve.entity.Shelve;

import java.util.List;

@Service
public class Machine {

	private List<Shelve> shelves = Lists.newArrayList();

	private ChangeStorage changeStorage;

	public Machine() {
		shelves.add(Shelve.of(ProductFactory.createCocaCola(), RandomUtils.nextInt(5, 11)));
		shelves.add(Shelve.of(ProductFactory.createChocolateBar(), RandomUtils.nextInt(5, 11)));
		shelves.add(Shelve.of(ProductFactory.createMineralWater(), RandomUtils.nextInt(5, 11)));
		changeStorage = new ChangeStorage();
	}

	public Shelve getShelve(Integer index) {
		return shelves.get(index);
	}

	public List<Shelve> getShelves() {
		return Lists.newArrayList(shelves);
	}

}
