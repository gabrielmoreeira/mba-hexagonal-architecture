package br.com.fullcycle.hexagonal.application.usecases.event;

import br.com.fullcycle.hexagonal.application.repository.InMemoryCustomerRepository;
import br.com.fullcycle.hexagonal.application.repository.InMemoryEventRepository;
import br.com.fullcycle.hexagonal.application.repository.InMemoryTicketRepository;
import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubscribeCustomerToEventUseCaseTest {

    @Test
    @DisplayName("Deve comprar um ticket de um evento")
    public void testReserveTicket() throws Exception {

        // given -> Dado
        final var expectedTicketSize = 1;
        final var aPartner = Partner.newPartner("John Doe", "73.079.912/1727-06", "john.doe@gmail.com");

        final var anEvent = Event.newEvent("Disney on Ice", "2021-01-01", 10, aPartner);
        final var aCustomer = Customer.newCustomer("Gabriel Doe", "392.765.124-97", "gabriel.doe@gmail.com");

        final var customerId = aCustomer.customerId().value();
        final var eventId = anEvent.getEventId().value();

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(customerId, eventId);

        final var customerRepository = new InMemoryCustomerRepository();
        final var eventRepository = new InMemoryEventRepository();
        final var ticketRepository = new InMemoryTicketRepository();

        // when -> quando
        final var useCase = new SubscribeCustomerToEventUseCase(
                customerRepository,
                eventRepository,
                ticketRepository);

        customerRepository.create(aCustomer);
        eventRepository.create(anEvent);


        final var outPut = useCase.execute(subscribeInput);

        // then -> então
        Assertions.assertEquals(eventId, outPut.eventId());
        Assertions.assertNotNull(outPut.ticketId());
        Assertions.assertNotNull(outPut.reservationDate());
        Assertions.assertEquals(TicketStatus.PENDING.name(), outPut.ticketStatus());

        final var actualEvent = eventRepository.eventOfId(anEvent.getEventId());
        Assertions.assertEquals(expectedTicketSize, actualEvent.get().allTickets().size());

    }

    @Test
    @DisplayName("Não Deve comprar um ticket com um cliente não existente")
    public void testReserveTicketWithoutCustomer() throws Exception {

        // given -> Dado
        final var expectedError = "Customer not found";
        final var aPartner = Partner.newPartner("John Doe", "73.079.912/1727-06", "john.doe@gmail.com");
        final var anEvent = Event.newEvent("Disney on Ice", "2021-01-01", 10, aPartner);

        final var customerId = CustomerId.unique().value();
        final var eventId = anEvent.getEventId();

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(customerId, eventId.value());

        final var customerRepository = new InMemoryCustomerRepository();
        final var eventRepository = new InMemoryEventRepository();
        final var ticketRepository = new InMemoryTicketRepository();

        // when -> quando
        final var useCase = new SubscribeCustomerToEventUseCase(
                customerRepository,
                eventRepository,
                ticketRepository);

        eventRepository.create(anEvent);

        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(subscribeInput));

        // then -> então
        Assertions.assertEquals(expectedError, actualException.getMessage());

    }

    @Test
    @DisplayName("Não Deve comprar um ticket de um evento que não existe")
    public void testReserveTicketWithoutEvent() throws Exception {

        // given -> Dado
        final var expectedError = "Event not found";
        final var aCustomer = Customer.newCustomer("Gabriel Doe", "392.765.124-97", "gabriel.doe@gmail.com");

        final var customerId = aCustomer.customerId().value();
        final var eventId = EventId.unique().value();

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(customerId, eventId);

        final var customerRepository = new InMemoryCustomerRepository();
        final var eventRepository = new InMemoryEventRepository();
        final var ticketRepository = new InMemoryTicketRepository();

        // when -> quando
        final var useCase = new SubscribeCustomerToEventUseCase(
                customerRepository,
                eventRepository,
                ticketRepository);

        customerRepository.create(aCustomer);

        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(subscribeInput));

        // then -> então
        Assertions.assertEquals(expectedError, actualException.getMessage());

    }

    @Test
    @DisplayName("Um mesmo cliente não pode comprar mais de um ticket por evento")
    public void testReserveTicketMoreThanOnce() throws Exception {

        // given -> Dado
        final var expectedError = "Email already registered";

        final var expectedTicketSize = 1;
        final var aPartner = Partner.newPartner("John Doe", "73.079.912/1727-06", "john.doe@gmail.com");

        final var anEvent = Event.newEvent("Disney on Ice", "2021-01-01", 10, aPartner);
        final var aCustomer = Customer.newCustomer("Gabriel Doe", "392.765.124-97", "gabriel.doe@gmail.com");

        final var customerId = aCustomer.customerId().value();
        final var eventId = anEvent.getEventId();

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(customerId, eventId.value());

        final var customerRepository = new InMemoryCustomerRepository();
        final var eventRepository = new InMemoryEventRepository();
        final var ticketRepository = new InMemoryTicketRepository();

        // when -> quando
        final var useCase = new SubscribeCustomerToEventUseCase(
                customerRepository,
                eventRepository,
                ticketRepository);

        final var ticket = anEvent.reserveTicket(aCustomer.customerId());

        customerRepository.create(aCustomer);
        eventRepository.create(anEvent);
        ticketRepository.create(ticket);

        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(subscribeInput));

        // then -> então
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Um mesmo cliente não pode comprar de um evento que não há mais cadeiras")
    public void testReserveTicketWithoutSlots() throws Exception {

        // given -> Dado
        final var expectedError = "Event sold out";

        final var expectedTicketSize = 1;
        final var aPartner = Partner.newPartner("John Doe", "73.079.912/1727-06", "john.doe@gmail.com");

        final var anEvent = Event.newEvent("Disney on Ice", "2021-01-01", 1, aPartner);
        final var aCustomer = Customer.newCustomer("Gabriel Doe", "392.765.124-97", "gabriel.doe@gmail.com");
        final var aCustomer2 = Customer.newCustomer("Pedro Doe", "392.111.124-97", "pedro.doe@gmail.com");

        final var customerId = aCustomer.customerId().value();
        final var eventId = anEvent.getEventId();

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(customerId, eventId.value());

        final var customerRepository = new InMemoryCustomerRepository();
        final var eventRepository = new InMemoryEventRepository();
        final var ticketRepository = new InMemoryTicketRepository();

        final var ticket = anEvent.reserveTicket(aCustomer2.customerId());

        customerRepository.create(aCustomer);
        customerRepository.create(aCustomer2);
        eventRepository.create(anEvent);
        ticketRepository.create(ticket);

        // when -> quando
        final var useCase = new SubscribeCustomerToEventUseCase(
                customerRepository,
                eventRepository,
                ticketRepository);


        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(subscribeInput));

        // then -> então
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

}