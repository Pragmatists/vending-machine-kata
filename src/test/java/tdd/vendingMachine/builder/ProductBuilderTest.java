package tdd.vendingMachine.builder;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import tdd.vendingMachine.Product;

public class ProductBuilderTest {

    @Test
    public void build_always_not_null_test() {
        assertThat(new ProductBuilder().build()).isNotNull();
    }

    @Test
    public void with_name_test() {
        // given
        final String prodName = "testName";

        // when
        Product product = new ProductBuilder().withName(prodName).build();

        // then
        assertThat(product.getName()).isEqualTo(prodName);
        assertThat(product.getPrice()).isNull();
    }

    @Test
    public void with_price_string_test() {
        // given
        final String prodPrice = "0.2234";

        // when
        Product product = new ProductBuilder().withPrice(prodPrice).build();

        // then
        assertThat(product.getPrice()).isEqualByComparingTo(new BigDecimal("0.22"));
        assertThat(product.getName()).isNull();
    }

    @Test
    public void with_price_big_decimal_test() {
        // given
        final BigDecimal prodPrice = new BigDecimal("0.22");

        // when
        Product product = new ProductBuilder().withPrice(prodPrice).build();

        // then
        assertThat(product.getPrice()).isEqualByComparingTo(new BigDecimal("0.22"));
        assertThat(product.getName()).isNull();
    }
}
