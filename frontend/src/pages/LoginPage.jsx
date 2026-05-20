import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function LoginPage({ onLogin }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    // Simulación de una llamada a la API de login
    if (username && password) {
      // En una app real, aquí llamarías a tu BFF para autenticar
      // y recibirías un token.
      const fakeToken = 'fake-jwt-token';
      onLogin({ username, token: fakeToken });
      navigate('/dashboard');
    } else {
      setError('Por favor, ingresa tu usuario y contraseña.');
    }
  };

  return (
    <div className="login-container">
      <h2>Iniciar Sesión</h2>
      <form onSubmit={handleSubmit} className="login-form">
        <label>
          Usuario:
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </label>
        <label>
          Contraseña:
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </label>
        <button type="submit">Ingresar</button>
      </form>
      {error && <p className="error">{error}</p>}
    </div>
  );
}
