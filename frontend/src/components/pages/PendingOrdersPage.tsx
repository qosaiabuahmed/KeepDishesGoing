import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { getOwnerOrders, acceptOrder, rejectOrder } from '../../services/api';
import type { OrderProjectionDto } from '../../types/api.types';
import OrderDetailModal from '../owner/OrderDetailModal';

const PendingOrders = () => {
    const { restaurantId } = useParams<{ restaurantId: string }>();
    const [selectedOrder, setSelectedOrder] = useState<OrderProjectionDto | null>(null);
    const [successMessage, setSuccessMessage] = useState('');
    const [currentTime, setCurrentTime] = useState(new Date());
    const queryClient = useQueryClient();

    const { data: orders = [], isLoading: loading, error } = useQuery({
        queryKey: ['pending-orders', restaurantId],
        queryFn: async () => {
            if (!restaurantId) {
                console.log('No restaurantId provided');
                return [];
            }

            console.log('Fetching submitted orders for restaurant:', restaurantId);

            const response = await getOwnerOrders(restaurantId, 'SUBMITTED');
            console.log('Submitted orders response:', response.data);
            console.log('Number of submitted orders:', response.data.length);

            // Double-check on frontend (safety filter) to ensure only SUBMITTED orders
            const submittedOrders = response.data.filter(order => {
                if (order.status !== 'SUBMITTED') {
                    console.warn(`Unexpected order status from /submitted endpoint: ${order.status} for order ${order.orderId}`);
                    return false;
                }
                return true;
            });

            console.log('Final submitted orders count:', submittedOrders.length);
            return submittedOrders;
        },
        enabled: !!restaurantId,
        refetchInterval: 30000, // 30 seconds auto-refresh
    });

    useEffect(() => {
        const timer = setInterval(() => {
            setCurrentTime(new Date());
        }, 1000);

        return () => clearInterval(timer);
    }, []);

    const acceptMutation = useMutation({
        mutationFn: ({ restaurantId, orderId }: { restaurantId: string; orderId: string }) =>
            acceptOrder(restaurantId, orderId),
        onSuccess: () => {
            setSuccessMessage('Order accepted successfully!');
            setSelectedOrder(null);
            queryClient.invalidateQueries({ queryKey: ['pending-orders'] });
            setTimeout(() => setSuccessMessage(''), 3000);
        },
        onError: (error: unknown) => {
            const err = error as AxiosError<{ error?: string; message?: string }>;
            console.error('Error accepting order:', err);
            alert(err.response?.data?.error || 'Failed to accept order');
        }
    });

    const rejectMutation = useMutation({
        mutationFn: ({ restaurantId, orderId, reason }: { restaurantId: string; orderId: string; reason: string }) =>
            rejectOrder(restaurantId, orderId, { reason }),
        onSuccess: () => {
            setSuccessMessage('Order rejected successfully');
            setSelectedOrder(null);
            queryClient.invalidateQueries({ queryKey: ['pending-orders'] });
            setTimeout(() => setSuccessMessage(''), 3000);
        },
        onError: (error: unknown) => {
            const err = error as AxiosError<{ error?: string; message?: string }>;
            console.error('Error rejecting order:', err);
            alert(err.response?.data?.error || 'Failed to reject order');
        }
    });

    const handleAcceptOrder = (orderId: string) => {
        if (!restaurantId) return;
        acceptMutation.mutate({ restaurantId, orderId });
    };

    const handleRejectOrder = (orderId: string, reason: string) => {
        if (!restaurantId) return;
        rejectMutation.mutate({ restaurantId, orderId, reason });
    };

    const formatDate = (dateString: string) => {
        const date = new Date(dateString);
        return date.toLocaleString('en-US', {
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const getTimeElapsed = (orderTime: string) => {
        const now = new Date();
        const orderDate = new Date(orderTime);
        const diffMs = now.getTime() - orderDate.getTime();
        const diffMins = Math.floor(diffMs / 60000);

        if (diffMins < 1) return 'Just now';
        if (diffMins === 1) return '1 minute ago';
        return `${diffMins} minutes ago`;
    };

    const isCloseToDeadline = (orderTime: string) => {
        const now = new Date();
        const orderDate = new Date(orderTime);
        const diffMs = now.getTime() - orderDate.getTime();
        const diffMins = Math.floor(diffMs / 60000);
        return diffMins >= 4;
    };

    const getTimeRemaining = (orderTime: string) => {
        const orderDate = new Date(orderTime);
        const deadline = new Date(orderDate.getTime() + 5 * 60 * 1000); // 5 minutes from order time
        const remainingMs = deadline.getTime() - currentTime.getTime();

        if (remainingMs <= 0) {
            return { total: 0, minutes: 0, seconds: 0, expired: true };
        }

        const minutes = Math.floor(remainingMs / 60000);
        const seconds = Math.floor((remainingMs % 60000) / 1000);

        return { total: remainingMs, minutes, seconds, expired: false };
    };

    const formatCountdown = (orderTime: string) => {
        const { minutes, seconds, expired } = getTimeRemaining(orderTime);

        if (expired) {
            return 'EXPIRED';
        }

        return `${minutes}:${seconds.toString().padStart(2, '0')}`;
    };

    const getCountdownClassName = (orderTime: string) => {
        const { total } = getTimeRemaining(orderTime);

        if (total <= 0) return 'countdown-expired';
        if (total < 60000) return 'countdown-critical'; // < 1 minute
        if (total < 120000) return 'countdown-warning'; // < 2 minutes
        return 'countdown-normal';
    };

    if (loading) {
        return <div className="loading">Loading pending orders...</div>;
    }

    return (
        <div className="pending-orders-container">
            <div className="page-header">
                <div>
                    <Link to="/owner/restaurants" className="back-link">
                        &larr; Back to Restaurants
                    </Link>
                    <h1>Pending Orders</h1>
                    <p className="page-subtitle">
                        Orders awaiting your response. Auto-declined after 5 minutes.
                    </p>
                </div>
                <button
                    className="btn-secondary btn-sm"
                    onClick={() => queryClient.invalidateQueries({ queryKey: ['pending-orders', restaurantId] })}
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
                    <h2>No Pending Orders</h2>
                    <p>All orders have been processed or there are no new orders at this time.</p>
                </div>
            ) : (
                <div className="orders-grid">
                    {orders.map((order) => (
                        <div
                            key={order.orderId}
                            className={`order-card ${isCloseToDeadline(order.orderTime) ? 'order-urgent' : ''}`}
                            onClick={() => setSelectedOrder(order)}
                        >
                            <div className="order-card-header">
                                <h3>Order #{order.orderId.substring(0, 8)}</h3>
                                {isCloseToDeadline(order.orderTime) && (
                                    <span className="badge badge-warning">Urgent</span>
                                )}
                            </div>

                            <div className="order-card-body">
                                {/* Countdown Timer - Most Prominent */}
                                <div className={`countdown-timer ${getCountdownClassName(order.orderTime)}`}>
                                    <div className="countdown-icon">⏱️</div>
                                    <div className="countdown-info">
                                        <div className="countdown-time">{formatCountdown(order.orderTime)}</div>
                                        <div className="countdown-label">until auto-decline</div>
                                    </div>
                                </div>

                                <div className="order-info-row">
                                    <span className="order-label">Customer:</span>
                                    <span className="order-value">{order.customerName}</span>
                                </div>

                                <div className="order-info-row">
                                    <span className="order-label">Placed:</span>
                                    <span className="order-value">
                                        {getTimeElapsed(order.orderTime)}
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

                                <div className="order-items-preview">
                                    {order.items.slice(0, 2).map((item, idx) => (
                                        <div key={idx} className="order-item-preview">
                                            {item.quantity}x {item.dishName}
                                        </div>
                                    ))}
                                    {order.items.length > 2 && (
                                        <div className="order-item-preview-more">
                                            +{order.items.length - 2} more
                                        </div>
                                    )}
                                </div>
                            </div>

                            <div className="order-card-footer">
                                <span className="order-time-detail">{formatDate(order.orderTime)}</span>
                                <span className="order-click-hint">Click to view details</span>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            <OrderDetailModal
                order={selectedOrder}
                isOpen={selectedOrder !== null}
                onClose={() => setSelectedOrder(null)}
                onAccept={handleAcceptOrder}
                onReject={handleRejectOrder}
            />
        </div>
    );
};

export default PendingOrders;