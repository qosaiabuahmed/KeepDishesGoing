import { useEffect, useState } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { getOrder } from '../../services/api';
import {
    ORDER_STEPS,
    getStatusColor,
    getStatusLabel,
    getStatusDescription,
    getCurrentStepIndex as getStepIndex,
    getProgressPercentage as getProgress,
    getTimeSinceLastUpdate as getTimeSince
} from '../../utils/orderStatus';

const OrderTrackingPage = () => {
    const { orderId } = useParams<{ orderId: string }>();
    const location = useLocation();
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const [copySuccess, setCopySuccess] = useState(false);

    const fromCheckout = location.state?.fromCheckout;
    // Always use the current frontend page URL for sharing (not the backend API URL)
    const trackingUrl = window.location.href;

    const { data: order, isLoading, error, dataUpdatedAt } = useQuery({
        queryKey: ['order', orderId],
        queryFn: async () => {
            if (!orderId) return null;
            const response = await getOrder(orderId);
            return response.data;
        },
        enabled: !!orderId,
        refetchInterval: (query) => {
            const data = query.state.data;
            if (!data) return 2000;
            if (data.status === 'IN_DELIVERY') return 1000; // 1 second for active delivery
            if (data.status === 'DELIVERED' || data.status === 'CANCELLED') return false; // Stop polling
            return 2000; // 2 seconds default
        }
    });

    const lastUpdated = new Date(dataUpdatedAt);

    const [, setTick] = useState(0);
    useEffect(() => {
        const timer = setInterval(() => setTick(t => t + 1), 1000);
        return () => clearInterval(timer);
    }, []);

    if (isLoading && !order) {
        return <div className="loading">Loading order details...</div>;
    }

    if (error && !order) {
        return (
            <div className="error-container">
                <h2>Error</h2>
                <p className="error-message">Failed to load order details. Please try again.</p>
                <button onClick={() => navigate('/customer/restaurants')} className="btn-primary">
                    Browse Restaurants
                </button>
            </div>
        );
    }

    if (!order) {
        return (
            <div className="error-container">
                <h2>Order Not Found</h2>
                <p>We couldn't find the order you're looking for.</p>
                <button onClick={() => navigate('/customer/restaurants')} className="btn-primary">
                    Browse Restaurants
                </button>
            </div>
        );
    }

    const progress = getProgress(order.status);
    const currentStepIndex = getStepIndex(order.status);
    const isCancelled = order.status === 'CANCELLED';

    return (
        <div className="order-tracking-container">
            {fromCheckout && (
                <div className="success-message" style={{ marginBottom: '30px' }}>
                    <h2>Order Placed Successfully!</h2>
                    <p>Thank you for your order. You can track your order status below.</p>
                </div>
            )}

            {/* Always show tracking URL box */}
            <div style={{
                marginBottom: '20px',
                padding: '15px',
                background: fromCheckout ? 'white' : '#f8fafc',
                borderRadius: '8px',
                border: fromCheckout ? '2px solid #27ae60' : '1px solid #e5e7eb'
            }}>
                <p style={{
                    fontWeight: '600',
                    marginBottom: '10px',
                    color: '#2c3e50',
                    fontSize: '14px'
                }}>
                    {fromCheckout ? 'Save this tracking link to check your order anytime:' : 'Share this tracking link:'}
                </p>
                <div style={{
                    display: 'flex',
                    gap: '10px',
                    alignItems: 'center'
                }}>
                    <input
                        type="text"
                        value={trackingUrl}
                        readOnly
                        style={{
                            flex: 1,
                            padding: '10px',
                            border: '1px solid #ddd',
                            borderRadius: '6px',
                            fontSize: '13px',
                            color: '#2c3e50',
                            backgroundColor: '#f8f9fa'
                        }}
                        onClick={(e) => (e.target as HTMLInputElement).select()}
                    />
                    <button
                        onClick={() => {
                            navigator.clipboard.writeText(trackingUrl).catch((err) => {
                                console.error('Failed to copy:', err);
                            });
                            setCopySuccess(true);
                            setTimeout(() => setCopySuccess(false), 2000);
                        }}
                        style={{
                            padding: '10px 20px',
                            background: copySuccess ? '#27ae60' : 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                            color: 'white',
                            border: 'none',
                            borderRadius: '6px',
                            cursor: 'pointer',
                            fontSize: '14px',
                            fontWeight: '600',
                            whiteSpace: 'nowrap',
                            transition: 'all 0.3s ease'
                        }}
                    >
                        {copySuccess ? '✓ Copied!' : 'Copy Link'}
                    </button>
                </div>
                <p style={{
                    marginTop: '10px',
                    fontSize: '12px',
                    color: '#666'
                }}>
                    {fromCheckout
                        ? 'Bookmark this page or save the link to track your order later without logging in.'
                        : 'Anyone with this link can track this order without logging in.'}
                </p>
            </div>

            <div className="tracking-header">
                <div className="tracking-title">
                    <h1>Order #{order.orderId.substring(0, 8)}</h1>
                    <p className="tracking-subtitle">Ordered on {new Date(order.orderTime).toLocaleString()}</p>
                </div>
                {order.status === 'IN_DELIVERY' && (
                    <div className="live-badge">
                        <span className="live-dot"></span>
                        LIVE
                    </div>
                )}
            </div>

            {/* Arc Progress Visualization */}
            <div className="progress-arc-container">
                <svg className="progress-arc" viewBox="0 0 1200 200" preserveAspectRatio="xMidYMid meet">
                    {/* Background arc - subtle curve */}
                    <path
                        d="M 50 150 Q 600 50, 1150 150"
                        fill="none"
                        stroke="#e5e7eb"
                        strokeWidth="8"
                        strokeLinecap="round"
                    />
                    {/* Progress arc */}
                    {!isCancelled && (
                        <path
                            d="M 50 150 Q 600 50, 1150 150"
                            fill="none"
                            stroke={getStatusColor(order.status)}
                            strokeWidth="8"
                            strokeLinecap="round"
                            strokeDasharray="1650"
                            strokeDashoffset={1650 - (1650 * progress) / 100}
                            className="progress-arc-fill"
                        />
                    )}

                    {/* Status dots */}
                    {ORDER_STEPS.map((step, index) => {
                        const totalSteps = ORDER_STEPS.length - 1;
                        const t = index / totalSteps;

                        // Quadratic bezier curve: P = (1-t)²P0 + 2(1-t)tP1 + t²P2
                        const x = Math.pow(1 - t, 2) * 50 + 2 * (1 - t) * t * 600 + Math.pow(t, 2) * 1150;
                        const y = Math.pow(1 - t, 2) * 150 + 2 * (1 - t) * t * 50 + Math.pow(t, 2) * 150;

                        const isActive = index <= currentStepIndex && !isCancelled;
                        const isCurrent = index === currentStepIndex && !isCancelled;

                        return (
                            <g key={step}>
                                <circle
                                    cx={x}
                                    cy={y}
                                    r="6"
                                    fill={isActive ? getStatusColor(order.status) : '#e5e7eb'}
                                    stroke="white"
                                    strokeWidth="2"
                                />
                                <text
                                    x={x}
                                    y={y + 25}
                                    textAnchor="middle"
                                    className="status-label-svg"
                                    fill={isActive ? '#1f2937' : '#9ca3af'}
                                    fontSize="11"
                                    fontWeight={isCurrent ? '600' : 'normal'}
                                >
                                    {step.replace('_', ' ')}
                                </text>
                            </g>
                        );
                    })}
                </svg>

                {/* Current Status Display */}
                <div className="current-status-display">
                    <h2 className="status-title" style={{ color: getStatusColor(order.status) }}>
                        {getStatusLabel(order.status)}
                    </h2>
                    <p className="status-description">{getStatusDescription(order.status)}</p>
                    {order.status === 'IN_DELIVERY' && (
                        <div className="last-update">
                            Last updated: {getTimeSince(lastUpdated)}
                        </div>
                    )}
                </div>
            </div>

            {/* Courier Tracking */}
            {order.status === 'IN_DELIVERY' && order.courierLocation && (
                <div className="courier-tracking-section">
                    <h3>Courier Location</h3>
                    <div className="courier-info">
                        <div className="courier-detail">
                            <span>Latitude:</span>
                            <strong>{order.courierLocation.latitude.toFixed(6)}</strong>
                        </div>
                        <div className="courier-detail">
                            <span>Longitude:</span>
                            <strong>{order.courierLocation.longitude.toFixed(6)}</strong>
                        </div>
                        <div className="courier-detail">
                            <span>Last Updated:</span>
                            <strong>{new Date(order.courierLocation.lastUpdated).toLocaleTimeString()}</strong>
                        </div>
                    </div>
                </div>
            )}

            {/* Order Details */}
            <div className="order-details-section">
                <div className="order-details-card">
                    <h3>Order Items</h3>
                    <div className="order-items-list-tracking">
                        {order.items.map((item, index) => (
                            <div key={index} className="order-item-tracking">
                                <div className="item-info">
                                    <span className="item-name-tracking">{item.dishName}</span>
                                    <span className="item-details">
                                        €{item.unitPrice.toFixed(2)} × {item.quantity}
                                    </span>
                                </div>
                                <span className="item-total">€{item.totalPrice.toFixed(2)}</span>
                            </div>
                        ))}
                    </div>
                    <div className="order-total-tracking">
                        <span>Total:</span>
                        <strong>€{order.totalAmount.toFixed(2)}</strong>
                    </div>
                </div>

                <div className="order-details-card">
                    <h3>Delivery Address</h3>
                    <div className="address-display">
                        <p>{order.deliveryAddress.street} {order.deliveryAddress.number}</p>
                        <p>{order.deliveryAddress.postalCode} {order.deliveryAddress.city}</p>
                        <p>{order.deliveryAddress.country}</p>
                    </div>

                    <h3>Customer Information</h3>
                    <div className="info-display">
                        <div className="info-row-tracking">
                            <span>Name:</span>
                            <strong>{order.customerName}</strong>
                        </div>
                        <div className="info-row-tracking">
                            <span>Email:</span>
                            <strong>{order.contactEmail}</strong>
                        </div>
                    </div>

                    <h3>Payment Information</h3>
                    <div className="info-display">
                        <div className="info-row-tracking">
                            <span>Transaction ID:</span>
                            <strong>{order.paymentTransactionId.substring(0, 16)}...</strong>
                        </div>
                    </div>
                </div>
            </div>

            {/* Actions */}
            <div className="tracking-actions">
                <button
                    onClick={() => navigate('/customer/restaurants')}
                    className="btn-primary"
                >
                    Browse More Restaurants
                </button>
                <button
                    onClick={() => queryClient.invalidateQueries({ queryKey: ['order', orderId] })}
                    className="btn-secondary"
                    disabled={isLoading}
                >
                    {isLoading ? 'Refreshing...' : 'Refresh Status'}
                </button>
            </div>

            {order.status !== 'DELIVERED' && order.status !== 'CANCELLED' && (
                <div className="auto-update-notice">
                    {order.status === 'IN_DELIVERY'
                        ? 'This page updates every second during delivery'
                        : 'This page updates every 2 seconds'}
                </div>
            )}
        </div>
    );
};

export default OrderTrackingPage;