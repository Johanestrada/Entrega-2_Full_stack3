import { render, screen, fireEvent } from '@testing-library/react';
import { test, expect, vi } from 'vitest';
import SearchForm from './SearchForm';

test('renderiza el formulario de búsqueda y responde al cambio de input', () => {
  const setQuery = vi.fn();
  const setMode = vi.fn();
  const buscarAcademico = vi.fn((event) => event.preventDefault());

  render(
    <SearchForm
      query="123"
      onSetQuery={setQuery}
      mode="run"
      onSetMode={setMode}
      onBuscarAcademico={buscarAcademico}
    />
  );

  expect(screen.getByPlaceholderText('Ingresa RUN de estudiante')).toBeTruthy();
  expect(screen.getByRole('button', { name: /buscar/i })).toBeTruthy();

  fireEvent.change(screen.getByPlaceholderText('Ingresa RUN de estudiante'), {
    target: { value: '456' },
  });

  expect(setQuery).toHaveBeenCalledWith('456');
});
