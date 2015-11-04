package pl.codeweaver

import java.math.BigDecimal

interface CoinOutput {
    fun out(coins: List<BigDecimal>)
}

class ConsoleCoinOutput : CoinOutput {
    override fun out(coins: List<BigDecimal>) {
        println("Coins out: " + coins.joinToString())
    }
}