import { Link } from 'react-router-dom';
import { useEffect, useState } from 'react';
import carousel1 from '../assets/img/1-Estudiantes.jpg';
import carousel2 from '../assets/img/UBO-2.jpg';
import imgA from '../assets/img/A_UNO_1722396 (1).jpg.jpeg';
import imgB from '../assets/img/foto_0000000220260619222408.jpg';
import imgC from '../assets/img/images.jpg';

export default function HomePage() {
  return (
    <section className="home-landing bg-white text-dark">
      {/* Carrusel full-width pegado al header (sin espacio) */}
      <div className="container-fluid px-0">
        <div id="homeCarousel" className="carousel slide" data-bs-ride="carousel" data-bs-interval="1000">
          <div className="carousel-indicators">
            <button type="button" data-bs-target="#homeCarousel" data-bs-slide-to="0" className="active" aria-current="true" aria-label="Slide 1"></button>
            <button type="button" data-bs-target="#homeCarousel" data-bs-slide-to="1" aria-label="Slide 2"></button>
            <button type="button" data-bs-target="#homeCarousel" data-bs-slide-to="2" aria-label="Slide 3"></button>
          </div>
          <div className="carousel-inner">
            <div className="carousel-item active">
              <img src={carousel1} className="d-block w-100" alt="Estudiantes" style={{ height: 420, objectFit: 'cover' }} />
              <div className="carousel-caption d-none d-md-block text-dark">
                <h5>Bienvenido al Portal Académico Bernardo O’Higgins</h5>
                <p>Accede fácilmente a información de estudiantes, evaluaciones y asistencia.</p>
              </div>
            </div>
            <div className="carousel-item">
              <img src={carousel2} className="d-block w-100" alt="UBO" style={{ height: 420, objectFit: 'cover' }} />
              <div className="carousel-caption d-none d-md-block text-dark">
                <h5>Herramientas para docentes</h5>
                <p>Gestión rápida de notas y controles de asistencia.</p>
              </div>
            </div>
            <div className="carousel-item">
              <img src={carousel1} className="d-block w-100" alt="Información familias" style={{ height: 420, objectFit: 'cover' }} />
              <div className="carousel-caption d-none d-md-block text-dark">
                <h5>Información para familias</h5>
                <p>Mantente al día con el desempeño y eventos del colegio.</p>
              </div>
            </div>
          </div>
          <button className="carousel-control-prev" type="button" data-bs-target="#homeCarousel" data-bs-slide="prev">
            <span className="carousel-control-prev-icon" aria-hidden="true"></span>
            <span className="visually-hidden">Anterior</span>
          </button>
          <button className="carousel-control-next" type="button" data-bs-target="#homeCarousel" data-bs-slide="next">
            <span className="carousel-control-next-icon" aria-hidden="true"></span>
            <span className="visually-hidden">Siguiente</span>
          </button>
        </div>
      </div>

      <div className="container py-5 mt-0">

        {/* Sección de noticias (tarjetas locales) */}
        <div className="row mb-5">
          <div className="col-12">
            <h2 className="h4 mb-3">Noticias</h2>
            <div className="row g-3">
              {[
                { title: 'Apertura de curso 2026', text: 'Fechas y actividades de inicio del año lectivo.', img: imgA, link: '#' },
                { title: 'Charla para familias', text: 'Invitación a reunión informativa sobre evaluación.', img: imgB, link: '#' },
                { title: 'Mantenimiento programado', text: 'Aviso de mantenimiento del sistema el fin de semana.', img: imgC, link: '#' },
              ].map((n, i) => (
                <div className="col-md-4" key={i}>
                  <div className="card news-card h-100">
                    {n.img && <img src={n.img} className="card-img-top" alt={n.title} style={{ height: 180, objectFit: 'cover' }} />}
                    <div className="card-body d-flex flex-column">
                      <h5 className="card-title">{n.title}</h5>
                      <p className="card-text text-body-secondary flex-grow-1">{n.text}</p>
                      <div className="mt-2 d-flex justify-content-between align-items-center">
                        <small className="text-muted">Portal</small>
                        <a href={n.link} target="_blank" rel="noopener noreferrer" className="btn btn-sm btn-primary">Ver noticia</a>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* "Últimos estudiantes" movidos a la vista de Alumnos */}
      </div>
    </section>
  );
}
