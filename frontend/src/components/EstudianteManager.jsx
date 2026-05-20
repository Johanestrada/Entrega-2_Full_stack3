import { useState } from 'react';
import { getStudentsByCourse, postNewStudent, getAcademicDataByRun, deleteStudent } from '../services/academicApi';
import SearchForm from './SearchForm';
import './Manager.css';

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
    <div className="manager-container">
      <div className="manager-header">
        <h2>Gestión de Estudiantes</h2>
        <button onClick={() => setShowForm(!showForm)} className="btn-primary">
          {showForm ? 'Cancelar' : 'Agregar Estudiante'}
        </button>
      </div>
      {showForm && (
        <div className="form-section">
          <h3>Nuevo Estudiante</h3>
          <form onSubmit={handleAddStudent} className="manager-form">
            <div className="form-group">
              <label htmlFor="run">RUN</label>
              <input id="run" type="text" value={run} onChange={(e) => setRun(e.target.value)} required />
            </div>
            <div className="form-group">
              <label htmlFor="nombre">Nombre Completo</label>
              <input id="nombre" type="text" value={nombre} onChange={(e) => setNombre(e.target.value)} required />
            </div>
            <div className="form-group">
              <label htmlFor="curso">Curso</label>
              <input id="curso" type="text" value={curso} onChange={(e) => setCurso(e.target.value)} required />
            </div>
            <button type="submit" className="btn-success">Guardar Estudiante</button>
          </form>
        </div>
      )}

      <div className="form-section">
        <h3>Buscar Estudiantes por Curso</h3>
        <SearchForm query={query} onSetQuery={setQuery} mode={mode} onSetMode={setMode} onBuscarAcademico={handleSearch} />
      </div>

      {status === 'loading' && <p>Buscando estudiantes...</p>}
      {status === 'error' && <p className="error">{error}</p>}

      <div className="table-section">
        {status === 'success' && (
          <>
          <h3>Resultados de la Búsqueda</h3>
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
                    <button onClick={() => handleDelete(estudiante.id)} className="btn-danger">Eliminar</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          </>
        )} 
      </div>
    </div>
  );
}