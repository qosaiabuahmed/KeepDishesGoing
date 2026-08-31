import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import {
    getAllDishes,
    publishDish,
    unpublishDish,
    markDishOutOfStock,
    markDishInStock,
    getRestaurantById,
    publishAllPending
} from '../../services/api';
import { PublishStatus, StockStatus } from '../../types/api.types';
import { AxiosError } from 'axios';
import SchedulePublicationDialog from '../owner/SchedulePublicationDialog';

const DishListPage = () => {
    const { restaurantId } = useParams<{ restaurantId: string }>();
    const navigate = useNavigate();
    const [showScheduleModal, setShowScheduleModal] = useState(false);
    const queryClient = useQueryClient();

    const { data, isLoading: loading, error } = useQuery({
        queryKey: ['restaurant-dishes', restaurantId],
        queryFn: async () => {
            if (!restaurantId) return null;
            const [restaurantRes, dishesRes] = await Promise.all([
                getRestaurantById(restaurantId),
                getAllDishes(restaurantId)
            ]);
            return { restaurant: restaurantRes.data, dishes: dishesRes.data };
        },
        enabled: !!restaurantId
    });

    const restaurant = data?.restaurant ?? null;
    const dishes = data?.dishes ?? [];
    const pendingChangesCount = dishes.filter(dish => dish.hasPendingChanges).length;

    const publishMutation = useMutation({
        mutationFn: (dishId: string) => publishDish(restaurantId!, dishId),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ['restaurant-dishes', restaurantId] }),
        onError: (err: unknown) => {
            const error = err as AxiosError<{ message: string }>;
            alert(error.response?.data?.message || 'Failed to publish dish');
        }
    });

    const unpublishMutation = useMutation({
        mutationFn: (dishId: string) => unpublishDish(restaurantId!, dishId),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ['restaurant-dishes', restaurantId] }),
        onError: (err: unknown) => {
            const error = err as AxiosError<{ message: string }>;
            alert(error.response?.data?.message || 'Failed to unpublish dish');
        }
    });

    const toggleStockMutation = useMutation({
        mutationFn: ({ dishId, currentStatus }: { dishId: string; currentStatus: string }) => {
            if (currentStatus === StockStatus.IN_STOCK) {
                return markDishOutOfStock(restaurantId!, dishId);
            } else {
                return markDishInStock(restaurantId!, dishId);
            }
        },
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ['restaurant-dishes', restaurantId] }),
        onError: (err: unknown) => {
            const error = err as AxiosError<{ message: string }>;
            alert(error.response?.data?.message || 'Failed to update stock status');
        }
    });

    const publishAllMutation = useMutation({
        mutationFn: () => publishAllPending(restaurantId!),
        onSuccess: () => {
            alert('All pending changes published successfully!');
            queryClient.invalidateQueries({ queryKey: ['restaurant-dishes', restaurantId] });
        },
        onError: (err: unknown) => {
            const error = err as AxiosError<{ message: string }>;
            alert(error.response?.data?.message || 'Failed to publish all pending changes');
        }
    });

    const handlePublish = (dishId: string) => publishMutation.mutate(dishId);
    const handleUnpublish = (dishId: string) => unpublishMutation.mutate(dishId);
    const handleToggleStock = (dishId: string, currentStatus: string) =>
        toggleStockMutation.mutate({ dishId, currentStatus });

    const handlePublishAllPending = () => {
        if (pendingChangesCount === 0) {
            alert('No pending changes to publish');
            return;
        }

        const confirm = window.confirm(
            `Publish all ${pendingChangesCount} pending ${pendingChangesCount === 1 ? 'change' : 'changes'}? All draft changes will become visible to customers immediately.`
        );

        if (!confirm) return;
        publishAllMutation.mutate();
    };

    if (loading) return <div className="loading">Loading dishes...</div>;
    if (error) return <div className="error">Failed to load dishes</div>;

    const dishesWithPendingChanges = dishes.filter(d => d.hasPendingChanges);
    const publishedDishes = dishes.filter(d => d.publishStatus === PublishStatus.PUBLISHED);

    return (
        <div className="dish-list">
            <div className="header">
                <div>
                    <button onClick={() => navigate('/owner/restaurants')} className="btn-back">
                        ← Back to Restaurants
                    </button>
                    <h1>
                        {restaurant?.name} - Dishes
                        {pendingChangesCount > 0 && (
                            <span style={{
                                marginLeft: '1rem',
                                fontSize: '0.85rem',
                                backgroundColor: '#667eea',
                                color: '#ffffff',
                                padding: '0.35rem 0.75rem',
                                borderRadius: '20px',
                                fontWeight: '600',
                                WebkitTextFillColor: '#ffffff'
                            }}>
                                {pendingChangesCount} pending
                            </span>
                        )}
                    </h1>
                </div>
                <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
                    {pendingChangesCount > 0 && (
                        <>
                            <button onClick={handlePublishAllPending} className="btn-success">
                                Publish All ({pendingChangesCount})
                            </button>
                            <button onClick={() => setShowScheduleModal(true)} className="btn-info">
                                Schedule Publication
                            </button>
                        </>
                    )}
                    <button onClick={() => navigate(`/owner/restaurants/${restaurantId}/orders`)} className="btn-info">
                        View Orders
                    </button>
                    <button onClick={() => navigate(`/owner/restaurants/${restaurantId}/dishes/create`)} className="btn-primary">
                        Add New Dish
                    </button>
                </div>
            </div>

            {dishes.length === 0 ? (
                <div className="empty-state">
                    <p>No dishes yet. Add your first dish!</p>
                </div>
            ) : (
                <div className="dish-grid">
                    {dishes.map((dish) => (
                        <div key={dish.id} className="dish-card">
                            <img src={dish.imageUrl} alt={dish.name} className="dish-image" />
                            <div className="dish-info">
                                <h3>
                                    {dish.name}
                                    {dish.hasPendingChanges && dish.draftName && dish.draftName !== dish.name && (
                                        <span style={{ color: '#ff9800', fontSize: '0.85em', marginLeft: '0.5rem' }}>
                                            → {dish.draftName}
                                        </span>
                                    )}
                                </h3>
                                <p className="dish-type">{dish.type}</p>
                                <p className="dish-description">{dish.description}</p>
                                <p className="dish-price">
                                    €{dish.price.toFixed(2)}
                                    {dish.hasPendingChanges && dish.draftPrice != null && dish.draftPrice !== dish.price && (
                                        <span style={{ color: '#ff9800', fontSize: '0.85em', marginLeft: '0.5rem' }}>
                                            → €{dish.draftPrice.toFixed(2)}
                                        </span>
                                    )}
                                </p>

                                <div className="dish-tags">
                                    {dish.foodTags.map(tag => (
                                        <span key={tag} className="tag">{tag}</span>
                                    ))}
                                </div>

                                <div className="dish-status">
                                    <span className={`badge ${dish.publishStatus.toLowerCase()}`}>
                                        {dish.publishStatus}
                                    </span>
                                    <span className={`badge ${dish.stockStatus.toLowerCase()}`}>
                                        {dish.stockStatus}
                                    </span>
                                    {dish.hasPendingChanges && (
                                        <span className="badge pending">Pending Changes</span>
                                    )}
                                </div>

                                <div className="dish-actions">
                                    <button
                                        onClick={() => navigate(`/owner/restaurants/${restaurantId}/dishes/${dish.id}/edit`)}
                                        className="btn-secondary btn-sm"
                                    >
                                        Edit
                                    </button>

                                    {dish.publishStatus === PublishStatus.DRAFT && (
                                        <button onClick={() => handlePublish(dish.id)} className="btn-success btn-sm">
                                            Publish
                                        </button>
                                    )}

                                    {dish.publishStatus === PublishStatus.PUBLISHED && (
                                        <>
                                            <button onClick={() => handleUnpublish(dish.id)} className="btn-warning btn-sm">
                                                Unpublish
                                            </button>
                                            <button
                                                onClick={() => handleToggleStock(dish.id, dish.stockStatus)}
                                                className="btn-info btn-sm"
                                            >
                                                {dish.stockStatus === StockStatus.IN_STOCK ? 'Mark Out' : 'Mark In'}
                                            </button>
                                        </>
                                    )}
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            <SchedulePublicationDialog
                isOpen={showScheduleModal}
                onClose={() => setShowScheduleModal(false)}
                dishesWithPendingChanges={dishesWithPendingChanges}
                publishedDishes={publishedDishes}
                restaurantId={restaurantId!}
            />
        </div>
    );
};

export default DishListPage;