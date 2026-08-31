import { useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useRestaurants } from '../../hooks/useRestaurants';
import { RestaurantFilterBar } from '../customer/RestaurantFilterBar';

const priceRangeSymbols: Record<string, string> = {
    CHEAP: '€',
    REGULAR: '€€',
    EXPENSIVE: '€€€',
    PREMIUM: '€€€€'
};

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
                className="restaurant-image"
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

const RestaurantListPage = () => {
    const navigate = useNavigate();
    const [searchParams, setSearchParams] = useSearchParams();

    const selectedCuisine = searchParams.get('cuisine') || '';
    const selectedPriceRange = searchParams.get('priceRange') || '';

    const { restaurants, loading, error } = useRestaurants({
        cuisine: selectedCuisine,
        priceRange: selectedPriceRange
    });

    const handleCuisineChange = (cuisine: string) => {
        const newParams = new URLSearchParams(searchParams);
        if (cuisine) {
            newParams.set('cuisine', cuisine);
        } else {
            newParams.delete('cuisine');
        }
        setSearchParams(newParams);
    };

    const handlePriceRangeChange = (priceRange: string) => {
        const newParams = new URLSearchParams(searchParams);
        if (priceRange) {
            newParams.set('priceRange', priceRange);
        } else {
            newParams.delete('priceRange');
        }
        setSearchParams(newParams);
    };

    const handleClearFilters = () => {
        setSearchParams(new URLSearchParams());
    };

    if (loading) return <div className="loading">Loading restaurants...</div>;
    if (error) return <div className="error">{error}</div>;

    return (
        <div className="restaurant-list">
            <div className="header">
                <h1>Browse Restaurants</h1>
                <p className="subtitle">Choose from our amazing selection of local restaurants</p>
            </div>

            <RestaurantFilterBar
                selectedCuisine={selectedCuisine}
                selectedPriceRange={selectedPriceRange}
                onCuisineChange={handleCuisineChange}
                onPriceRangeChange={handlePriceRangeChange}
                onClearFilters={handleClearFilters}
            />

            {restaurants.length === 0 ? (
                <div className="empty-state">
                    <p>No restaurants found matching your filters.</p>
                    <button className="btn-secondary" onClick={handleClearFilters}>
                        Clear Filters
                    </button>
                </div>
            ) : (
                <div className="restaurant-grid">
                    {restaurants.map((restaurant) => (
                        <div
                            key={restaurant.id}
                            className="restaurant-card customer-card"
                            onClick={() => navigate(`/customer/restaurants/${restaurant.id}`)}
                        >
                            <ImageCarousel imageUrls={restaurant.imageUrls} altText={restaurant.name} />
                            <div className="restaurant-info">
                                <h3>{restaurant.name}</h3>
                                <div className="restaurant-meta">
                                    <p className="cuisine">{restaurant.cuisine}</p>
                                    {restaurant.priceRange && (
                                        <span className="price-range-badge">
                                            {priceRangeSymbols[restaurant.priceRange]}
                                        </span>
                                    )}
                                </div>
                                <p className="address">
                                    {restaurant.address.street} {restaurant.address.number}, {restaurant.address.city}
                                </p>
                                {restaurant.averageMenuPrice && (
                                    <p className="average-price">
                                        Avg. price: €{restaurant.averageMenuPrice.toFixed(2)}
                                    </p>
                                )}
                                <div className="restaurant-stats">
                                    <span className={`status ${restaurant.isOpen ? 'open' : 'closed'}`}>
                                        {restaurant.isOpen ? 'Open Now' : 'Closed'}
                                    </span>
                                    <span className="dish-count">
                                        {restaurant.availableDishesCount} dishes
                                    </span>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default RestaurantListPage;