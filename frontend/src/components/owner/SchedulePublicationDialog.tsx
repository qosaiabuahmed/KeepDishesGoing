import { useState } from 'react';
import { schedulePublication } from '../../services/api';
import { type DishDto, type SchedulePublicationRequest } from '../../types/api.types';
import { AxiosError } from 'axios';

interface SchedulePublicationDialogProps {
    isOpen: boolean;
    onClose: () => void;
    dishesWithPendingChanges: DishDto[];
    publishedDishes: DishDto[];
    restaurantId: string;
    onScheduleSuccess?: () => void;
}

const SchedulePublicationDialog = ({
    isOpen,
    onClose,
    dishesWithPendingChanges,
    publishedDishes,
    restaurantId,
    onScheduleSuccess
}: SchedulePublicationDialogProps) => {
    const [selectedToPublish, setSelectedToPublish] = useState<string[]>([]);
    const [selectedToUnpublish, setSelectedToUnpublish] = useState<string[]>([]);
    const [scheduledDateTime, setScheduledDateTime] = useState<string>('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (selectedToPublish.length === 0 && selectedToUnpublish.length === 0) {
            alert('Please select at least one dish to publish or unpublish');
            return;
        }

        if (!scheduledDateTime) {
            alert('Please select a date and time');
            return;
        }

        const scheduledTime = new Date(scheduledDateTime);
        if (scheduledTime <= new Date()) {
            alert('Scheduled time must be in the future');
            return;
        }

        try {
            const request: SchedulePublicationRequest = {
                dishIdsToPublish: selectedToPublish,
                dishIdsToUnpublish: selectedToUnpublish,
                scheduledTime: scheduledTime.toISOString()
            };

            await schedulePublication(restaurantId, request);
            alert(`Publication scheduled successfully for ${scheduledTime.toLocaleString()}`);

            // Reset state
            setSelectedToPublish([]);
            setSelectedToUnpublish([]);
            setScheduledDateTime('');

            onScheduleSuccess?.();
            onClose();
        } catch (err) {
            const error = err as AxiosError<{ message: string }>;
            alert(error.response?.data?.message || 'Failed to schedule publication');
        }
    };

    const toggleDishSelection = (dishId: string, list: 'publish' | 'unpublish') => {
        if (list === 'publish') {
            setSelectedToPublish(prev =>
                prev.includes(dishId) ? prev.filter(id => id !== dishId) : [...prev, dishId]
            );
        } else {
            setSelectedToUnpublish(prev =>
                prev.includes(dishId) ? prev.filter(id => id !== dishId) : [...prev, dishId]
            );
        }
    };

    if (!isOpen) return null;

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content modal-large" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Schedule Publication</h2>
                    <button className="modal-close" onClick={onClose}>×</button>
                </div>

                <form onSubmit={handleSubmit}>
                    <div className="modal-body">
                        {/* Dishes to Publish */}
                        {dishesWithPendingChanges.length > 0 && (
                            <div className="form-group">
                                <label style={{ fontSize: '1.1rem', marginBottom: '1rem', display: 'block' }}>
                                    Dishes to Publish (apply pending changes):
                                </label>
                                <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
                                    {dishesWithPendingChanges.map(dish => (
                                        <label key={dish.id} className="checkbox-label" style={{ padding: '0.75rem', border: '1px solid #e0e0e0', borderRadius: '8px' }}>
                                            <input
                                                type="checkbox"
                                                checked={selectedToPublish.includes(dish.id)}
                                                onChange={() => toggleDishSelection(dish.id, 'publish')}
                                            />
                                            <span style={{ fontWeight: '500' }}>
                                                {dish.name}
                                                {dish.draftName && dish.draftName !== dish.name && (
                                                    <span style={{ color: '#ff9800', marginLeft: '0.5rem' }}>→ {dish.draftName}</span>
                                                )}
                                            </span>
                                        </label>
                                    ))}
                                </div>
                            </div>
                        )}

                        {/* Dishes to Unpublish */}
                        {publishedDishes.length > 0 && (
                            <div className="form-group">
                                <label style={{ fontSize: '1.1rem', marginBottom: '1rem', display: 'block' }}>
                                    Dishes to Unpublish (remove from menu):
                                </label>
                                <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
                                    {publishedDishes.map(dish => (
                                        <label key={dish.id} className="checkbox-label" style={{ padding: '0.75rem', border: '1px solid #e0e0e0', borderRadius: '8px' }}>
                                            <input
                                                type="checkbox"
                                                checked={selectedToUnpublish.includes(dish.id)}
                                                onChange={() => toggleDishSelection(dish.id, 'unpublish')}
                                            />
                                            <span style={{ fontWeight: '500' }}>{dish.name}</span>
                                        </label>
                                    ))}
                                </div>
                            </div>
                        )}

                        {/* Date/Time Picker */}
                        <div className="form-group">
                            <label htmlFor="scheduledDateTime" style={{ fontSize: '1.1rem' }}>Go live at:</label>
                            <input
                                type="datetime-local"
                                id="scheduledDateTime"
                                value={scheduledDateTime}
                                onChange={(e) => setScheduledDateTime(e.target.value)}
                                min={new Date().toISOString().slice(0, 16)}
                                required
                                style={{ fontSize: '1rem' }}
                            />
                            <small style={{ display: 'block', marginTop: '0.5rem', color: '#7f8c8d' }}>
                                Selected dishes will be published/unpublished at this time
                            </small>
                        </div>
                    </div>

                    <div className="modal-footer">
                        <button type="button" onClick={onClose} className="btn-secondary">
                            Cancel
                        </button>
                        <button type="submit" className="btn-success">
                            Schedule
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default SchedulePublicationDialog;