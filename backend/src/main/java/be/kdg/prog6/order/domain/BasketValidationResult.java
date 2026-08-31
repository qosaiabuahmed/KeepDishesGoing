package be.kdg.prog6.order.domain;

import be.kdg.prog6.common.domain.DishId;

import java.util.ArrayList;
import java.util.List;

public class BasketValidationResult {
    private final boolean valid;
    private final List<DishValidationIssue> issues;

    public BasketValidationResult(boolean valid, List<DishValidationIssue> issues) {
        this.valid = valid;
        this.issues = new ArrayList<>(issues);
    }

    public static BasketValidationResult valid() {
        return new BasketValidationResult(true, List.of());
    }

    public static BasketValidationResult invalid(List<DishValidationIssue> issues) {
        return new BasketValidationResult(false, issues);
    }

    public boolean isValid() {
        return valid;
    }

    public List<DishValidationIssue> getIssues() {
        return List.copyOf(issues);
    }

    public record DishValidationIssue(
            DishId dishId,
            String dishName,
            ValidationIssueType issueType,
            String message
    ) {}

    public enum ValidationIssueType {
        DISH_NOT_FOUND,
        DISH_UNPUBLISHED,
        DISH_OUT_OF_STOCK
    }
}