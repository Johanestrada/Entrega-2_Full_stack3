import { useState } from 'react';
import { getAcademicDataByRun, postAttendanceStudent, getStudentsByCourse } from '../services/academicApi';
import SearchForm from './SearchForm';
export default function AsistenciaManager() {
  const [estudiantes, setEstudiantes] = useState([]);
  const [selectedStudent, setSelectedStudent] = useState(null);
  const [asistencias, setAsistencias] = useState([]);
  const [expandedStudent, setExpandedStudent] = useState(null);
  const [status, setStatus] = useState('idle');
  const [error, setError] = useState(null);
  // Estados para la búsqueda
  const [query, setQuery] = useState('');
  const [mode, setMode] = useState('curso'); // Empezar por curso

  // Estados para la nueva asistencia
  const [presente, setPresente] = useState(true);

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
    // Si ya estaba seleccionado, lo deseleccionamos. Si no, lo seleccionamos.
    setSelectedStudent(selectedStudent?.id === student.id ? null : student);
    setPresente(true); // Resetear el estado por defecto
  };

  const handleAddAttendance = async (e) => {
    e.preventDefault();
    if (!selectedStudent) {
      setError('Error: No hay un estudiante seleccionado.');
      return;
    }
    try {
      await postAttendanceStudent(selectedStudent.id, presente);
      alert(`Asistencia registrada para ${selectedStudent.nombre}`);
      setSelectedStudent(null); // Ocultar el formulario después de registrar
    } catch (err) {
      console.error("Error al registrar asistencia:", err);
      setError('No se pudo registrar la asistencia. Inténtalo de nuevo.');
    }
  };

  const toggleStudentDetails = async (student) => {
    if (expandedStudent?.id === student.id) {
      setExpandedStudent(null);
    } else {
      // Cargar asistencias del estudiante
      try {
        const data = await getAcademicDataByRun(student.run);
        setAsistencias(data.asistencias || []);
        setExpandedStudent(student);
      } catch (err) {
        setError('Error al cargar las asistencias');
      }
    }
  };

  return (
    <div className="manager-wrapper">
      <div className="manager-container">
        <header className="manager-header">
          <h1>Gestión de Asistencias</h1>
          <p>Busca estudiantes por curso y registra su asistencia diaria.</p>
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
            <h3>Marcar asistencia para <span style={{ color: '#10b981' }}>{selectedStudent.nombre}</span></h3>
            <form onSubmit={handleAddAttendance}>
              <select value={presente} onChange={(e) => setPresente(e.target.value === 'true')}>
                <option value={true}>Presente</option>
                <option value={false}>Ausente</option>
              </select>
              <button type="submit" className="btn-add-asistencia">Guardar Asistencia</button>
            </form>
          </section>
        )}

        <section>
          <div style={{ marginBottom: '1.5rem' }}>
            <h2>Resultados de la búsqueda</h2>
            <p style={{ color: '#64748b', fontSize: '0.875rem' }}>
              Usa las acciones para ver historial o marcar asistencia.
            </p>
          </div>

          <div className="manager-section">
            {status === 'success' && (
              <div className="table-responsive">
                <table className="table table-striped">
                  <thead>
                    <tr>
                      <th>Nombre</th>
                      <th>RUN</th>
                      <th>Curso</th>
                      <th>Acciones</th>
                    </tr>
                  </thead>
                  <tbody>
                    {estudiantes.map((student) => (
                      <tr key={student.id}>
                        <td>{student.nombre}</td>
                        <td>{student.run}</td>
                        <td>{student.curso}</td>
                        <td>
                          <button className="btn btn-sm btn-outline-secondary me-2" onClick={() => toggleStudentDetails(student)}>
                            Historial
                          </button>
                          <button className="btn btn-sm btn-outline-primary" onClick={() => handleSelectStudent(student)}>
                            Marcar
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}

            {expandedStudent && (
              <div className="student-details mt-3">
                <h5>Asistencias de {expandedStudent.nombre}</h5>
                {asistencias.length > 0 ? (
                  <ul className="details-list">
                    {asistencias.map((asistencia) => (
                      <li key={asistencia.id}>
                        <span>{new Date(asistencia.fecha).toLocaleDateString()}</span>
                        <span className={asistencia.presente ? 'asistencia-presente' : 'asistencia-ausente'}>
                          {asistencia.presente ? 'Presente' : 'Ausente'}
                        </span>
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p>Sin registros de asistencia.</p>
                )}
              </div>
            )}
          </div>
        </section>
      </div>
    </div>
  );
}