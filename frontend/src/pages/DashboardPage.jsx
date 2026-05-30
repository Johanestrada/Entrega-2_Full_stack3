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
      <div className="container-fluid py-4 py-lg-5 px-3 px-lg-4">
        <div className="dashboard-topbar card border-0 shadow-sm mb-4">
          <div className="card-body d-flex flex-column flex-lg-row align-items-lg-center justify-content-between gap-3">
            <div>
              <span className="eyebrow-pill">Panel de gestión</span>
              <h1 className="h3 fw-bold mb-1 mt-2">Dashboard académico</h1>
              <p className="text-body-secondary mb-0">Administra estudiantes, evaluaciones y asistencias desde un solo lugar.</p>
            </div>
            <button onClick={handleLogout} className="btn btn-outline-danger">
              Cerrar sesión
            </button>
          </div>
        </div>

        <div className="dashboard-tabs card border-0 shadow-sm mb-4">
          <div className="card-body">
            <div className="nav nav-pills flex-column flex-md-row gap-2">
              <button onClick={() => setActiveTab('estudiantes')} className={`nav-link ${activeTab === 'estudiantes' ? 'active' : ''}`}>
                Estudiantes
              </button>
              <button onClick={() => setActiveTab('evaluaciones')} className={`nav-link ${activeTab === 'evaluaciones' ? 'active' : ''}`}>
                Evaluaciones
              </button>
              <button onClick={() => setActiveTab('asistencias')} className={`nav-link ${activeTab === 'asistencias' ? 'active' : ''}`}>
                Asistencias
              </button>
            </div>
          </div>
        </div>

        <div className="dashboard-content">{renderContent()}</div>
      </div>
    </section>
  );
}
