package be.kdg.prog6.order.adapter.in.dto;

public record ValidationIssueDto(
        String dishId,
        String dishName,
        String issueType,
        String message
) {}