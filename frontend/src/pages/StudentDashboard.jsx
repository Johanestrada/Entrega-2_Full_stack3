import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getAcademicDataByRun, getAcademicDataByRunPublic } from '../services/academicApi';

export default function StudentDashboard() {
  const { run } = useParams();
  const navigate = useNavigate();
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let mounted = true;
    (async () => {
      setLoading(true);
      setError(null);
      try {
        const stored = localStorage.getItem('user');
        const hasToken = !!stored;
        const res = hasToken ? await getAcademicDataByRun(run) : await getAcademicDataByRunPublic(run);
        if (mounted) setData(res);
      } catch (err) {
        if (mounted) setError(err.message || 'No se pudo cargar los datos del estudiante');
      } finally {
        if (mounted) setLoading(false);
      }
    })();
    return () => { mounted = false; };
  }, [run]);

  if (loading) return <div className="container py-5">Cargando estudiante...</div>;
  if (error) return <div className="container py-5 text-danger">{error}</div>;
  if (!data || !data.estudiante) return <div className="container py-5">No se encontró el estudiante.</div>;

  const { estudiante, asistencias = [], evaluaciones = [] } = data;

  return (
    <div className="container py-5">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
        <div>
          <h1>{estudiante.nombre}</h1>
          <p className="text-muted">RUN: {estudiante.run} • Curso: {estudiante.curso}</p>
        </div>
        <div>
          <button className="btn btn-secondary" onClick={() => navigate(-1)}>Volver</button>
        </div>
      </div>

      <div className="row g-4">
        <div className="col-md-6">
          <div className="card p-3">
            <h5>Asistencias</h5>
            {asistencias.length === 0 ? (
              <p className="text-muted">No hay registros de asistencia.</p>
            ) : (
              <ul>
                {asistencias.map((a, i) => (
                  <li key={i}>{a.fecha} — {a.presente ? 'Presente' : 'Ausente'}</li>
                ))}
              </ul>
            )}
          </div>
        </div>

        <div className="col-md-6">
          <div className="card p-3">
            <h5>Evaluaciones</h5>
            {evaluaciones.length === 0 ? (
              <p className="text-muted">No hay evaluaciones registradas.</p>
            ) : (
              <table className="table">
                <thead>
                  <tr>
                    <th>Materia</th>
                    <th>Nota</th>
                    <th>Fecha</th>
                  </tr>
                </thead>
                <tbody>
                  {evaluaciones.map((ev) => (
                    <tr key={ev.id || ev.materia}>
                      <td>{ev.materia}</td>
                      <td>{ev.nota}</td>
                      <td>{ev.fecha || ev.createdAt}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
