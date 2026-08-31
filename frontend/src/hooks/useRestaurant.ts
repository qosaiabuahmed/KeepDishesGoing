import { useQuery } from '@tanstack/react-query';
import { getRestaurantById } from '../services/api';

export const useRestaurant = (restaurantId: string | undefined) => {
    const { data, isLoading, error } = useQuery({
        queryKey: ['restaurant', restaurantId],
        queryFn: async () => {
            if (!restaurantId) throw new Error('Restaurant ID is required');
            const response = await getRestaurantById(restaurantId);
            return response.data;
        },
        enabled: !!restaurantId,
    });

    return {
        restaurant: data || null,
        loading: isLoading,
        error: error ? 'Failed to load restaurant' : '',
    };
};