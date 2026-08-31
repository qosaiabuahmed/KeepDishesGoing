import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getPriceRangeCriteria, updatePriceRangeCriteria } from '../../services/api';
import type { UpdatePriceRangeCriteriaRequest } from '../../types/api.types';
import RestaurantPriceRangeList from '../admin/RestaurantPriceRangeList';

function AdminPriceRangeCriteriaPage() {
    const queryClient = useQueryClient();
    const [formData, setFormData] = useState<UpdatePriceRangeCriteriaRequest>({
        lowThreshold: 10,
        mediumThreshold: 30,
        highThreshold: 60
    });
    const [errorMessage, setErrorMessage] = useState<string>('');
    const [successMessage, setSuccessMessage] = useState<string>('');

    const { data: criteria, isLoading } = useQuery({
        queryKey: ['priceRangeCriteria'],
        queryFn: async () => {
            const response = await getPriceRangeCriteria();
            setFormData({
                lowThreshold: response.data.lowThreshold,
                mediumThreshold: response.data.mediumThreshold,
                highThreshold: response.data.highThreshold
            });
            return response.data;
        }
    });

    const updateMutation = useMutation({
        mutationFn: updatePriceRangeCriteria,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['priceRangeCriteria'] });
            setSuccessMessage('Price range criteria updated successfully. All restaurant price ranges have been recalculated.');
            setErrorMessage('');
            setTimeout(() => setSuccessMessage(''), 5000);
        },
        onError: (error: Error) => {
            setErrorMessage(error.message || 'Failed to update price range criteria');
            setSuccessMessage('');
        }
    });

    const handleInputChange = (field: keyof UpdatePriceRangeCriteriaRequest, value: string) => {
        const numValue = parseFloat(value);
        setFormData(prev => ({
            ...prev,
            [field]: isNaN(numValue) ? 0 : numValue
        }));
        setErrorMessage('');
        setSuccessMessage('');
    };

    const validateForm = (): string | null => {
        if (formData.lowThreshold <= 0 || formData.mediumThreshold <= 0 || formData.highThreshold <= 0) {
            return 'All thresholds must be positive numbers';
        }
        if (formData.lowThreshold >= formData.mediumThreshold) {
            return 'Low threshold must be less than medium threshold';
        }
        if (formData.mediumThreshold >= formData.highThreshold) {
            return 'Medium threshold must be less than high threshold';
        }
        return null;
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        const validationError = validateForm();
        if (validationError) {
            setErrorMessage(validationError);
            return;
        }

        updateMutation.mutate(formData);
    };

    const handleReset = () => {
        if (criteria) {
            setFormData({
                lowThreshold: criteria.lowThreshold,
                mediumThreshold: criteria.mediumThreshold,
                highThreshold: criteria.highThreshold
            });
            setErrorMessage('');
            setSuccessMessage('');
        }
    };

    if (isLoading) {
        return <div className="loading">Loading price range criteria...</div>;
    }

    return (
        <div className="page-container">
            <div className="page-header">
                <h1>Price Range Criteria Management</h1>
                <p className="text-muted">
                    Configure the thresholds that determine restaurant price range categories
                </p>
            </div>

            {successMessage && (
                <div className="alert alert-success">
                    {successMessage}
                </div>
            )}

            {errorMessage && (
                <div className="alert alert-error">
                    {errorMessage}
                </div>
            )}

            <div className="criteria-container">
                <div className="criteria-info-card">
                    <h3>Current Price Range Categories</h3>
                    <div className="price-ranges">
                        <div className="price-range-item">
                            <span className="price-symbol">€</span>
                            <div className="price-range-details">
                                <strong>Cheap</strong>
                                <span className="text-muted">≤ €{formData.lowThreshold.toFixed(2)}</span>
                            </div>
                        </div>
                        <div className="price-range-item">
                            <span className="price-symbol">€€</span>
                            <div className="price-range-details">
                                <strong>Regular</strong>
                                <span className="text-muted">
                                    €{formData.lowThreshold.toFixed(2)} - €{formData.mediumThreshold.toFixed(2)}
                                </span>
                            </div>
                        </div>
                        <div className="price-range-item">
                            <span className="price-symbol">€€€</span>
                            <div className="price-range-details">
                                <strong>Expensive</strong>
                                <span className="text-muted">
                                    €{formData.mediumThreshold.toFixed(2)} - €{formData.highThreshold.toFixed(2)}
                                </span>
                            </div>
                        </div>
                        <div className="price-range-item">
                            <span className="price-symbol">€€€€</span>
                            <div className="price-range-details">
                                <strong>Premium</strong>
                                <span className="text-muted">&gt; €{formData.highThreshold.toFixed(2)}</span>
                            </div>
                        </div>
                    </div>
                </div>

                <form onSubmit={handleSubmit} className="criteria-form">
                    <h3>Update Thresholds</h3>

                    <div className="form-group">
                        <label htmlFor="lowThreshold">
                            Low Threshold (€)
                            <span className="label-hint">Cheap price range</span>
                        </label>
                        <input
                            type="number"
                            id="lowThreshold"
                            step="0.01"
                            min="0.01"
                            value={formData.lowThreshold}
                            onChange={(e) => handleInputChange('lowThreshold', e.target.value)}
                            className="form-input"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="mediumThreshold">
                            Medium Threshold (€€)
                            <span className="label-hint">Regular price range</span>
                        </label>
                        <input
                            type="number"
                            id="mediumThreshold"
                            step="0.01"
                            min="0.01"
                            value={formData.mediumThreshold}
                            onChange={(e) => handleInputChange('mediumThreshold', e.target.value)}
                            className="form-input"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="highThreshold">
                            High Threshold (€€€)
                            <span className="label-hint">Expensive price range</span>
                        </label>
                        <input
                            type="number"
                            id="highThreshold"
                            step="0.01"
                            min="0.01"
                            value={formData.highThreshold}
                            onChange={(e) => handleInputChange('highThreshold', e.target.value)}
                            className="form-input"
                            required
                        />
                    </div>

                    <div className="form-actions">
                        <button
                            type="button"
                            onClick={handleReset}
                            className="btn-secondary"
                            disabled={updateMutation.isPending}
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            className="btn-primary"
                            disabled={updateMutation.isPending}
                        >
                            {updateMutation.isPending ? 'Saving...' : 'Save Changes'}
                        </button>
                    </div>
                </form>
            </div>

            <div className="info-box">
                <h4>How it works</h4>
                <ul>
                    <li>Restaurant price ranges are calculated based on their average menu price</li>
                    <li>Changes take effect immediately and all restaurants are recalculated</li>
                    <li>All changes are tracked for historical analysis</li>
                </ul>
            </div>

            <div style={{ marginTop: '3rem', padding: '2rem', background: 'white', borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.1)' }}>
                <RestaurantPriceRangeList />
            </div>
        </div>
    );
}

export default AdminPriceRangeCriteriaPage;