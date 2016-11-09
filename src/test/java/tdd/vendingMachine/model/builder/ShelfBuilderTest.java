package tdd.vendingMachine.model.builder;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import tdd.vendingMachine.model.Product;
import tdd.vendingMachine.model.Shelf;
import tdd.vendingMachine.model.builder.IBuilder;
import tdd.vendingMachine.model.builder.ProductBuilder;
import tdd.vendingMachine.model.builder.ShelfBuilder;

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
