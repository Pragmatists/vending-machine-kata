package tdd.vendingmachine.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import tdd.vendingmachine.domain.dto.CoinDto;

import java.util.Objects;

@ToString
@EqualsAndHashCode
class Coin {

    private final Denomination denomination;

    private Coin(Denomination denomination) {
        this.denomination = Objects.requireNonNull(denomination);
    }

    static Coin create(CoinDto coinDto) {
        Objects.requireNonNull(coinDto);
        Denomination denomination = Denomination.create(coinDto.getValue());
        return new Coin(denomination);
    }

    CoinDto toDto() {
        return new CoinDto(value().value());
    }

    Money value() {
        return denomination.value();
    }

    Denomination denomination() {
        return denomination;
    }
}
