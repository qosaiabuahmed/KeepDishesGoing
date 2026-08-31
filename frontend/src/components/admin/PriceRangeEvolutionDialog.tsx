import { useQuery } from '@tanstack/react-query';
import { getPriceRangeEvolution } from '../../services/api';
import PriceRangeEvolutionChart from './PriceRangeEvolutionChart';

interface PriceRangeEvolutionDialogProps {
    restaurantId: string;
    restaurantName: string;
    onClose: () => void;
}

function PriceRangeEvolutionDialog({ restaurantId, restaurantName, onClose }: PriceRangeEvolutionDialogProps) {
    const { data, isLoading, error } = useQuery({
        queryKey: ['priceRangeEvolution', restaurantId],
        queryFn: async () => {
            const response = await getPriceRangeEvolution(restaurantId);
            return response.data;
        }
    });

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()} style={{ maxWidth: '1000px', width: '90%' }}>
                <div className="modal-header">
                    <h2>Price Range Evolution</h2>
                    <button className="modal-close" onClick={onClose}>×</button>
                </div>

                <div className="modal-body">
                    {isLoading && (
                        <div style={{ textAlign: 'center', padding: '40px' }}>
                            <p>Loading price range evolution data...</p>
                        </div>
                    )}

                    {error && (
                        <div className="alert alert-error">
                            <p>Failed to load price range evolution data.</p>
                            <p style={{ fontSize: '14px', marginTop: '8px' }}>
                                {error instanceof Error ? error.message : 'Unknown error occurred'}
                            </p>
                        </div>
                    )}

                    {data && !isLoading && !error && (
                        <PriceRangeEvolutionChart snapshots={data} restaurantName={restaurantName} />
                    )}
                </div>

                <div className="modal-footer">
                    <button className="btn-secondary" onClick={onClose}>
                        Close
                    </button>
                </div>
            </div>
        </div>
    );
}

export default PriceRangeEvolutionDialog;