import { useState } from 'react';

interface RejectOrderModalProps {
    isOpen: boolean;
    onClose: () => void;
    onConfirm: (reason: string) => void;
    isLoading: boolean;
}

const RejectOrderModal = ({ isOpen, onClose, onConfirm, isLoading }: RejectOrderModalProps) => {
    const [reason, setReason] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        if (reason.trim().length < 10) {
            setError('Rejection reason must be at least 10 characters');
            return;
        }

        onConfirm(reason.trim());
    };

    const handleClose = () => {
        if (!isLoading) {
            setReason('');
            setError('');
            onClose();
        }
    };

    if (!isOpen) return null;

    return (
        <div className="modal-overlay" onClick={handleClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Reject Order</h2>
                    <button
                        className="modal-close"
                        onClick={handleClose}
                        disabled={isLoading}
                    >
                        &times;
                    </button>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className="modal-body">
                        <p className="modal-text">
                            Please provide a reason for rejecting this order. This will be sent to the customer.
                        </p>
                        <div className="form-group">
                            <label htmlFor="reason">Rejection Reason *</label>
                            <textarea
                                id="reason"
                                rows={4}
                                value={reason}
                                onChange={(e) => {
                                    setReason(e.target.value);
                                    setError('');
                                }}
                                placeholder="e.g., Kitchen is too busy right now, ingredient unavailable, etc."
                                disabled={isLoading}
                                required
                            />
                            {error && <span className="error-text">{error}</span>}
                            <small className="form-hint">Minimum 10 characters</small>
                        </div>
                    </div>
                    <div className="modal-footer">
                        <button
                            type="button"
                            className="btn-secondary"
                            onClick={handleClose}
                            disabled={isLoading}
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            className="btn-danger"
                            disabled={isLoading || reason.trim().length < 10}
                        >
                            {isLoading ? 'Rejecting...' : 'Reject Order'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default RejectOrderModal;