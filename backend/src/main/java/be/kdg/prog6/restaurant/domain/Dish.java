package be.kdg.prog6.restaurant.domain;

import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.StockStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Dish {
    private final DishId id;

    private DishName publishedName;
    private DishType publishedType;
    private final List<FoodTag> publishedFoodTags;
    private Description publishedDescription;
    private Price publishedPrice;
    private ImageUrl publishedImageUrl;

    private DishName draftName;
    private DishType draftType;
    private final List<FoodTag> draftFoodTags;
    private Description draftDescription;
    private Price draftPrice;
    private ImageUrl draftImageUrl;

    private PublishStatus publishStatus;
    private StockStatus stockStatus;
    private boolean hasPendingChanges;

    public Dish(
            DishId id,
            DishName publishedName,
            DishType publishedType,
            List<FoodTag> publishedFoodTags,
            Description publishedDescription,
            Price publishedPrice,
            ImageUrl publishedImageUrl,
            DishName draftName,
            DishType draftType,
            List<FoodTag> draftFoodTags,
            Description draftDescription,
            Price draftPrice,
            ImageUrl draftImageUrl,
            PublishStatus publishStatus,
            StockStatus stockStatus,
            boolean hasPendingChanges) {
        this.id = id;

        this.publishedName = publishedName;
        this.publishedType = publishedType;
        this.publishedFoodTags = new ArrayList<>(publishedFoodTags);
        this.publishedDescription = publishedDescription;
        this.publishedPrice = publishedPrice;
        this.publishedImageUrl = publishedImageUrl;

        this.draftName = draftName;
        this.draftType = draftType;
        this.draftFoodTags = new ArrayList<>(draftFoodTags);
        this.draftDescription = draftDescription;
        this.draftPrice = draftPrice;
        this.draftImageUrl = draftImageUrl;

        this.publishStatus = publishStatus;
        this.stockStatus = stockStatus;
        this.hasPendingChanges = hasPendingChanges;
    }

    public void publish() {
        if (publishStatus == PublishStatus.PUBLISHED && !hasPendingChanges) {
            throw new IllegalStateException("Dish is already published with no pending changes");
        }

        this.publishedName = this.draftName;
        this.publishedType = this.draftType;
        this.publishedFoodTags.clear();
        this.publishedFoodTags.addAll(this.draftFoodTags);
        this.publishedDescription = this.draftDescription;
        this.publishedPrice = this.draftPrice;
        this.publishedImageUrl = this.draftImageUrl;

        this.publishStatus = PublishStatus.PUBLISHED;
        this.hasPendingChanges = false;
    }

    public void unpublish() {
        if (publishStatus == PublishStatus.DRAFT) {
            throw new IllegalStateException("Cannot unpublish a draft dish");
        }
        this.publishStatus = PublishStatus.UNPUBLISHED;
        this.hasPendingChanges = false;
    }

    public void markOutOfStock() {
        if (publishStatus != PublishStatus.PUBLISHED) {
            throw new IllegalStateException("Only published dishes can be marked out of stock");
        }
        this.stockStatus = StockStatus.OUT_OF_STOCK;
    }

    public void markInStock() {
        if (stockStatus != StockStatus.OUT_OF_STOCK) {
            throw new IllegalStateException("Dish is not out of stock");
        }
        this.stockStatus = StockStatus.IN_STOCK;
    }

    public void updateDetails(
            DishName name,
            DishType type,
            List<FoodTag> foodTags,
            Description description,
            Price price,
            ImageUrl imageUrl
    ) {
        this.draftName = name;
        this.draftType = type;
        this.draftFoodTags.clear();
        this.draftFoodTags.addAll(foodTags);
        this.draftDescription = description;
        this.draftPrice = price;
        this.draftImageUrl = imageUrl;
        this.hasPendingChanges = true;
    }

    public boolean isAvailableForOrder() {
        return publishStatus == PublishStatus.PUBLISHED
                && stockStatus == StockStatus.IN_STOCK;
    }

    public boolean isVisible() {
        return publishStatus == PublishStatus.PUBLISHED;
    }

    public static Dish createNew(
            DishName name,
            DishType type,
            List<FoodTag> foodTags,
            Description description,
            Price price,
            ImageUrl imageUrl
    ) {
        DishId newId = DishId.of(UUID.randomUUID().toString());
        return new Dish(
                newId,
                name, type, foodTags, description, price, imageUrl,
                name, type, foodTags, description, price, imageUrl,
                PublishStatus.DRAFT,
                StockStatus.IN_STOCK,
                false
        );
    }

    public DishId getId() { return id; }
    public DishName getName() { return publishedName; }
    public DishType getType() { return publishedType; }
    public List<FoodTag> getFoodTags() { return List.copyOf(publishedFoodTags); }
    public Description getDescription() { return publishedDescription; }
    public Price getPrice() { return publishedPrice; }
    public ImageUrl getImageUrl() { return publishedImageUrl; }
    public PublishStatus getPublishStatus() { return publishStatus; }
    public StockStatus getStockStatus() { return stockStatus; }
    public boolean hasPendingChanges() { return hasPendingChanges; }

    public DishName getDraftName() { return draftName; }
    public DishType getDraftType() { return draftType; }
    public List<FoodTag> getDraftFoodTags() { return List.copyOf(draftFoodTags); }
    public Description getDraftDescription() { return draftDescription; }
    public Price getDraftPrice() { return draftPrice; }
    public ImageUrl getDraftImageUrl() { return draftImageUrl; }
}