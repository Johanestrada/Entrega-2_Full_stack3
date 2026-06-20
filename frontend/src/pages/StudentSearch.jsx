import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAcademicDataByRunPublic } from '../services/academicApi';

export default function StudentSearch() {
  const [run, setRun] = useState('');
  const [status, setStatus] = useState('idle');
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!run) return;
    setStatus('loading');
    setError(null);
    try {
      const res = await getAcademicDataByRunPublic(run);
      if (res && res.estudiante) {
        navigate(`/estudiante/${encodeURIComponent(run)}`);
        return;
      }
      setError('No se encontró el estudiante con ese RUN.');
    } catch (err) {
      setError(err.message || 'Error buscando estudiante');
    } finally {
      setStatus('idle');
    }
  };

  return (
    <section className="container py-5">
      <div className="card p-4">
        <h3>Buscar estudiante por RUN</h3>
        <p className="text-muted">Ingresa el RUN del estudiante para ver su dashboard.</p>
        <form onSubmit={handleSubmit} className="mt-3">
          <input
            className="form-control form-control-lg mb-3"
            type="text"
            value={run}
            onChange={(e) => setRun(e.target.value)}
            placeholder="Ej: 12.345.678-9"
            aria-label="RUN"
          />
          <div className="d-flex gap-2">
            <button type="submit" className="btn btn-primary">Buscar</button>
            <button type="button" className="btn btn-secondary" onClick={() => { setRun(''); setError(null); }}>Limpiar</button>
          </div>
          {status === 'loading' && <p className="mt-3">Buscando...</p>}
          {error && <p className="text-danger mt-3">{error}</p>}
        </form>
      </div>
    </section>
  );
}
