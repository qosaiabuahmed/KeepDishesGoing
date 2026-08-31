import { useQuery } from '@tanstack/react-query';
import { getCustomerMenu } from '../services/api';

export const useMenu = (
    restaurantId: string | undefined,
    filters?: { type?: string; foodTags?: string[]; sortBy?: string }
) => {
    const typeKey = filters?.type || '';
    const foodTagsKey = filters?.foodTags?.join(',') || '';
    const sortByKey = filters?.sortBy || '';

    const { data, isLoading, error, refetch } = useQuery({
        queryKey: ['menu', restaurantId, typeKey, foodTagsKey, sortByKey],
        queryFn: async () => {
            if (!restaurantId) throw new Error('Restaurant ID is required');

            const params: { type?: string; foodTags?: string[]; sortBy?: string } = {};
            if (typeKey) params.type = typeKey;
            if (filters?.foodTags && filters.foodTags.length > 0) params.foodTags = filters.foodTags;
            if (sortByKey) params.sortBy = sortByKey;

            const response = await getCustomerMenu(restaurantId, params);
            return response.data;
        },
        enabled: !!restaurantId,
    });

    return {
        dishes: data || [],
        loading: isLoading,
        error: error ? 'Failed to load menu' : '',
        refetch,
    };
};