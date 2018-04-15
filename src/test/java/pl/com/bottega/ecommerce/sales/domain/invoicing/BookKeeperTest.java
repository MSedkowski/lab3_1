package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        Money net = new Money(10);
        RequestItem item = new RequestItem(productData, 10, net);
        List<RequestItem> listOfItems = new ArrayList<>();
        listOfItems.add(item);
        when(productData.getType()).thenReturn(ProductType.FOOD);
        when(invoiceRequest.getClientData()).thenReturn(client);
        when(invoiceRequest.getItems()).thenReturn(listOfItems);
        when(taxPolicy.calculateTax(ProductType.FOOD, net)).thenReturn(new Tax(net, "jedzenie"));
        Invoice newInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(newInvoice.getItems().size(), is(1));
    }

    @Test
    public void createInvoiceWithTwoPosition() {
        productData = mock(ProductData.class);
        ProductData productData2 = mock(ProductData.class);
        Money net = new Money(10);
        RequestItem item = new RequestItem(productData, 10, net);
        RequestItem item2 = new RequestItem(productData2, 12, net);
        List<RequestItem> listOfItems = new ArrayList<>();
        listOfItems.add(item);
        listOfItems.add(item2);
        when(productData.getType()).thenReturn(ProductType.FOOD);
        when(productData2.getType()).thenReturn(ProductType.DRUG);
        when(invoiceRequest.getClientData()).thenReturn(client);
        when(invoiceRequest.getItems()).thenReturn(listOfItems);
        when(taxPolicy.calculateTax(productData.getType(), net)).thenReturn(new Tax(net, "jedzenie"));
        when(taxPolicy.calculateTax(productData2.getType(), net)).thenReturn(new Tax(net, "leki"));
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(1)).calculateTax(productData.getType(), net);
        verify(taxPolicy, times(1)).calculateTax(productData2.getType(), net);
    }

    

}