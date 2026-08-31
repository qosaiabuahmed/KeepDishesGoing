package be.kdg.prog6.restaurant.adapter.out;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "restaurants", schema = "restaurant")
public class RestaurantJpaEntity {

    @Id
    @Column(name = "restaurant_id")
    private UUID id;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "cuisine", nullable = false, length = 50)
    private String cuisine;

    @Embedded
    private AddressEmbeddable address;

    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    @Column(name = "default_prep_time_minutes", nullable = false)
    private int defaultPrepTimeMinutes;

    @Column(name = "opening_hours", nullable = false, length = 500)
    private String openingHours;

    @Column(name = "is_open", nullable = false)
    private boolean isOpen;


    protected RestaurantJpaEntity() {
    }

    public RestaurantJpaEntity(
            UUID id,
            UUID ownerId,
            String name,
            String cuisine,
            AddressEmbeddable address,
            String contactEmail,
            List<String> imageUrls,
            int defaultPrepTimeMinutes,
            String openingHours,
            boolean isOpen) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.cuisine = cuisine;
        this.address = address;
        this.contactEmail = contactEmail;
        this.imageUrls = new ArrayList<>(imageUrls);
        this.defaultPrepTimeMinutes = defaultPrepTimeMinutes;
        this.openingHours = openingHours;
        this.isOpen = isOpen;
    }

    public UUID getId() { return id; }

    public UUID getOwnerId() { return ownerId; }

    public String getName() { return name; }

    public String getCuisine() { return cuisine; }

    public AddressEmbeddable getAddress() { return address; }

    public String getContactEmail() { return contactEmail; }

    public List<String> getImageUrls() { return imageUrls; }

    public int getDefaultPrepTimeMinutes() { return defaultPrepTimeMinutes; }

    public String getOpeningHours() { return openingHours; }

    public boolean isOpen() { return isOpen; }

}