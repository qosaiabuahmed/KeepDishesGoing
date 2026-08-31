package be.kdg.prog6.restaurant.adapter.in.scheduler;

import be.kdg.prog6.restaurant.port.in.ExecuteScheduledPublicationsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PublicationScheduler {

    private static final Logger log = LoggerFactory.getLogger(PublicationScheduler.class);

    private final ExecuteScheduledPublicationsUseCase executeScheduledPublicationsUseCase;

    public PublicationScheduler(ExecuteScheduledPublicationsUseCase executeScheduledPublicationsUseCase) {
        this.executeScheduledPublicationsUseCase = executeScheduledPublicationsUseCase;
    }

    @Scheduled(cron = "0 * * * * *")
    public void executeScheduledPublications() {
        log.debug("Running scheduled publication check");
        executeScheduledPublicationsUseCase.executeScheduledPublications();
    }
}