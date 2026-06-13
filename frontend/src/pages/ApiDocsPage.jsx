import { useEffect, useState } from 'react';

const SERVICES = [
  { name: 'BFF', url: (import.meta.env.VITE_BFF_URL || 'http://localhost:8084') + '/v3/api-docs' },
  { name: 'Estudiante', url: 'http://localhost:8081/v3/api-docs' },
  { name: 'Asistencia', url: 'http://localhost:8082/v3/api-docs' },
  { name: 'Evaluacion', url: 'http://localhost:8083/v3/api-docs' },
];

function ServiceList({ spec }) {
  if (!spec || !spec.paths) return <div>No spec available.</div>;
  return (
    <div>
      {Object.entries(spec.paths).map(([path, methods]) => (
        <div key={path} style={{ borderBottom: '1px solid #eee', padding: '6px 0' }}>
          <strong>{path}</strong>
          <div style={{ marginLeft: '8px' }}>
            {Object.entries(methods).map(([method, info]) => (
              <div key={method}>
                <em style={{ color: '#0ea5e9' }}>{method.toUpperCase()}</em> - {info.summary || info.operationId || ''}
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
}

export default function ApiDocsPage() {
  const [specs, setSpecs] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    Promise.all(
      SERVICES.map(s => fetch(s.url).then(r => r.ok ? r.json() : Promise.reject(s.name + ' ' + r.status)))
    ).then(results => {
      const map = {};
      SERVICES.forEach((s, i) => map[s.name] = results[i]);
      setSpecs(map);
      setLoading(false);
    }).catch(err => {
      setError(String(err));
      setLoading(false);
    });
  }, []);

  return (
    <div className="container py-4">
      <h2>Documentación de APIs (OpenAPI)</h2>
      <p>Lista de endpoints descubiertos en los servicios.</p>
      {loading && <p>Cargando especificaciones...</p>}
      {error && <div style={{ color: 'red' }}>Error: {error}</div>}

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
        {Object.keys(specs).length === 0 && !loading && !error && <div>No hay specs cargadas.</div>}
        {Object.entries(specs).map(([service, spec]) => (
          <section key={service} style={{ border: '1px solid #e5e7eb', padding: '12px', borderRadius: '6px' }}>
            <h4>{service}</h4>
            <p style={{ fontSize: '0.9rem', color: '#6b7280' }}>{spec.info?.title || 'Spec'} - {spec.info?.version || ''}</p>
            <ServiceList spec={spec} />
          </section>
        ))}
      </div>
    </div>
  );
}
