package be.kdg.prog6.restaurant.core;

import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.restaurant.domain.Menu;
import be.kdg.prog6.restaurant.domain.ScheduledPublication;
import be.kdg.prog6.restaurant.port.in.ExecuteScheduledPublicationsUseCase;
import be.kdg.prog6.restaurant.port.out.LoadMenuPort;
import be.kdg.prog6.restaurant.port.out.LoadScheduledPublicationPort;
import be.kdg.prog6.restaurant.port.out.SaveMenuPort;
import be.kdg.prog6.restaurant.port.out.SaveScheduledPublicationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.time.Instant;
import java.util.List;

@Service
public class ExecuteScheduledPublicationsUseCaseImpl implements ExecuteScheduledPublicationsUseCase {

    private static final Logger log = LoggerFactory.getLogger(ExecuteScheduledPublicationsUseCaseImpl.class);

    private final LoadScheduledPublicationPort loadScheduledPublicationPort;
    private final SaveScheduledPublicationPort saveScheduledPublicationPort;
    private final LoadMenuPort loadMenuPort;
    private final List<SaveMenuPort> saveMenuPorts;

    public ExecuteScheduledPublicationsUseCaseImpl(
            LoadScheduledPublicationPort loadScheduledPublicationPort,
            SaveScheduledPublicationPort saveScheduledPublicationPort,
            LoadMenuPort loadMenuPort,
            List<SaveMenuPort> saveMenuPorts) {
        this.loadScheduledPublicationPort = loadScheduledPublicationPort;
        this.saveScheduledPublicationPort = saveScheduledPublicationPort;
        this.loadMenuPort = loadMenuPort;
        this.saveMenuPorts = saveMenuPorts;
    }

    @Transactional
    @Override
    public void executeScheduledPublications() {
        List<ScheduledPublication> pendingPublications =
                loadScheduledPublicationPort.findPendingPublicationsReadyForExecution(Instant.now());

        log.debug("Found {} scheduled publications ready for execution", pendingPublications.size());

        for (ScheduledPublication scheduledPublication : pendingPublications) {
            try {
                executePublication(scheduledPublication);
            } catch (Exception e) {
                log.error("Failed to execute scheduled publication {}: {}",
                        scheduledPublication.getId().value(), e.getMessage(), e);
            }
        }
    }

    private void executePublication(ScheduledPublication scheduledPublication) {
        log.info("Executing scheduled publication {} for restaurant {}",
                scheduledPublication.getId().value(),
                scheduledPublication.getRestaurantId().value());

        Menu menu = loadMenuPort.loadByRestaurantId(scheduledPublication.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        for (DishId dishId : scheduledPublication.getDishIdsToPublish()) {
            try {
                menu.publishDish(dishId);
                log.debug("Published dish {} as part of scheduled publication", dishId.value());
            } catch (Exception e) {
                log.error("Failed to publish dish {}: {}", dishId.value(), e.getMessage());
            }
        }

        for (DishId dishId : scheduledPublication.getDishIdsToUnpublish()) {
            try {
                menu.unpublishDish(dishId);
                log.debug("Unpublished dish {} as part of scheduled publication", dishId.value());
            } catch (Exception e) {
                log.error("Failed to unpublish dish {}: {}", dishId.value(), e.getMessage());
            }
        }

        saveMenuPorts.forEach(port -> port.save(menu));
        menu.clearDomainEvents();

        scheduledPublication.markAsExecuted();
        saveScheduledPublicationPort.save(scheduledPublication);

        log.info("Successfully executed scheduled publication {}", scheduledPublication.getId().value());
    }
}