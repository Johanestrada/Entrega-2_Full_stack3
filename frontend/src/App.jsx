import { useState } from 'react';
import './App.css';
import { useAcademicData } from './hooks/useAcademicData';
import SearchForm from './components/SearchForm';
import AcademicCards from './components/AcademicCards';

function App() {
  const [estudianteId, setEstudianteId] = useState('');
  const { data, error, loading, buscarAcademico } = useAcademicData();

  const handleBuscarAcademico = async (event) => {
    event.preventDefault();
    await buscarAcademico(estudianteId);
  };

  return (
    <div className="app-container">
      <header className="site-header">
        <h1>🎓 Portal Académico</h1>
        <nav>
          <ul>
            <li><a href="#">Inicio</a></li>
            <li><a href="#">Estudiantes</a></li>
            <li><a href="#">Evaluaciones</a></li>
            <li><a href="#">Contacto</a></li>
          </ul>
        </nav>
      </header>

      <main className="main-content">
        <p>Busca los datos combinados desde el BFF.</p>

        <SearchForm
          estudianteId={estudianteId}
          onSetEstudianteId={setEstudianteId}
          onBuscarAcademico={handleBuscarAcademico}
        />

        {loading && <p className="info">Cargando...</p>}
        {error && <p className="error">{error}</p>}

        {data && <AcademicCards data={data} />}
      </main>

      <footer className="site-footer">
        <p>&copy; 2026 Portal Académico. Todos los derechos reservados.</p>
      </footer>
    </div>
  );
}

export default App;
