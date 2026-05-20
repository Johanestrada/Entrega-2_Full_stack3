import { useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route, Link, Navigate } from 'react-router-dom';
import './App.css';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';import HomePage from './pages/HomePage';

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
        
        <header className="site-header">
          <h1>Portal Académico</h1>
        </header>

        <main className="main-content">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/login" element={<LoginPage onLogin={handleLogin} />} />
            <Route
              path="/dashboard"
              element={user ? <DashboardPage handleLogout={handleLogout} /> : <Navigate to="/login" replace />}
            />
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
