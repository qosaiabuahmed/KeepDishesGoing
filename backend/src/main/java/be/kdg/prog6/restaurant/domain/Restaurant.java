package be.kdg.prog6.restaurant.domain;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.common.events.RestaurantClosedEvent;
import be.kdg.prog6.common.events.RestaurantCreatedEvent;
import be.kdg.prog6.common.events.RestaurantOpenedEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Restaurant {
    private final RestaurantId id;
    private final OwnerId ownerId;
    private RestaurantName name;
    private CuisineType cuisine;
    private Address address;
    private ContactEmail contactEmail;
    private final List<ImageUrl> imageUrls;
    private PrepTime defaultPrepTime;
    private OpeningHours openingHours;
    private boolean isOpen;
    private final List<Object> domainEvents = new ArrayList<>();

    public Restaurant(
            RestaurantId id,
            OwnerId ownerId,
            RestaurantName name,
            CuisineType cuisine,
            Address address,
            ContactEmail contactEmail,
            List<ImageUrl> imageUrls,
            PrepTime defaultPrepTime,
            OpeningHours openingHours,
            boolean isOpen) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.cuisine = cuisine;
        this.address = address;
        this.contactEmail = contactEmail;
        this.imageUrls = new ArrayList<>(imageUrls);
        this.defaultPrepTime = defaultPrepTime;
        this.openingHours = openingHours;
        this.isOpen = isOpen;
    }

    public void openRestaurant() {
        if (this.isOpen) {
            throw new IllegalStateException("Restaurant is already manually opened");
        }
        this.isOpen = true;
        domainEvents.add(new RestaurantOpenedEvent(this.id, LocalDateTime.now()));
    }


    public void closeRestaurant() {
        if (!this.isOpen) {
            throw new IllegalStateException("Restaurant is already manually closed");
        }
        this.isOpen = false;
        domainEvents.add(new RestaurantClosedEvent(this.id, LocalDateTime.now()));
    }


    public boolean canAcceptOrders() {
        if (!isOpen) {
            return false;
        }

        return openingHours.isOpenNow();
    }


    public RestaurantStatus getCurrentStatus() {
        boolean scheduledOpen = openingHours.isOpenNow();

        if (!isOpen) {
            return RestaurantStatus.MANUALLY_CLOSED;
        } else if (scheduledOpen) {
            return RestaurantStatus.OPEN_BY_SCHEDULE;
        } else {
            return RestaurantStatus.MANUALLY_OPENED;
        }
    }

    public static Restaurant createNew(
            OwnerId ownerId,
            RestaurantName name,
            CuisineType cuisine,
            Address address,
            ContactEmail contactEmail,
            List<ImageUrl> imageUrls,
            PrepTime defaultPrepTime,
            OpeningHours openingHours
    ) {
        RestaurantId newId = RestaurantId.of(UUID.randomUUID().toString());
        boolean isOpenBySchedule = openingHours.isOpenNow();
        Restaurant restaurant = new Restaurant(
                newId,
                ownerId,
                name,
                cuisine,
                address,
                contactEmail,
                imageUrls,
                defaultPrepTime,
                openingHours,
                isOpenBySchedule
        );

        restaurant.domainEvents.add(new RestaurantCreatedEvent(
                newId,
                name.value(),
                cuisine.name(),
                address.street(),
                address.number(),
                address.postalCode(),
                address.city(),
                address.country(),
                contactEmail.value(),
                imageUrls.stream().map(ImageUrl::value).toList(),
                isOpenBySchedule,
                LocalDateTime.now()
        ));

        return restaurant;
    }

    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    public RestaurantId getId() { return id; }
    public OwnerId getOwnerId() { return ownerId; }
    public RestaurantName getName() { return name; }
    public CuisineType getCuisine() { return cuisine; }
    public Address getAddress() { return address; }
    public ContactEmail getContactEmail() { return contactEmail; }
    public List<ImageUrl> getImageUrls() { return Collections.unmodifiableList(imageUrls); }
    public PrepTime getDefaultPrepTime() { return defaultPrepTime; }
    public OpeningHours getOpeningHours() { return openingHours; }
    public boolean isOpen() { return isOpen; }

    public enum RestaurantStatus {
        OPEN_BY_SCHEDULE,
        MANUALLY_OPENED,
        MANUALLY_CLOSED
    }
}