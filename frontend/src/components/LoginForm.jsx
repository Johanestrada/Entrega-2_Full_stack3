import { useState } from 'react';
import { loginUser } from '../services/authApi';

export default function LoginForm({ onLogin }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!username || !password) {
      setError('Usuario y contraseña son obligatorios.');
      return;
    }

    setLoading(true);
    try {
      const authData = await loginUser(username, password);
      onLogin(authData);
    } catch (err) {
      setError(err.message || 'Usuario o contraseña incorrectos.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleSubmit} className="login-form">
        <label>
          Usuario
          <input value={username} onChange={(e) => setUsername(e.target.value)} />
        </label>
        <label>
          Contraseña
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        </label>
        {error && <p className="error">{error}</p>}
        <button type="submit" disabled={loading}>
          {loading ? 'Ingresando...' : 'Entrar'}
        </button>
      </form>
    </div>
  );
}
