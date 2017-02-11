package tdd.vendingMachine;

import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import tdd.vendingMachine.enumeration.CoinsEnum;
import tdd.vendingMachine.services.CoinsRegisterMapFactory;

public class CoinsRegisterMapFactoryTest {

	@Test
	public void coinRegisterMapShouldContainAllCoinsEnumKeys () {
		Map<CoinsEnum, Integer> emptyRegisterMap = CoinsRegisterMapFactory.getEmptytCoinRegister();
		Map<CoinsEnum, Integer> defaultRegisterMap = CoinsRegisterMapFactory.getDefaultCoinRegister();
		
		for (CoinsEnum coin : CoinsEnum.values()) {
			Assertions.assertThat(emptyRegisterMap.containsKey(coin)).isTrue();
			Assertions.assertThat(defaultRegisterMap.containsKey(coin)).isTrue();
		}
	}
	
	@Test
	public void coinRegisterMapsCanBeMerged () {
		Map<CoinsEnum, Integer> singleCoinRegisterMap = CoinsRegisterMapFactory.getCoinRegister(1);
		Map<CoinsEnum, Integer> tenCointRegisterMap = CoinsRegisterMapFactory.getCoinRegister(10);
		Map<CoinsEnum, Integer> mergedRegisterMap = CoinsRegisterMapFactory.mergeRegisters(singleCoinRegisterMap, tenCointRegisterMap);
		for (CoinsEnum coin : CoinsEnum.values()) {
			Assertions.assertThat(mergedRegisterMap.containsKey(coin)).isTrue();
			Assertions.assertThat(mergedRegisterMap.get(coin)).isEqualTo(11);
			//	arguments should'nt be modified
			Assertions.assertThat(tenCointRegisterMap.get(coin)).isEqualTo(10);
			Assertions.assertThat(singleCoinRegisterMap.get(coin)).isEqualTo(1);
		}
	}
	
	@Test
	public void coinRegisterMapToListWithAllCoinsTest() {
		Map<CoinsEnum, Integer> registerMap = CoinsRegisterMapFactory.getCoinRegister(1);
		List<CoinsEnum> actual = CoinsRegisterMapFactory.coinRegisterMapToList(registerMap);
		Assertions.assertThat(actual).containsExactly(CoinsEnum.values());
		Assertions.assertThat(actual).isSortedAccordingTo((c1,c2) -> c2.getValue().compareTo(c1.getValue()));
	}
	
	@Test
	public void coinRegisterMapToListWithMissingCoinsTest() {
		Map<CoinsEnum, Integer> registerMap = CoinsRegisterMapFactory.getEmptytCoinRegister();
		registerMap.put(CoinsEnum.FIVE, 2);
		registerMap.put(CoinsEnum.ONE, 1);
		registerMap.put(CoinsEnum.POINT_ONE, 3);
		List<CoinsEnum> actual = CoinsRegisterMapFactory.coinRegisterMapToList(registerMap);
		Assertions.assertThat(actual).containsExactly(CoinsEnum.FIVE, CoinsEnum.FIVE, 
											CoinsEnum.ONE, 
											CoinsEnum.POINT_ONE, CoinsEnum.POINT_ONE, CoinsEnum.POINT_ONE);
		Assertions.assertThat(actual).isSortedAccordingTo((c1,c2) -> c2.getValue().compareTo(c1.getValue()));
	}
}
