import { useEffect, useState } from 'react';
import { getStudentsByCourse, getAcademicDataByRun, postNewEvaluation } from '../services/academicApi';
import SearchForm from './SearchForm';

import './ModernManager.css';
export default function EvaluacionManager() {
  const [reactMontado, setReactMontado] = useState(false);
  const [estudiantes, setEstudiantes] = useState([]);
  const [selectedStudent, setSelectedStudent] = useState(null);
  const [evaluaciones, setEvaluaciones] = useState([]);
  const [expandedStudent, setExpandedStudent] = useState(null);
  const [status, setStatus] = useState('idle');
  const [error, setError] = useState(null);
  const [query, setQuery] = useState('');
  const [mode, setMode] = useState('curso');
  const [materia, setMateria] = useState('');
  const [nota, setNota] = useState('');
  const [editingId, setEditingId] = useState(null);
  const [editNota, setEditNota] = useState('');

  useEffect(() => {
    setReactMontado(true);
  }, []);

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!query.trim()) return;

    setStatus('loading');
    setError(null);

    try {
      const data = await getStudentsByCourse(query);
      setEstudiantes(data || []);
      setStatus('success');
      setExpandedStudent(null);
      setEvaluaciones([]);
    } catch (err) {
      setError(err.message || 'Error al buscar estudiantes.');
      setStatus('error');
    }
  };

  const handleSelectStudent = (student) => {
    setSelectedStudent(student);
    setMateria('');
    setNota('');
    setEditingId(null);
    setError(null);
  };

  const handleAddEvaluation = async (e) => {
    e.preventDefault();
    if (!selectedStudent || !materia || !nota) {
      setError('Completa todos los campos.');
      return;
    }

    try {
      await postNewEvaluation({
        estudianteId: selectedStudent.id,
        materia,
        nota: parseFloat(nota),
      });

      // Recargar evaluaciones del estudiante desde el servidor
      const data = await getAcademicDataByRun(selectedStudent.run);
      setEvaluaciones(data.evaluaciones || []);
      setExpandedStudent(selectedStudent);

      setSelectedStudent(null);
      setMateria('');
      setNota('');
      setError(null);
    } catch (err) {
      setError(err.message || 'No se pudo agregar la evaluación.');
    }
  };

  const handleEditEvaluacion = async (evaluacionId) => {
    if (!editNota || editNota < 1 || editNota > 7) {
      setError('La nota debe estar entre 1 y 7.');
      return;
    }

    try {
      const response = await fetch(`http://localhost:8084/academico/evaluaciones/${evaluacionId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: evaluacionId, nota: parseFloat(editNota) }),
      });

      if (!response.ok) throw new Error('Error al actualizar evaluación');

      setEvaluaciones(evaluaciones.map(e => 
        e.id === evaluacionId ? { ...e, nota: parseFloat(editNota) } : e
      ));
      setEditingId(null);
      setEditNota('');
      setError(null);
    } catch (err) {
      setError(err.message || 'No se pudo actualizar la evaluación.');
    }
  };

  const handleDeleteEvaluacion = async (evaluacionId) => {
    if (!window.confirm('¿Eliminar esta evaluación?')) return;

    try {
      const response = await fetch(`http://localhost:8084/academico/evaluaciones/${evaluacionId}`, {
        method: 'DELETE',
      });

      if (!response.ok) throw new Error('Error al eliminar evaluación.');

      setEvaluaciones(evaluaciones.filter((e) => e.id !== evaluacionId));
      setError(null);
    } catch (err) {
      setError(err.message || 'No se pudo eliminar la evaluación.');
    }
  };

  const toggleStudentDetails = async (student) => {
    if (expandedStudent?.id === student.id) {
      setExpandedStudent(null);
      setEvaluaciones([]);
      return;
    }

    try {
      const data = await getAcademicDataByRun(student.run);
      setEvaluaciones(data.evaluaciones || []);
      setExpandedStudent(student);
      setError(null);
    } catch (err) {
      setError('Error al cargar las evaluaciones.');
    }
  };

  const getNotaColor = (nota) => {
    const n = parseFloat(nota);
    if (n >= 6) return 'bg-green-50 border-green-300';
    if (n >= 5) return 'bg-yellow-50 border-yellow-300';
    return 'bg-red-50 border-red-300';
  };

  const getPromedioColor = (notas) => {
    if (!notas || notas.length === 0) return 'text-gray-400';
    const promedio = notas.reduce((acc, it) => acc + parseFloat(it.nota), 0) / notas.length;
    if (promedio >= 6) return 'text-green-600';
    if (promedio >= 5) return 'text-yellow-600';
    return 'text-red-600';
  };

  return (
    <div className="manager-wrapper">
      <div className="manager-container">
        <header className="manager-header">
          <h1>Gestión de Evaluaciones</h1>
          <p>Busca estudiantes por curso, agrega calificaciones y gestiona las notas directamente desde el panel.</p>
        </header>

        {error && (
          <div className="manager-section" style={{ borderColor: '#fca5a5', backgroundColor: '#fef2f2', color: '#b91c1c' }}>
            <p style={{ fontWeight: '600' }}>Error</p>
            <p>{error}</p>
          </div>
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

        {selectedStudent && (
          <section className="form-add">
            <h3>Agregar evaluación para <span style={{ color: '#0ea5e9' }}>{selectedStudent.nombre}</span></h3>
            <form onSubmit={handleAddEvaluation}>
              <input
                value={materia}
                onChange={(e) => setMateria(e.target.value)}
                placeholder="Ej: Matemáticas"
                required
              />
              <input
                type="number"
                min="1"
                max="7"
                step="0.1"
                value={nota}
                onChange={(e) => setNota(e.target.value)}
                placeholder="Ej: 6.5"
                required
              />
              <button type="submit" className="btn-add-eval">Guardar Evaluación</button>
            </form>
          </section>
        )}

        <section>
          <div style={{ marginBottom: '1.5rem' }}>
            <h2>Resultados de la búsqueda</h2>
            <p style={{ color: '#64748b', fontSize: '0.875rem' }}>
              Haz click en el nombre para ver las evaluaciones y gestionar notas.
            </p>
          </div>

          <div className="student-grid">
            {estudiantes.map((student) => (
              <article key={student.id} className="student-card">
                <button
                  onClick={() => toggleStudentDetails(student)}
                  className="student-card-header"
                >
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <div>
                      <h4>{student.nombre}</h4>
                      <p>RUN {student.run}</p>
                    </div>
                    <div className="toggle-icon">
                      {expandedStudent?.id === student.id ? '▼' : '▶'}
                    </div>
                  </div>
                </button>

                {expandedStudent?.id === student.id && (
                  <div className="student-details">
                    <h5>Evaluaciones</h5>
                    {evaluaciones.length > 0 ? (
                      <ul className="details-list">
                        {evaluaciones.map((evaluacion) => (
                          <li key={evaluacion.id}>
                            <span>{evaluacion.materia}</span>
                            <span className={evaluacion.nota >= 4 ? 'nota-buena' : 'nota-mala'}>{evaluacion.nota}</span>
                          </li>
                        ))}
                      </ul>
                    ) : (
                      <p>Sin evaluaciones registradas.</p>
                    )}
                  </div>
                )}

                <div className="card-actions">
                  <button
                    onClick={() => handleSelectStudent(student)}
                    className="btn-add-eval"
                  >
                    Agregar Calificación
                  </button>
                </div>
              </article>
            ))}
          </div>
        </section>
      </div>
    </div>
  );
}