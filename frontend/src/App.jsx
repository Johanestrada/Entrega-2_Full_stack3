import { useState } from 'react';
import './App.css';

function App() {
  const [estudianteId, setEstudianteId] = useState('');
  const [data, setData] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const buscarAcademico = async (event) => {
    event.preventDefault();
    setError('');
    setData(null);

    if (!estudianteId) {
      setError('Ingresa un ID de estudiante.');
      return;
    }

    setLoading(true);

    try {
      const response = await fetch(`http://localhost:8084/academico/${estudianteId}`);
      if (!response.ok) {
        throw new Error(`Error al buscar: ${response.status}`);
      }
      const json = await response.json();
      setData(json);
    } catch (err) {
      setError('No se pudo obtener la información. Revisa que el BFF y los microservicios estén corriendo.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="app-container">
      <header className="header">
        <h1>🎓 Portal Académico</h1>
        <p>Consulta datos integrados desde el BFF</p>
      </header>

      <form className="search-form" onSubmit={buscarAcademico}>
        <input
          id="estudianteId"
          type="number"
          value={estudianteId}
          onChange={(event) => setEstudianteId(event.target.value)}
          placeholder="Ingresa ID de estudiante"
        />
        <button type="submit">🔍 Buscar</button>
      </form>

      {loading && <p className="info">Cargando...</p>}
      {error && <p className="error">{error}</p>}

      {data && (
        <section className="cards-container">
          <div className="card">
            <h2>👤 Estudiante</h2>
            <p><strong>Nombre:</strong> {data.estudiante?.nombre}</p>
            <p><strong>Curso:</strong> {data.estudiante?.curso}</p>
          </div>

          <div className="card">
            <h2>📅 Asistencias</h2>
            <ul>
              {data.asistencias?.map((a, i) => (
                <li key={i}>{a.fecha} - {a.presente ? 'Presente' : 'Ausente'}</li>
              ))}
            </ul>
          </div>

          <div className="card">
            <h2>📝 Evaluaciones</h2>
            <ul>
              {data.evaluaciones?.map((e, i) => (
                <li key={i}>{e.materia}: {e.nota}</li>
              ))}
            </ul>
          </div>
        </section>
      )}
    </main>
  );
}

export default App;
