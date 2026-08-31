import { type ReactNode } from 'react';
import { useQuery } from '@tanstack/react-query';
import { getBasket } from '../services/api';
import { BasketContext } from '../hooks/basketContext.ts';

export const BasketProvider = ({ children }: { children: ReactNode }) => {
    const { data: basket = null, isLoading, refetch } = useQuery({
        queryKey: ['basket'],
        queryFn: async () => {
            try {
                const response = await getBasket();
                return response.data;
            } catch (error) {
                console.error('Failed to fetch basket:', error);
                return null;
            }
        },
        retry: false,
    });

    const refreshBasket = async () => {
        await refetch();
    };

    return (
        <BasketContext.Provider value={{ basket, refreshBasket, isLoading }}>
            {children}
        </BasketContext.Provider>
    );
};