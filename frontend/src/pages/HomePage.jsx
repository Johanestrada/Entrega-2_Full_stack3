import { Link } from 'react-router-dom';

export default function HomePage() {
  return (
    <section className="home-page">
      <div className="home-hero">
        <div className="hero-copy">
          <span className="eyebrow">Bienvenido al Colegio</span>
          <h1>Portal Académico</h1>
          <p>
            Consulta asistencias, evaluaciones y datos de estudiantes desde una sola plataforma.
          </p>
          <div className="hero-actions">
            <Link className="button" to="/login">Iniciar sesión</Link>
            <Link className="button button-secondary" to="/register">Registrarse</Link>
          </div>
        </div>
        <div className="hero-cards">
          <article className="hero-card">
            <div className="hero-card-icon">🏫</div>
            <h2>Infraestructura</h2>
            <p>Imágenes y noticias del colegio para toda la comunidad.</p>
          </article>
          <article className="hero-card">
            <div className="hero-card-icon">📚</div>
            <h2>Educación</h2>
            <p>Herramientas para ver resultados y progreso académico.</p>
          </article>
          <article className="hero-card">
            <div className="hero-card-icon">👩‍🏫</div>
            <h2>Profesores</h2>
            <p>Accede a la información de cada alumno con un solo clic.</p>
          </article>
        </div>
      </div>
    </section>
  );
}
