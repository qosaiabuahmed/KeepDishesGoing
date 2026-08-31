import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import keycloak from './keycloak'

console.log('Starting application...');

const root = createRoot(document.getElementById('root')!);

keycloak.init({
    onLoad: 'check-sso',
    silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
    checkLoginIframe: false,
    pkceMethod: 'S256'
}).then((authenticated) => {
    console.log('Keycloak initialized. Authenticated:', authenticated);

    root.render(
        <StrictMode>
            <App />
        </StrictMode>
    );
}).catch((error) => {
    console.error('Keycloak initialization failed:', error);

    root.render(
        <div style={{ padding: '2rem', textAlign: 'center', fontFamily: 'sans-serif' }}>
            <h1 style={{ color: '#e74c3c' }}>Keycloak Error</h1>
            <p>Failed to initialize Keycloak authentication.</p>
            <p>Make sure Keycloak is running on <strong>{import.meta.env.VITE_KEYCLOAK_URL}</strong></p>
            <pre style={{
                textAlign: 'left',
                background: '#f5f5f5',
                padding: '1rem',
                marginTop: '1rem',
                overflow: 'auto',
                borderRadius: '5px'
            }}>
        {JSON.stringify(error, null, 2)}
      </pre>
            <button
                onClick={() => window.location.reload()}
                style={{
                    marginTop: '1rem',
                    padding: '0.75rem 1.5rem',
                    background: '#3498db',
                    color: 'white',
                    border: 'none',
                    borderRadius: '5px',
                    cursor: 'pointer',
                    fontSize: '1rem'
                }}
            >
                Retry
            </button>
        </div>
    );
});