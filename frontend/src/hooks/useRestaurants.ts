import { useQuery } from '@tanstack/react-query';
import { getCustomerRestaurants } from '../services/api';

export const useRestaurants = (filters?: { cuisine?: string; priceRange?: string }) => {
    const cuisineKey = filters?.cuisine || '';
    const priceRangeKey = filters?.priceRange || '';

    const { data, isLoading, error, refetch } = useQuery({
        queryKey: ['restaurants', cuisineKey, priceRangeKey],
        queryFn: async () => {
            const params: { cuisine?: string; priceRange?: string } = {};
            if (cuisineKey) params.cuisine = cuisineKey;
            if (priceRangeKey) params.priceRange = priceRangeKey;

            const response = await getCustomerRestaurants(params);
            return response.data;
        },
    });

    return {
        restaurants: data || [],
        loading: isLoading,
        error: error ? 'Failed to load restaurants' : '',
        refetch,
    };
};