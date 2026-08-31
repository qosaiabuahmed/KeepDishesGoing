import { createContext } from 'react';
import type { BasketDto } from '../types/api.types.ts';

interface BasketContextType {
    basket: BasketDto | null;
    refreshBasket: () => Promise<void>;
    isLoading: boolean;
}

export const BasketContext = createContext<BasketContextType | undefined>(undefined);
