package tdd.vendingMachine;

import java.math.BigDecimal;

/**
 * @author macko
 * @since 2015-08-19
 */
public class Product {

    private final ProductType productType;

    public Product(ProductType productType) {
        this.productType = productType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return productType.equals(product.productType);

    }

    @Override
    public int hashCode() {
        return productType.hashCode();
    }

    public static class ProductType {
        private final String name;
        private final BigDecimal price;

        public ProductType(String name, String price) {
            this.name = name;
            this.price = new BigDecimal(price);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ProductType that = (ProductType) o;

            if (!name.equals(that.name)) return false;
            return price.equals(that.price);

        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + price.hashCode();
            return result;
        }
    }
}
