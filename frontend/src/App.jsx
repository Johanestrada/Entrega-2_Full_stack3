import { useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route, Link, Navigate } from 'react-router-dom';
import './App.css';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import StudentDashboard from './pages/StudentDashboard';
import HomePage from './pages/HomePage';
import StudentSearch from './pages/StudentSearch';

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
              Portal Académico Bernardo O’Higgins
            </Link>
            <div className="navbar-nav ms-auto flex-row align-items-center gap-2">
              <Link className="nav-link" to="/alumnos">
                Alumnos
              </Link>
              <Link className="nav-link" to="/dashboard">
                Docente
              </Link>
            </div>
          </div>
        </header>

        <main className="main-content">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/login" element={<LoginPage onLogin={handleLogin} />} />
            <Route path="/register" element={<RegisterPage />} />
              <Route path="/estudiante/:run" element={<StudentDashboard />} />
              <Route path="/alumnos" element={<StudentSearch />} />
            <Route
              path="/dashboard"
              element={user ? <DashboardPage handleLogout={handleLogout} /> : <Navigate to="/login" replace />}
            />
              
          </Routes>
        </main>

        <footer className="site-footer bg-dark text-white py-5">
          <div className="container">
            <div className="row gy-4">
              <div className="col-md-4">
                <h5>Contacto</h5>
                <ul className="list-unstyled">
                  <li>Dirección: Calle Siempre Viva 123, Santiago</li>
                  <li>Email: <a href="AllanGajardso@bernhiggins.cl" className="text-white">contacto@bernhiggins.cl</a></li>
                  <li>Tel: <a href="tel:+56912345678" className="text-white">+56 9 1234 5678</a></li>
                </ul>
              </div>

              <div className="col-md-3">
                <h5>Nosotros</h5>
                <ul className="list-unstyled">
                  <li><a href="#" className="text-white">Quiénes somos</a></li>
                  <li><a href="#" className="text-white">Equipo</a></li>
                  <li><a href="#" className="text-white">Misión y visión</a></li>
                </ul>
              </div>

              <div className="col-md-3">
                <h5>Enlaces de interés</h5>
                <ul className="list-unstyled">
                  <li><a href="#" className="text-white">Política de privacidad</a></li>
                  <li><a href="#" className="text-white">Términos de uso</a></li>
                  <li><a href="#" className="text-white">Centro de ayuda</a></li>
                </ul>
              </div>

              <div className="col-md-2">
                <h5>Síguenos</h5>
                <div className="d-flex gap-2 mt-2">
                  <a href="#" aria-label="Facebook" className="text-white">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                      <path d="M22 12C22 6.48 17.52 2 12 2S2 6.48 2 12c0 4.84 3.44 8.84 7.94 9.76v-6.91H7.9v-2.85h2.04V9.41c0-2.02 1.2-3.13 3.03-3.13.88 0 1.8.16 1.8.16v1.98h-1.02c-1.01 0-1.32.63-1.32 1.27v1.53h2.25l-.36 2.85h-1.89v6.91C18.56 20.84 22 16.84 22 12z"/>
                    </svg>
                  </a>
                  <a href="#" aria-label="Twitter" className="text-white">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                      <path d="M22.46 6c-.77.35-1.6.58-2.46.69a4.27 4.27 0 0 0 1.88-2.37 8.48 8.48 0 0 1-2.7 1.03 4.24 4.24 0 0 0-7.22 3.86A12.02 12.02 0 0 1 3.15 4.6a4.24 4.24 0 0 0 1.31 5.66 4.2 4.2 0 0 1-1.92-.53v.05a4.25 4.25 0 0 0 3.4 4.16 4.3 4.3 0 0 1-1.91.07 4.25 4.25 0 0 0 3.96 2.95A8.51 8.51 0 0 1 2 19.54 12.02 12.02 0 0 0 8.29 21c7.55 0 11.68-6.26 11.68-11.68 0-.18-.01-.35-.02-.53A8.36 8.36 0 0 0 22.46 6z"/>
                    </svg>
                  </a>
                  <a href="https://www.instagram.com/kiddvoodoo/" aria-label="Instagram" className="text-white">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                      <path d="M7 2C4.24 2 2 4.24 2 7v10c0 2.76 2.24 5 5 5h10c2.76 0 5-2.24 5-5V7c0-2.76-2.24-5-5-5H7zm5 6.5A4.5 4.5 0 1 1 7.5 13 4.5 4.5 0 0 1 12 8.5zm5.5-.5a1 1 0 1 1-1 1 1 1 0 0 1 1-1zM12 9a3 3 0 1 0 3 3 3 3 0 0 0-3-3z"/>
                    </svg>
                  </a>
                </div>
              </div>
            </div>

            <div className="row mt-4">
              <div className="col-12 text-center text-white-50">
              </div>
            </div>
          </div>
        </footer>
      </div>
    </BrowserRouter>
  );
}

export default App;
