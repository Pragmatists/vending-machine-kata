package pl.codeweaver

import java.math.BigDecimal
import java.util.*

class RecordingCoinOutput(val coins: MutableList<String> = ArrayList<String>()) : CoinOutput, Iterable<String> by coins {

    override fun out(coins: List<BigDecimal>) {
        this.coins += coins.map { it.toString() }
    }

}