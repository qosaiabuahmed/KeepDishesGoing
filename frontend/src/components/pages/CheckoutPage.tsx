import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { AxiosError } from 'axios';
import { useBasket } from '../../hooks/useBasket';
import { checkout } from '../../services/api';
import type { CheckoutRequest } from '../../types/api.types';

const Checkout = () => {
    const { basket } = useBasket();
    const navigate = useNavigate();
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const [formData, setFormData] = useState<CheckoutRequest>({
        customerName: '',
        contactEmail: '',
        street: '',
        number: '',
        city: '',
        postalCode: '',
        country: '',
        currency: 'eur'
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);

        try {
            setIsSubmitting(true);
            const response = await checkout(formData);

            navigate(`/customer/orders/${response.data.orderId}`, {
                state: {
                    fromCheckout: true
                }
            });
        } catch (error: unknown) {
            const err = error as AxiosError<{ error?: string; message?: string }>;
            console.error('Checkout failed:', err);

            const errorMessage = err.response?.data?.message || '';
            if (errorMessage.includes('currently closed') || errorMessage.includes('restaurant is closed')) {
                alert('Sorry, this restaurant is now closed and cannot accept your order. Please try again during opening hours.');
                navigate('/customer/restaurants');
                return;
            }

            setError(
                errorMessage || 'Failed to process checkout. Please try again.'
            );
        } finally {
            setIsSubmitting(false);
        }
    };

    if (!basket || basket.items.length === 0) {
        return (
            <div className="checkout-container">
                <h2>Checkout</h2>
                <div className="error-message">
                    Your basket is empty. Please add items before checking out.
                </div>
                <button onClick={() => navigate('/customer/restaurants')} className="btn-primary">
                    Browse Restaurants
                </button>
            </div>
        );
    }

    return (
        <div className="checkout-container">
            <h2>Checkout</h2>

            <div className="checkout-layout">
                <div className="checkout-form-section">
                    <h3>Delivery Information</h3>

                    {error && (
                        <div className="error-message" style={{ marginBottom: '20px' }}>
                            {error}
                        </div>
                    )}

                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label htmlFor="customerName">Full Name *</label>
                            <input
                                type="text"
                                id="customerName"
                                name="customerName"
                                value={formData.customerName}
                                onChange={handleChange}
                                required
                                placeholder="John Doe"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="contactEmail">Email *</label>
                            <input
                                type="email"
                                id="contactEmail"
                                name="contactEmail"
                                value={formData.contactEmail}
                                onChange={handleChange}
                                required
                                placeholder="john@example.com"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="street">Street Name *</label>
                            <input
                                type="text"
                                id="street"
                                name="street"
                                value={formData.street}
                                onChange={handleChange}
                                required
                                placeholder="Main Street"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="number">Street Number *</label>
                            <input
                                type="text"
                                id="number"
                                name="number"
                                value={formData.number}
                                onChange={handleChange}
                                required
                                placeholder="123A"
                            />
                        </div>

                        <div className="form-row">
                            <div className="form-group">
                                <label htmlFor="city">City *</label>
                                <input
                                    type="text"
                                    id="city"
                                    name="city"
                                    value={formData.city}
                                    onChange={handleChange}
                                    required
                                    placeholder="Brussels"
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="postalCode">Postal Code *</label>
                                <input
                                    type="text"
                                    id="postalCode"
                                    name="postalCode"
                                    value={formData.postalCode}
                                    onChange={handleChange}
                                    required
                                    placeholder="1000"
                                />
                            </div>
                        </div>

                        <div className="form-group">
                            <label htmlFor="country">Country *</label>
                            <input
                                type="text"
                                id="country"
                                name="country"
                                value={formData.country}
                                onChange={handleChange}
                                required
                                placeholder="Belgium"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="currency">Currency</label>
                            <select
                                id="currency"
                                name="currency"
                                value={formData.currency}
                                onChange={handleChange}
                            >
                                <option value="eur">EUR</option>
                                <option value="usd">USD</option>
                                <option value="gbp">GBP</option>
                            </select>
                        </div>

                        <div className="checkout-actions">
                            <button
                                type="button"
                                onClick={() => navigate('/customer/basket')}
                                className="btn-secondary"
                                disabled={isSubmitting}
                            >
                                Back to Basket
                            </button>
                            <button
                                type="submit"
                                className="btn-primary"
                                disabled={isSubmitting}
                            >
                                {isSubmitting ? 'Processing...' : 'Place Order'}
                            </button>
                        </div>
                    </form>
                </div>

                <div className="order-summary-section">
                    <h3>Order Summary</h3>
                    <div className="summary-items">
                        {basket.items.map((item) => (
                            <div key={item.dishId} className="summary-item">
                                <div className="summary-item-info">
                                    <span className="summary-item-name">{item.dishName}</span>
                                    <span className="summary-item-quantity">x{item.quantity}</span>
                                </div>
                                <span className="summary-item-price">
                                    €{item.totalPrice.toFixed(2)}
                                </span>
                            </div>
                        ))}
                    </div>
                    <div className="summary-total">
                        <strong>Total:</strong>
                        <strong>€{basket.totalPrice.toFixed(2)}</strong>
                    </div>
                    <div className="payment-info">
                        <p style={{ fontSize: '0.9em', color: '#666', marginTop: '20px' }}>
                            Payment will be processed securely via Stripe.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Checkout;