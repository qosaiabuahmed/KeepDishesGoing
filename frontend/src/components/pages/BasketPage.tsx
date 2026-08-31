import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { useBasket } from '../../hooks/useBasket';
import { removeDishFromBasket, clearBasket, validateBasket, getRestaurantById } from '../../services/api';
import type { ValidationResultDto } from '../../types/api.types';

const BasketPage = () => {
    const navigate = useNavigate();
    const { basket, refreshBasket, isLoading } = useBasket();
    const [validationResult, setValidationResult] = useState<ValidationResultDto | null>(null);
    const [isValidating, setIsValidating] = useState(false);
    const [removing, setRemoving] = useState<string | null>(null);

    const { data: restaurant } = useQuery({
        queryKey: ['restaurant', basket?.restaurantId],
        queryFn: async () => {
            if (!basket?.restaurantId) return null;
            const response = await getRestaurantById(basket.restaurantId);
            return response.data;
        },
        enabled: !!basket?.restaurantId
    });

    const handleRemoveDish = async (dishId: string) => {
        try {
            setRemoving(dishId);
            await removeDishFromBasket(dishId);
            await refreshBasket();
            setValidationResult(null);
        } catch (error) {
            console.error('Failed to remove dish:', error);
        } finally {
            setRemoving(null);
        }
    };

    const handleClearBasket = async () => {
        if (!confirm('Are you sure you want to clear your basket?')) return;

        try {
            await clearBasket();
            await refreshBasket();
            setValidationResult(null);
        } catch (error) {
            console.error('Failed to clear basket:', error);
        }
    };

    const handleCheckout = async () => {
        if (restaurant && !restaurant.isOpen) {
            alert('Sorry, this restaurant is currently closed and cannot accept orders. Please try again during opening hours.');
            return;
        }

        try {
            setIsValidating(true);
            const response = await validateBasket();
            setValidationResult(response.data);

            if (response.data.valid) {
                navigate('/customer/checkout');
            }
        } catch (error) {
            console.error('Failed to validate basket:', error);
        } finally {
            setIsValidating(false);
        }
    };

    if (isLoading) {
        return <div className="loading">Loading basket...</div>;
    }

    if (!basket || basket.items.length === 0) {
        return (
            <div className="basket-empty">
                <h2>Your Basket</h2>
                <p>Your basket is empty. Start adding some delicious dishes!</p>
            </div>
        );
    }

    return (
        <div className="basket-container">
            <div className="basket-header">
                <h2>Your Basket</h2>
                <button onClick={handleClearBasket} className="btn-secondary btn-sm">
                    Clear Basket
                </button>
            </div>

            {validationResult && !validationResult.valid && (
                <div className="validation-errors">
                    <h3>Checkout Blocked - Please Fix Issues:</h3>
                    <ul>
                        {validationResult.issues.map((issue) => (
                            <li key={issue.dishId} className="validation-issue">
                                <strong>{issue.dishName}:</strong> {issue.message}
                                <button
                                    onClick={() => handleRemoveDish(issue.dishId)}
                                    className="btn-secondary btn-sm"
                                >
                                    Remove
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>
            )}

            {validationResult && validationResult.valid && (
                <div className="validation-success">
                    Basket is valid! Ready for checkout.
                </div>
            )}

            {restaurant && !restaurant.isOpen && (
                <div className="restaurant-closed-warning">
                    <strong>Warning:</strong> {restaurant.name} is currently closed. You cannot proceed with checkout until the restaurant opens.
                </div>
            )}

            <div className="basket-items">
                {basket.items.map((item) => (
                    <div key={item.dishId} className="basket-item">
                        <div className="basket-item-info">
                            <h3>{item.dishName}</h3>
                            <p className="basket-item-quantity">Quantity: {item.quantity}</p>
                            <p className="basket-item-price">
                                €{item.unitPrice.toFixed(2)} × {item.quantity} = €{item.totalPrice.toFixed(2)}
                            </p>
                        </div>
                        <button
                            onClick={() => handleRemoveDish(item.dishId)}
                            disabled={removing === item.dishId}
                            className="btn-danger btn-sm"
                        >
                            {removing === item.dishId ? 'Removing...' : 'Remove'}
                        </button>
                    </div>
                ))}
            </div>

            <div className="basket-summary">
                <div className="basket-total">
                    <strong>Total Items:</strong> {basket.totalItemCount}
                </div>
                <div className="basket-total">
                    <strong>Total Price:</strong> €{basket.totalPrice.toFixed(2)}
                </div>
            </div>

            <div className="basket-actions">
                <button
                    onClick={handleCheckout}
                    disabled={isValidating || (validationResult !== null && !validationResult.valid)}
                    className="btn-primary btn-large"
                >
                    {isValidating ? 'Validating...' : validationResult && validationResult.valid ? 'Proceed to Checkout' : 'Validate & Checkout'}
                </button>
            </div>
        </div>
    );
};

export default BasketPage;