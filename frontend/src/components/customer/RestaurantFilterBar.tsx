interface RestaurantFilterBarProps {
    selectedCuisine: string;
    selectedPriceRange: string;
    onCuisineChange: (cuisine: string) => void;
    onPriceRangeChange: (priceRange: string) => void;
    onClearFilters: () => void;
}

const cuisineLabels: Record<string, string> = {
    ITALIAN: 'Italian',
    FRENCH: 'French',
    JAPANESE: 'Japanese',
    CHINESE: 'Chinese',
    INDIAN: 'Indian',
    MEXICAN: 'Mexican',
    THAI: 'Thai',
    GREEK: 'Greek',
    AMERICAN: 'American',
    MEDITERRANEAN: 'Mediterranean',
    MIDDLE_EASTERN: 'Middle Eastern',
    VIETNAMESE: 'Vietnamese',
    KOREAN: 'Korean',
    SPANISH: 'Spanish',
    TURKISH: 'Turkish'
};

const priceRangeLabels: Record<string, { symbol: string; description: string }> = {
    CHEAP: { symbol: '€', description: 'Cheap (< €10)' },
    REGULAR: { symbol: '€€', description: 'Regular (€11-€30)' },
    EXPENSIVE: { symbol: '€€€', description: 'Expensive (€31-€60)' },
    PREMIUM: { symbol: '€€€€', description: 'Premium (> €60)' }
};

export const RestaurantFilterBar = ({
    selectedCuisine,
    selectedPriceRange,
    onCuisineChange,
    onPriceRangeChange,
    onClearFilters
}: RestaurantFilterBarProps) => {
    const hasActiveFilters = selectedCuisine !== '' || selectedPriceRange !== '';

    return (
        <div className="filter-bar">
            <div className="filter-group">
                <label htmlFor="cuisine-filter" className="filter-label">
                    Cuisine
                </label>
                <select
                    id="cuisine-filter"
                    className="filter-select"
                    value={selectedCuisine}
                    onChange={(e) => onCuisineChange(e.target.value)}
                >
                    <option value="">All Cuisines</option>
                    {Object.entries(cuisineLabels).map(([value, label]) => (
                        <option key={value} value={value}>
                            {label}
                        </option>
                    ))}
                </select>
            </div>

            <div className="filter-group">
                <label htmlFor="price-filter" className="filter-label">
                    Price Range
                </label>
                <select
                    id="price-filter"
                    className="filter-select"
                    value={selectedPriceRange}
                    onChange={(e) => onPriceRangeChange(e.target.value)}
                >
                    <option value="">All Prices</option>
                    {Object.entries(priceRangeLabels).map(([value, { symbol, description }]) => (
                        <option key={value} value={value}>
                            {symbol} - {description}
                        </option>
                    ))}
                </select>
            </div>

            {hasActiveFilters && (
                <button
                    type="button"
                    className="btn-secondary btn-sm filter-clear"
                    onClick={onClearFilters}
                >
                    Clear Filters
                </button>
            )}
        </div>
    );
};