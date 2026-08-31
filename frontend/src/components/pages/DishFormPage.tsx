import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { createDish, updateDish, getDishById, publishDish } from '../../services/api';
import { type CreateDishRequest, DishType, FoodTag, PublishStatus } from '../../types/api.types';
import { AxiosError } from 'axios';

interface DishFormProps {
    isEdit?: boolean;
}

const DishForm = ({ isEdit = false }: DishFormProps) => {
    const { restaurantId, dishId } = useParams<{ restaurantId: string; dishId: string }>();
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const [loading, setLoading] = useState(false);
    const [publishing, setPublishing] = useState(false);
    const [error, setError] = useState<string>('');

    const [formData, setFormData] = useState<CreateDishRequest>({
        name: '',
        type: DishType.MAIN,
        foodTags: [],
        description: '',
        price: 0,
        imageUrl: ''
    });

    const { data: originalDish } = useQuery({
        queryKey: ['dish', restaurantId, dishId],
        queryFn: async () => {
            if (!restaurantId || !dishId) return null;
            const response = await getDishById(restaurantId, dishId);
            return response.data;
        },
        enabled: isEdit && !!restaurantId && !!dishId,
    });

    useEffect(() => {
        if (originalDish) {
            setFormData({
                name: originalDish.draftName ?? originalDish.name,
                type: originalDish.draftType ?? originalDish.type,
                foodTags: originalDish.draftFoodTags ?? originalDish.foodTags,
                description: originalDish.draftDescription ?? originalDish.description,
                price: originalDish.draftPrice ?? originalDish.price,
                imageUrl: originalDish.draftImageUrl ?? originalDish.imageUrl
            });
        }
    }, [originalDish]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            setLoading(true);
            setError('');

            if (isEdit && dishId) {
                await updateDish(restaurantId!, dishId, formData);
                alert('Draft saved! Changes are not yet visible to customers. Click "Publish Changes" to make them live.');
            } else {
                await createDish(restaurantId!, formData);
                alert('Dish created as draft. Click "Publish" in the dish list to make it visible to customers.');
            }

            // Invalidate the dish list query to refresh the data
            queryClient.invalidateQueries({ queryKey: ['restaurant-dishes', restaurantId] });

            navigate(`/owner/restaurants/${restaurantId}/dishes`);
        } catch (error) {
            const axiosError = error as AxiosError<{ message: string }>;
            setError(axiosError.response?.data?.message || `Failed to ${isEdit ? 'update' : 'create'} dish`);
        } finally {
            setLoading(false);
        }
    };

    const handlePublish = async () => {
        if (!dishId || !restaurantId) return;

        const confirmPublish = window.confirm(
            'Publish changes? All draft changes will become visible to customers immediately.'
        );

        if (!confirmPublish) return;

        try {
            setPublishing(true);
            setError('');

            // First save the current form data
            await updateDish(restaurantId, dishId, formData);

            // Then publish
            await publishDish(restaurantId, dishId);

            // Invalidate the dish list query to refresh the data
            queryClient.invalidateQueries({ queryKey: ['restaurant-dishes', restaurantId] });

            alert('Changes published successfully! Customers can now see the updates.');
            navigate(`/owner/restaurants/${restaurantId}/dishes`);
        } catch (error) {
            const axiosError = error as AxiosError<{ message: string }>;
            setError(axiosError.response?.data?.message || 'Failed to publish dish');
        } finally {
            setPublishing(false);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;

        if (name === 'price') {
            setFormData(prev => ({ ...prev, [name]: parseFloat(value) || 0 }));
        } else {
            setFormData(prev => ({ ...prev, [name]: value }));
        }
    };

    const toggleFoodTag = (tag: string) => {
        setFormData(prev => ({
            ...prev,
            foodTags: prev.foodTags.includes(tag)
                ? prev.foodTags.filter(t => t !== tag)
                : [...prev.foodTags, tag]
        }));
    };

    return (
        <div className="dish-form">
            <h1>{isEdit ? 'Edit Dish' : 'Create New Dish'}</h1>

            {error && <div className="error">{error}</div>}

            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Dish Name *</label>
                    <input
                        type="text"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Type *</label>
                    <select name="type" value={formData.type} onChange={handleChange} required>
                        <option value="STARTER">Starter</option>
                        <option value="MAIN">Main Course</option>
                        <option value="DESSERT">Dessert</option>
                    </select>
                </div>

                <div className="form-group">
                    <label>Description *</label>
                    <textarea
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        rows={4}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Price (€) *</label>
                    <input
                        type="number"
                        name="price"
                        value={formData.price}
                        onChange={handleChange}
                        step="0.01"
                        min="0"
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Image URL *</label>
                    <input
                        type="url"
                        name="imageUrl"
                        value={formData.imageUrl}
                        onChange={handleChange}
                        placeholder="https://example.com/dish-image.jpg"
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Food Tags</label>
                    <div className="food-tags-grid">
                        {Object.values(FoodTag).map(tag => (
                            <label key={tag} className="checkbox-label">
                                <input
                                    type="checkbox"
                                    checked={formData.foodTags.includes(tag)}
                                    onChange={() => toggleFoodTag(tag)}
                                />
                                {tag}
                            </label>
                        ))}
                    </div>
                </div>

                <div className="form-actions">
                    <button
                        type="button"
                        onClick={() => navigate(`/owner/restaurants/${restaurantId}/dishes`)}
                        className="btn-secondary"
                    >
                        Cancel
                    </button>
                    <button type="submit" disabled={loading} className="btn-primary">
                        {loading ? 'Saving...' : (isEdit ? 'Save Draft' : 'Create Dish')}
                    </button>
                    {isEdit && originalDish && originalDish.publishStatus === PublishStatus.PUBLISHED && (
                        <button
                            type="button"
                            onClick={handlePublish}
                            disabled={publishing}
                            className="btn-success"
                        >
                            {publishing ? 'Publishing...' : 'Publish Changes'}
                        </button>
                    )}
                </div>
            </form>
        </div>
    );
};

export default DishForm;