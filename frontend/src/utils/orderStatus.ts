import type { OrderStatus } from '../types/api.types';

export const ORDER_STEPS: OrderStatus[] = ['SUBMITTED', 'CONFIRMED', 'READY', 'IN_DELIVERY', 'DELIVERED'];

export const getStatusColor = (status: OrderStatus): string => {
    switch (status) {
        case 'SUBMITTED':
            return '#f59e0b';
        case 'CONFIRMED':
            return '#3b82f6';
        case 'READY':
            return '#f97316';
        case 'IN_DELIVERY':
            return '#8b5cf6';
        case 'DELIVERED':
            return '#10b981';
        case 'CANCELLED':
            return '#ef4444';
        default:
            return '#6b7280';
    }
};

export const getStatusLabel = (status: OrderStatus): string => {
    switch (status) {
        case 'SUBMITTED':
            return 'Order Submitted';
        case 'CONFIRMED':
            return 'Order Confirmed';
        case 'READY':
            return 'Ready for Pickup';
        case 'IN_DELIVERY':
            return 'Out for Delivery';
        case 'DELIVERED':
            return 'Delivered';
        case 'CANCELLED':
            return 'Cancelled';
        default:
            return status;
    }
};

export const getStatusDescription = (status: OrderStatus): string => {
    switch (status) {
        case 'SUBMITTED':
            return 'Your order has been placed and is waiting for restaurant confirmation.';
        case 'CONFIRMED':
            return 'The restaurant has confirmed your order and is preparing your food.';
        case 'READY':
            return 'Your food is ready and waiting for delivery pickup.';
        case 'IN_DELIVERY':
            return 'Your order is on its way to you!';
        case 'DELIVERED':
            return 'Your order has been delivered. Enjoy your meal!';
        case 'CANCELLED':
            return 'This order has been cancelled.';
        default:
            return '';
    }
};

export const getCurrentStepIndex = (status: OrderStatus): number => {
    if (status === 'CANCELLED') return 0;
    return ORDER_STEPS.indexOf(status);
};

export const getProgressPercentage = (status: OrderStatus): number => {
    if (status === 'CANCELLED') return 0;
    const currentIndex = getCurrentStepIndex(status);
    return (currentIndex / (ORDER_STEPS.length - 1)) * 100;
};

export const getTimeSinceLastUpdate = (lastUpdated: Date): string => {
    const seconds = Math.floor((new Date().getTime() - lastUpdated.getTime()) / 1000);
    if (seconds < 10) return 'Just now';
    if (seconds < 60) return `${seconds} seconds ago`;
    const minutes = Math.floor(seconds / 60);
    if (minutes === 1) return '1 minute ago';
    return `${minutes} minutes ago`;
};
