package pl.codeweaver

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.*
import kotlin.math.minus
import kotlin.math.plus

class VendingMachine(val money: Money, val merchandise: Merchandise, val diplay: Display, val coinOutput: CoinOutput) {

    private val state = VendingMachineState()

    fun selectRow(row: Int) {
        diplay.display(merchandise.price(row))
        state.start(merchandise.price(row))
    }

    fun putCoin(value: String) {
        val paidLeft = state.paidLeft(value)
        if (paidLeft > ZERO) {
            diplay.display(paidLeft.toString())
            return
        }

        val change = money.pay(state.price, state.userCoins)
        if (change == null) {
            diplay.display("Nie mam jak wydać")
            coinOutput.out(state.userCoins)
            return
        }

        if (change.isNotEmpty()) {
            coinOutput.out(change)
        }
    }

    companion object Builder {
        fun build(money: Money = Money(),
                  merchandise: Merchandise = Merchandise(),
                  diplay: Display = ConsoleDisplay(),
                  coinOutput: CoinOutput = ConsoleCoinOutput()): VendingMachine {

            merchandise.
                    map { it.row }.
                    containsDuplicates().
                    rise { RuntimeException("There can be only one product on one row") }

            return VendingMachine(Money.fill(money.coinAndCount), merchandise, diplay, coinOutput)
        }
    }

    fun cancle() {
        if (state.userCoins.isNotEmpty()) {
            coinOutput.out(state.userCoins)
        }
        state.start("0")
    }
}

private class VendingMachineState() {
    var price = ZERO
    var userCoins = ArrayList<BigDecimal>()

    fun start(price: String) {
        this.price = price.big()
        userCoins = ArrayList<BigDecimal>()
    }

    fun paidLeft(value: String): BigDecimal {
        userCoins.add(value.big())
        val paid = userCoins.fold(ZERO) { fst, snd -> fst + snd }
        return price - paid
    }
}

private fun <T> Iterable<T>.containsDuplicates(): Boolean {
    val elements = this.toList()
    return elements.distinct().size != elements.size
}

private fun Boolean.rise(action: () -> Exception): Boolean {
    if (this == true) {
        throw action()
    }
    return this
}