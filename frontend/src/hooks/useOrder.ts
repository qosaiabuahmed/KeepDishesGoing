import { useQuery } from '@tanstack/react-query';
import { getOrder } from '../services/api';

export const useOrder = (orderId: string | undefined) => {
    const { data, isLoading, error, refetch } = useQuery({
        queryKey: ['order', orderId],
        queryFn: async () => {
            if (!orderId) return null;
            const response = await getOrder(orderId);
            return response.data;
        },
        enabled: !!orderId
    });

    return {
        order: data || null,
        loading: isLoading,
        error: error ? 'Failed to load order details. Please try again.' : null,
        refetch,
    };
};