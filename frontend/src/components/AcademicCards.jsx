function Card({ title, children }) {
  return (
    <div className="card">
      <h2>{title}</h2>
      {children}
    </div>
  );
}

function StudentCard({ estudiante }) {
  return (
    <Card title="👤 Estudiante">
      {estudiante ? (
        <>
          <p><strong>Nombre:</strong> {estudiante.nombre}</p>
          <p><strong>Curso:</strong> {estudiante.curso ?? estudiante.edad ?? 'No disponible'}</p>
        </>
      ) : (
        <p>No hay datos de estudiante.</p>
      )}
    </Card>
  );
}

function AttendanceCard({ asistencias }) {
  return (
    <Card title="📅 Asistencias">
      {asistencias && asistencias.length > 0 ? (
        <ul>
          {asistencias.map((a, index) => (
            <li key={index}>{a.fecha} - {a.presente ? 'Presente' : 'Ausente'}</li>
          ))}
        </ul>
      ) : (
        <p>No hay asistencias registradas.</p>
      )}
    </Card>
  );
}

function EvaluationCard({ evaluaciones }) {
  return (
    <Card title="📝 Evaluaciones">
      {evaluaciones && evaluaciones.length > 0 ? (
        <ul>
          {evaluaciones.map((e, index) => (
            <li key={index}>{e.materia ?? e.asignatura}: {e.nota}</li>
          ))}
        </ul>
      ) : (
        <p>No hay evaluaciones disponibles.</p>
      )}
    </Card>
  );
}

function AcademicCards({ data }) {
  return (
    <section className="cards-container">
      <StudentCard estudiante={data?.estudiante} />
      <AttendanceCard asistencias={data?.asistencias} />
      <EvaluationCard evaluaciones={data?.evaluaciones} />
    </section>
  );
}

export default AcademicCards;
