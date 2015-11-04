package pl.codeweaver

class Merchandise(vararg val products: ProductAndRow) : Iterable<ProductAndRow> {
    fun price(row: Int) = products.find { it.row == row }?.price.orEmpty()
    override fun iterator(): Iterator<ProductAndRow> = products.iterator()
}
