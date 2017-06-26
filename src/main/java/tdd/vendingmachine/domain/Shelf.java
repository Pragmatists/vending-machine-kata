package tdd.vendingmachine.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import tdd.vendingmachine.domain.dto.ShelfDto;

@EqualsAndHashCode(of = "number")
@ToString
class Shelf {

    private final ShelfNumber number;
    private final ProductType productType;
    private final ProductCount productCount;

    private Shelf(ShelfNumber number, ProductType productType, ProductCount productCount) {
        this.number = number;
        this.productType = productType;
        this.productCount = productCount;
    }

    static Shelf create(ShelfDto shelfDto) {
        ShelfNumber shelfNumber = new ShelfNumber(shelfDto.getShelfNumber());
        ProductType productType = ProductType.create(shelfDto.getProductType());
        ProductCount productCount = new ProductCount(shelfDto.getProductCount());
        return new Shelf(shelfNumber, productType, productCount);
    }

    boolean isEmpty() {
        return productCount.isZero();
    }

    Price price() {
        return productType.price();
    }
}
