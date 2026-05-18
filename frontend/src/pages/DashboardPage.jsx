import { useState } from 'react';
import { useAcademicData } from '../hooks/useAcademicData';
import SearchForm from '../components/SearchForm';
import AcademicCards from '../components/AcademicCards';

export default function DashboardPage() {
  const [query, setQuery] = useState('');
  const [mode, setMode] = useState('run');
  const { data, error, loading, buscarAcademico } = useAcademicData();

  const handleBuscarAcademico = async (event) => {
    event.preventDefault();
    await buscarAcademico(query, mode);
  };

  return (
    <section className="dashboard-page">
      <div className="dashboard-panel">
        <h1>Panel Académico</h1>
        <p>Busca los datos combinados desde el BFF.</p>
        <SearchForm
          query={query}
          onSetQuery={setQuery}
          mode={mode}
          onSetMode={setMode}
          onBuscarAcademico={handleBuscarAcademico}
        />
        {loading && <p className="info">Cargando...</p>}
        {error && <p className="error">{error}</p>}
        {data && <AcademicCards data={data} />}
      </div>
    </section>
  );
}
