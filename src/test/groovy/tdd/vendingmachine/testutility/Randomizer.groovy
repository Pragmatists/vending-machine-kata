package tdd.vendingmachine.testutility

import tdd.vendingmachine.domain.dto.CoinDto

class Randomizer {

    private static Random RANDOM = new Random()
    private static List<BigDecimal> examplePrices = [1.0, 1.5, 2.2, 2.5, 2.8, 3.0, 3.5]
    private static List<BigDecimal> coinDenominations = [0.1, 0.2, 0.5, 1.0, 2.0, 5.0]
    private static List<String> exampleProductTypeNames = ["Cola drink 0.25l", "chocolate bar", "mineral water 0.33l"]

    static int aShelfCount() {
        return RANDOM.nextInt(99) + 1 // 1 - 99
    }
    
    static String aShelfNumber() {
        return aShelfCount()
    }

    static BigDecimal aPriceValue() {
        return randomFrom(examplePrices)
    }
    
    static String aProductTypeName() {
        return randomFrom(exampleProductTypeNames)
    }
    
    static int aNotEmptyProductCount() {
        return RANDOM.nextInt(21) + 2 // 2 - 20
    }
    
    private static <T> T randomFrom(List<T> list) {
        return list.get(RANDOM.nextInt(list.size()))
    }
    
    static BigDecimal aDenomination() {
        return randomFrom(coinDenominations)
    }
    
    static CoinDto aCoin() {
        return new CoinDto(randomFrom(coinDenominations))
    }
}
