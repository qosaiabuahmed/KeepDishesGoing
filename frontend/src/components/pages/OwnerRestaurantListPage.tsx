import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getAllRestaurants, openRestaurant, closeRestaurant } from '../../services/api';
import { useNavigate } from 'react-router-dom';

const RestaurantList = () => {
    const navigate = useNavigate();
    const queryClient = useQueryClient();

    const { data: restaurants = [], isLoading: loading, error } = useQuery({
        queryKey: ['owner-restaurants'],
        queryFn: async () => {
            const response = await getAllRestaurants();
            return response.data;
        }
    });

    const toggleMutation = useMutation({
        mutationFn: async ({ restaurantId, isOpen }: { restaurantId: string; isOpen: boolean }) => {
            return isOpen ? closeRestaurant(restaurantId) : openRestaurant(restaurantId);
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['owner-restaurants'] });
        },
        onError: (err) => {
            console.error('Failed to toggle restaurant status', err);
        }
    });

    const handleToggleStatus = (restaurantId: string, currentlyOpen: boolean) => {
        toggleMutation.mutate({ restaurantId, isOpen: currentlyOpen });
    };

    if (loading) return <div className="loading">Loading restaurants...</div>;
    if (error) return <div className="error">Failed to load restaurants</div>;

    return (
        <div className="restaurant-list">
            <div className="header">
                <h1>My Restaurants</h1>
                <button onClick={() => navigate('/owner/restaurants/create')} className="btn-primary">
                    Create New Restaurant
                </button>
            </div>

            {restaurants.length === 0 ? (
                <div className="empty-state">
                    <p>No restaurants yet. Create your first one!</p>
                </div>
            ) : (
                <div className="restaurant-grid">
                    {restaurants.map((restaurant) => (
                        <div
                            key={restaurant.id}
                            className="restaurant-card"
                        >
                            {restaurant.imageUrls.length > 0 && (
                                <img
                                    src={restaurant.imageUrls[0]}
                                    alt={restaurant.name}
                                    className="restaurant-image"
                                    onClick={() => navigate(`/owner/restaurants/${restaurant.id}/dishes`)}
                                />
                            )}
                            <div className="restaurant-info">
                                <h3
                                    onClick={() => navigate(`/owner/restaurants/${restaurant.id}/dishes`)}
                                    style={{cursor: 'pointer'}}
                                >
                                    {restaurant.name}
                                </h3>
                                <p className="cuisine">{restaurant.cuisine}</p>
                                <p className="address">
                                    {restaurant.address.street} {restaurant.address.number}, {restaurant.address.city}
                                </p>
                                <p className="contact">{restaurant.contactEmail}</p>
                                <div className="restaurant-stats">
                                    <span className={`status ${restaurant.isOpen ? 'open' : 'closed'}`}>
                                        {restaurant.isOpen ? 'Open' : 'Closed'}
                                    </span>
                                    <button
                                        className={restaurant.isOpen ? 'btn-warning btn-sm' : 'btn-success btn-sm'}
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            handleToggleStatus(restaurant.id, restaurant.isOpen);
                                        }}
                                    >
                                        {restaurant.isOpen ? 'Close' : 'Open'}
                                    </button>
                                    <span className="dishes-count">{restaurant.availableDishesCount} dishes</span>
                                </div>
                                <div className="restaurant-actions">
                                    <button
                                        className="btn-secondary btn-sm"
                                        onClick={() => navigate(`/owner/restaurants/${restaurant.id}/orders`)}
                                    >
                                        Pending Orders
                                    </button>
                                    <button
                                        className="btn-primary btn-sm"
                                        onClick={() => navigate(`/owner/restaurants/${restaurant.id}/current-orders`)}
                                    >
                                        Current Orders
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default RestaurantList;