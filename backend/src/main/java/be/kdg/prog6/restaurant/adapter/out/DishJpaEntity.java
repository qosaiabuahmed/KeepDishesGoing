package be.kdg.prog6.restaurant.adapter.out;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dishes", schema = "restaurant")
public class DishJpaEntity {

    @Id
    @Column(name = "dish_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuJpaEntity menu;

    @Column(name = "restaurant_id", nullable = false)
    private UUID restaurantId;

    @Column(name = "published_name", nullable = false, length = 100)
    private String publishedName;

    @Column(name = "published_type", nullable = false, length = 20)
    private String publishedType;

    @ElementCollection
    @CollectionTable(
            name = "dish_published_food_tags",
            schema = "restaurant",
            joinColumns = @JoinColumn(name = "dish_id")
    )
    @Column(name = "food_tag")
    private List<String> publishedFoodTags = new ArrayList<>();

    @Column(name = "published_description", nullable = false, length = 500)
    private String publishedDescription;

    @Column(name = "published_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal publishedPrice;

    @Column(name = "published_image_url", nullable = false)
    private String publishedImageUrl;

    @Column(name = "draft_name", nullable = false, length = 100)
    private String draftName;

    @Column(name = "draft_type", nullable = false, length = 20)
    private String draftType;

    @ElementCollection
    @CollectionTable(
            name = "dish_draft_food_tags",
            schema = "restaurant",
            joinColumns = @JoinColumn(name = "dish_id")
    )
    @Column(name = "food_tag")
    private List<String> draftFoodTags = new ArrayList<>();

    @Column(name = "draft_description", nullable = false, length = 500)
    private String draftDescription;

    @Column(name = "draft_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal draftPrice;

    @Column(name = "draft_image_url", nullable = false)
    private String draftImageUrl;

    @Column(name = "publish_status", nullable = false, length = 20)
    private String publishStatus;

    @Column(name = "stock_status", nullable = false, length = 20)
    private String stockStatus;

    @Column(name = "has_pending_changes", nullable = false)
    private boolean hasPendingChanges;

    protected DishJpaEntity() {}

    public DishJpaEntity(
            UUID id,
            MenuJpaEntity menu,
            UUID restaurantId,
            String publishedName,
            String publishedType,
            List<String> publishedFoodTags,
            String publishedDescription,
            BigDecimal publishedPrice,
            String publishedImageUrl,
            String draftName,
            String draftType,
            List<String> draftFoodTags,
            String draftDescription,
            BigDecimal draftPrice,
            String draftImageUrl,
            String publishStatus,
            String stockStatus,
            boolean hasPendingChanges) {
        this.id = id;
        this.menu = menu;
        this.restaurantId = restaurantId;

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

    public UUID getId() { return id; }

    public MenuJpaEntity getMenu() { return menu; }

    public UUID getRestaurantId() { return restaurantId; }

    public String getPublishedName() { return publishedName; }

    public String getPublishedType() { return publishedType; }

    public List<String> getPublishedFoodTags() { return publishedFoodTags; }

    public String getPublishedDescription() { return publishedDescription; }

    public BigDecimal getPublishedPrice() { return publishedPrice; }

    public String getPublishedImageUrl() { return publishedImageUrl; }

    public String getDraftName() { return draftName; }

    public String getDraftType() { return draftType; }

    public List<String> getDraftFoodTags() { return draftFoodTags; }

    public String getDraftDescription() { return draftDescription; }

    public BigDecimal getDraftPrice() { return draftPrice; }

    public String getDraftImageUrl() { return draftImageUrl; }

    public String getPublishStatus() { return publishStatus; }

    public String getStockStatus() { return stockStatus; }

    public boolean isHasPendingChanges() { return hasPendingChanges; }
}