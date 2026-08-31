package be.kdg.prog6.order.adapter.in.dto;

import java.util.List;

public record ValidationResultDto(
        boolean valid,
        List<ValidationIssueDto> issues
) {}