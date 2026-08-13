package br.com.fullcycle.application.infrastructure.dtos;

public record NewEventDTO(
        String name,
        String date,
        Integer totalSpots,
        String partnerId
) {
}
