package br.com.fullcycle.application.infrastructure.jpa.repositories;

import br.com.fullcycle.application.infrastructure.jpa.entities.TicketEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TicketJpaRepository extends CrudRepository<TicketEntity, UUID> {
}
