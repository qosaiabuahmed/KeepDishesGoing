export interface RestaurantDto {
    id: string;
    name: string;
    cuisine: string;
    address: AddressDto;
    contactEmail: string;
    imageUrls: string[];
    defaultPrepTimeMinutes: number;
    openingHours: OpeningHoursDto;
    isOpen: boolean;
    availableDishesCount: number;
    priceRange?: string; // NEW: CHEAP, REGULAR, EXPENSIVE, PREMIUM
    averageMenuPrice?: number; // NEW: Average price of all dishes
}

export interface AddressDto {
    street: string;
    number: string;
    postalCode: string;
    city: string;
    country: string;
}

export interface OpeningHoursDto {
    schedule: string;
}

export interface DishDto {
    id: string;
    // Published fields (what customers see)
    name: string;
    type: string;
    foodTags: string[];
    description: string;
    price: number;
    imageUrl: string;
    // Draft fields (pending changes for owners) - optional for backward compatibility
    draftName?: string;
    draftType?: string;
    draftFoodTags?: string[];
    draftDescription?: string;
    draftPrice?: number;
    draftImageUrl?: string;
    // Status flags
    publishStatus: string;
    stockStatus: string;
    hasPendingChanges: boolean;
    isAvailableForOrder: boolean;
    isVisible: boolean;
}

export const DishType = {
    STARTER: 'STARTER',
    MAIN: 'MAIN',
    DESSERT: 'DESSERT'
} as const;

export const FoodTag = {
    LACTOSE: 'LACTOSE',
    GLUTEN: 'GLUTEN',
    VEGAN: 'VEGAN',
    VEGETARIAN: 'VEGETARIAN',
    NUTS: 'NUTS',
    SEAFOOD: 'SEAFOOD',
    SPICY: 'SPICY',
    ORGANIC: 'ORGANIC',
    HALAL: 'HALAL',
    KOSHER: 'KOSHER'
} as const;

export type FoodTag = typeof FoodTag[keyof typeof FoodTag];

export const PublishStatus = {
    DRAFT: 'DRAFT',
    PUBLISHED: 'PUBLISHED',
    UNPUBLISHED: 'UNPUBLISHED'
} as const;

export const StockStatus = {
    IN_STOCK: 'IN_STOCK',
    OUT_OF_STOCK: 'OUT_OF_STOCK'
} as const;

export const Cuisine = {
    ITALIAN: 'ITALIAN',
    FRENCH: 'FRENCH',
    JAPANESE: 'JAPANESE',
    CHINESE: 'CHINESE',
    INDIAN: 'INDIAN',
    MEXICAN: 'MEXICAN',
    THAI: 'THAI',
    GREEK: 'GREEK',
    AMERICAN: 'AMERICAN',
    MEDITERRANEAN: 'MEDITERRANEAN',
    MIDDLE_EASTERN: 'MIDDLE_EASTERN',
    VIETNAMESE: 'VIETNAMESE',
    KOREAN: 'KOREAN',
    SPANISH: 'SPANISH',
    TURKISH: 'TURKISH'
} as const;

export const PriceRange = {
    CHEAP: 'CHEAP',
    REGULAR: 'REGULAR',
    EXPENSIVE: 'EXPENSIVE',
    PREMIUM: 'PREMIUM'
} as const;

export const DishSortBy = {
    PRICE: 'price',
    PRICE_DESC: 'price_desc',
    NAME: 'name'
} as const;

export type Cuisine = typeof Cuisine[keyof typeof Cuisine];
export type PriceRange = typeof PriceRange[keyof typeof PriceRange];
export type DishSortBy = typeof DishSortBy[keyof typeof DishSortBy];

export interface CreateRestaurantRequest {
    name: string;
    cuisine: string;
    address: CreateAddressRequest;
    contactEmail: string;
    imageUrls: string[];
    defaultPrepTimeMinutes: number;
    openingHours: string;
}

export interface CreateAddressRequest {
    street: string;
    number: string;
    postalCode: string;
    city: string;
    country: string;
}

export interface CreateDishRequest {
    name: string;
    type: string;
    foodTags: string[];
    description: string;
    price: number;
    imageUrl: string;
}

export interface UpdateDishRequest {
    name: string;
    type: string;
    foodTags: string[];
    description: string;
    price: number;
    imageUrl: string;
}

export interface SchedulePublicationRequest {
    dishIdsToPublish: string[];
    dishIdsToUnpublish: string[];
    scheduledTime: string; // ISO 8601 datetime
}

export interface BasketItemDto {
    dishId: string;
    dishName: string;
    unitPrice: number;
    quantity: number;
    totalPrice: number;
}

export interface BasketDto {
    basketId: string;
    restaurantId: string | null;
    items: BasketItemDto[];
    totalPrice: number;
    totalItemCount: number;
}

export interface AddDishToBasketRequest {
    restaurantId: string;
    dishId: string;
    dishName: string;
    unitPrice: number;
    quantity: number;
}

export interface ValidationIssueDto {
    dishId: string;
    dishName: string;
    issueType: 'DISH_NOT_FOUND' | 'DISH_OUT_OF_STOCK';
    message: string;
}

export interface ValidationResultDto {
    valid: boolean;
    issues: ValidationIssueDto[];
}

export interface DeliveryAddressDto {
    street: string;
    number: string;
    city: string;
    postalCode: string;
    country: string;
}

export interface OrderItemDto {
    dishId: string;
    dishName: string;
    unitPrice: number;
    quantity: number;
    totalPrice: number;
}

export interface CourierLocationDto {
    latitude: number;
    longitude: number;
    lastUpdated: string;
}

export interface OrderDto {
    orderId: string;
    customerId: string;
    restaurantId: string;
    customerName: string;
    contactEmail: string;
    deliveryAddress: DeliveryAddressDto;
    items: OrderItemDto[];
    totalAmount: number;
    paymentTransactionId: string;
    orderTime: string;
    status: OrderStatus;
    courierLocation?: CourierLocationDto | null;
}

export interface OrderProjectionDto extends OrderDto {
    rejectionReason: string | null;
    decisionTime: string | null;
}

export interface RejectOrderRequest {
    reason: string;
}

export interface CheckoutRequest {
    customerName: string;
    contactEmail: string;
    street: string;
    number: string;
    city: string;
    postalCode: string;
    country: string;
    currency: string;
}

export interface CheckoutResponse {
    orderId: string;
    message: string;
    trackingUrl: string;
}

export type OrderStatus =
    | 'SUBMITTED'
    | 'CONFIRMED'
    | 'READY'
    | 'IN_DELIVERY'
    | 'DELIVERED'
    | 'CANCELLED';

export interface PriceRangeCriteriaDto {
    lowThreshold: number;
    mediumThreshold: number;
    highThreshold: number;
}

export interface UpdatePriceRangeCriteriaRequest {
    lowThreshold: number;
    mediumThreshold: number;
    highThreshold: number;
}

export interface PriceRangeSnapshotDto {
    date: string;
    priceRange: PriceRange;
    averageMenuPrice: number;
    lowThreshold: number;
    mediumThreshold: number;
    highThreshold: number;
}