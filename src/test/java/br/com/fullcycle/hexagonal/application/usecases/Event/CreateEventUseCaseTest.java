package br.com.fullcycle.hexagonal.application.usecases.Event;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.event.CreateEventUseCase;
import br.com.fullcycle.hexagonal.infrastructure.models.Event;
import br.com.fullcycle.hexagonal.infrastructure.models.Partner;
import br.com.fullcycle.hexagonal.infrastructure.services.EventService;
import br.com.fullcycle.hexagonal.infrastructure.services.PartnerService;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class CreateEventUseCaseTest {

    @Test
    @DisplayName("Deve criar um evento")
    public void testCreate() throws Exception {

        // given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;
        final var expectedPartnerId = TSID.fast().toLong();

        final var createInput =
                new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId, expectedTotalSpots);

        // when
        final var eventService = Mockito.mock(EventService.class);
        final var partnerService = Mockito.mock(PartnerService.class);

        when(partnerService.findById(eq(expectedPartnerId)))
                .thenReturn(Optional.of(new Partner()));

        when(eventService.save(any())).thenAnswer(a -> {
            final var e = a.getArgument(0, Event.class);
            e.setId(TSID.fast().toLong());
            return e;
        });

        final var useCase = new CreateEventUseCase(eventService, partnerService);
        final var outPut = useCase.execute(createInput);

        // then
        Assertions.assertNotNull(outPut.id());
        Assertions.assertEquals(expectedDate, outPut.date());
        Assertions.assertEquals(expectedName, outPut.name());
        Assertions.assertEquals(expectedTotalSpots, outPut.totalSpots());

    }

    @Test
    @DisplayName("Não deve criar um evento quando o Partner não for encontrado")
    public void testCreateEventWhenPartnerDoesntExistsShouldThrowError() throws Exception {

        // given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;
        final var expectedPartnerId = TSID.fast().toLong();
        final var expectedError = "Partner not found";

        final var createInput =
                new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId, expectedTotalSpots);

        // when
        final var eventService = Mockito.mock(EventService.class);
        final var partnerService = Mockito.mock(PartnerService.class);

        when(partnerService.findById(eq(expectedPartnerId)))
                .thenReturn(Optional.empty());

        final var useCase = new CreateEventUseCase(eventService, partnerService);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());

    }
}
