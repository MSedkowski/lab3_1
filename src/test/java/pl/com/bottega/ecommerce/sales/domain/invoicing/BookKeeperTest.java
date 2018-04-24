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
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
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
    private Money net;
    private RequestItem item;

    @Before
    public void setUp() {
        bookKeeper = new BookKeeper(invoiceFactory);
        client = new ClientData(Id.generate(), "Janek");
        productData = new ProductDataBuilder().build();
        net = new Money(10);
        item = new RequestItemBuilder().build();
    }

    @Test
    public void createInvoiceWithOnePosition() {
        List<RequestItem> listOfItems = new ArrayList<>();
        listOfItems.add(item);
        Tax tax = new TaxBuilder()
                        .withAmount(net)
                        .build();

        when(invoiceRequest.getClientData()).thenReturn(client);
        when(invoiceRequest.getItems()).thenReturn(listOfItems);
        when(taxPolicy.calculateTax(item.getProductData().getType(), net)).thenReturn(tax);

        Invoice newInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertThat(newInvoice.getItems().size(), is(1));
    }

    @Test
    public void createInvoiceWithTwoPosition() {
        ProductData productData2 = new ProductDataBuilder()
                                            .withName("default2")
                                            .withPrice(new Money(12))
                                            .withType(ProductType.DRUG)
                                            .build();
        RequestItem item2 = new RequestItemBuilder()
                                            .withProductData(productData2)
                                            .withQuantity(12)
                                            .build();
        List<RequestItem> listOfItems = new ArrayList<>();
        listOfItems.add(item);
        listOfItems.add(item2);
        Tax tax = new TaxBuilder()
                .withAmount(net)
                .build();
        Tax tax2 = new TaxBuilder()
                .withAmount(net)
                .withDescription("Leki")
                .build();


        when(invoiceRequest.getClientData()).thenReturn(client);
        when(invoiceRequest.getItems()).thenReturn(listOfItems);
        when(taxPolicy.calculateTax(item.getProductData().getType(), net)).thenReturn(tax);
        when(taxPolicy.calculateTax(item2.getProductData().getType(), net)).thenReturn(tax2);

        bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(1)).calculateTax(item.getProductData().getType(), net);
        verify(taxPolicy, times(1)).calculateTax(item2.getProductData().getType(), net);
    }


    @Test
    public void createInvoiceWithTwoSamePositions() {
        List<RequestItem> listOfItems = new ArrayList<>();
        listOfItems.add(item);
        listOfItems.add(item);
        Tax tax = new TaxBuilder()
                .withAmount(net)
                .build();

        when(invoiceRequest.getClientData()).thenReturn(client);
        when(invoiceRequest.getItems()).thenReturn(listOfItems);
        when(taxPolicy.calculateTax(item.getProductData().getType(), net)).thenReturn(tax);

        Invoice newInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(2)).calculateTax(item.getProductData().getType(), net);
        assertThat(newInvoice.getItems().size(), is(2));
    }

    @Test
    public void createInvoiceWithoutAnyPosition() {
        when(invoiceRequest.getClientData()).thenReturn(client);
        when(invoiceRequest.getItems()).thenReturn(new ArrayList<RequestItem>());

        Invoice newInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertThat(newInvoice.getItems().size(), is(0));
    }

}