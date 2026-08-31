import type { ReactNode } from 'react';
import keycloak from '../../keycloak';

interface AdminProtectedRouteProps {
    children: ReactNode;
    onLogin: () => void;
}

function AdminProtectedRoute({ children, onLogin }: AdminProtectedRouteProps) {
    const isAuthenticated = keycloak.authenticated || false;
    const hasAdminRole = keycloak.hasRealmRole('admin');

    if (!isAuthenticated) {
        return (
            <div className="login-page">
                <div className="login-container">
                    <h1>Keep Dishes Going</h1>
                    <p>Admin Panel</p>
                    <p className="text-muted">Administrator access required</p>
                    <button onClick={onLogin} className="btn-primary btn-large">
                        Login with Keycloak
                    </button>
                </div>
            </div>
        );
    }

    if (!hasAdminRole) {
        return (
            <div className="login-page">
                <div className="login-container">
                    <h1>Access Denied</h1>
                    <p>You do not have administrator privileges</p>
                    <button onClick={() => window.location.href = '/'} className="btn-secondary">
                        Back to Home
                    </button>
                </div>
            </div>
        );
    }

    return <>{children}</>;
}

export default AdminProtectedRoute;