package pl.codeweaver

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class VendingMachineTest {

    companion object Const {
        val COKE_90 = ProductAndRow("coke", "0.90", 1)
    }

    @Test fun shouldFindDuplicatedRows() {
        // given
        val merchandiseOnSameRow = Merchandise(COKE_90, ProductAndRow("water", "1.90", 1))
        // when
        val caugth = from {
            VendingMachine.build(
                    money = Money(),
                    merchandise = merchandiseOnSameRow
            )
        }
        // then
        assertThat(caugth).
                hasMessage("There can be only one product on one row")
    }

    @Test fun shouldDisplaySelectedRow() {
        // given
        val display = RecordingDisplay()
        val machine = VendingMachine.build(
                merchandise = Merchandise(COKE_90),
                diplay = display
        )
        // when
        machine.selectRow(1)
        // then
        assertThat(display).containsExactly("0.90")
    }

    @Test fun shouldUpdateLackingChange() {
        // given
        val display = RecordingDisplay()
        val machine = VendingMachine.build(
                merchandise = Merchandise(COKE_90),
                diplay = display
        )
        // when
        machine.selectRow(1)
        machine.putCoin("0.5")
        machine.putCoin("0.2")

        // then
        assertThat(display).containsExactly("0.90", "0.40", "0.20")
    }

    @Test fun shouldBuyProductAndReturnChange() {
        // given
        val display = RecordingDisplay()
        val coinOutput = RecordingCoinOutput()
        val machine = VendingMachine.build(
                money = Money.from(CoinAndCount("0.1", 1), CoinAndCount("1", 1)),
                merchandise = Merchandise(COKE_90),
                diplay = display,
                coinOutput = coinOutput
        )
        // when
        machine.selectRow(1)
        machine.putCoin("2")

        // then
        assertThat(coinOutput).containsOnly("0.1", "1")
    }

    @Test fun shouldReturnMoneyIfCannotGiveChange() {
        // given
        val display = RecordingDisplay()
        val coinOutput = RecordingCoinOutput()
        val machine = VendingMachine.build(
                money = Money.from(CoinAndCount("0.5", 1)),
                merchandise = Merchandise(COKE_90),
                diplay = display,
                coinOutput = coinOutput
        )
        // when
        machine.selectRow(1)
        machine.putCoin("2")

        // then
        assertThat(display).containsOnly("0.90", "Nie mam jak wydać")
        assertThat(coinOutput).containsOnly("2")
    }

    @Test fun shouldReturnMoneyIfCanceled() {
        // given
        val display = RecordingDisplay()
        val coinOutput = RecordingCoinOutput()
        val machine = VendingMachine.build(
                money = Money.from(CoinAndCount("0.5", 1)),
                merchandise = Merchandise(COKE_90),
                diplay = display,
                coinOutput = coinOutput
        )
        // when
        machine.selectRow(1)
        machine.putCoin("0.5")
        machine.cancle()

        // then
        assertThat(coinOutput).containsOnly("0.5")
    }

    private fun from(call: () -> Any): Exception? = try {
        call(); null
    } catch (e: Exception) {
        e
    }

}