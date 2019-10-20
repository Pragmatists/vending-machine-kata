package tdd.vendingMachine.shelf;

import org.junit.Test;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.product.ProductType;

import static org.assertj.core.api.Assertions.assertThat;

public class ShelfTest {

    private Shelf shelfImpl = new Shelf(5);

    @Test
    public void shouldAddProductWhenShelfProductTypeIsSameAsProductType() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        shelfImpl.setProductsType(ProductType.COLA);
        Product cola = new Product(ProductType.COLA);
        //when
        shelfImpl.add(cola);
        //then
        assertThat(shelfImpl.productsType).isEqualTo(ProductType.COLA);
        assertThat(shelfImpl.contains(cola)).isTrue();
    }

    @Test
    public void shouldNotAddProductIfTypeIsMismatch() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        shelfImpl.setProductsType(ProductType.COLA);
        Product chips = new Product(ProductType.CHIPS);
        //when
        Product result = shelfImpl.push(chips);
        //then
        assertThat(result).isNull();
    }

    @Test(expected = CannotChangeShelfProductsTypeException.class)
    public void shouldThrowExceptionWhenWeTryToChangeProductTypeOnNotEmptyListWithOtherProducts() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        shelfImpl.setProductsType(ProductType.COLA);
        Product chips = new Product(ProductType.COLA);
        shelfImpl.push(chips);
        //when
        shelfImpl.setProductsType(ProductType.CHIPS);
        //then
        //catchException
    }

}
