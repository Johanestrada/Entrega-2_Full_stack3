import { render, screen, fireEvent } from '@testing-library/react';
import { test, expect, vi } from 'vitest';
import SearchForm from './SearchForm';

test('renderiza el formulario de búsqueda y responde al cambio de input', () => {
  const setEstudianteId = vi.fn();
  const buscarAcademico = vi.fn((event) => event.preventDefault());

  render(
    <SearchForm
      estudianteId="123"
      onSetEstudianteId={setEstudianteId}
      onBuscarAcademico={buscarAcademico}
    />
  );

  expect(screen.getByPlaceholderText('Ingresa ID de estudiante')).toBeTruthy();
  expect(screen.getByRole('button', { name: /buscar/i })).toBeTruthy();

  fireEvent.change(screen.getByPlaceholderText('Ingresa ID de estudiante'), {
    target: { value: '456' },
  });

  expect(setEstudianteId).toHaveBeenCalledWith('456');
});
