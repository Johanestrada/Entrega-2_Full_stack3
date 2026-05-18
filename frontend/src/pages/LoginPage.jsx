import { useNavigate, Link } from 'react-router-dom';
import LoginForm from '../components/LoginForm';

export default function LoginPage({ onLogin }) {
  const navigate = useNavigate();

  const handleLogin = (userData) => {
    onLogin(userData);
    navigate('/dashboard');
  };

  return (
    <section className="auth-page">
      <div className="auth-panel">
        <h1>Iniciar sesión</h1>
        <p>Ingresa tus credenciales para acceder al portal académico.</p>
        <LoginForm onLogin={handleLogin} />
        <p className="auth-help">
          ¿Aún no tienes cuenta? <Link to="/register">Regístrate aquí</Link>.
        </p>
      </div>
    </section>
  );
}
