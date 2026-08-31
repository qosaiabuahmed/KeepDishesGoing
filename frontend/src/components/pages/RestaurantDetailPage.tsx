import { useState } from 'react';
import { useParams, useNavigate, useSearchParams } from 'react-router-dom';
import { addDishToBasket, clearBasket } from '../../services/api';
import { useBasket } from '../../hooks/useBasket';
import { useRestaurant } from '../../hooks/useRestaurant';
import { useMenu } from '../../hooks/useMenu';
import type { DishDto, AddDishToBasketRequest } from '../../types/api.types';
import { DishFilterBar } from '../customer/DishFilterBar';

const ImageCarousel = ({ imageUrls, altText }: { imageUrls: string[], altText: string }) => {
    const [currentIndex, setCurrentIndex] = useState(0);

    if (imageUrls.length === 0) return null;

    const nextImage = (e: React.MouseEvent) => {
        e.stopPropagation();
        setCurrentIndex((prev) => (prev + 1) % imageUrls.length);
    };

    const prevImage = (e: React.MouseEvent) => {
        e.stopPropagation();
        setCurrentIndex((prev) => (prev - 1 + imageUrls.length) % imageUrls.length);
    };

    const goToImage = (index: number, e: React.MouseEvent) => {
        e.stopPropagation();
        setCurrentIndex(index);
    };

    return (
        <div className="image-carousel">
            <img
                src={imageUrls[currentIndex]}
                alt={altText}
                className="restaurant-hero-image"
            />
            {imageUrls.length > 1 && (
                <>
                    <button className="carousel-btn prev" onClick={prevImage}>
                        ‹
                    </button>
                    <button className="carousel-btn next" onClick={nextImage}>
                        ›
                    </button>
                    <div className="carousel-dots">
                        {imageUrls.map((_, index) => (
                            <button
                                key={index}
                                className={`dot ${index === currentIndex ? 'active' : ''}`}
                                onClick={(e) => goToImage(index, e)}
                            />
                        ))}
                    </div>
                </>
            )}
        </div>
    );
};

const foodTagDisplayNames: Record<string, string> = {
    LACTOSE: 'Lactose',
    GLUTEN: 'Gluten',
    VEGAN: 'Vegan',
    VEGETARIAN: 'Vegetarian',
    NUTS: 'Nuts',
    SEAFOOD: 'Seafood',
    SPICY: 'Spicy',
    ORGANIC: 'Organic',
    HALAL: 'Halal',
    KOSHER: 'Kosher'
};

const CustomerRestaurantDetail = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const { basket, refreshBasket } = useBasket();
    const [searchParams, setSearchParams] = useSearchParams();
    const [addingDish, setAddingDish] = useState<string | null>(null);
    const [error, setError] = useState<string>('');

    const selectedType = searchParams.get('type') || '';
    const selectedSort = searchParams.get('sortBy') || '';
    const selectedFoodTags = searchParams.getAll('foodTags');

    const { restaurant } = useRestaurant(id);
    const { dishes, loading } = useMenu(id, {
        type: selectedType,
        foodTags: selectedFoodTags,
        sortBy: selectedSort
    });

    const handleTypeChange = (type: string) => {
        const newParams = new URLSearchParams(searchParams);
        if (type) {
            newParams.set('type', type);
        } else {
            newParams.delete('type');
        }
        setSearchParams(newParams);
    };

    const handleFoodTagToggle = (tag: string) => {
        const newParams = new URLSearchParams(searchParams);
        const currentTags = newParams.getAll('foodTags');

        if (currentTags.includes(tag)) {
            newParams.delete('foodTags');
            currentTags.filter(t => t !== tag).forEach(t => newParams.append('foodTags', t));
        } else {
            newParams.append('foodTags', tag);
        }

        setSearchParams(newParams);
    };

    const handleSortChange = (sort: string) => {
        const newParams = new URLSearchParams(searchParams);
        if (sort) {
            newParams.set('sortBy', sort);
        } else {
            newParams.delete('sortBy');
        }
        setSearchParams(newParams);
    };

    const handleClearFilters = () => {
        setSearchParams(new URLSearchParams());
    };

    const handleAddToBasket = async (dish: DishDto) => {
        if (!id) return;

        if (basket?.restaurantId && basket.restaurantId !== id) {
            const confirmed = confirm(
                `Your basket contains items from another restaurant. Do you want to clear your basket and start a new order from ${restaurant?.name}?`
            );

            if (!confirmed) return;

            try {
                await clearBasket();
                await refreshBasket();
            } catch (err) {
                setError('Failed to clear basket');
                console.error(err);
                return;
            }
        }

        try {
            setAddingDish(dish.id);
            const request: AddDishToBasketRequest = {
                restaurantId: id,
                dishId: dish.id,
                dishName: dish.name,
                unitPrice: dish.price,
                quantity: 1
            };

            await addDishToBasket(request);
            await refreshBasket();
            setError('');
        } catch (err) {
            setError('Failed to add dish to basket');
            console.error(err);
        } finally {
            setAddingDish(null);
        }
    };

    if (loading) return <div className="loading">Loading...</div>;
    if (error) return <div className="error">{error}</div>;
    if (!restaurant) return <div className="error">Restaurant not found</div>;

    return (
        <div className="customer-restaurant-detail">
            <button onClick={() => navigate('/customer/restaurants')} className="btn-back">
                ← Back to Restaurants
            </button>

            <div className="restaurant-header">
                <ImageCarousel imageUrls={restaurant.imageUrls} altText={restaurant.name} />
                <div className="restaurant-header-info">
                    <h1>{restaurant.name}</h1>
                    <p className="cuisine">{restaurant.cuisine}</p>
                    <p className="address">
                        {restaurant.address.street} {restaurant.address.number}, {restaurant.address.city}
                    </p>
                    <p className="contact">{restaurant.contactEmail}</p>
                    <span className={`status ${restaurant.isOpen ? 'open' : 'closed'}`}>
                        {restaurant.isOpen ? '🟢 Open Now' : '🔴 Closed'}
                    </span>
                </div>
            </div>

            {!restaurant.isOpen && (
                <div className="restaurant-closed-banner">
                    <h3>This restaurant is currently closed</h3>
                    <p>This restaurant is not accepting orders at the moment. Please check back during opening hours.</p>
                </div>
            )}

            <div className="menu-section">
                <h2>Menu</h2>

                <DishFilterBar
                    selectedType={selectedType}
                    selectedFoodTags={selectedFoodTags}
                    selectedSort={selectedSort}
                    onTypeChange={handleTypeChange}
                    onFoodTagToggle={handleFoodTagToggle}
                    onSortChange={handleSortChange}
                    onClearFilters={handleClearFilters}
                />

                {dishes.length === 0 ? (
                    <div className="empty-state">
                        <p>No dishes found matching your filters.</p>
                        <button className="btn-secondary" onClick={handleClearFilters}>
                            Clear Filters
                        </button>
                    </div>
                ) : (
                    <div className="dish-grid">
                        {dishes.map((dish) => (
                            <div key={dish.id} className="dish-card customer-dish-card">
                                <img src={dish.imageUrl} alt={dish.name} className="dish-image" />
                                <div className="dish-info">
                                    <h3>{dish.name}</h3>
                                    <p className="dish-type">{dish.type}</p>

                                    {dish.foodTags && dish.foodTags.length > 0 && (
                                        <div className="food-tags">
                                            {dish.foodTags.map((tag) => (
                                                <span key={tag} className="food-tag">
                                                    {foodTagDisplayNames[tag] || tag}
                                                </span>
                                            ))}
                                        </div>
                                    )}

                                    <p className="dish-description">{dish.description}</p>
                                    <p className="dish-price">€{dish.price.toFixed(2)}</p>

                                    {!restaurant.isOpen ? (
                                        <button disabled className="btn-primary btn-sm">
                                            Restaurant Closed
                                        </button>
                                    ) : dish.stockStatus === 'OUT_OF_STOCK' ? (
                                        <span className="out-of-stock-badge">Out of Stock</span>
                                    ) : (
                                        <button
                                            onClick={() => handleAddToBasket(dish)}
                                            disabled={addingDish === dish.id}
                                            className="btn-primary btn-sm"
                                        >
                                            {addingDish === dish.id ? 'Adding...' : 'Add to Basket'}
                                        </button>
                                    )}
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default CustomerRestaurantDetail;