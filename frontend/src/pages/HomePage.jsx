import { Link } from 'react-router-dom';

export default function HomePage() {
  return (
    <section className="home-page">
      <div className="home-hero">
        <div className="hero-copy">
          <span className="eyebrow">Bienvenido</span>
          <h1>Portal Académico</h1>
          <p>
            Consulta asistencias, evaluaciones y datos de estudiantes desde una sola plataforma.
          </p>
          <div className="hero-actions">
            <Link className="button" to="/login">Iniciar sesión</Link>
          </div>
        </div>
        <div className="hero-cards">
          <article className="hero-card">
            <div className="hero-card-icon">👤</div>
            <h2>Estudiantes</h2>
            <p>Gestiona la información completa de cada alumno.</p>
          </article>
          <article className="hero-card">
            <div className="hero-card-icon">📝</div>
            <h2>Evaluaciones</h2>
            <p>Registra y consulta las calificaciones de forma sencilla.</p>
          </article>
          <article className="hero-card">
            <div className="hero-card-icon">📅</div>
            <h2>Asistencias</h2>
            <p>Lleva un control diario de la asistencia de los cursos.</p>
          </article>
        </div>
      </div>
    </section>
  );
}
