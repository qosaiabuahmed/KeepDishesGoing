import axios, {type AxiosResponse } from 'axios';
import type {
    RestaurantDto,
    DishDto,
    CreateRestaurantRequest,
    CreateDishRequest,
    UpdateDishRequest,
    BasketDto,
    AddDishToBasketRequest,
    ValidationResultDto,
    CheckoutRequest,
    CheckoutResponse,
    OrderDto,
    OrderProjectionDto,
    RejectOrderRequest,
    SchedulePublicationRequest,
    PriceRangeCriteriaDto,
    UpdatePriceRangeCriteriaRequest,
    PriceRangeSnapshotDto
} from '../types/api.types';
import { getCustomerId } from '../utils/customerId';

const api = axios.create({
    baseURL: import.meta.env.VITE_BACKEND_URL,
});

export const setAuthToken = (token: string | undefined): void => {
    if (token) {
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    } else {
        delete api.defaults.headers.common['Authorization'];
    }
};

export const getAllRestaurants = (): Promise<AxiosResponse<RestaurantDto[]>> =>
    api.get<RestaurantDto[]>('/api/restaurants');

export const getRestaurantById = (id: string): Promise<AxiosResponse<RestaurantDto>> =>
    api.get<RestaurantDto>(`/api/restaurants/${id}`);

export const createRestaurant = (request: CreateRestaurantRequest): Promise<AxiosResponse<void>> =>
    api.post<void>('/api/restaurants', request);

export const getAllDishes = (restaurantId: string): Promise<AxiosResponse<DishDto[]>> =>
    api.get<DishDto[]>(`/api/restaurants/${restaurantId}/dishes`);

export const getDishById = (restaurantId: string, dishId: string): Promise<AxiosResponse<DishDto>> =>
    api.get<DishDto>(`/api/restaurants/${restaurantId}/dishes/${dishId}`);

export const createDish = (restaurantId: string, request: CreateDishRequest): Promise<AxiosResponse<void>> =>
    api.post<void>(`/api/restaurants/${restaurantId}/dishes`, request);

export const updateDish = (restaurantId: string, dishId: string, request: UpdateDishRequest): Promise<AxiosResponse<void>> =>
    api.put<void>(`/api/restaurants/${restaurantId}/dishes/${dishId}`, request);

export const publishDish = (restaurantId: string, dishId: string): Promise<AxiosResponse<void>> =>
    api.post<void>(`/api/restaurants/${restaurantId}/dishes/${dishId}/publish`);

export const unpublishDish = (restaurantId: string, dishId: string): Promise<AxiosResponse<void>> =>
    api.post<void>(`/api/restaurants/${restaurantId}/dishes/${dishId}/unpublish`);

export const markDishOutOfStock = (restaurantId: string, dishId: string): Promise<AxiosResponse<void>> =>
    api.post<void>(`/api/restaurants/${restaurantId}/dishes/${dishId}/out-of-stock`);

export const markDishInStock = (restaurantId: string, dishId: string): Promise<AxiosResponse<void>> =>
    api.post<void>(`/api/restaurants/${restaurantId}/dishes/${dishId}/in-stock`);

export const getPendingChangesCount = (restaurantId: string): Promise<AxiosResponse<number>> =>
    api.get<number>(`/api/restaurants/${restaurantId}/dishes/pending-changes/count`);

export const publishAllPending = (restaurantId: string): Promise<AxiosResponse<void>> =>
    api.post<void>(`/api/restaurants/${restaurantId}/dishes/publish-all-pending`);

export const schedulePublication = (restaurantId: string, request: SchedulePublicationRequest): Promise<AxiosResponse<void>> =>
    api.post<void>(`/api/restaurants/${restaurantId}/dishes/schedule-publication`, request);

export const openRestaurant = (restaurantId: string): Promise<AxiosResponse<void>> =>
    api.post<void>(`/api/restaurants/${restaurantId}/open`);

export const closeRestaurant = (restaurantId: string): Promise<AxiosResponse<void>> =>
    api.post<void>(`/api/restaurants/${restaurantId}/close`);
export const getCustomerMenu = (
    restaurantId: string,
    params?: {
        type?: string;
        foodTags?: string[];
        sortBy?: string;
    }
) => {
    const queryParams = new URLSearchParams();

    if (params?.type) {
        queryParams.append('type', params.type);
    }

    if (params?.foodTags && params.foodTags.length > 0) {
        params.foodTags.forEach(tag => queryParams.append('foodTags', tag));
    }

    if (params?.sortBy) {
        queryParams.append('sortBy', params.sortBy);
    }

    const queryString = queryParams.toString();
    const url = queryString
        ? `/api/customer/restaurants/${restaurantId}/menu?${queryString}`
        : `/api/customer/restaurants/${restaurantId}/menu`;

    return api.get<DishDto[]>(url);
};

export const getCustomerRestaurants = (params?: {
    cuisine?: string;
    priceRange?: string;
}): Promise<AxiosResponse<RestaurantDto[]>> => {
    const queryParams = new URLSearchParams();

    if (params?.cuisine) {
        queryParams.append('cuisine', params.cuisine);
    }

    if (params?.priceRange) {
        queryParams.append('priceRange', params.priceRange);
    }

    const queryString = queryParams.toString();
    const url = queryString
        ? `/api/customer/restaurants?${queryString}`
        : '/api/customer/restaurants';

    return api.get<RestaurantDto[]>(url);
};

export const addDishToBasket = (request: AddDishToBasketRequest): Promise<AxiosResponse<void>> => {
    const customerId = getCustomerId();
    return api.post<void>('/api/customer/basket/items', request, {
        headers: { 'X-Customer-Id': customerId }
    });
};

export const getBasket = (): Promise<AxiosResponse<BasketDto>> => {
    const customerId = getCustomerId();
    return api.get<BasketDto>('/api/customer/basket', {
        headers: { 'X-Customer-Id': customerId }
    });
};

export const removeDishFromBasket = (dishId: string): Promise<AxiosResponse<void>> => {
    const customerId = getCustomerId();
    return api.delete<void>(`/api/customer/basket/items/${dishId}`, {
        headers: { 'X-Customer-Id': customerId }
    });
};

export const clearBasket = (): Promise<AxiosResponse<void>> => {
    const customerId = getCustomerId();
    return api.delete<void>('/api/customer/basket', {
        headers: { 'X-Customer-Id': customerId }
    });
};

export const validateBasket = (): Promise<AxiosResponse<ValidationResultDto>> => {
    const customerId = getCustomerId();
    return api.post<ValidationResultDto>('/api/customer/basket/validate', {}, {
        headers: { 'X-Customer-Id': customerId }
    });
};

export const checkout = (request: CheckoutRequest): Promise<AxiosResponse<CheckoutResponse>> => {
    const customerId = getCustomerId();
    return api.post<CheckoutResponse>('/api/customer/orders/checkout', request, {
        headers: { 'X-Customer-Id': customerId }
    });
};

export const getOrder = (orderId: string): Promise<AxiosResponse<OrderDto>> => {
    return api.get<OrderDto>(`/api/customer/orders/${orderId}`);
};

export const getOwnerOrders = (restaurantId: string, status?: string): Promise<AxiosResponse<OrderProjectionDto[]>> => {
    if (status === 'SUBMITTED') {
        return api.get<OrderProjectionDto[]>(`/api/restaurants/${restaurantId}/orders/submitted`);
    } else if (status === 'CONFIRMED') {
        return api.get<OrderProjectionDto[]>(`/api/restaurants/${restaurantId}/orders/confirmed`);
    } else {
        return api.get<OrderProjectionDto[]>(`/api/restaurants/${restaurantId}/orders`);
    }
};

export const getSubmittedOrders = (restaurantId: string): Promise<AxiosResponse<OrderProjectionDto[]>> => {
    return api.get<OrderProjectionDto[]>(`/api/restaurants/${restaurantId}/orders/submitted`);
};

export const getConfirmedOrders = (restaurantId: string): Promise<AxiosResponse<OrderProjectionDto[]>> => {
    return api.get<OrderProjectionDto[]>(`/api/restaurants/${restaurantId}/orders/confirmed`);
};

export const getAllOwnerOrders = (restaurantId: string): Promise<AxiosResponse<OrderProjectionDto[]>> => {
    return api.get<OrderProjectionDto[]>(`/api/restaurants/${restaurantId}/orders`);
};

export const acceptOrder = (restaurantId: string, orderId: string): Promise<AxiosResponse<void>> => {
    return api.post<void>(`/api/restaurants/${restaurantId}/orders/${orderId}/accept`);
};

export const rejectOrder = (restaurantId: string, orderId: string, request: RejectOrderRequest): Promise<AxiosResponse<void>> => {
    return api.post<void>(`/api/restaurants/${restaurantId}/orders/${orderId}/reject`, request);
};

export const markOrderReady = (restaurantId: string, orderId: string): Promise<AxiosResponse<void>> => {
    return api.post<void>(`/api/restaurants/${restaurantId}/orders/${orderId}/ready`);
};

export const getPriceRangeCriteria = (): Promise<AxiosResponse<PriceRangeCriteriaDto>> =>
    api.get<PriceRangeCriteriaDto>('/api/admin/price-range-criteria');

export const updatePriceRangeCriteria = (request: UpdatePriceRangeCriteriaRequest): Promise<AxiosResponse<PriceRangeCriteriaDto>> =>
    api.patch<PriceRangeCriteriaDto>('/api/admin/price-range-criteria', request);

export const getPriceRangeEvolution = (restaurantId: string): Promise<AxiosResponse<PriceRangeSnapshotDto[]>> =>
    api.get<PriceRangeSnapshotDto[]>(`/api/admin/price-range-criteria/restaurants/${restaurantId}/price-evolution`);

export default api;