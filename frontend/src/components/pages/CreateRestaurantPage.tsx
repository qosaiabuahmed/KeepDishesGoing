import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQueryClient } from '@tanstack/react-query';
import { createRestaurant } from '../../services/api';
import type { CreateRestaurantRequest } from '../../types/api.types';
import { AxiosError } from 'axios';

interface DaySchedule {
    day: string;
    openTime: string;
    closeTime: string;
    isClosed: boolean;
}

const DAYS = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];

const CreateRestaurant = () => {
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string>('');

    const [openingHours, setOpeningHours] = useState<DaySchedule[]>(
        DAYS.map(day => ({
            day,
            openTime: '11:00',
            closeTime: '22:00',
            isClosed: false
        }))
    );

    const [formData, setFormData] = useState({
        name: '',
        cuisine: 'ITALIAN',
        address: {
            street: '',
            number: '',
            postalCode: '',
            city: '',
            country: 'Belgium'
        },
        contactEmail: '',
        imageUrls: [''],
        defaultPrepTimeMinutes: 30
    });

    const formatOpeningHours = (): string => {
        return openingHours
            .filter(day => !day.isClosed)
            .map(day => `${day.day}:${day.openTime}-${day.closeTime}`)
            .join(',');
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            setLoading(true);
            setError('');
            const formattedHours = formatOpeningHours();
            console.log('Formatted opening hours:', formattedHours);

            const requestData: CreateRestaurantRequest = {
                ...formData,
                openingHours: formatOpeningHours()
            };

            console.log('Request data:', requestData);

            await createRestaurant(requestData);

            queryClient.invalidateQueries({ queryKey: ['owner-restaurants'] });

            navigate('/owner/restaurants');
        } catch (err) {
            const error = err as AxiosError<{ message: string }>;
            setError(error.response?.data?.message || 'Failed to create restaurant');
        } finally {
            setLoading(false);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;

        if (name.startsWith('address.')) {
            const addressField = name.split('.')[1];
            setFormData(prev => ({
                ...prev,
                address: { ...prev.address, [addressField]: value }
            }));
        } else if (name === 'defaultPrepTimeMinutes') {
            setFormData(prev => ({ ...prev, [name]: parseInt(value) || 0 }));
        } else {
            setFormData(prev => ({ ...prev, [name]: value }));
        }
    };

    const handleImageUrlChange = (index: number, value: string) => {
        const newUrls = [...formData.imageUrls];
        newUrls[index] = value;
        setFormData(prev => ({ ...prev, imageUrls: newUrls }));
    };

    const addImageUrl = () => {
        setFormData(prev => ({ ...prev, imageUrls: [...prev.imageUrls, ''] }));
    };

    const removeImageUrl = (index: number) => {
        if (formData.imageUrls.length > 1) {
            const newUrls = formData.imageUrls.filter((_, i) => i !== index);
            setFormData(prev => ({ ...prev, imageUrls: newUrls }));
        }
    };

    const updateDaySchedule = (index: number, field: keyof DaySchedule, value: string | boolean) => {
        const newHours = [...openingHours];
        newHours[index] = { ...newHours[index], [field]: value };
        setOpeningHours(newHours);
    };

    const applyToAllDays = () => {
        const template = openingHours[0];
        const newSchedule = DAYS.map(day => ({
            day,
            openTime: template.openTime,
            closeTime: template.closeTime,
            isClosed: template.isClosed
        }));
        setOpeningHours(newSchedule);
    };

    return (
        <div className="create-restaurant">
            <h1>Create New Restaurant</h1>

            {error && <div className="error">{error}</div>}

            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Restaurant Name *</label>
                    <input
                        type="text"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Cuisine Type *</label>
                    <select name="cuisine" value={formData.cuisine} onChange={handleChange} required>
                        <option value="ITALIAN">Italian</option>
                        <option value="CHINESE">Chinese</option>
                        <option value="JAPANESE">Japanese</option>
                        <option value="INDIAN">Indian</option>
                        <option value="MEXICAN">Mexican</option>
                        <option value="FRENCH">French</option>
                        <option value="AMERICAN">American</option>
                        <option value="BELGIAN">Belgian</option>
                    </select>
                </div>

                <div className="form-group">
                    <label>Contact Email *</label>
                    <input
                        type="email"
                        name="contactEmail"
                        value={formData.contactEmail}
                        onChange={handleChange}
                        required
                    />
                </div>

                <fieldset>
                    <legend>Address</legend>
                    <div className="form-row">
                        <div className="form-group">
                            <label>Street *</label>
                            <input
                                type="text"
                                name="address.street"
                                value={formData.address.street}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>Number *</label>
                            <input
                                type="text"
                                name="address.number"
                                value={formData.address.number}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>
                    <div className="form-row">
                        <div className="form-group">
                            <label>Postal Code *</label>
                            <input
                                type="text"
                                name="address.postalCode"
                                value={formData.address.postalCode}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>City *</label>
                            <input
                                type="text"
                                name="address.city"
                                value={formData.address.city}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>
                    <div className="form-group">
                        <label>Country *</label>
                        <input
                            type="text"
                            name="address.country"
                            value={formData.address.country}
                            onChange={handleChange}
                            required
                        />
                    </div>
                </fieldset>

                <div className="form-group">
                    <div className="image-urls-header">
                        <label>Restaurant Images</label>
                        <button type="button" onClick={addImageUrl} className="btn-primary btn-sm">
                            + Add Image
                        </button>
                    </div>
                    {formData.imageUrls.map((url, index) => (
                        <div key={index} className="image-url-row">
                            <input
                                type="url"
                                value={url}
                                onChange={(e) => handleImageUrlChange(index, e.target.value)}
                                placeholder="https://example.com/image.jpg"
                                className="image-url-input"
                            />
                            {formData.imageUrls.length > 1 && (
                                <button
                                    type="button"
                                    onClick={() => removeImageUrl(index)}
                                    className="btn-danger btn-sm"
                                >
                                    Remove
                                </button>
                            )}
                        </div>
                    ))}
                </div>

                <div className="form-group">
                    <label>Default Prep Time (minutes) *</label>
                    <input
                        type="number"
                        name="defaultPrepTimeMinutes"
                        value={formData.defaultPrepTimeMinutes}
                        onChange={handleChange}
                        min="1"
                        required
                    />
                </div>

                <fieldset className="opening-hours-section">
                    <div className="opening-hours-header">
                        <legend>Opening Hours *</legend>
                        <button
                            type="button"
                            onClick={applyToAllDays}
                            className="btn-secondary btn-sm"
                        >
                            Apply Monday to All Days
                        </button>
                    </div>

                    <div className="opening-hours-grid">
                        {openingHours.map((daySchedule, index) => (
                            <div key={daySchedule.day} className="day-schedule-row">
                                <label className="day-label">
                                    {daySchedule.day.substring(0, 3)}
                                </label>

                                <label className="checkbox-wrapper">
                                    <input
                                        type="checkbox"
                                        checked={daySchedule.isClosed}
                                        onChange={(e) => updateDaySchedule(index, 'isClosed', e.target.checked)}
                                    />
                                    <span>Closed</span>
                                </label>

                                {!daySchedule.isClosed && (
                                    <>
                                        <input
                                            type="time"
                                            value={daySchedule.openTime}
                                            onChange={(e) => updateDaySchedule(index, 'openTime', e.target.value)}
                                            className="time-input"
                                            required
                                        />
                                        <span className="time-separator">to</span>
                                        <input
                                            type="time"
                                            value={daySchedule.closeTime}
                                            onChange={(e) => updateDaySchedule(index, 'closeTime', e.target.value)}
                                            className="time-input"
                                            required
                                        />
                                    </>
                                )}
                            </div>
                        ))}
                    </div>
                </fieldset>

                <div className="form-actions">
                    <button type="button" onClick={() => navigate('/owner/restaurants')} className="btn-secondary">
                        Cancel
                    </button>
                    <button type="submit" disabled={loading} className="btn-primary">
                        {loading ? 'Creating...' : 'Create Restaurant'}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default CreateRestaurant;