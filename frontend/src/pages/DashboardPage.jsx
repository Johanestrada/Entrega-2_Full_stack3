import { useState } from 'react';
import EstudianteManager from '../components/EstudianteManager';
import EvaluacionManager from '../components/EvaluacionManager';
import AsistenciaManager from '../components/AsistenciaManager';
import '../components/Dashboard.css';

export default function DashboardPage({ handleLogout }) {
  const [activeTab, setActiveTab] = useState('estudiantes');

  const renderContent = () => {
    switch (activeTab) {
      case 'estudiantes':
        return <EstudianteManager />;
      case 'evaluaciones':
        return <EvaluacionManager />;
      case 'asistencias':
        return <AsistenciaManager />;
      default:
        return <EstudianteManager />;
    }
  };

  return (
    <section className="dashboard-shell">
      <div className="dashboard-grid container-fluid py-4 px-3 px-lg-4">
        <aside className="dashboard-sidebar card p-3 shadow-sm">
          <div className="sidebar-header mb-3">
            <h2 className="h5 mb-1">Panel de gestión</h2>
            <p className="muted small mb-0">Dashboard académico</p>
          </div>
          <nav className="nav flex-column gap-2">
            <button onClick={() => setActiveTab('estudiantes')} className={`nav-link btn btn-light text-start ${activeTab === 'estudiantes' ? 'active' : ''}`}>
              Estudiantes
            </button>
            <button onClick={() => setActiveTab('evaluaciones')} className={`nav-link btn btn-light text-start ${activeTab === 'evaluaciones' ? 'active' : ''}`}>
              Evaluaciones
            </button>
            <button onClick={() => setActiveTab('asistencias')} className={`nav-link btn btn-light text-start ${activeTab === 'asistencias' ? 'active' : ''}`}>
              Asistencias
            </button>
          </nav>

          <div className="sidebar-footer mt-4">
            <button onClick={handleLogout} className="btn btn-danger w-100">Cerrar sesión</button>
          </div>
        </aside>
        <div className="dashboard-content mt-4">{renderContent()}</div>
        
      </div>
    </section>
  );
}
