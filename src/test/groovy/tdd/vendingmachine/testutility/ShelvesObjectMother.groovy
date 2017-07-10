package tdd.vendingmachine.testutility

import tdd.vendingmachine.domain.dto.ShelfDto

import static tdd.vendingmachine.testutility.Randomizer.aShelfCount
import static tdd.vendingmachine.testutility.ShelfTestBuilder.aShelf

class ShelvesObjectMother {
    
    static Set<ShelfDto> shelves() {
        return (1..aShelfCount()).collect { number -> aShelf().withNumber(number.toString()).build() }
    }
    
    static Map<String, ShelfDto> shelvesByNumber() {
        return shelves().collectEntries { shelf -> [(shelf.shelfNumber): shelf] }
    }
}
