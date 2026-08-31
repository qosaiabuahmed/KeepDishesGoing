import { useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { setAuthToken } from './services/api';
import keycloak from './keycloak';
import LandingPage from './components/pages/LandingPage';
import RestaurantListPage from './components/pages/RestaurantListPage';
import RestaurantDetailPage from './components/pages/RestaurantDetailPage';
import BasketPage from './components/pages/BasketPage';
import CheckoutPage from './components/pages/CheckoutPage';
import OrderTrackingPage from './components/pages/OrderTrackingPage';
import OwnerRestaurantListPage from './components/pages/OwnerRestaurantListPage';
import CreateRestaurantPage from './components/pages/CreateRestaurantPage';
import DishListPage from './components/pages/DishListPage';
import DishFormPage from './components/pages/DishFormPage';
import PendingOrdersPage from './components/pages/PendingOrdersPage';
import CurrentOrdersPage from './components/pages/CurrentOrdersPage';
import AdminPriceRangeCriteriaPage from './components/pages/AdminPriceRangeCriteriaPage';
import BasketIcon from './components/customer/BasketIcon';
import AdminProtectedRoute from './components/common/AdminProtectedRoute';
import { BasketProvider } from './contexts/BasketContext';
import './App.css';

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            staleTime: 1000 * 60 * 5, // 5 minutes
            retry: 1,
        },
    },
});

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [username, setUsername] = useState<string>('');

    useEffect(() => {
        console.log('App mounted, checking authentication...');
        const authenticated = keycloak.authenticated || false;
        setIsAuthenticated(authenticated);
        setIsLoading(false);

        if (authenticated && keycloak.tokenParsed) {
            setUsername(keycloak.tokenParsed.preferred_username || 'User');
        }

        if (keycloak.token) {
            console.log('Setting auth token');
            setAuthToken(keycloak.token);
        }

        const interval = setInterval(() => {
            keycloak.updateToken(70).then((refreshed) => {
                if (refreshed) {
                    console.log('Token refreshed');
                    setAuthToken(keycloak.token);
                }
            }).catch(() => {
                console.error('Failed to refresh token');
            });
        }, 60000);

        return () => clearInterval(interval);
    }, []);

    const handleLogin = () => {
        console.log('Login clicked');
        keycloak.login();
    };

    const handleLogout = () => {
        console.log('Logout clicked');
        keycloak.logout();
    };

    if (isLoading) {
        return <div className="loading">Loading...</div>;
    }

    return (
        <QueryClientProvider client={queryClient}>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<LandingPage />} />

                <Route path="/customer/*" element={
                    <BasketProvider>
                        <div className="app">
                            <nav className="navbar">
                                <div className="nav-container">
                                    <h2>Keep Dishes Going</h2>
                                    <div className="nav-user" style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
                                        <BasketIcon />
                                        <button onClick={() => window.location.href = '/'} className="btn-secondary btn-sm">
                                            Back to Home
                                        </button>
                                    </div>
                                </div>
                            </nav>
                            <main className="main-content">
                                <Routes>
                                    <Route path="restaurants" element={<RestaurantListPage />} />
                                    <Route path="restaurants/:id" element={<RestaurantDetailPage />} />
                                    <Route path="basket" element={<BasketPage />} />
                                    <Route path="checkout" element={<CheckoutPage />} />
                                    <Route path="orders/:orderId" element={<OrderTrackingPage />} />
                                </Routes>
                            </main>
                        </div>
                    </BasketProvider>
                } />

                <Route path="/owner/*" element={
                    !isAuthenticated ? (
                        <div className="login-page">
                            <div className="login-container">
                                <h1>Keep Dishes Going</h1>
                                <p>Restaurant Management System</p>
                                <button onClick={handleLogin} className="btn-primary btn-large">
                                    Login with Keycloak
                                </button>
                            </div>
                        </div>
                    ) : (
                        <div className="app">
                            <nav className="navbar">
                                <div className="nav-container">
                                    <h2>Keep Dishes Going</h2>
                                    <div className="nav-user">
                                        <span>Welcome, {username}!</span>
                                        <button onClick={handleLogout} className="btn-secondary btn-sm">
                                            Logout
                                        </button>
                                    </div>
                                </div>
                            </nav>
                            <main className="main-content">
                                <Routes>
                                    <Route path="restaurants" element={<OwnerRestaurantListPage />} />
                                    <Route path="restaurants/create" element={<CreateRestaurantPage />} />
                                    <Route path="restaurants/:restaurantId/dishes" element={<DishListPage />} />
                                    <Route path="restaurants/:restaurantId/dishes/create" element={<DishFormPage />} />
                                    <Route path="restaurants/:restaurantId/dishes/:dishId/edit" element={<DishFormPage isEdit />} />
                                    <Route path="restaurants/:restaurantId/orders" element={<PendingOrdersPage />} />
                                    <Route path="restaurants/:restaurantId/current-orders" element={<CurrentOrdersPage />} />
                                </Routes>
                            </main>
                        </div>
                    )
                } />

                <Route path="/admin/*" element={
                    <AdminProtectedRoute onLogin={handleLogin}>
                        <div className="app">
                            <nav className="navbar">
                                <div className="nav-container">
                                    <h2>Keep Dishes Going - Admin</h2>
                                    <div className="nav-user">
                                        <span>Welcome, {username}!</span>
                                        <button onClick={handleLogout} className="btn-secondary btn-sm">
                                            Logout
                                        </button>
                                    </div>
                                </div>
                            </nav>
                            <main className="main-content">
                                <Routes>
                                    <Route path="price-ranges" element={<AdminPriceRangeCriteriaPage />} />
                                </Routes>
                            </main>
                        </div>
                    </AdminProtectedRoute>
                } />

                <Route path="/restaurants" element={<Navigate to="/owner/restaurants" replace />} />
            </Routes>
        </BrowserRouter>
        </QueryClientProvider>
    );
}

export default App;