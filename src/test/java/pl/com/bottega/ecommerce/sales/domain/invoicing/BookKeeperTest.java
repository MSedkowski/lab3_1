package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookKeeperTest {

    private BookKeeper bookKeeper;
    private InvoiceFactory invoiceFactory = new InvoiceFactory();
    @Mock
    private TaxPolicy taxPolicy;
    @Mock
    private InvoiceRequest invoiceRequest;

    private ClientData client;

    private ProductData productData;

    @Before
    public void setUp() {
        bookKeeper = new BookKeeper(invoiceFactory);
        client = new ClientData(Id.generate(), "Janek");
    }

    @Test
    public void createInvoiceWithOnePosition() {
        productData = mock(ProductData.class);
        Tax tax = mock(Tax.class);
        Money net = new Money(10);
        RequestItem item = new RequestItem(productData, 10, net);
        List<RequestItem> listOfItems = new ArrayList<>();
        listOfItems.add(item);
        when(productData.getType()).thenReturn(ProductType.FOOD);
        when(invoiceRequest.getClientData()).thenReturn(client);
        when(invoiceRequest.getItems()).thenReturn(listOfItems);
        when(taxPolicy.calculateTax(ProductType.FOOD, net)).thenReturn(tax);
        when(tax.getAmount()).thenReturn(net);
        Invoice newInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(newInvoice.getItems().size(), is(1));
    }

}