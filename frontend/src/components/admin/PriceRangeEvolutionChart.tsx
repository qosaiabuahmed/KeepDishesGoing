import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import type { PriceRangeSnapshotDto } from '../../types/api.types';

interface PriceRangeEvolutionChartProps {
    snapshots: PriceRangeSnapshotDto[];
    restaurantName: string;
}

interface ChartDataPoint {
    date: string;
    displayDate: string;
    priceRange: string;
    priceRangeValue: number;
    averageMenuPrice: number;
    lowThreshold: number;
    mediumThreshold: number;
    highThreshold: number;
}

function PriceRangeEvolutionChart({ snapshots, restaurantName }: PriceRangeEvolutionChartProps) {
    const priceRangeToValue: Record<string, number> = {
        CHEAP: 1,
        REGULAR: 2,
        EXPENSIVE: 3,
        PREMIUM: 4
    };

    const valueToPriceRange: Record<number, string> = {
        1: 'CHEAP',
        2: 'REGULAR',
        3: 'EXPENSIVE',
        4: 'PREMIUM'
    };

    const chartData: ChartDataPoint[] = snapshots.map((snapshot) => {
        const date = new Date(snapshot.date);
        const displayDate = date.toLocaleDateString('en-GB', {
            day: '2-digit',
            month: 'short',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });

        return {
            date: snapshot.date,
            displayDate,
            priceRange: snapshot.priceRange,
            priceRangeValue: priceRangeToValue[snapshot.priceRange],
            averageMenuPrice: snapshot.averageMenuPrice,
            lowThreshold: snapshot.lowThreshold,
            mediumThreshold: snapshot.mediumThreshold,
            highThreshold: snapshot.highThreshold
        };
    });

    const priceRangeColors: Record<string, string> = {
        CHEAP: '#22c55e',
        REGULAR: '#3b82f6',
        EXPENSIVE: '#f59e0b',
        PREMIUM: '#ef4444'
    };

    const priceRangeLabels: Record<string, string> = {
        CHEAP: 'Cheap (€)',
        REGULAR: 'Regular (€€)',
        EXPENSIVE: 'Expensive (€€€)',
        PREMIUM: 'Premium (€€€€)'
    };

    const CustomDot = (props: { cx?: number; cy?: number; index?: number; payload?: ChartDataPoint }) => {
        const { cx, cy, payload } = props;
        if (cx === undefined || cy === undefined || !payload) return null;

        return (
            <circle
                cx={cx}
                cy={cy}
                r={8}
                fill={priceRangeColors[payload.priceRange]}
                stroke="white"
                strokeWidth={3}
            />
        );
    };

    const CustomTooltip = ({ active, payload }: { active?: boolean; payload?: Array<{ payload: ChartDataPoint }> }) => {
        if (active && payload && payload.length > 0) {
            const data = payload[0].payload;
            return (
                <div className="custom-tooltip" style={{
                    backgroundColor: 'white',
                    border: '2px solid ' + priceRangeColors[data.priceRange],
                    padding: '12px',
                    borderRadius: '8px',
                    boxShadow: '0 4px 12px rgba(0,0,0,0.15)'
                }}>
                    <p style={{ fontWeight: 'bold', marginBottom: '8px', fontSize: '14px' }}>{data.displayDate}</p>
                    <p style={{
                        color: priceRangeColors[data.priceRange],
                        fontWeight: 'bold',
                        fontSize: '16px',
                        marginBottom: '8px'
                    }}>
                        {priceRangeLabels[data.priceRange]}
                    </p>
                    <hr style={{ margin: '8px 0', borderColor: '#e5e7eb' }} />
                    <p style={{ margin: '4px 0', fontSize: '12px', color: '#666' }}>Restaurant Avg Price: €{data.averageMenuPrice.toFixed(2)}</p>
                    <p style={{ margin: '4px 0', fontSize: '11px', color: '#888' }}>Thresholds at this time:</p>
                    <p style={{ margin: '2px 0', fontSize: '11px', color: '#888' }}>€{data.lowThreshold.toFixed(2)} | €{data.mediumThreshold.toFixed(2)} | €{data.highThreshold.toFixed(2)}</p>
                </div>
            );
        }
        return null;
    };

    if (snapshots.length === 0) {
        return (
            <div style={{ textAlign: 'center', padding: '40px', color: '#666' }}>
                <p>No price range history available for this restaurant.</p>
                <p style={{ fontSize: '14px', marginTop: '8px' }}>
                    History is recorded when the admin updates price range criteria thresholds.
                </p>
            </div>
        );
    }

    return (
        <div>
            <h3 style={{ marginBottom: '20px', textAlign: 'center' }}>
                Price Range Evolution for {restaurantName}
            </h3>

            <div style={{ marginBottom: '20px', padding: '15px', backgroundColor: '#f0f9ff', borderRadius: '8px', border: '1px solid #bae6fd' }}>
                <p style={{ margin: 0, fontSize: '14px', color: '#0369a1' }}>
                    <strong>Restaurant's Average Menu Price:</strong> €{snapshots[0]?.averageMenuPrice.toFixed(2)} (constant)
                </p>
                <p style={{ margin: '4px 0 0 0', fontSize: '12px', color: '#0c4a6e' }}>
                    This chart shows how the restaurant's classification changed as YOU (admin) adjusted the global price range thresholds.
                </p>
            </div>

            <ResponsiveContainer width="100%" height={400}>
                <LineChart data={chartData} margin={{ top: 20, right: 30, left: 20, bottom: 80 }}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis
                        dataKey="displayDate"
                        angle={-45}
                        textAnchor="end"
                        height={100}
                        tick={{ fontSize: 11 }}
                        interval={0}
                    />
                    <YAxis
                        domain={[0, 5]}
                        ticks={[1, 2, 3, 4]}
                        tickFormatter={(value) => priceRangeLabels[valueToPriceRange[value]] || ''}
                        tick={{ fontSize: 12 }}
                        width={100}
                    />
                    <Tooltip content={<CustomTooltip />} />
                    <Line
                        type="linear"
                        dataKey="priceRangeValue"
                        stroke="#8b5cf6"
                        strokeWidth={3}
                        dot={<CustomDot />}
                        activeDot={{ r: 10 }}
                    />
                </LineChart>
            </ResponsiveContainer>

            <div style={{ marginTop: '20px', padding: '15px', backgroundColor: '#f8fafc', borderRadius: '8px' }}>
                <h4 style={{ marginBottom: '10px', fontSize: '14px' }}>Price Range Categories:</h4>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '15px', fontSize: '13px' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                        <div style={{ width: '20px', height: '20px', backgroundColor: priceRangeColors.CHEAP, borderRadius: '50%' }}></div>
                        <span>Cheap (€)</span>
                    </div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                        <div style={{ width: '20px', height: '20px', backgroundColor: priceRangeColors.REGULAR, borderRadius: '50%' }}></div>
                        <span>Regular (€€)</span>
                    </div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                        <div style={{ width: '20px', height: '20px', backgroundColor: priceRangeColors.EXPENSIVE, borderRadius: '50%' }}></div>
                        <span>Expensive (€€€)</span>
                    </div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                        <div style={{ width: '20px', height: '20px', backgroundColor: priceRangeColors.PREMIUM, borderRadius: '50%' }}></div>
                        <span>Premium (€€€€)</span>
                    </div>
                </div>
                <p style={{ marginTop: '12px', fontSize: '12px', color: '#64748b' }}>
                    The line shows the price range category over time. Each colored dot represents the restaurant's classification at that moment when you changed the global thresholds.
                </p>
            </div>
        </div>
    );
}

export default PriceRangeEvolutionChart;