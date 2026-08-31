package be.kdg.prog6.restaurant.port.in;

public interface PublishAllPendingChangesUseCase {
    void publishAllPendingChanges(PublishAllPendingChangesCommand command);
}