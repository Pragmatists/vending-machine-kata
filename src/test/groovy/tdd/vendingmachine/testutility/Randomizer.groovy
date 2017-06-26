package tdd.vendingmachine.testutility

class Randomizer {

    private static Random RANDOM = new Random()
    private static List<BigDecimal> examplePrices = [1.0, 1.5, 2.2, 2.5, 2.8, 3.0, 3.5]
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
}
