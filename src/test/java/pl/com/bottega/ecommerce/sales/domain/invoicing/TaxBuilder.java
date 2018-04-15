package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sharedkernel.Money;

public class TaxBuilder {

    Money amount = new Money(120);
    String description = "Jedzenie";

    public TaxBuilder() { }

    public TaxBuilder withAmount(Money amount) {
        this.amount = amount;
        return this;
    }

    public TaxBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public Tax build() {
        return new Tax(amount, description);
    }
}
