import { useNavigate } from 'react-router-dom';

const LandingPage = () => {
    const navigate = useNavigate();

    return (
        <div className="landing-page">
            <div className="landing-container">
                <h1>Keep Dishes Going</h1>
                <p className="tagline">Fresh food delivered to your door</p>

                <div className="role-selection">
                    <button
                        className="btn-primary btn-large"
                        onClick={() => navigate('/customer/restaurants')}
                    >
                        🍽️ I'm a Customer
                    </button>

                    <button
                        className="btn-secondary btn-large"
                        onClick={() => navigate('/owner/restaurants')}
                    >
                        👨‍🍳 I'm an Owner
                    </button>

                    <button
                        className="btn-secondary btn-large"
                        onClick={() => navigate('/admin/price-ranges')}
                    >
                        🔐 Admin Panel
                    </button>
                </div>

                <div className="features">
                    <div className="feature">
                        <span className="feature-icon">⚡</span>
                        <h3>Fast Delivery</h3>
                        <p>Get your food delivered quickly</p>
                    </div>
                    <div className="feature">
                        <span className="feature-icon">🎯</span>
                        <h3>Fresh Ingredients</h3>
                        <p>Quality food from local restaurants</p>
                    </div>
                    <div className="feature">
                        <span className="feature-icon">📱</span>
                        <h3>Easy Ordering</h3>
                        <p>Simple and intuitive interface</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default LandingPage;