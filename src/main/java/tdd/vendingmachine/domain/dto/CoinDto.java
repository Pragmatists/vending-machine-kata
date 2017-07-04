package tdd.vendingmachine.domain.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import tdd.vendingmachine.config.BigDecimalConfiguration;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode
@ToString
public class CoinDto {
    private BigDecimal value;

    public CoinDto(BigDecimal value) {
        this.value = BigDecimalConfiguration.trimToDefaultScale(value);
    }
}
