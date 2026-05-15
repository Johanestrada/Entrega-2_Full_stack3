const BFF_BASE_URL = import.meta.env.VITE_BFF_URL ?? 'http://localhost:8084';

export async function fetchAcademicData(estudianteId) {
  const response = await fetch(`${BFF_BASE_URL}/academico/${estudianteId}`);

  if (!response.ok) {
    throw new Error(`Error al obtener información académica: ${response.status}`);
  }

  return response.json();
}
