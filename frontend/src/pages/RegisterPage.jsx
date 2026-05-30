import { useState } from 'react';
import { Link } from 'react-router-dom';
import { registerUser } from '../services/authApi';

export default function RegisterPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  // Eliminado campo email
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setMessage('');
    setLoading(true);

    if (!username || !password) {
      setError('Completa todos los campos para registrarte.');
      setLoading(false);
      return;
    }

    try {
      await registerUser(username, password);
      setMessage('Registro exitoso. Ahora puedes iniciar sesión.');
      setUsername('');
      setPassword('');
    } catch (err) {
      setError(err.message || 'Error al registrar la cuenta.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="auth-shell">
      <div className="container py-5">
        <div className="row justify-content-center">
          <div className="col-lg-10 col-xl-9">
            <div className="auth-layout">
              <div className="auth-visual d-none d-lg-flex">
                <div>
                  <span className="eyebrow-pill">Registro</span>
                  <h1 className="display-6 fw-bold text-white mt-3">
                    Crea una cuenta con una presentación visual coherente.
                  </h1>
                  <p className="text-white-50 mt-3 mb-0">
                    Mantuvimos la simplicidad del flujo y mejoramos la jerarquía con Bootstrap.
                  </p>
                </div>
              </div>

              <div className="auth-panel card border-0 shadow-lg">
                <div className="card-body p-4 p-md-5">
                  <h2 className="h3 fw-bold mb-2">Registro</h2>
                  <p className="text-body-secondary mb-4">Crea tu cuenta para usar el portal académico.</p>

                  <form className="vstack gap-3" onSubmit={handleSubmit}>
                    <div>
                      <label className="form-label">Usuario</label>
                      <input className="form-control form-control-lg" value={username} onChange={(e) => setUsername(e.target.value)} />
                    </div>
                    <div>
                      <label className="form-label">Contraseña</label>
                      <input className="form-control form-control-lg" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                    </div>

                    {error && <div className="alert alert-danger mb-0">{error}</div>}
                    {message && <div className="alert alert-success mb-0">{message}</div>}

                    <button type="submit" className="btn btn-primary btn-lg w-100" disabled={loading}>
                      {loading ? 'Creando cuenta...' : 'Crear cuenta'}
                    </button>
                  </form>

                  <p className="text-body-secondary mt-4 mb-0">
                    ¿Ya tienes cuenta? <Link to="/login">Inicia sesión</Link>.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
