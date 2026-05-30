import { useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route, Link, Navigate } from 'react-router-dom';
import './App.css';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import HomePage from './pages/HomePage';

function App() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  useEffect(() => {
    if (user) {
      localStorage.setItem('user', JSON.stringify(user));
    } else {
      localStorage.removeItem('user');
    }
  }, [user]);

  const handleLogin = ({ username, token }) => {
    setUser({ username, token });
  };

  const handleLogout = () => {
    setUser(null);
  };

  return (
    <BrowserRouter>
      <div className="app-shell">
        <header className="navbar navbar-dark glass-nav sticky-top">
          <div className="container-fluid px-4 py-2">
            <Link className="navbar-brand fw-semibold brand-mark" to="/">
              Portal Académico
            </Link>
            <div className="navbar-nav ms-auto flex-row align-items-center gap-2">
              <Link className="nav-link" to="/">
                Inicio
              </Link>
              <Link className="nav-link" to="/login">
                Login
              </Link>
              <Link className="nav-link" to="/dashboard">
                Dashboard
              </Link>
              <Link className="nav-link" to="/register">
                Crear cuenta
              </Link>
            </div>
          </div>
        </header>

        <main className="main-content">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/login" element={<LoginPage onLogin={handleLogin} />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route
              path="/dashboard"
              element={user ? <DashboardPage handleLogout={handleLogout} /> : <Navigate to="/login" replace />}
            />
          </Routes>
        </main>

        <footer className="site-footer">
          <div className="container-fluid px-4 py-3 d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-2">
            <span>&copy; 2026 Portal Académico.</span>
            <span className="footer-note">Bootstrap + estilo limpio inspirado en paneles 3D.</span>
          </div>
        </footer>
      </div>
    </BrowserRouter>
  );
}

export default App;
