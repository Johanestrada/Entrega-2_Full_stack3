import { useState, useCallback } from 'react';
import { fetchAcademicData } from '../services/academicApi';

export function useAcademicData() {
  const [data, setData] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const buscarAcademico = useCallback(async (estudianteId) => {
    setError('');
    setData(null);

    if (!estudianteId) {
      setError('Ingresa un ID de estudiante.');
      return;
    }

    setLoading(true);
    try {
      const academico = await fetchAcademicData(estudianteId);
      setData(academico);
    } catch (err) {
      setError('No se pudo obtener la información. Revisa que el BFF y los microservicios estén corriendo.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, []);

  return {
    data,
    error,
    loading,
    buscarAcademico,
  };
}
