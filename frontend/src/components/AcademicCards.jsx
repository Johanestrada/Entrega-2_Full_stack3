import React, { useState, useEffect, Fragment } from 'react';
import { postAttendanceStudent, postNewEvaluation } from '../services/academicApi';

function StudentInfoTable({ estudiante }) {
  return (
    <div className="table-section">
      <h2>👤 Información del Estudiante</h2>
      <table className="info-table">
        <tbody>
          <tr>
            <td><strong>RUN:</strong></td>
            <td>{estudiante.run}</td>
          </tr>
          <tr>
            <td><strong>Nombre:</strong></td>
            <td>{estudiante.nombre}</td>
          </tr>
          <tr>
            <td><strong>Curso:</strong></td>
            <td>{estudiante.curso ?? estudiante.edad ?? 'No disponible'}</td>
          </tr>
        </tbody>
      </table>
    </div>
  );
}

function AttendanceTable({ asistencias, estudianteId }) {
  const [local, setLocal] = useState(asistencias ?? []);
  const [status, setStatus] = useState('idle');
  const [message, setMessage] = useState('');

  useEffect(() => {
    setLocal(asistencias ?? []);
  }, [asistencias]);

  const handleMark = async (presente) => {
    setStatus('loading');
    setMessage('');
    try {
      const id = estudianteId ?? null;
      if (!id) throw new Error('ID de estudiante no disponible');
      const created = await postAttendanceStudent(id, presente);
      setLocal((prev) => [created, ...prev]);
      setStatus('success');
      setMessage(presente ? 'Marcado presente' : 'Marcado ausente');
    } catch (err) {
      setStatus('error');
      setMessage(err.message || 'Error al marcar asistencia');
    }
  };

  return (
    <div className="table-section">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h2>📅 Asistencias</h2>
        {estudianteId && (
          <div>
            <button onClick={() => handleMark(true)} disabled={status === 'loading'} className="btn-primary">Presente</button>
            <button onClick={() => handleMark(false)} disabled={status === 'loading'} className="btn-danger">Ausente</button>
          </div>
        )}
      </div>
      {message && <p className={status === 'error' ? 'error' : 'info'}>{message}</p>}
      {local && local.length > 0 ? (
        <table className="data-table">
          <thead>
            <tr>
              <th>Fecha</th>
              <th>Estado</th>
            </tr>
          </thead>
          <tbody>
            {local.map((a, index) => (
              <tr key={index}>
                <td>{a.fecha}</td>
                <td><span className={a.presente ? 'badge-present' : 'badge-absent'}>{a.presente ? '✓ Presente' : '✗ Ausente'}</span></td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>No hay asistencias registradas.</p>
      )}
    </div>
  );
}

function EvaluationTable({ evaluaciones, estudianteId }) {
  const [localEvaluaciones, setLocalEvaluaciones] = useState(evaluaciones ?? []);
  const [showForm, setShowForm] = useState(false);
  const [newMateria, setNewMateria] = useState('');
  const [newNota, setNewNota] = useState('');
  const [status, setStatus] = useState('idle');
  const [message, setMessage] = useState('');

  useEffect(() => {
    setLocalEvaluaciones(evaluaciones ?? []);
  }, [evaluaciones]);

  const handleAddEvaluation = async (e) => {
    e.preventDefault();
    setStatus('loading');
    setMessage('');
    try {
      const created = await postNewEvaluation({ estudianteId, materia: newMateria, nota: parseFloat(newNota) });
      setLocalEvaluaciones(prev => [created, ...prev]);
      setStatus('success');
      setMessage('Calificación agregada con éxito.');
      setShowForm(false);
      setNewMateria('');
      setNewNota('');
    } catch (err) {
      setStatus('error');
      setMessage(err.message || 'Error al agregar la calificación.');
    }
  };

  return (
    <div className="table-section">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
        <h2>📝 Evaluaciones</h2>
        {estudianteId && (
          <button onClick={() => setShowForm(!showForm)} className="btn-small">
            {showForm ? 'Cancelar' : 'Agregar Calificación'}
          </button>
        )}
      </div>

      {showForm && (
        <form onSubmit={handleAddEvaluation} className="add-evaluation-form">
          <input
            type="text"
            placeholder="Asignatura"
            value={newMateria}
            onChange={(e) => setNewMateria(e.target.value)}
            required
          />
          <input type="number" step="0.1" placeholder="Nota" value={newNota} onChange={(e) => setNewNota(e.target.value)} required />
          <button type="submit" disabled={status === 'loading'}>{status === 'loading' ? 'Guardando...' : 'Guardar'}</button>
        </form>
      )}
      {message && <p className={status === 'error' ? 'error' : 'info'}>{message}</p>}

      {localEvaluaciones && localEvaluaciones.length > 0 ? (
        <table className="data-table">
          <thead>
            <tr>
              <th>Materia</th>
              <th>Nota</th>
            </tr>
          </thead>
          <tbody>
            {localEvaluaciones.map((e, index) => (
              <tr key={index}>
                <td>{e.materia ?? e.asignatura}</td>
                <td>{e.nota}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>No hay evaluaciones disponibles.</p>
      )}
    </div>
  );
}

function StudentListTable({ estudiantes }) {
  const [status, setStatus] = useState({});
  const [messages, setMessages] = useState({});

  const handleMark = async (idx, presente) => {
    // Bloqueo para evitar clics múltiples mientras está cargando
    if (status[idx] === 'loading') return;

    setStatus((prev) => ({ ...prev, [idx]: 'loading' }));
    setMessages((prev) => ({ ...prev, [idx]: '' })); // Limpiar mensaje anterior

    try {
      const estudiante = estudiantes[idx];
      const id = estudiante.id ?? estudiante.estudianteId ?? null;
      if (!id) throw new Error('ID de estudiante no disponible');

      await postAttendanceStudent(id, presente);

      setStatus((prev) => ({ ...prev, [idx]: 'success' }));
      setMessages((prev) => ({ ...prev, [idx]: presente ? 'Presente' : 'Ausente' }));
    } catch (err) {
      setMessages((prev) => ({ ...prev, [idx]: err.message || 'Error' }));
      // El estado de 'loading' se limpiará en el 'finally'
    } finally {
      // Si no fue exitoso, volvemos al estado de error para permitir reintentos
      setStatus((prev) => (prev[idx] === 'success' ? prev : { ...prev, [idx]: 'error' }));
    }
  };

  return (
    <div className="table-section">
      <h2>👥 Estudiantes del Curso</h2>
      <table className="data-table">
        <thead>
          <tr>
            <th>RUN</th>
            <th>Nombre</th>
            <th>Curso</th>
          </tr>
        </thead>
        <tbody>
          {estudiantes.map((estudiante, idx) => {
            const isLoading = status[idx] === 'loading';
            const isDone = status[idx] === 'success';
            const hasError = status[idx] === 'error';

            return (
              <Fragment key={estudiante.run ?? idx}>
                <tr className="student-info-row">
                  <td>{estudiante.run}</td>
                  <td>{estudiante.nombre}</td>
                  <td>{estudiante.curso}</td>
                </tr>
                <tr className="student-action-row">
                  <td></td>
                  <td colSpan="2">
                    {isDone ? (
                      <span className="success-text">
                        {messages[idx]}
                      </span>
                    ) : (
                      <div className="action-container">
                        <div className="action-buttons">
                          <button
                            onClick={() => handleMark(idx, true)}
                            disabled={isLoading}
                            className="btn-small btn-primary"
                          >
                            {isLoading ? '...' : '✓ Presente'}
                          </button>
                          <button
                            onClick={() => handleMark(idx, false)}
                            disabled={isLoading}
                            className="btn-small btn-danger"
                          >
                            {isLoading ? '...' : '✗ Ausente'}
                          </button>
                        </div>
                        {isLoading && <span className="info-text">Marcando asistencia...</span>}
                        {/* Si hay error, los botones se reactivan para reintentar */}
                        {hasError && !isLoading && <span className="error-text">{messages[idx]}</span>}
                      </div>
                    )}
                  </td>
                </tr>
              </Fragment>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}

function AcademicCards({ data }) {
  if (Array.isArray(data)) {
    return (
      <section className="cards-container">
        {data.length > 0 ? (
          <StudentListTable estudiantes={data} />
        ) : (
          <div className="table-section">
            <h2>No se encontraron estudiantes</h2>
            <p>Prueba con otro curso.</p>
          </div>
        )}
      </section>
    );
  }

  const estudianteObj = data?.estudiante;
  const estudianteId = estudianteObj ? (estudianteObj.id ?? estudianteObj.estudianteId) : null;

  return (
    <section className="cards-container">
      {estudianteObj && <StudentInfoTable estudiante={estudianteObj} />}
      <AttendanceTable asistencias={data?.asistencias} estudianteId={estudianteId} />
      <EvaluationTable evaluaciones={data?.evaluaciones} estudianteId={estudianteId} />
    </section>
  );
}

export default AcademicCards;
