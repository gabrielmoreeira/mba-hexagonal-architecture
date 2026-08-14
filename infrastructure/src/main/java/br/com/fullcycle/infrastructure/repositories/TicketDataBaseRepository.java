package br.com.fullcycle.infrastructure.repositories;

import br.com.fullcycle.hexagonal.application.domain.event.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketId;
import br.com.fullcycle.hexagonal.application.repositories.TicketRepository;
import br.com.fullcycle.infrastructure.jpa.entities.TicketEntity;
import br.com.fullcycle.infrastructure.jpa.repositories.TicketJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

// Interface adapter
@Component
public class TicketDataBaseRepository implements TicketRepository {

    private final TicketJpaRepository ticketJpaRepository;


    public TicketDataBaseRepository(TicketJpaRepository ticketJpaRepository) {
        this.ticketJpaRepository = Objects.requireNonNull(ticketJpaRepository);
    }

    @Override
    public Optional<Ticket> ticketOfId(TicketId anId) {
        Objects.requireNonNull(anId, "id cannot be null");
        return this.ticketJpaRepository.findById(UUID.fromString(anId.value()))
                .map(TicketEntity::toTicket);
    }

    @Override
    public Ticket create(Ticket ticket) {
        return this.ticketJpaRepository.save(TicketEntity.of(ticket))
                .toTicket();
    }

    @Override
    public Ticket update(Ticket ticket) {
        return this.ticketJpaRepository.save(TicketEntity.of(ticket))
                .toTicket();
    }

    @Override
    public void deleteAll() {
        this.ticketJpaRepository.deleteAll();
    }
}
