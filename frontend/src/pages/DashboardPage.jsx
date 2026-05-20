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
    <div className="dashboard-container">
      <div className="dashboard-header">
        <div className="dashboard-nav">
          <button onClick={() => setActiveTab('estudiantes')} className={activeTab === 'estudiantes' ? 'active' : ''}>Estudiantes</button>
          <button onClick={() => setActiveTab('evaluaciones')} className={activeTab === 'evaluaciones' ? 'active' : ''}>Evaluaciones</button>
          <button onClick={() => setActiveTab('asistencias')} className={activeTab === 'asistencias' ? 'active' : ''}>Asistencias</button>
        </div>
        <button onClick={handleLogout} className="logout-button">Cerrar Sesión</button>
      </div>

      <div className="dashboard-content">{renderContent()}</div>
    </div>
  );
}