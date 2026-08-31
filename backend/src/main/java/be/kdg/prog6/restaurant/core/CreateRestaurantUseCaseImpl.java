package be.kdg.prog6.restaurant.core;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.adapter.in.request.CreateAddressRequest;
import be.kdg.prog6.restaurant.domain.OwnerId;
import be.kdg.prog6.restaurant.domain.*;
import be.kdg.prog6.restaurant.port.in.CreateRestaurantCommand;
import be.kdg.prog6.restaurant.port.in.CreateRestaurantUseCase;
import be.kdg.prog6.restaurant.port.out.LoadRestaurantPort;
import be.kdg.prog6.restaurant.port.out.SaveRestaurantPort;
import be.kdg.prog6.restaurant.port.out.SaveMenuPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CreateRestaurantUseCaseImpl implements CreateRestaurantUseCase {

    private final LoadRestaurantPort loadRestaurantPort;
    private final List<SaveRestaurantPort> saveRestaurantPorts;
    private final List<SaveMenuPort> saveMenuPorts;

    public CreateRestaurantUseCaseImpl(
            LoadRestaurantPort loadRestaurantPort,
            List<SaveRestaurantPort> saveRestaurantPorts,
            List<SaveMenuPort> saveMenuPorts) {
        this.loadRestaurantPort = loadRestaurantPort;
        this.saveRestaurantPorts = saveRestaurantPorts;
        this.saveMenuPorts = saveMenuPorts;
    }

    @Override
    public RestaurantId createRestaurant(CreateRestaurantCommand command) {
        OwnerId ownerId = OwnerId.of(command.ownerId());
        RestaurantName name = new RestaurantName(command.name());

        validateOwnerUniqueness(ownerId);
        validateRestaurantNameUniqueness(name);

        CuisineType cuisine = CuisineType.valueOf(command.cuisine().toUpperCase());
        CreateAddressRequest addr = command.address();
        Address address = new Address(
                addr.street(),
                addr.number(),
                addr.postalCode(),
                addr.city(),
                addr.country()
        );
        ContactEmail contactEmail = new ContactEmail(command.contactEmail());
        var imageUrls = command.imageUrls().stream()
                .map(ImageUrl::new)
                .collect(Collectors.toList());
        PrepTime prepTime = new PrepTime(command.defaultPrepTimeMinutes());
        OpeningHours openingHours = OpeningHours.parse(command.openingHours());

        Restaurant restaurant = Restaurant.createNew(
                ownerId,
                name,
                cuisine,
                address,
                contactEmail,
                imageUrls,
                prepTime,
                openingHours
        );

        saveRestaurantPorts.forEach(port -> port.save(restaurant));
        restaurant.clearDomainEvents();

        Menu menu = Menu.createNew(restaurant.getId());
        saveMenuPorts.forEach(port -> port.save(menu));
        menu.clearDomainEvents();

        return restaurant.getId();
    }

    private void validateOwnerUniqueness(OwnerId ownerId) {
        if (loadRestaurantPort.loadByOwnerId(ownerId).isPresent()) {
            throw new IllegalStateException("Owner already has a restaurant");
        }
    }

    private void validateRestaurantNameUniqueness(RestaurantName name) {
        if (loadRestaurantPort.loadByName(name).isPresent()) {
            throw new IllegalStateException("Restaurant with name '" + name.value() + "' already exists");
        }
    }
}