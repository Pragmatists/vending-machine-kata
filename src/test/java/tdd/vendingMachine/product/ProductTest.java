package tdd.vendingMachine.product;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {

    @Test
    public void shouldAssignType() throws Exception {
        //given
        ProductType type = ProductType.CHIPS;
        //when
        Product product = new Product(type);
        //then
        assertThat(product.getType()).isEqualTo(type);
    }

    @Test
    public void shouldReturnProductPrice() throws Exception {
        //given
        ProductType type = ProductType.CHIPS;
        //when
        Product product = new Product(type);
        //then
        assertThat(product.getPrice()).isEqualTo(ProductType.CHIPS.getPrice());
    }
}
