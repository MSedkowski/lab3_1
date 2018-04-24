package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

public class ProductDataBuilder {
    private Id productId = Id.generate();
    private Money price = new Money(10);

    private String name = "default";

    private Date snapshotDate = new Date();

    private ProductType type = ProductType.FOOD;

    public ProductDataBuilder() {}

    public ProductDataBuilder withId(Id id) {
        this.productId = id;
        return this;
    }

    public ProductDataBuilder withPrice(Money price) {
        this.price = price;
        return this;
    }

    public ProductDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductDataBuilder withSnapshotDate(Date snapshotDate) {
        this.snapshotDate = snapshotDate;
        return this;
    }

    public ProductDataBuilder withType(ProductType type) {
        this.type = type;
        return this;
    }

    public ProductData build() {
        return new ProductData(productId, price, name, type, snapshotDate);
    }
}
