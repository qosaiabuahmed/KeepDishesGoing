package be.kdg.prog6.order.domain;

import be.kdg.prog6.common.domain.RestaurantId;

import java.math.BigDecimal;
import java.util.List;

public class RestaurantProjection {
    private final RestaurantId restaurantId;
    private String name;
    private String cuisine;
    private String street;
    private String number;
    private String postalCode;
    private String city;
    private String country;
    private String contactEmail;
    private List<String> imageUrls;
    private boolean isOpen;
    private String priceRange;
    private BigDecimal averageMenuPrice;

    public RestaurantProjection(
            RestaurantId restaurantId,
            String name,
            String cuisine,
            String street,
            String number,
            String postalCode,
            String city,
            String country,
            String contactEmail,
            List<String> imageUrls,
            boolean isOpen,
            String priceRange,
            BigDecimal averageMenuPrice) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.cuisine = cuisine;
        this.street = street;
        this.number = number;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.contactEmail = contactEmail;
        this.imageUrls = imageUrls;
        this.isOpen = isOpen;
        this.priceRange = priceRange;
        this.averageMenuPrice = averageMenuPrice;
    }

    public RestaurantId getRestaurantId() { return restaurantId; }
    public String getName() { return name; }
    public String getCuisine() { return cuisine; }
    public String getStreet() { return street; }
    public String getNumber() { return number; }
    public String getPostalCode() { return postalCode; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getContactEmail() { return contactEmail; }
    public List<String> getImageUrls() { return imageUrls; }
    public boolean isOpen() { return isOpen; }
    public String getPriceRange() { return priceRange; }
    public BigDecimal getAverageMenuPrice() { return averageMenuPrice; }

    public BigDecimal getAverageMenuPriceOrZero() {
        return averageMenuPrice != null ? averageMenuPrice : BigDecimal.ZERO;
    }

    public void setOpen(boolean open) { isOpen = open; }
    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }
    public void setAverageMenuPrice(BigDecimal averageMenuPrice) { this.averageMenuPrice = averageMenuPrice; }
}