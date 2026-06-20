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

  // Recent students (persisted from creación)
  const [recentStudents, setRecentStudents] = useState([]);
  const [confirmRunFor, setConfirmRunFor] = useState(null);
  const [inputRun, setInputRun] = useState('');
  const [confirmError, setConfirmError] = useState('');
  const normalizeRun = (s) => (s || '').toString().replace(/[^0-9kK]/g, '').toLowerCase();

  useEffect(() => {
    try {
      const raw = localStorage.getItem('recentStudents');
      setRecentStudents(raw ? JSON.parse(raw) : []);
    } catch (e) {
      setRecentStudents([]);
    }
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
      // Guardar RUN en localStorage para acceso posterior aunque se cierre sesión
      try {
        const key = 'recentStudents';
        const targetRun = (nuevoEstudiante && (nuevoEstudiante.run || nuevoEstudiante.rut)) || run;
        if (targetRun) {
          const raw = localStorage.getItem(key);
          const list = raw ? JSON.parse(raw) : [];
          const normalized = targetRun.toString();
          const updated = [normalized, ...list.filter((r) => r !== normalized)].slice(0, 5);
          localStorage.setItem(key, JSON.stringify(updated));
        }
      } catch (e) {
        // ignore storage errors
      }
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
        {recentStudents.length > 0 && (
          <section className="manager-section">
            <h3>Últimos estudiantes creados</h3>
            <div className="d-flex gap-2 flex-wrap mb-3">
              {recentStudents.map((r) => (
                <button
                  type="button"
                  key={r}
                  className="btn btn-outline-primary"
                  onClick={() => { setConfirmRunFor(r); setInputRun(''); setConfirmError(''); }}
                >
                  {r}
                </button>
              ))}
            </div>
          </section>
        )}

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
                onSetMode={isStandalone ? setMode : () => {}}
                onBuscarAcademico={handleSearch}
                allowRun={isStandalone}
              />
          </section>
        )}

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
      {/* Modal para confirmar RUT antes de mostrar dashboard del estudiante */}
      {confirmRunFor && (
        <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 2000 }}>
          <div className="card p-4" style={{ width: 420 }}>
            <h5 className="mb-2">Confirmar identidad</h5>
            <p className="text-muted">Ingresa tu RUT para ver el dashboard de <strong>{confirmRunFor}</strong></p>
            <input className="form-control mb-2" value={inputRun} onChange={(e) => setInputRun(e.target.value)} placeholder="Ingresa tu RUT" />
            {confirmError && <div className="text-danger mb-2">{confirmError}</div>}
            <div className="d-flex justify-content-end gap-2">
              <button className="btn btn-outline-secondary" onClick={() => { setConfirmRunFor(null); setInputRun(''); setConfirmError(''); }}>Cancelar</button>
              <button className="btn btn-primary" onClick={() => {
                const entered = normalizeRun(inputRun);
                const target = normalizeRun(confirmRunFor);
                if (!entered) { setConfirmError('Ingresa un RUT válido'); return; }
                if (entered === target) {
                  setConfirmError('');
                  setConfirmRunFor(null);
                  navigate(`/estudiante/${encodeURIComponent(confirmRunFor)}`);
                } else {
                  setConfirmError('RUT no coincide con el estudiante seleccionado');
                }
              }}>Ver dashboard</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}