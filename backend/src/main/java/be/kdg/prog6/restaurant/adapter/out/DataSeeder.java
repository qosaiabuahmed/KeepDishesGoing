package be.kdg.prog6.restaurant.adapter.out;

import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.adapter.in.request.CreateAddressRequest;
import be.kdg.prog6.restaurant.port.in.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final GetAllRestaurantsUseCase getAllRestaurantsUseCase;
    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final CreateDishUseCase createDishUseCase;
    private final PublishDishUseCase publishDishUseCase;

    public DataSeeder(
            GetAllRestaurantsUseCase getAllRestaurantsUseCase,
            CreateRestaurantUseCase createRestaurantUseCase,
            CreateDishUseCase createDishUseCase,
            PublishDishUseCase publishDishUseCase) {
        this.getAllRestaurantsUseCase = getAllRestaurantsUseCase;
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.createDishUseCase = createDishUseCase;
        this.publishDishUseCase = publishDishUseCase;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!getAllRestaurantsUseCase.getAllRestaurants().isEmpty()) {
            log.info("Database already contains restaurants. Skipping seeding.");
            return;
        }

        log.info("Starting database seeding...");

        try {
            RestaurantId pizzaPlace = createPizzaPlace();
            RestaurantId burgerBar = createBurgerBar();
            RestaurantId sushiSpot = createSushiSpot();

            log.info("Database seeding completed successfully!");
            log.info("Created 3 restaurants with dishes");
        } catch (Exception e) {
            log.error("Error during database seeding: {}", e.getMessage(), e);
        }
    }

    private RestaurantId createPizzaPlace() {
        log.info("Creating Pizza Place restaurant...");

        String ownerId = UUID.randomUUID().toString();
        CreateRestaurantCommand restaurantCommand = new CreateRestaurantCommand(
                ownerId,
                "Bella Italia Pizzeria",
                "ITALIAN",
                new CreateAddressRequest("Via Roma", "42", "1000", "Brussels", "Belgium"),
                "info@bellaitalia.be",
                List.of(
                        "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4",
                        "https://images.unsplash.com/photo-1414235077428-338989a2e8c0",
                        "https://images.unsplash.com/photo-1555396273-367ea4eb4db5",
                        "https://images.unsplash.com/photo-1552566626-52f8b828add9"
                ),
                25,
                "MONDAY:11:00-22:00,TUESDAY:11:00-22:00,WEDNESDAY:11:00-22:00,THURSDAY:11:00-22:00,FRIDAY:11:00-22:00,SATURDAY:11:00-22:00,SUNDAY:11:00-22:00"
        );

        RestaurantId restaurantId = createRestaurantUseCase.createRestaurant(restaurantCommand);
        String restaurantIdStr = restaurantId.value().toString();

        createAndPublishDish(restaurantIdStr, "Margherita Pizza", "MAIN",
                List.of("VEGETARIAN", "GLUTEN", "LACTOSE"),
                "Classic pizza with tomato sauce, mozzarella, and fresh basil",
                new BigDecimal("12.50"),
                "https://images.unsplash.com/photo-1574071318508-1cdbab80d002");

        createAndPublishDish(restaurantIdStr, "Pepperoni Pizza", "MAIN",
                List.of("GLUTEN", "LACTOSE", "SPICY"),
                "Pizza with tomato sauce, mozzarella, and spicy pepperoni",
                new BigDecimal("14.00"),
                "https://images.unsplash.com/photo-1628840042765-356cda07504e");

        createAndPublishDish(restaurantIdStr, "Quattro Formaggi", "MAIN",
                List.of("VEGETARIAN", "GLUTEN", "LACTOSE"),
                "Four cheese pizza: mozzarella, gorgonzola, parmesan, and taleggio",
                new BigDecimal("15.50"),
                "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38");

        createAndPublishDish(restaurantIdStr, "Bruschetta", "STARTER",
                List.of("VEGETARIAN", "VEGAN", "GLUTEN"),
                "Grilled bread with tomatoes, garlic, basil, and olive oil",
                new BigDecimal("7.00"),
                "https://images.unsplash.com/photo-1572695157366-5e585ab2b69f");

        createAndPublishDish(restaurantIdStr, "Tiramisu", "DESSERT",
                List.of("VEGETARIAN", "GLUTEN", "LACTOSE"),
                "Classic Italian dessert with coffee-soaked ladyfingers and mascarpone",
                new BigDecimal("6.50"),
                "https://images.unsplash.com/photo-1571877227200-a0d98ea607e9");

        log.info("Pizza Place created with 5 dishes");
        return restaurantId;
    }

    private RestaurantId createBurgerBar() {
        log.info("Creating Burger Bar restaurant...");

        String ownerId = UUID.randomUUID().toString();
        CreateRestaurantCommand restaurantCommand = new CreateRestaurantCommand(
                ownerId,
                "The Burger Joint",
                "AMERICAN",
                new CreateAddressRequest("Burger Street", "15", "2000", "Antwerp", "Belgium"),
                "contact@burgerjoint.be",
                List.of(
                        "https://images.unsplash.com/photo-1550547660-d9450f859349",
                        "https://images.unsplash.com/photo-1571091718767-18b5b1457add",
                        "https://images.unsplash.com/photo-1466978913421-dad2ebd01d17",
                        "https://images.unsplash.com/photo-1559329007-40df8a9345d8"
                ),
                20,
                "MONDAY:12:00-23:00,TUESDAY:12:00-23:00,WEDNESDAY:12:00-23:00,THURSDAY:12:00-23:00,FRIDAY:12:00-23:00,SATURDAY:12:00-23:00,SUNDAY:12:00-23:00"
        );

        RestaurantId restaurantId = createRestaurantUseCase.createRestaurant(restaurantCommand);
        String restaurantIdStr = restaurantId.value().toString();

        createAndPublishDish(restaurantIdStr, "Classic Cheeseburger", "MAIN",
                List.of("GLUTEN", "LACTOSE"),
                "Beef patty with cheddar cheese, lettuce, tomato, and special sauce",
                new BigDecimal("11.00"),
                "https://images.unsplash.com/photo-1568901346375-23c9450c58cd");

        createAndPublishDish(restaurantIdStr, "BBQ Bacon Burger", "MAIN",
                List.of("GLUTEN", "LACTOSE", "SPICY"),
                "Beef patty with bacon, BBQ sauce, crispy onions, and cheddar",
                new BigDecimal("13.50"),
                "https://images.unsplash.com/photo-1553979459-d2229ba7433b");

        createAndPublishDish(restaurantIdStr, "Veggie Burger", "MAIN",
                List.of("VEGETARIAN", "VEGAN", "GLUTEN"),
                "Plant-based patty with avocado, lettuce, tomato, and vegan mayo",
                new BigDecimal("12.00"),
                "https://images.unsplash.com/photo-1520072959219-c595dc870360");

        createAndPublishDish(restaurantIdStr, "Buffalo Wings", "STARTER",
                List.of("SPICY", "LACTOSE"),
                "Crispy chicken wings with buffalo sauce and blue cheese dip",
                new BigDecimal("8.50"),
                "https://images.unsplash.com/photo-1527477396000-e27163b481c2");

        createAndPublishDish(restaurantIdStr, "Chocolate Brownie", "DESSERT",
                List.of("VEGETARIAN", "GLUTEN", "LACTOSE", "NUTS"),
                "Rich chocolate brownie served warm with vanilla ice cream",
                new BigDecimal("6.00"),
                "https://images.unsplash.com/photo-1606313564200-e75d5e30476c");

        log.info("Burger Bar created with 5 dishes");
        return restaurantId;
    }

    private RestaurantId createSushiSpot() {
        log.info("Creating Sushi Spot restaurant...");

        String ownerId = UUID.randomUUID().toString();
        CreateRestaurantCommand restaurantCommand = new CreateRestaurantCommand(
                ownerId,
                "Tokyo Sushi House",
                "JAPANESE",
                new CreateAddressRequest("Sakura Lane", "8", "9000", "Ghent", "Belgium"),
                "hello@tokyosushi.be",
                List.of(
                        "https://images.unsplash.com/photo-1579584425555-c3ce17fd4351",
                        "https://images.unsplash.com/photo-1555992336-fb0d29498b13",
                        "https://images.unsplash.com/photo-1578474846511-04ba529f0b88",
                        "https://images.unsplash.com/photo-1590846406792-0adc7f938f1d"
                ),
                30,
                "TUESDAY:17:00-22:00,WEDNESDAY:17:00-22:00,THURSDAY:17:00-22:00,FRIDAY:17:00-22:00,SATURDAY:17:00-22:00,SUNDAY:17:00-22:00"
        );

        RestaurantId restaurantId = createRestaurantUseCase.createRestaurant(restaurantCommand);
        String restaurantIdStr = restaurantId.value().toString();

        createAndPublishDish(restaurantIdStr, "Salmon Nigiri Set", "MAIN",
                List.of("SEAFOOD"),
                "Fresh salmon nigiri sushi, 6 pieces",
                new BigDecimal("16.00"),
                "https://images.unsplash.com/photo-1579584425555-c3ce17fd4351");

        createAndPublishDish(restaurantIdStr, "California Roll", "MAIN",
                List.of("SEAFOOD", "GLUTEN"),
                "Classic California roll with crab, avocado, and cucumber, 8 pieces",
                new BigDecimal("12.00"),
                "https://images.unsplash.com/photo-1579584425555-c3ce17fd4351");

        createAndPublishDish(restaurantIdStr, "Dragon Roll", "MAIN",
                List.of("SEAFOOD", "GLUTEN"),
                "Eel and avocado roll topped with avocado slices, 8 pieces",
                new BigDecimal("18.50"),
                "https://images.unsplash.com/photo-1579584425555-c3ce17fd4351");

        createAndPublishDish(restaurantIdStr, "Edamame", "STARTER",
                List.of("VEGETARIAN", "VEGAN"),
                "Steamed soybeans with sea salt",
                new BigDecimal("5.50"),
                "https://images.unsplash.com/photo-1608039755401-742074f0548d");

        createAndPublishDish(restaurantIdStr, "Miso Soup", "STARTER",
                List.of("VEGETARIAN", "VEGAN"),
                "Traditional Japanese miso soup with tofu and seaweed",
                new BigDecimal("4.00"),
                "https://images.unsplash.com/photo-1547592166-23ac45744acd");

        createAndPublishDish(restaurantIdStr, "Green Tea Ice Cream", "DESSERT",
                List.of("VEGETARIAN", "LACTOSE"),
                "Authentic Japanese green tea ice cream",
                new BigDecimal("6.00"),
                "https://images.unsplash.com/photo-1563805042-7684c019e1cb");

        log.info("Sushi Spot created with 6 dishes");
        return restaurantId;
    }

    private void createAndPublishDish(String restaurantId, String name, String type,
                                      List<String> foodTags, String description,
                                      BigDecimal price, String imageUrl) {
        CreateDishCommand dishCommand = new CreateDishCommand(
                restaurantId,
                name,
                type,
                foodTags,
                description,
                price,
                imageUrl
        );

        DishId dishId = createDishUseCase.createDish(dishCommand);

        PublishDishCommand publishCommand = new PublishDishCommand(
                restaurantId,
                dishId.value().toString()
        );

        publishDishUseCase.publishDish(publishCommand);
    }
}