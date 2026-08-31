import { useState } from 'react';
import type { OrderProjectionDto } from '../../types/api.types';
import RejectOrderModal from './RejectOrderModal';

interface OrderDetailModalProps {
    order: OrderProjectionDto | null;
    isOpen: boolean;
    onClose: () => void;
    onAccept: (orderId: string) => void;
    onReject: (orderId: string, reason: string) => void;
}

const OrderDetailModal = ({
    order,
    isOpen,
    onClose,
    onAccept,
    onReject
}: OrderDetailModalProps) => {
    const [isRejectModalOpen, setIsRejectModalOpen] = useState(false);
    const [isProcessing, setIsProcessing] = useState(false);

    if (!isOpen || !order) return null;

    const handleAccept = async () => {
        setIsProcessing(true);
        try {
            await onAccept(order.orderId);
        } finally {
            setIsProcessing(false);
        }
    };

    const handleReject = async (reason: string) => {
        setIsProcessing(true);
        try {
            await onReject(order.orderId, reason);
            setIsRejectModalOpen(false);
        } finally {
            setIsProcessing(false);
        }
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

    return (
        <>
            <div className="modal-overlay" onClick={onClose}>
                <div className="modal-content modal-large" onClick={(e) => e.stopPropagation()}>
                    <div className="modal-header">
                        <h2>Order Details</h2>
                        <button
                            className="modal-close"
                            onClick={onClose}
                            disabled={isProcessing}
                        >
                            &times;
                        </button>
                    </div>
                    <div className="modal-body">
                        <div className="order-detail-grid">
                            {/* Order Info */}
                            <div className="order-detail-section">
                                <h3>Order Information</h3>
                                <div className="order-detail-info">
                                    <div className="info-row">
                                        <span className="info-label">Order ID:</span>
                                        <span className="info-value">{order.orderId.substring(0, 8)}...</span>
                                    </div>
                                    <div className="info-row">
                                        <span className="info-label">Time:</span>
                                        <span className={`info-value ${isCloseToDeadline(order.orderTime) ? 'text-warning' : ''}`}>
                                            {formatDate(order.orderTime)} ({getTimeElapsed(order.orderTime)})
                                        </span>
                                    </div>
                                    {isCloseToDeadline(order.orderTime) && (
                                        <div className="warning-box">
                                            <strong>Warning:</strong> This order is close to the 5-minute auto-decline deadline!
                                        </div>
                                    )}
                                </div>
                            </div>

                            {/* Customer Info */}
                            <div className="order-detail-section">
                                <h3>Customer Information</h3>
                                <div className="order-detail-info">
                                    <div className="info-row">
                                        <span className="info-label">Name:</span>
                                        <span className="info-value">{order.customerName}</span>
                                    </div>
                                    <div className="info-row">
                                        <span className="info-label">Email:</span>
                                        <span className="info-value">{order.contactEmail}</span>
                                    </div>
                                </div>
                            </div>

                            {/* Delivery Address */}
                            <div className="order-detail-section">
                                <h3>Delivery Address</h3>
                                <div className="order-detail-info">
                                    <p>
                                        {order.deliveryAddress.street} {order.deliveryAddress.number}<br />
                                        {order.deliveryAddress.postalCode} {order.deliveryAddress.city}<br />
                                        {order.deliveryAddress.country}
                                    </p>
                                </div>
                            </div>

                            {/* Order Items */}
                            <div className="order-detail-section order-items-section">
                                <h3>Order Items</h3>
                                <div className="order-items-list">
                                    {order.items.map((item, index) => (
                                        <div key={index} className="order-item">
                                            <div className="order-item-details">
                                                <span className="order-item-name">{item.dishName}</span>
                                                <span className="order-item-quantity">x{item.quantity}</span>
                                            </div>
                                            <div className="order-item-pricing">
                                                <span className="order-item-unit-price">€{item.unitPrice.toFixed(2)} each</span>
                                                <span className="order-item-total">€{item.totalPrice.toFixed(2)}</span>
                                            </div>
                                        </div>
                                    ))}
                                    <div className="order-total">
                                        <span className="order-total-label">Total Amount:</span>
                                        <span className="order-total-value">€{order.totalAmount.toFixed(2)}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="modal-footer">
                        <button
                            className="btn-secondary"
                            onClick={onClose}
                            disabled={isProcessing}
                        >
                            Close
                        </button>
                        <div className="action-buttons">
                            <button
                                className="btn-danger"
                                onClick={() => setIsRejectModalOpen(true)}
                                disabled={isProcessing}
                            >
                                Reject Order
                            </button>
                            <button
                                className="btn-success"
                                onClick={handleAccept}
                                disabled={isProcessing}
                            >
                                {isProcessing ? 'Processing...' : 'Accept Order'}
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <RejectOrderModal
                isOpen={isRejectModalOpen}
                onClose={() => setIsRejectModalOpen(false)}
                onConfirm={handleReject}
                isLoading={isProcessing}
            />
        </>
    );
};

export default OrderDetailModal;