import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { getCustomerRestaurants } from '../../services/api';
import PriceRangeEvolutionDialog from './PriceRangeEvolutionDialog';
import type { RestaurantDto } from '../../types/api.types';

function RestaurantPriceRangeList() {
    const [selectedRestaurant, setSelectedRestaurant] = useState<RestaurantDto | null>(null);

    const { data: restaurants, isLoading, error } = useQuery({
        queryKey: ['customerRestaurants'],
        queryFn: async () => {
            const response = await getCustomerRestaurants();
            return response.data;
        }
    });

    const getPriceRangeDisplay = (priceRange?: string) => {
        const priceRangeMap: Record<string, string> = {
            CHEAP: '€',
            REGULAR: '€€',
            EXPENSIVE: '€€€',
            PREMIUM: '€€€€'
        };
        return priceRange ? priceRangeMap[priceRange] || 'N/A' : 'N/A';
    };

    const getPriceRangeClass = (priceRange?: string) => {
        const classMap: Record<string, string> = {
            CHEAP: 'price-range-cheap',
            REGULAR: 'price-range-regular',
            EXPENSIVE: 'price-range-expensive',
            PREMIUM: 'price-range-premium'
        };
        return priceRange ? classMap[priceRange] || '' : '';
    };

    if (isLoading) {
        return <div className="loading">Loading restaurants...</div>;
    }

    if (error) {
        return (
            <div className="alert alert-error">
                Failed to load restaurants: {error instanceof Error ? error.message : 'Unknown error'}
            </div>
        );
    }

    if (!restaurants || restaurants.length === 0) {
        return (
            <div style={{ textAlign: 'center', padding: '20px', color: '#666' }}>
                No restaurants available
            </div>
        );
    }

    return (
        <div>
            <h3>Restaurants and Their Price Ranges</h3>
            <p className="text-muted" style={{ marginBottom: '20px' }}>
                View how each restaurant's price range classification has evolved over time
            </p>

            <div className="restaurant-price-range-table">
                <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                    <thead>
                        <tr style={{ borderBottom: '2px solid #e5e7eb' }}>
                            <th style={{ padding: '12px', textAlign: 'left', fontWeight: '600' }}>Restaurant</th>
                            <th style={{ padding: '12px', textAlign: 'left', fontWeight: '600' }}>Cuisine</th>
                            <th style={{ padding: '12px', textAlign: 'center', fontWeight: '600' }}>Avg Menu Price</th>
                            <th style={{ padding: '12px', textAlign: 'center', fontWeight: '600' }}>Current Range</th>
                            <th style={{ padding: '12px', textAlign: 'center', fontWeight: '600' }}>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {restaurants.map((restaurant) => (
                            <tr key={restaurant.id} style={{ borderBottom: '1px solid #e5e7eb' }}>
                                <td style={{ padding: '12px' }}>
                                    <div style={{ fontWeight: '500' }}>{restaurant.name}</div>
                                    <div style={{ fontSize: '12px', color: '#666' }}>
                                        {restaurant.address.city}
                                    </div>
                                </td>
                                <td style={{ padding: '12px' }}>
                                    <span style={{
                                        padding: '4px 8px',
                                        backgroundColor: '#f3f4f6',
                                        borderRadius: '4px',
                                        fontSize: '13px'
                                    }}>
                                        {restaurant.cuisine}
                                    </span>
                                </td>
                                <td style={{ padding: '12px', textAlign: 'center' }}>
                                    {restaurant.averageMenuPrice !== undefined
                                        ? `€${restaurant.averageMenuPrice.toFixed(2)}`
                                        : 'N/A'}
                                </td>
                                <td style={{ padding: '12px', textAlign: 'center' }}>
                                    <span className={`price-range-badge ${getPriceRangeClass(restaurant.priceRange)}`}>
                                        {getPriceRangeDisplay(restaurant.priceRange)}
                                    </span>
                                </td>
                                <td style={{ padding: '12px', textAlign: 'center' }}>
                                    <button
                                        onClick={() => setSelectedRestaurant(restaurant)}
                                        style={{
                                            fontSize: '13px',
                                            padding: '6px 12px',
                                            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                                            color: 'white',
                                            border: 'none',
                                            borderRadius: '6px',
                                            cursor: 'pointer',
                                            transition: 'transform 0.2s, box-shadow 0.2s'
                                        }}
                                        onMouseEnter={(e) => {
                                            e.currentTarget.style.transform = 'translateY(-2px)';
                                            e.currentTarget.style.boxShadow = '0 4px 12px rgba(102, 126, 234, 0.4)';
                                        }}
                                        onMouseLeave={(e) => {
                                            e.currentTarget.style.transform = 'translateY(0)';
                                            e.currentTarget.style.boxShadow = 'none';
                                        }}
                                    >
                                        View Evolution
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {selectedRestaurant && (
                <PriceRangeEvolutionDialog
                    restaurantId={selectedRestaurant.id}
                    restaurantName={selectedRestaurant.name}
                    onClose={() => setSelectedRestaurant(null)}
                />
            )}
        </div>
    );
}

export default RestaurantPriceRangeList;