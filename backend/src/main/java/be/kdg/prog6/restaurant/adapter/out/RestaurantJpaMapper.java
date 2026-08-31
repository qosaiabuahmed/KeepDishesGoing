package be.kdg.prog6.restaurant.adapter.out;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.*;
import org.springframework.stereotype.Component;

@Component
public class RestaurantJpaMapper {

    public RestaurantJpaEntity toJpaEntity(Restaurant restaurant) {
        return new RestaurantJpaEntity(
                restaurant.getId().value(),
                restaurant.getOwnerId().value(),
                restaurant.getName().value(),
                restaurant.getCuisine().name(),
                toAddressEmbeddable(restaurant.getAddress()),
                restaurant.getContactEmail().value(),
                restaurant.getImageUrls().stream()
                        .map(ImageUrl::value)
                        .toList(),
                restaurant.getDefaultPrepTime().minutes(),
                restaurant.getOpeningHours().toStorageFormat(),
                restaurant.isOpen()
        );
    }

    private AddressEmbeddable toAddressEmbeddable(Address address) {
        return new AddressEmbeddable(
                address.street(),
                address.number(),
                address.postalCode(),
                address.city(),
                address.country()
        );
    }

    private Address toAddress(AddressEmbeddable embeddable) {
        return new Address(
                embeddable.getStreet(),
                embeddable.getNumber(),
                embeddable.getPostalCode(),
                embeddable.getCity(),
                embeddable.getCountry()
        );
    }

    public Restaurant toDomainModel(RestaurantJpaEntity entity) {
        return new Restaurant(
                new RestaurantId(entity.getId()),
                new OwnerId(entity.getOwnerId()),
                new RestaurantName(entity.getName()),
                CuisineType.valueOf(entity.getCuisine()),
                toAddress(entity.getAddress()),
                new ContactEmail(entity.getContactEmail()),
                entity.getImageUrls().stream()
                        .map(ImageUrl::new)
                        .toList(),
                new PrepTime(entity.getDefaultPrepTimeMinutes()),
                OpeningHours.parse(entity.getOpeningHours()),
                entity.isOpen()
        );
    }
}