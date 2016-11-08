package tdd.vendingMachine.builder;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import tdd.vendingMachine.Product;
import tdd.vendingMachine.Shelf;

public class ShelfBuilderTest {

    @Test
    public void build_always_not_null_test() {
        assertThat(new ShelfBuilder().build()).isNotNull();
    }

    @Test
    public void with_product_test() {
        // given
        final IBuilder<Product> productBuilder = new ProductBuilder();

        // when
        Shelf shelf = new ShelfBuilder().withProduct(productBuilder).build();

        // then
        assertThat(shelf.getProduct()).isNotNull();
        assertThat(shelf.getProductCount()).isNull();
    }

    @Test
    public void with_product_count_test() {
        // given
        final int productCount = 123;

        // when
        Shelf shelf = new ShelfBuilder().withProductCount(productCount).build();

        // then
        assertThat(shelf.getProduct()).isNull();
        assertThat(shelf.getProductCount().intValue()).isEqualTo(productCount);
    }
}
