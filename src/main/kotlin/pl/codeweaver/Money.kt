package pl.codeweaver

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.*
import kotlin.math.div
import kotlin.math.minus
import kotlin.math.plus
import kotlin.math.times

class Money(val coinAndCount: MutableList<CoinAndCount> = Money.EMPTY.toArrayList()) {

    fun pay(price: BigDecimal, paidWithCoins: List<BigDecimal>): List<BigDecimal>? {
        val paid = paidWithCoins.fold(ZERO) { fst, snd -> fst + snd }
        val change = paid - price

        if (change == ZERO) {
            addCoins(paidWithCoins.map { it.toString() })
            return listOf()
        }

        val changeCoins = GreedyChangeAlgorithm.change(toPenny(change), asArray()).
                map { fromPenny(it) }

        if (changeCoins.isEmpty()) {
            return null
        }

        addCoins(paidWithCoins.map { it.toString() })

        removeCoins(changeCoins.map { it.toString() })

        return changeCoins
    }

    private fun addCoins(coins: List<String>) {
        for (coin in coins) {
            coinAndCount.update({ it.coin == coin }) {
                it.copy(count = it.count + 1)
            }
        }
    }

    private fun removeCoins(coins: List<String>) {
        for (coin in coins) {
            coinAndCount.update({ it.coin == coin }) {
                it.copy(count = it.count - 1)
            }
        }
    }

    private fun asArray(): List<List<Int>> {
        return coinAndCount.map { listOf(toPenny(it.coin), it.count) }
    }

    private fun fromPenny(change: Int) = (BigDecimal(change) / "100".big())
    private fun toPenny(change: String) = toPenny(change.big())
    private fun toPenny(change: BigDecimal): Int = (change * "100".big()).toInt()

    companion object Factory {
        val EMPTY = arrayOf(
                CoinAndCount("5", 0), CoinAndCount("2", 0),
                CoinAndCount("1", 0), CoinAndCount("0.5", 0),
                CoinAndCount("0.2", 0), CoinAndCount("0.1", 0))

        fun fill(coins: List<CoinAndCount>): Money {
            val startCoins = ArrayList<CoinAndCount>(EMPTY.asList())
            for ((coin, count) in coins) {
                startCoins.update({ it.coin == coin }) {
                    it.copy(count = it.count + count)
                }
            }
            return Money(startCoins)
        }

        fun from(vararg coins: CoinAndCount): Money {
            return Money(coins.toArrayList())
        }
    }
}

private fun MutableList<CoinAndCount>.update(predicate: (CoinAndCount) -> Boolean,
                                             transform: (CoinAndCount) -> CoinAndCount): Unit {
    val idx = this.indexOfFirst(predicate)
    if (idx > 0) {
        this[idx] = transform(this[idx])
    }
}

