package tdd.vendingMachine

import spock.lang.Specification
import tdd.vendingMachine.domain.VendingMachine

import static tdd.vendingMachine.domain.Denomination.*

/**
 * @author kdkz
 */
class MainSpec extends Specification {

    private VendingMachine vendingMachine

    def setup() {
        vendingMachine = new VendingMachine()
    }
//
//    def "insert coin without selecting shelf"() {
//        expect:
//        vendingMachine.insertCoin(FIVE, 1)
//    }
//
//    def "select shelf nr 3"() {
//        given:
//        int shelfNumber = 3
//
//        expect:
//        vendingMachine.selectShelf(shelfNumber)
//    }
//
//    def "select cancel button"() {
//        expect:
//        vendingMachine.cancelTransaction()
//    }
//
//    def "select shelf nr 2 and insert 0.20 x 3 at once and then cancel. Vending machine has no coins"(){
//        given:
//        int shelfNumber = 2
//
//        expect:
//        vendingMachine.selectShelf(shelfNumber)
//        vendingMachine.insertCoin(ONE_FIFTH, 3)
//        vendingMachine.cancelTransaction()
//    }
//
//    def "select shelf nr 2 and insert 0.20 x 3 one by one and then cancel. Vending machine has no coins"(){
//        given:
//        int shelfNumber = 2
//
//        expect:
//        vendingMachine.selectShelf(shelfNumber)
//        vendingMachine.insertCoin(ONE_FIFTH, 1)
//        vendingMachine.insertCoin(ONE_FIFTH, 1)
//        vendingMachine.insertCoin(ONE_FIFTH, 1)
//        vendingMachine.cancelTransaction()
//    }
//
//    def "select shelf nr 1 and insert exact amount as product price. Vending machine has no coins"() {
//        given:
//        int shelfNumber = 1
//
//        expect:
//        vendingMachine.selectShelf(shelfNumber)
//        vendingMachine.insertCoin(FIVE, 1)
//        vendingMachine.insertCoin(TWO, 1)
//        vendingMachine.insertCoin(ONE_FIFTH, 1)
//        vendingMachine.insertCoin(ONE_TENTH, 2)
//    }
//
//    def "select shelf nr 2 and insert more amount than product price. Vending machine has no coins."(){
//        given:
//        int shelfNumber = 2
//
//        expect:
//        vendingMachine.selectShelf(shelfNumber)
//        vendingMachine.insertCoin(ONE_FIFTH, 3)
//        vendingMachine.insertCoin(HALF, 1)
//        vendingMachine.insertCoin(ONE_FIFTH, 1)
//    }
//
//    def "select shelf nr 2 and insert more amount than product price. Vending machine has no coins. Rest will be returned"(){
//        given:
//        int shelfNumber = 2
//
//        expect:
//        vendingMachine.selectShelf(shelfNumber)
//        vendingMachine.insertCoin(ONE_FIFTH, 4)
//        vendingMachine.insertCoin(ONE_TENTH, 1)
//        vendingMachine.insertCoin(ONE_FIFTH, 3)
//    }
//
//    def "try to buy 2 products from shelf nr 3 one by one"() {
//        given:
//        int shelfNumber1 = 3
//        int shelfNumber2 = 2
//
//        expect:
//        vendingMachine.selectShelf(shelfNumber1)
//        vendingMachine.insertCoin(TWO, 1)
//        vendingMachine.insertCoin(HALF, 3)
//        vendingMachine.insertCoin(ONE_FIFTH, 1)
//
//        vendingMachine.selectShelf(shelfNumber2)
//        vendingMachine.insertCoin(FIVE, 1)
//    }
}
