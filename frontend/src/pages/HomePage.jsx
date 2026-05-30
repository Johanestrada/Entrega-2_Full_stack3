import { Link } from 'react-router-dom';

export default function HomePage() {
  return (
    <section className="home-landing">
      <div className="container py-5">
        <div className="row align-items-center g-5">
          <div className="col-lg-6">
            <span className="eyebrow-pill">Portal académico</span>
            <h1 className="display-4 fw-bold text-white mt-3">
              Una interfaz limpia para seguir estudiantes, notas y asistencia.
            </h1>
            <p className="lead text-white-50 mt-3 mb-4">
              Diseñamos una experiencia visual más moderna con Bootstrap, paneles con profundidad y una composición inspirada en escenas 3D.
            </p>
            <div className="d-flex flex-wrap gap-3">
              <Link className="btn btn-light btn-lg px-4" to="/login">
                Iniciar sesión
              </Link>
            </div>

            <div className="row g-3 mt-4">
              <div className="col-sm-4">
                <div className="mini-metric">
                  <strong>RUN</strong>
                  <span>Búsqueda rápida</span>
                </div>
              </div>
              <div className="col-sm-4">
                <div className="mini-metric">
                  <strong>Notas</strong>
                  <span>Edición simple</span>
                </div>
              </div>
              <div className="col-sm-4">
                <div className="mini-metric">
                  <strong>Asistencia</strong>
                  <span>Control diario</span>
                </div>
              </div>
            </div>
          </div>

          <div className="col-lg-6">
            <div className="scene-panel">
              <div className="orb orb-one" />
              <div className="orb orb-two" />
              <div className="orb orb-three" />
              <div className="glass-card scene-card scene-card-top">
                <span className="badge rounded-pill text-bg-info-subtle text-info-emphasis mb-2">Vista general</span>
                <h2 className="h4 text-white mb-1">Colegio conectado</h2>
                <p className="mb-0 text-white-50">Datos académicos organizados en una pantalla limpia.</p>
              </div>
              <div className="glass-card scene-card scene-card-bottom">
                <div className="d-flex justify-content-between align-items-center">
                  <div>
                    <small className="text-white-50 d-block">Estado del sistema</small>
                    <strong className="text-white">Operativo</strong>
                  </div>
                  <div className="pulse-dot" />
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="row g-4 mt-5">
          {[
            {
              title: 'Estudiantes',
              text: 'Gestiona registros con tarjetas limpias y jerarquía clara.',
            },
            {
              title: 'Evaluaciones',
              text: 'Bloques visuales y acciones fáciles de escanear.',
            },
            {
              title: 'Asistencias',
              text: 'Paneles consistentes con foco en lectura rápida.',
            },
          ].map((item) => (
            <div className="col-md-4" key={item.title}>
              <div className="feature-card h-100">
                <h3 className="h5">{item.title}</h3>
                <p className="mb-0 text-body-secondary">{item.text}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
