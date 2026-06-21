import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { getStudentsByCourse, postNewStudent, getAcademicDataByRun, deleteStudent } from '../services/academicApi';
import SearchForm from './SearchForm';

import './ModernManager.css';
export default function EstudianteManager() {
  const navigate = useNavigate();
  const location = useLocation();
  const isStandalone = location.pathname === '/alumnos';
  const [estudiantes, setEstudiantes] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [status, setStatus] = useState('idle');
  const [error, setError] = useState(null);

  // removed recent students UI; teachers can search by RUN directly

  useEffect(() => {
    // no-op: removed recent students usage
  }, []);

  // Estados para la búsqueda
  const [query, setQuery] = useState('');
  const [mode, setMode] = useState('curso'); // Empezar por curso por defecto

  // Estados para el nuevo estudiante
  const [nombre, setNombre] = useState('');
  const [run, setRun] = useState('');
  const [curso, setCurso] = useState('');
  const [submitting, setSubmitting] = useState(false);

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
        setEstudiantes(data);
      } else { // mode === 'run'
        const resumen = await getAcademicDataByRun(query);
        // Si encontramos el estudiante, redirigimos al dashboard del estudiante
        if (resumen && resumen.estudiante) {
          navigate(`/estudiante/${encodeURIComponent(query)}`);
          return;
        }
        // Si la API devolvió null (404) o no contiene estudiante, mostrar mensaje
        setEstudiantes([]);
        setError('No se encontró el estudiante con ese RUN.');
        setStatus('error');
        return;
      }
      setStatus('success');
    } catch (err) {
      setError(err.message || 'No se pudo encontrar la información.');
      setStatus('error');
    }
  };

  // Búsqueda simplificada cuando se accede a la página /alumnos (solo búsqueda por RUN)
  const handleSearchRunOnly = async (e) => {
    e.preventDefault();
    if (!query) return;
    setStatus('loading');
    setError(null);
    try {
      const resumen = await getAcademicDataByRun(query);
      if (resumen && resumen.estudiante) {
        navigate(`/estudiante/${encodeURIComponent(query)}`);
        return;
      }
      setError('No se encontró el estudiante con ese RUN.');
    } catch (err) {
      setError(err.message || 'Error buscando estudiante');
    } finally {
      setStatus('idle');
    }
  };

  const handleAddStudent = async (e) => {
    e.preventDefault();
    if (submitting) return;
    setSubmitting(true);
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
      // (no recentStudents persistence)
    } catch (err) {
      console.error("Error al agregar estudiante:", err);
      setError(err.message || 'No se pudo agregar el estudiante. Inténtalo de nuevo.');
    } finally {
      setSubmitting(false);
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

        {/* Últimos estudiantes creados (solo visible en la vista Alumnos) */}
        {/* removed 'Últimos estudiantes creados' section */}

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

        {isStandalone ? (
          <section className="manager-section">
            <h2>Buscar Estudiante por RUN</h2>
            <form onSubmit={handleSearchRunOnly} className="search-form">
              <input
                className="form-control form-control-lg mb-3"
                type="text"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Ingresa RUN del estudiante"
              />
              <button type="submit" className="btn btn-primary btn-lg">Buscar</button>
              {status === 'loading' && <p className="mt-2">Buscando...</p>}
              {status === 'error' && error && <p className="text-danger mt-2">{error}</p>}
            </form>
          </section>
        ) : (
          <section className="manager-section">
            <h2>Buscar Estudiantes</h2>
              <SearchForm
                query={query}
                onSetQuery={setQuery}
                mode={mode}
                onSetMode={setMode}
                onBuscarAcademico={handleSearch}
                allowRun={true}
              />
          </section>
        )}

        <section>
          <div style={{ marginBottom: '1.5rem' }}>
            <h2>Resultados de la búsqueda</h2>
          </div>

          {status === 'loading' && <p>Buscando...</p>}
          {status === 'error' && <p style={{ color: '#b91c1c' }}>{error}</p>}

            <div className="manager-section">
              {status === 'success' && (
                <div className="table-responsive">
                  <table className="table table-striped">
                    <thead>
                      <tr>
                        <th>Nombre</th>
                        <th>RUN</th>
                        <th>Curso</th>
                        <th>Promedio</th>
                        <th>Acciones</th>
                      </tr>
                    </thead>
                    <tbody>
                      {estudiantes.map((student) => (
                        <tr key={student.id}>
                          <td>{student.nombre}</td>
                          <td>{student.run}</td>
                          <td>{student.curso}</td>
                          <td>{student.promedio ?? student.promedioFinal ?? '-'}</td>
                          <td>
                            <button
                              className="btn btn-sm btn-outline-primary me-2"
                              onClick={() => navigate(`/estudiante/${encodeURIComponent(student.run)}`)}
                            >
                              Ver
                            </button>
                            <button
                              className="btn btn-sm btn-danger"
                              onClick={() => handleDelete(student.id)}
                            >
                              Eliminar
                            </button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
              {status === 'success' && estudiantes.length === 0 && (
                <p>No se encontraron estudiantes con ese criterio.</p>
              )}
            </div>
          {status === 'success' && estudiantes.length === 0 && (
            <p>No se encontraron estudiantes con ese criterio.</p>
          )}
        </section>
      </div>
      {/* removed recent-students confirmation modal */}
    </div>
  );
}