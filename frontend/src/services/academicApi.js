const BFF_BASE_URL = import.meta.env.VITE_BFF_URL || 'http://localhost:8184';

export async function fetchAcademicData(query, mode = 'run') {
  let url;
  if (mode === 'curso') {
    url = `${BFF_BASE_URL}/academico/curso/${query}`;
  } else {
    url = `${BFF_BASE_URL}/academico/run/${query}`;
  }

  const response = await fetch(url);

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || `Error al obtener información académica: ${response.status}`);
  }

  return response.json();
}

export async function postAttendanceStudent(estudianteId, presente) {
  const url = `${BFF_BASE_URL}/academico/estudiante/${estudianteId}/asistencia`;
  const response = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ presente }),
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || `Error al marcar asistencia: ${response.status}`);
  }

  return response.json();
}

export async function postAttendanceCurso(curso, presente) {
  const url = `${BFF_BASE_URL}/academico/curso/${curso}/asistencia`;
  const response = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ presente }),
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || `Error al marcar asistencia por curso: ${response.status}`);
  }

  return response.json();
}

export async function postNewEvaluation({ estudianteId, materia, nota }) {
  const url = `${BFF_BASE_URL}/academico/estudiante/${estudianteId}/evaluacion`;
  const response = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ materia, nota }),
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || `Error al guardar la evaluación: ${response.status}`);
  }

  return response.json();
}
