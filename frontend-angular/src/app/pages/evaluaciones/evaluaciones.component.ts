import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EvaluacionService, Evaluacion } from '../../services/evaluacion.service';
import { EstudianteService, Estudiante } from '../../services/estudiante.service';

@Component({
  selector: 'app-evaluaciones',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './evaluaciones.component.html',
  styleUrl: './evaluaciones.component.css'
})
export class EvaluacionesComponent implements OnInit {
  evaluaciones: Evaluacion[] = [];
  estudiantes: Estudiante[] = [];
  selectedEvaluacion: Evaluacion | null = null;
  showForm = false;
  loading = false;
  
  formData: Evaluacion = {
    estudiante_id: 0,
    nombre_evaluacion: '',
    fecha: '',
    puntaje: 0,
    calificacion: 0,
    tipo: '',
    observaciones: ''
  };

  constructor(
    private evaluacionService: EvaluacionService,
    private estudianteService: EstudianteService
  ) {}

  ngOnInit(): void {
    this.loadEvaluaciones();
    this.loadEstudiantes();
  }

  loadEvaluaciones(): void {
    this.loading = true;
    this.evaluacionService.obtenerEvaluaciones().subscribe({
      next: (data) => {
        this.evaluaciones = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar evaluaciones:', error);
        this.loading = false;
      }
    });
  }

  loadEstudiantes(): void {
    this.estudianteService.obtenerEstudiantes().subscribe({
      next: (data) => {
        this.estudiantes = data;
      },
      error: (error) => console.error('Error al cargar estudiantes:', error)
    });
  }

  getEstudianteName(id: number): string {
    const estudiante = this.estudiantes.find(e => e.id === id);
    return estudiante ? `${estudiante.nombre} ${estudiante.apellido}` : 'Desconocido';
  }

  openForm(evaluacion?: Evaluacion): void {
    if (evaluacion) {
      this.formData = { ...evaluacion };
      this.selectedEvaluacion = evaluacion;
    } else {
      this.resetForm();
    }
    this.showForm = true;
  }

  resetForm(): void {
    this.formData = {
      estudiante_id: 0,
      nombre_evaluacion: '',
      fecha: '',
      puntaje: 0,
      calificacion: 0,
      tipo: '',
      observaciones: ''
    };
    this.selectedEvaluacion = null;
  }

  closeForm(): void {
    this.showForm = false;
    this.resetForm();
  }

  saveEvaluacion(): void {
    if (this.selectedEvaluacion?.id) {
      this.evaluacionService.actualizarEvaluacion(this.selectedEvaluacion.id, this.formData).subscribe({
        next: () => {
          this.loadEvaluaciones();
          this.closeForm();
        },
        error: (error) => console.error('Error al actualizar:', error)
      });
    } else {
      this.evaluacionService.crearEvaluacion(this.formData).subscribe({
        next: () => {
          this.loadEvaluaciones();
          this.closeForm();
        },
        error: (error) => console.error('Error al crear:', error)
      });
    }
  }

  deleteEvaluacion(id: number): void {
    if (confirm('¿Estás seguro de que deseas eliminar esta evaluación?')) {
      this.evaluacionService.eliminarEvaluacion(id).subscribe({
        next: () => this.loadEvaluaciones(),
        error: (error) => console.error('Error al eliminar:', error)
      });
    }
  }

  getCalificacionClass(calificacion: number): string {
    if (calificacion >= 7) return 'badge bg-success';
    if (calificacion >= 5) return 'badge bg-warning';
    return 'badge bg-danger';
  }
}
