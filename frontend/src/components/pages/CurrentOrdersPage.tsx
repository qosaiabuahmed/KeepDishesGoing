import { useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { getConfirmedOrders, markOrderReady } from '../../services/api';

const CurrentOrders = () => {
    const { restaurantId } = useParams<{ restaurantId: string }>();
    const [successMessage, setSuccessMessage] = useState('');
    const [confirmingOrderId, setConfirmingOrderId] = useState<string | null>(null);
    const queryClient = useQueryClient();

    const { data: orders = [], isLoading: loading, error } = useQuery({
        queryKey: ['confirmed-orders', restaurantId],
        queryFn: async () => {
            if (!restaurantId) {
                console.log('No restaurantId provided');
                return [];
            }

            console.log('Fetching confirmed orders for restaurant:', restaurantId);

            const response = await getConfirmedOrders(restaurantId);
            console.log('Confirmed orders response:', response.data);
            console.log('Number of confirmed orders:', response.data.length);

            const confirmedOrders = response.data.filter(order => {
                if (order.status !== 'CONFIRMED') {
                    console.warn(`Unexpected order status from /confirmed endpoint: ${order.status} for order ${order.orderId}`);
                    return false;
                }
                return true;
            });

            console.log('Final confirmed orders count:', confirmedOrders.length);
            return confirmedOrders;
        },
        enabled: !!restaurantId,
        refetchInterval: 30000,
    });

    const markReadyMutation = useMutation({
        mutationFn: ({ restaurantId, orderId }: { restaurantId: string; orderId: string }) =>
            markOrderReady(restaurantId, orderId),
        onSuccess: () => {
            setSuccessMessage('Order marked as ready for pickup!');
            setConfirmingOrderId(null);
            queryClient.invalidateQueries({ queryKey: ['confirmed-orders'] });
            setTimeout(() => setSuccessMessage(''), 3000);
        },
        onError: (error: unknown) => {
            const err = error as AxiosError<{ error?: string; message?: string }>;
            console.error('Error marking order ready:', err);
            alert(err.response?.data?.error || 'Failed to mark order ready');
            setConfirmingOrderId(null);
        }
    });

    const handleMarkReady = (orderId: string) => {
        if (!restaurantId) return;
        markReadyMutation.mutate({ restaurantId, orderId });
    };


    const getTimeElapsed = (orderTime: string) => {
        const now = new Date();
        const orderDate = new Date(orderTime);
        const diffMs = now.getTime() - orderDate.getTime();
        const diffMins = Math.floor(diffMs / 60000);

        if (diffMins < 1) return 'Just now';
        if (diffMins === 1) return '1 minute ago';
        if (diffMins < 60) return `${diffMins} minutes ago`;
        const hours = Math.floor(diffMins / 60);
        if (hours === 1) return '1 hour ago';
        return `${hours} hours ago`;
    };

    if (loading) {
        return <div className="loading">Loading current orders...</div>;
    }

    return (
        <div className="pending-orders-container">
            <div className="page-header">
                <div>
                    <Link to="/owner/restaurants" className="back-link">
                        &larr; Back to Restaurants
                    </Link>
                    <h1>Current Orders</h1>
                    <p className="page-subtitle">
                        Orders confirmed and being prepared. Mark as ready when complete.
                    </p>
                </div>
                <button
                    className="btn-secondary btn-sm"
                    onClick={() => queryClient.invalidateQueries({ queryKey: ['confirmed-orders', restaurantId] })}
                    disabled={loading}
                >
                    {loading ? 'Refreshing...' : 'Refresh'}
                </button>
            </div>

            {successMessage && (
                <div className="success-message">
                    {successMessage}
                </div>
            )}

            {error && (
                <div className="error-message">
                    Failed to load orders
                </div>
            )}

            {orders.length === 0 ? (
                <div className="empty-state">
                    <h2>No Current Orders</h2>
                    <p>All orders have been marked as ready or there are no confirmed orders at this time.</p>
                </div>
            ) : (
                <div className="orders-grid">
                    {orders.map((order) => (
                        <div key={order.orderId} className="order-card">
                            <div className="order-card-header">
                                <h3>Order #{order.orderId.substring(0, 8)}</h3>
                                <span className="badge badge-success">Confirmed</span>
                            </div>

                            <div className="order-card-body">
                                <div className="order-info-row">
                                    <span className="order-label">Customer:</span>
                                    <span className="order-value">{order.customerName}</span>
                                </div>

                                <div className="order-info-row">
                                    <span className="order-label">Email:</span>
                                    <span className="order-value">{order.contactEmail}</span>
                                </div>

                                <div className="order-info-row">
                                    <span className="order-label">Accepted:</span>
                                    <span className="order-value">
                                        {getTimeElapsed(order.decisionTime || order.orderTime)}
                                    </span>
                                </div>

                                <div className="order-info-row">
                                    <span className="order-label">Items:</span>
                                    <span className="order-value">
                                        {order.items.reduce((sum, item) => sum + item.quantity, 0)} items
                                    </span>
                                </div>

                                <div className="order-info-row">
                                    <span className="order-label">Total:</span>
                                    <span className="order-value order-total">
                                        €{order.totalAmount.toFixed(2)}
                                    </span>
                                </div>

                                <div className="order-items-list">
                                    <div className="order-items-header">Order Items:</div>
                                    {order.items.map((item, idx) => (
                                        <div key={idx} className="order-item-detail">
                                            <span className="item-quantity">{item.quantity}x</span>
                                            <span className="item-name">{item.dishName}</span>
                                            <span className="item-price">€{item.totalPrice.toFixed(2)}</span>
                                        </div>
                                    ))}
                                </div>

                                <div className="order-delivery-address">
                                    <div className="address-label">Delivery Address:</div>
                                    <div className="address-text">
                                        {order.deliveryAddress.street} {order.deliveryAddress.number}, {order.deliveryAddress.city}
                                        <br />
                                        {order.deliveryAddress.postalCode}, {order.deliveryAddress.country}
                                    </div>
                                </div>
                            </div>

                            <div className="order-card-footer">
                                {confirmingOrderId === order.orderId ? (
                                    <div className="confirmation-dialog">
                                        <p>Mark this order as ready for pickup?</p>
                                        <div className="confirmation-buttons">
                                            <button
                                                className="btn-primary btn-sm"
                                                onClick={() => handleMarkReady(order.orderId)}
                                            >
                                                Yes, Mark Ready
                                            </button>
                                            <button
                                                className="btn-secondary btn-sm"
                                                onClick={() => setConfirmingOrderId(null)}
                                            >
                                                Cancel
                                            </button>
                                        </div>
                                    </div>
                                ) : (
                                    <button
                                        className="btn-primary btn-full-width"
                                        onClick={() => setConfirmingOrderId(order.orderId)}
                                    >
                                        Mark Ready for Pickup
                                    </button>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default CurrentOrders;