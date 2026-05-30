const BFF_BASE_URL = import.meta.env.VITE_BFF_URL || 'http://localhost:8184';

export async function loginUser(username, password) {
  const response = await fetch(`${BFF_BASE_URL}/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password }),
  });

  if (!response.ok) {
    throw new Error('Credenciales inválidas o servidor no disponible');
  }

  return response.json();
}

export async function registerUser(username, password) {
  const response = await fetch(`${BFF_BASE_URL}/auth/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password }),
  });
  if (!response.ok) {
    const error = await response.text();
    throw new Error(error || 'Error al registrar usuario');
  }
  return response.text();
}
