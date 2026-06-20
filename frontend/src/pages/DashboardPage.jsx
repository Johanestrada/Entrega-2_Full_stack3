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

        <main className="dashboard-main">
          <div className="dashboard-top card border-0 mb-4 p-3 shadow-sm">
            <div className="d-flex justify-content-between align-items-center">
              <div>
                <h3 className="mb-0">{activeTab === 'estudiantes' ? 'Gestión de Estudiantes' : activeTab === 'evaluaciones' ? 'Evaluaciones' : 'Asistencias'}</h3>
                <p className="text-muted small mb-0">Administra la información relevante desde este panel.</p>
              </div>
            </div>
          </div>

          <div className="dashboard-cards">
            <div className="row g-3">
              <div className="col-md-6">
                <div className="card p-3 shadow-sm">
                  <h5 className="mb-2">Acciones rápidas</h5>
                  <p className="text-muted small">Accede a las funciones más usadas: agregar estudiante, crear evaluación o registrar asistencia.</p>
                </div>
              </div>
              <div className="col-md-6">
                <div className="card p-3 shadow-sm">
                  <h5 className="mb-2">Resumen</h5>
                  <p className="text-muted small">Estudiantes activos, últimas evaluaciones y asistencias recientes.</p>
                </div>
              </div>
            </div>
          </div>

          <div className="dashboard-content mt-4">{renderContent()}</div>
        </main>
      </div>
    </section>
  );
}
