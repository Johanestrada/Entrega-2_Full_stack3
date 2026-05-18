import { useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route, Link, Navigate } from 'react-router-dom';
import './App.css';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';

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
      <div className="app-container">
        
        {/* HEADER MODIFICADO: Se eliminó el div intermedio innecesario */}
        <header className="site-header">
          <h1>🎓 Portal Académico</h1>
          
          <nav>
            <ul>
              <li><Link to="/">Inicio</Link></li>
              <li><Link to="/login">Login</Link></li>
              {user && <li><Link to="/dashboard">Panel</Link></li>}
            </ul>
          </nav>

          <div className="auth-actions">
            {user ? (
              <>
                <span className="user">{user.username}</span>
                <button onClick={handleLogout}>Salir</button>
              </>
            ) : (
              <span className="user">No has iniciado sesión</span>
            )}
          </div>
        </header>

        <main className="main-content">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/login" element={<LoginPage onLogin={handleLogin} />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route
              path="/dashboard"
              element={user ? <DashboardPage /> : <Navigate to="/login" replace />}
            />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </main>

        <footer className="site-footer">
          <p>&copy; 2026 Portal Académico. Todos los derechos reservados.</p>
        </footer>
      </div>
    </BrowserRouter>
  );
}

export default App;
