import { useState } from 'react';
import { Link } from 'react-router-dom';
// import { registerUser } from '../services/authApi'; // Asumiríamos que existe esta función

export default function RegisterPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setMessage('');
    setLoading(true);

    if (!username || !email || !password) {
      setError('Completa todos los campos para registrarte.');
      setLoading(false);
      return;
    }

    try {
      // const response = await registerUser({ username, email, password });
      // Simulación de llamada a API
      await new Promise(resolve => setTimeout(resolve, 1000));
      setMessage('Registro exitoso. Ahora puedes iniciar sesión.');
      setUsername('');
      setEmail('');
      setPassword('');
    } catch (err) {
      setError(err.message || 'Error al registrar la cuenta.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="auth-page">
      <div className="auth-panel">
        <h1>Registro</h1>
        <p>Crea tu cuenta para usar el portal académico.</p>
        <form className="login-form" onSubmit={handleSubmit}>
          <label>
            Usuario
            <input value={username} onChange={(e) => setUsername(e.target.value)} />
          </label>
          <label>
            Correo electrónico
            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
          </label>
          <label>
            Contraseña
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
          </label>
          {error && <p className="error">{error}</p>}
          {message && <p className="info">{message}</p>}
          <button type="submit" disabled={loading}>
            {loading ? 'Creando cuenta...' : 'Crear cuenta'}
          </button>
        </form>
        <p className="auth-help">
          Ya tienes cuenta? <Link to="/login">Inicia sesión</Link>.
        </p>
      </div>
    </section>
  );
}
