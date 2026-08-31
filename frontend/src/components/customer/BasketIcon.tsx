import { useNavigate } from 'react-router-dom';
import { useBasket } from '../../hooks/useBasket';

const BasketIcon = () => {
    const navigate = useNavigate();
    const { basket } = useBasket();

    const itemCount = basket?.totalItemCount || 0;

    return (
        <button
            onClick={() => navigate('/customer/basket')}
            className="basket-icon-button"
            style={{
                position: 'relative',
                padding: '8px 16px',
                background: 'transparent',
                border: '1px solid #ccc',
                borderRadius: '4px',
                cursor: 'pointer',
                fontSize: '16px'
            }}
        >
            🛒 Basket
            {itemCount > 0 && (
                <span
                    className="basket-badge"
                    style={{
                        position: 'absolute',
                        top: '-8px',
                        right: '-8px',
                        background: '#e74c3c',
                        color: 'white',
                        borderRadius: '50%',
                        width: '24px',
                        height: '24px',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontSize: '12px',
                        fontWeight: 'bold'
                    }}
                >
                    {itemCount}
                </span>
            )}
        </button>
    );
};

export default BasketIcon;