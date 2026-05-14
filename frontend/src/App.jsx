import { useState } from 'react';

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
      <h1>Portal Académico</h1>
      <p>Busca los datos combinados desde el BFF.</p>

      <form className="search-form" onSubmit={buscarAcademico}>
        <label htmlFor="estudianteId">ID de estudiante</label>
        <input
          id="estudianteId"
          type="number"
          value={estudianteId}
          onChange={(event) => setEstudianteId(event.target.value)}
          placeholder="Ej. 1"
        />
        <button type="submit">Buscar</button>
      </form>

      {loading && <p className="info">Cargando...</p>}
      {error && <p className="error">{error}</p>}

      {data && (
        <section className="result-card">
          <h2>Resultados</h2>
          <div className="result-block">
            <h3>Estudiante</h3>
            <pre>{JSON.stringify(data.estudiante, null, 2)}</pre>
          </div>
          <div className="result-block">
            <h3>Asistencias</h3>
            <pre>{JSON.stringify(data.asistencias, null, 2)}</pre>
          </div>
          <div className="result-block">
            <h3>Evaluaciones</h3>
            <pre>{JSON.stringify(data.evaluaciones, null, 2)}</pre>
          </div>
        </section>
      )}
    </main>
  );
}

export default App;
