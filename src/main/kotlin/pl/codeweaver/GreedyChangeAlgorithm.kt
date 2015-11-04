package pl.codeweaver

import java.util.*

object GreedyChangeAlgorithm {
    private val NO_MATCH = -1

    fun change(originalAmount: Int, originalCoins: List<List<Int>>): List<Int> {
        var amount = originalAmount
        val change = ArrayList<Int>()
        val coins = originalCoins.map { it.toArrayList() }

        while (amount > 0) {
            var matchIdx = NO_MATCH
            var matchValue = NO_MATCH

            for (i in 0..(coins.size - 1)) {
                val (value, count) = coins[i]
                if (count > 0 &&
                        value <= amount &&
                        value > matchValue) {
                    matchIdx = i
                    matchValue = value
                }
            }

            if (matchIdx == NO_MATCH) {
                return listOf()
            }

            val foundCoin = coins[matchIdx]
            foundCoin.count = foundCoin.count - 1
            amount -= foundCoin.value
            change.add(foundCoin.value)
        }

        return change
    }

    private var MutableList<Int>.value: Int
        get() {
            return this[0]
        }
        set(value) {
            this[0] = value
        }

    private var MutableList<Int>.count: Int
        get() {
            return this[1]
        }
        set(left) {
            this[1] = left
        }
}


