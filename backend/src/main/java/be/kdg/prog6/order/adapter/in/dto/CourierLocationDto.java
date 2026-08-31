package be.kdg.prog6.order.adapter.in.dto;

import java.time.LocalDateTime;

public record CourierLocationDto(
        Double latitude,
        Double longitude,
        LocalDateTime lastUpdated
) {}