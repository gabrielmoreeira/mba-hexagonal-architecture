package br.com.fullcycle.domain.event;

import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.ticket.TicketId;
import br.com.fullcycle.domain.exceptions.ValidationException;

public class EventTicket {

    private final EventTicketId eventTicketId;
    private final EventId eventId;
    private final CustomerId customerId;
    private TicketId ticketId;
    private int ordering;

    public EventTicket(final EventTicketId eventTicketId,
                       final EventId eventId,
                       final CustomerId customerId,
                       final TicketId ticketId,
                       final int ordering) {

        if (eventTicketId == null) {
            throw new ValidationException("Invalid eventTicketId for EventTicket");
        }

        if (eventId == null) {
            throw new ValidationException("Invalid eventId for EventTicket");
        }

        if (customerId == null) {
            throw new ValidationException("Invalid customerId for EventTicket");
        }

        this.eventTicketId = eventTicketId;
        this.customerId = customerId;
        this.eventId = eventId;
        this.ordering = ordering;
        this.ticketId = ticketId;
        this.setOrdering(ordering);
    }

    public EventTicket associateTicket(final TicketId aTicket) {
        this.ticketId = aTicket;
        return this;
    }

    public static EventTicket newTicket(final EventId eventId, final CustomerId customerId, final int ordering) {
        return new EventTicket(EventTicketId.unique(), eventId, customerId, null, ordering);
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

    public EventTicketId eventTicketId() {
        return eventTicketId;
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
