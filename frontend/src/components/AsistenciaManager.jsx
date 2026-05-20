import { useState } from 'react';
import { getAcademicDataByRun, postAttendanceStudent, getStudentsByCourse } from '../services/academicApi';
import SearchForm from './SearchForm';
import './Manager.css';

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
    <div className="manager-container">
      <div className="manager-header">
        <h2>Gestión de Asistencias</h2>
      </div>
      {selectedStudent && (
        <div className="form-section">
          <h3>Registrar Asistencia para {selectedStudent.nombre}</h3>
          <form onSubmit={handleAddAttendance} className="manager-form">
            <div className="form-group">
              <label htmlFor="presente">Estado</label>
              <select id="presente" value={presente} onChange={(e) => setPresente(e.target.value === 'true')}>
                <option value={true}>Presente</option>
                <option value={false}>Ausente</option>
              </select>
            </div>
            <button type="submit" className="btn-success">Guardar Asistencia</button>
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
            <h3 className="text-xl font-semibold mb-4">Resultados de la Búsqueda</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {estudiantes.map((student) => (
                <div key={student.id} className="border rounded p-4 bg-gray-50">
                  <button
                    onClick={() => toggleStudentDetails(student)}
                    className="w-full text-left font-semibold text-blue-600 hover:underline"
                  >
                    {student.nombre} ({student.run})
                    {expandedStudent?.id === student.id ? ' ▼' : ' ▶'}
                  </button>

                  {expandedStudent?.id === student.id && (
                    <div className="mt-4 bg-white p-4 rounded border-t-2">
                      <h3 className="font-bold mb-3">Asistencias:</h3>
                      {asistencias.length > 0 ? (
                        <ul className="space-y-2">
                          {asistencias.map((asistencia) => (
                            <li key={asistencia.id} className="flex justify-between items-center bg-gray-100 p-2 rounded">
                              <span>{new Date(asistencia.fecha).toLocaleDateString()}</span>
                              <span className={asistencia.presente ? 'text-green-600 font-semibold' : 'text-red-600 font-semibold'}>
                                {asistencia.presente ? 'Presente' : 'Ausente'}
                              </span>
                            </li>
                          ))}
                        </ul>
                      ) : (
                        <p className="text-gray-500">Sin registros de asistencia.</p>
                      )}
                    </div>
                  )}

                  <button
                    onClick={() => handleSelectStudent(student)}
                    className="mt-2 w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600"
                  >
                    Marcar Asistencia
                  </button>
                </div>
              ))}
            </div>
          </>
        )}
      </div>
    </div>
  );
}