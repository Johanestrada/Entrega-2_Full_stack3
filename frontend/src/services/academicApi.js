const BFF_URL = import.meta.env.VITE_BFF_URL || 'http://localhost:8184';

function getToken() {
  const user = localStorage.getItem('user');
  if (!user) return null;
  try {
    return JSON.parse(user).token;
  } catch {
    return null;
  }
}

async function fetchApi(path, options = {}) {
  // Soporte para peticiones sin auth: pasar { skipAuth: true } en options
  const { skipAuth, ...fetchOptions } = options || {};
  const token = getToken();
  const headers = { ...(fetchOptions.headers || {}) };
  if (!skipAuth && token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  const response = await fetch(`${BFF_URL}${path}`, { ...fetchOptions, headers });
  if (!response.ok) {
    // Manejar 404 de forma no excepcional: permitir que el caller trate 'null' como "no encontrado"
    if (response.status === 404) {
      return null;
    }
    const errorBody = await response.text();
    throw new Error(`Error en la API: ${response.status} ${response.statusText} - ${errorBody}`);
  }
  // Si la respuesta no tiene contenido (ej. 204 No Content), no intentes parsear JSON
  if (response.status === 204) {
    return null;
  }
  return response.json();
}

export function getAcademicDataByRun(run) {
  // If there's no token available, call the public variant to avoid 401 from the BFF
  const token = getToken();
  if (!token) {
    return fetchApi(`/academico/run/${run}`, { skipAuth: true });
  }
  return fetchApi(`/academico/run/${run}`);
}

// Variante pública que no incluye Authorization (útil para búsqueda de estudiante sin sesión)
export function getAcademicDataByRunPublic(run) {
  return fetchApi(`/academico/run/${run}`, { skipAuth: true });
}

export function getStudentsByCourse(curso) {
  return fetchApi(`/academico/curso/${curso}`);
}

export function postNewEvaluation({ estudianteId, materia, nota }) {
  return fetchApi('/academico/evaluaciones', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ estudianteId, materia, nota }),
  });
}

export function postAttendanceStudent(estudianteId, presente) {
  return fetchApi('/academico/asistencias', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ estudianteId, presente }),
  });
}

export function postNewStudent({ run, nombre, curso }) {
  return fetchApi('/academico/estudiantes', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ run, nombre, curso }),
  });
}

export function getAllStudents() {
    // Asumiendo que tienes un endpoint para obtener todos los estudiantes en el BFF
    // Si no existe, puedes crearlo o adaptar esta función.
    // Por ahora, lo conectamos al endpoint de curso para tener datos de ejemplo.
    // Lo ideal sería: return fetchApi('/estudiantes');
    return getStudentsByCourse('1-A'); // Endpoint de ejemplo
}

export function deleteStudent(id) {
  return fetchApi(`/academico/estudiantes/${id}`, {
    method: 'DELETE',
  });
}

export function getEvaluationsByStudentId(studentId) {
  // Este endpoint no existe en el BFF, usamos el que trae todo.
  // En un futuro, se podría crear un endpoint específico.
  return getAcademicDataByRun(studentId).then(data => data.evaluaciones || []);
}

export function getAttendanceByStudentId(studentId) {
  // Igual que con evaluaciones, reutilizamos el endpoint existente.
  return getAcademicDataByRun(studentId).then(data => data.asistencias || []);
}

export function updateEvaluation(id, nota) {
  return fetchApi(`/academico/evaluaciones/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ nota }),
  });
}

export function deleteEvaluation(id) {
  return fetchApi(`/academico/evaluaciones/${id}`, {
    method: 'DELETE',
  });
}