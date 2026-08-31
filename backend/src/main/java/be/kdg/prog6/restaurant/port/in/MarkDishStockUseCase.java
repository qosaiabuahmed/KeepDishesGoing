package be.kdg.prog6.restaurant.port.in;

public interface MarkDishStockUseCase {
    void markOutOfStock(MarkDishOutOfStockCommand command);
    void markInStock(MarkDishInStockCommand command);
}