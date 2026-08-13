package br.com.fullcycle.application.infrastructure.jpa.repositories;

import br.com.fullcycle.application.infrastructure.jpa.entities.EventEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EventJpaRepository extends CrudRepository<EventEntity, UUID> {

}
