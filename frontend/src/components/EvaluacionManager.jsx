import { useState } from 'react';
import { getStudentsByCourse, getAcademicDataByRun, postNewEvaluation } from '../services/academicApi';
import SearchForm from './SearchForm';
import './Manager.css';

export default function EvaluacionManager() {
  const [estudiantes, setEstudiantes] = useState([]);
  const [selectedStudent, setSelectedStudent] = useState(null); // Para saber a quién calificar
  const [status, setStatus] = useState('idle');
  const [error, setError] = useState(null);

  // Estados para la búsqueda
  const [query, setQuery] = useState('');
  const [mode, setMode] = useState('curso'); // Empezar por curso

  // Estados para la nueva evaluación
  const [materia, setMateria] = useState('');
  const [nota, setNota] = useState('');

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!query) return;

    setStatus('loading');
    setError(null);
    setEstudiantes([]);
    setSelectedStudent(null);

    try {
      let data;
      if (mode === 'curso') {
        data = await getStudentsByCourse(query);
      } else { // mode === 'run'
        const resumen = await getAcademicDataByRun(query);
        data = resumen && resumen.estudiante ? [resumen.estudiante] : [];
      }
      setEstudiantes(data);
      setStatus('success');
    } catch (err) {
      setError(err.message || 'No se pudo encontrar la información.');
      setStatus('error');
    }
  };

  const handleSelectStudent = (student) => {
    setSelectedStudent(selectedStudent?.id === student.id ? null : student);
    // Limpiar campos del formulario
    setMateria('');
    setNota('');
  };

  const handleAddEvaluation = async (e) => {
    e.preventDefault();
    if (!selectedStudent) {
      setError('Error: No hay un estudiante seleccionado.');
      return;
    }
    try {
      await postNewEvaluation({ estudianteId: selectedStudent.id, materia, nota });
      alert(`Evaluación registrada para ${selectedStudent.nombre}`);
      setSelectedStudent(null); // Ocultar el formulario
    } catch (err) {
      console.error("Error al agregar evaluación:", err);
      setError('No se pudo agregar la evaluación. Inténtalo de nuevo.');
    }
  };

  return (
    <div className="manager-container">
      <div className="manager-header">
        <h2>Gestión de Evaluaciones</h2>
      </div>
      {selectedStudent && (
        <div className="form-section">
          <h3>Nueva Evaluación para {selectedStudent.nombre}</h3>
          <form onSubmit={handleAddEvaluation} className="manager-form">
            <div className="form-group">
              <label htmlFor="materia">Materia</label>
              <input id="materia" type="text" value={materia} onChange={(e) => setMateria(e.target.value)} required />
            </div>
            <div className="form-group">
              <label htmlFor="nota">Nota</label>
              <input id="nota" type="number" step="0.1" min="1" max="7" value={nota} onChange={(e) => setNota(e.target.value)} required />
            </div>
            <button type="submit" className="btn-success">Guardar Evaluación</button>
          </form>
        </div>
      )}

      <div className="form-section">
        <h3>Buscar Estudiantes</h3>
        <SearchForm query={query} onSetQuery={setQuery} mode={mode} onSetMode={setMode} onBuscarAcademico={handleSearch} />
      </div>

      {status === 'loading' && <p>Buscando...</p>}
      {status === 'error' && <p className="error">{error}</p>}

      <div className="table-section">
        {status === 'success' && (
          <>
            <h3>Resultados de la Búsqueda</h3>
            {estudiantes.length > 0 ? (
              <table className="data-table">
                <thead>
                  <tr>
                    <th>RUN</th>
                    <th>Nombre</th>
                    <th>Curso</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {estudiantes.map((estudiante) => (
                    <tr key={estudiante.id}>
                      <td>{estudiante.run}</td>
                      <td>{estudiante.nombre}</td>
                      <td>{estudiante.curso}</td>
                      <td className="actions">
                        <button onClick={() => handleSelectStudent(estudiante)} className="btn-secondary">Agregar Calificación</button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <p>No se encontraron estudiantes con ese criterio.</p>
            )}
          </>
        )}
      </div>
    </div>
  );
}