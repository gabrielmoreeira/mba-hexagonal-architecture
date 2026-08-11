package br.com.fullcycle.hexagonal.application.domain.event;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class EventTicket {
    private final TicketId ticketId;
    private final CustomerId customerId;
    private final EventId eventId;
    private int ordering;

    protected EventTicket(final TicketId ticketId,
                          final EventId eventId,
                          final CustomerId customerId,
                          final int ordering) {
        if (ticketId == null) {
            throw new ValidationException("Invalid ticketId for EventTicket");
        }

        if (eventId == null) {
            throw new ValidationException("Invalid eventId for EventTicket");
        }

        if (customerId == null) {
            throw new ValidationException("Invalid customerId for EventTicket");
        }

        this.ticketId = ticketId;
        this.customerId = customerId;
        this.eventId = eventId;
        this.ordering = ordering;
        this.setOrdering(ordering);
    }

    public TicketId ticketId() {
        return ticketId;
    }

    public EventId eventId() {
        return eventId;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public int getOrdering() {
        return ordering;
    }

    private void setOrdering(final Integer ordering) {
        if (ordering == null) {
            throw new ValidationException("Invalid ordering for EventTicket");
        }

        this.ordering = ordering;
    }
}
