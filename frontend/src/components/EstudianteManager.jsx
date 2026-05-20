import { useState } from 'react';
import { getStudentsByCourse, postNewStudent, getAcademicDataByRun, deleteStudent } from '../services/academicApi';
import SearchForm from './SearchForm';

import './ModernManager.css';
export default function EstudianteManager() {
  const [estudiantes, setEstudiantes] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [status, setStatus] = useState('idle');
  const [error, setError] = useState(null);

  // Estados para la búsqueda
  const [query, setQuery] = useState('');
  const [mode, setMode] = useState('curso'); // Empezar por curso por defecto

  // Estados para el nuevo estudiante
  const [nombre, setNombre] = useState('');
  const [run, setRun] = useState('');
  const [curso, setCurso] = useState('');

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!query) return;

    setStatus('loading');
    setError(null);
    setEstudiantes([]);

    try {
      let data;
      if (mode === 'curso') {
        data = await getStudentsByCourse(query);
      } else { // mode === 'run'
        const resumen = await getAcademicDataByRun(query);
        // La búsqueda por RUN devuelve un objeto, la tabla espera un array.
        // Si encontramos un estudiante, lo ponemos en un array.
        data = resumen && resumen.estudiante ? [resumen.estudiante] : [];
      }
      setEstudiantes(data);
      setStatus('success');
    } catch (err) {
      setError(err.message || 'No se pudo encontrar la información.');
      setStatus('error');
    }
  };

  const handleAddStudent = async (e) => {
    e.preventDefault();
    try {
      const nuevoEstudiante = await postNewStudent({ run, nombre, curso });
      // Si la lista de estudiantes actual es del mismo curso, lo agregamos
      if (query.toLowerCase() === curso.toLowerCase()) {
        setEstudiantes([...estudiantes, nuevoEstudiante]);
      }
      // Limpiar formulario y ocultarlo
      setNombre('');
      setRun('');
      setCurso('');
      setShowForm(false);
    } catch (err) {
      // Aquí podrías mostrar un error al usuario
      console.error("Error al agregar estudiante:", err);
      // Opcional: mostrar un error en la UI
      // setError('No se pudo agregar el estudiante. Inténtalo de nuevo.');
    }
  };

  const handleDelete = async (id) => {
    // Pedir confirmación al usuario
    if (window.confirm('¿Estás seguro de que quieres eliminar este estudiante?')) {
      try {
        await deleteStudent(id);
        // Actualizar la lista de estudiantes en la UI para reflejar el borrado
        setEstudiantes(estudiantes.filter(e => e.id !== id));
      } catch (err) {
        setError(err.message || 'No se pudo eliminar el estudiante.');
      }
    }
  };

  return (
    <div className="manager-wrapper">
      <div className="manager-container">
        <header className="manager-header">
          <h1>Gestión de Estudiantes</h1>
          <p>Agrega, busca y elimina estudiantes del sistema.</p>
        </header>

        {error && (
          <div className="manager-section" style={{ borderColor: '#fca5a5', backgroundColor: '#fef2f2', color: '#b91c1c' }}>
            <p style={{ fontWeight: '600' }}>Error</p>
            <p>{error}</p>
          </div>
        )}

        <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: '2rem' }}>
          <button 
            onClick={() => setShowForm(!showForm)} 
            style={{ backgroundColor: '#7c3aed', color: 'white', padding: '0.75rem 1.5rem', borderRadius: '0.5rem', fontWeight: '600' }}
          >
            {showForm ? 'Cancelar' : 'Agregar Estudiante'}
          </button>
        </div>

        {showForm && (
          <section className="form-add">
            <h3>Nuevo Estudiante</h3>
            <form onSubmit={handleAddStudent}>
              <input value={run} onChange={(e) => setRun(e.target.value)} placeholder="RUN" required />
              <input value={nombre} onChange={(e) => setNombre(e.target.value)} placeholder="Nombre Completo" required />
              <input value={curso} onChange={(e) => setCurso(e.target.value)} placeholder="Curso" required />
              <button type="submit" style={{ backgroundColor: '#10b981', color: 'white' }}>Guardar Estudiante</button>
            </form>
          </section>
        )}

        <section className="manager-section">
          <h2>Buscar Estudiantes</h2>
          <SearchForm
            query={query}
            onSetQuery={setQuery}
            mode={mode}
            onSetMode={setMode}
            onBuscarAcademico={handleSearch}
          />
        </section>

        <section>
          <div style={{ marginBottom: '1.5rem' }}>
            <h2>Resultados de la búsqueda</h2>
          </div>

          {status === 'loading' && <p>Buscando...</p>}
          {status === 'error' && <p style={{ color: '#b91c1c' }}>{error}</p>}

          <div className="student-grid">
            {status === 'success' && estudiantes.map((student) => (
              <article key={student.id} className="student-card">
                <div className="student-card-header" style={{ paddingBottom: '1rem' }}>
                  <div>
                    <h4 style={{ color: '#5b21b6' }}>{student.nombre}</h4>
                    <p>RUN: {student.run}</p>
                    <p>Curso: {student.curso}</p>
                  </div>
                </div>

                <div className="card-actions">
                  <button
                    onClick={() => handleDelete(student.id)}
                    style={{ backgroundColor: '#dc2626', color: 'white' }}
                  >
                    Eliminar
                  </button>
                </div>
              </article>
            ))}
          </div>
          {status === 'success' && estudiantes.length === 0 && (
            <p>No se encontraron estudiantes con ese criterio.</p>
          )}
        </section>
      </div>
    </div>
  );
}