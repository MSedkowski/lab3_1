package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.mockito.Mockito.mock;

public class RequestItemBuilder {
    ProductData productData = mock(ProductData.class);
    int quantity = 10;
    Money totalCost = new Money(10);

    public RequestItemBuilder() { }

    public RequestItemBuilder withProductData(ProductData productData) {
        this.productData = productData;
        return this;
    }

    public RequestItemBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public RequestItemBuilder withTotalCost(Money totalCost) {
        this.totalCost = totalCost;
        return this;
    }

    public RequestItem build() {
        return new RequestItem(productData, quantity, totalCost);
    }

}
