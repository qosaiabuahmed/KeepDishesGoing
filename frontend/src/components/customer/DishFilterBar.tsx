import { DishSortBy } from '../../types/api.types';

interface DishFilterBarProps {
    selectedType: string;
    selectedFoodTags: string[];
    selectedSort: string;
    onTypeChange: (type: string) => void;
    onFoodTagToggle: (tag: string) => void;
    onSortChange: (sort: string) => void;
    onClearFilters: () => void;
}

const dishTypeLabels: Record<string, string> = {
    STARTER: 'Starters',
    MAIN: 'Main Courses',
    DESSERT: 'Desserts'
};

const foodTagLabels: Record<string, string> = {
    LACTOSE: 'Lactose',
    GLUTEN: 'Gluten',
    VEGAN: 'Vegan',
    VEGETARIAN: 'Vegetarian',
    NUTS: 'Nuts',
    SEAFOOD: 'Seafood',
    SPICY: 'Spicy',
    ORGANIC: 'Organic',
    HALAL: 'Halal',
    KOSHER: 'Kosher'
};

const sortOptions = [
    { value: '', label: 'Default Order' },
    { value: DishSortBy.PRICE, label: 'Price: Low to High' },
    { value: DishSortBy.PRICE_DESC, label: 'Price: High to Low' },
    { value: DishSortBy.NAME, label: 'Name: A to Z' }
];

export const DishFilterBar = ({
    selectedType,
    selectedFoodTags,
    selectedSort,
    onTypeChange,
    onFoodTagToggle,
    onSortChange,
    onClearFilters
}: DishFilterBarProps) => {
    const hasActiveFilters =
        selectedType !== '' ||
        selectedFoodTags.length > 0 ||
        selectedSort !== '';

    return (
        <div className="dish-filter-bar">
            <div className="filter-row">
                <div className="filter-group">
                    <label htmlFor="dish-type-filter" className="filter-label">
                        Type
                    </label>
                    <select
                        id="dish-type-filter"
                        className="filter-select"
                        value={selectedType}
                        onChange={(e) => onTypeChange(e.target.value)}
                    >
                        <option value="">All Types</option>
                        {Object.entries(dishTypeLabels).map(([value, label]) => (
                            <option key={value} value={value}>
                                {label}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="filter-group">
                    <label htmlFor="dish-sort" className="filter-label">
                        Sort By
                    </label>
                    <select
                        id="dish-sort"
                        className="filter-select"
                        value={selectedSort}
                        onChange={(e) => onSortChange(e.target.value)}
                    >
                        {sortOptions.map(({ value, label }) => (
                            <option key={value} value={value}>
                                {label}
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
                        Clear All Filters
                    </button>
                )}
            </div>

            <div className="filter-row">
                <div className="filter-group filter-group-wide">
                    <label className="filter-label">Dietary Preferences</label>
                    <div className="food-tags-filter">
                        {Object.entries(foodTagLabels).map(([value, label]) => (
                            <label key={value} className="food-tag-checkbox">
                                <input
                                    type="checkbox"
                                    checked={selectedFoodTags.includes(value)}
                                    onChange={() => onFoodTagToggle(value)}
                                />
                                <span className="food-tag-label">{label}</span>
                            </label>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
};