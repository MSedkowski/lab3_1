package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AddProductCommandHandlerTest {

    private ReservationRepository reservationRepository;
    private Reservation reservation;
    private AddProductCommand addProductCommand;
    private AddProductCommandHandler addProductCommandHandler;
    private ProductRepository productRepository;
    private Product product;
    private ClientRepository clientRepository;
    private Client client;
    private SystemContext systemContext;
    private SuggestionService suggestionService;

    @Before
    public void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        reservation = mock(Reservation.class);
        addProductCommand = new AddProductCommand(Id.generate(), Id.generate(), 12);
        addProductCommandHandler = new AddProductCommandHandler();
        productRepository = mock(ProductRepository.class);
        product = new ProductBuilder().build();
        clientRepository = mock(ClientRepository.class);
        client = new Client();
        systemContext = new SystemContext();
        suggestionService = mock(SuggestionService.class);

        Whitebox.setInternalState(addProductCommandHandler, "reservationRepository", reservationRepository);
        Whitebox.setInternalState(addProductCommandHandler, "productRepository", productRepository);
        Whitebox.setInternalState(addProductCommandHandler, "clientRepository", clientRepository);
        Whitebox.setInternalState(addProductCommandHandler, "suggestionService", suggestionService);
        Whitebox.setInternalState(addProductCommandHandler, "systemContext", systemContext);

        when(reservationRepository.load(addProductCommand.getOrderId())).thenReturn(reservation);
        when(productRepository.load(addProductCommand.getProductId())).thenReturn(product);
        when(clientRepository.load(systemContext.getSystemUser().getClientId())).thenReturn(client);
        when(suggestionService.suggestEquivalent(product, client)).thenReturn(product);
    }

    @Test
    public void ifReservationIsSavedWhenProductIsAvailable() {
        addProductCommandHandler.handle(addProductCommand);
        verify(reservationRepository, times(1)).save(reservation);
        verify(suggestionService, times(0)).suggestEquivalent(product, client);
    }

    @Test
    public void ifReservationIsSavedWhenProductIsNotAvailable() {
        addProductCommandHandler.handle(addProductCommand);
        verify(reservationRepository, times(1)).save(reservation);
        verify(suggestionService, times(1)).suggestEquivalent(product, client);
    }

}