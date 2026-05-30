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

    if (username && password) {
      const fakeToken = 'fake-jwt-token';
      onLogin({ username, token: fakeToken });
      navigate('/dashboard');
    } else {
      setError('Por favor, ingresa tu usuario y contraseña.');
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
                  <span className="eyebrow-pill">Acceso seguro</span>
                  <h1 className="display-6 fw-bold text-white mt-3">
                    Un acceso simple, limpio y centrado en el trabajo académico.
                  </h1>
                  <p className="text-white-50 mt-3 mb-0">
                    La pantalla usa Bootstrap y un panel visual con profundidad para que el login no se vea plano.
                  </p>
                </div>
              </div>

              <div className="auth-panel card border-0 shadow-lg">
                <div className="card-body p-4 p-md-5">
                  <h2 className="h3 fw-bold mb-2">Iniciar sesión</h2>
                  <p className="text-body-secondary mb-4">Ingresa tus credenciales para entrar al portal.</p>

                  <form onSubmit={handleSubmit} className="vstack gap-3">
                    <div>
                      <label className="form-label">Usuario</label>
                      <input
                        className="form-control form-control-lg"
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="usuario@colegio.cl"
                        required
                      />
                    </div>
                    <div>
                      <label className="form-label">Contraseña</label>
                      <input
                        className="form-control form-control-lg"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="••••••••"
                        required
                      />
                    </div>
                    {error && <div className="alert alert-danger mb-0">{error}</div>}
                    <button type="submit" className="btn btn-primary btn-lg w-100">
                      Entrar
                    </button>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
