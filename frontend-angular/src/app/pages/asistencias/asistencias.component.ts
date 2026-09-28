import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AsistenciaService, Asistencia } from '../../services/asistencia.service';
import { EstudianteService, Estudiante } from '../../services/estudiante.service';

@Component({
  selector: 'app-asistencias',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './asistencias.component.html',
  styleUrl: './asistencias.component.css'
})
export class AsistenciasComponent implements OnInit {
  asistencias: Asistencia[] = [];
  estudiantes: Estudiante[] = [];
  selectedAsistencia: Asistencia | null = null;
  showForm = false;
  loading = false;
  
  formData: Asistencia = {
    estudiante_id: 0,
    fecha: '',
    estado: 'presente',
    observaciones: ''
  };

  constructor(
    private asistenciaService: AsistenciaService,
    private estudianteService: EstudianteService
  ) {}

  ngOnInit(): void {
    this.loadAsistencias();
    this.loadEstudiantes();
  }

  loadAsistencias(): void {
    this.loading = true;
    this.asistenciaService.obtenerAsistencias().subscribe({
      next: (data) => {
        this.asistencias = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar asistencias:', error);
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

  openForm(asistencia?: Asistencia): void {
    if (asistencia) {
      this.formData = { ...asistencia };
      this.selectedAsistencia = asistencia;
    } else {
      this.resetForm();
    }
    this.showForm = true;
  }

  resetForm(): void {
    this.formData = {
      estudiante_id: 0,
      fecha: '',
      estado: 'presente',
      observaciones: ''
    };
    this.selectedAsistencia = null;
  }

  closeForm(): void {
    this.showForm = false;
    this.resetForm();
  }

  saveAsistencia(): void {
    if (this.selectedAsistencia?.id) {
      this.asistenciaService.actualizarAsistencia(this.selectedAsistencia.id, this.formData).subscribe({
        next: () => {
          this.loadAsistencias();
          this.closeForm();
        },
        error: (error) => console.error('Error al actualizar:', error)
      });
    } else {
      this.asistenciaService.crearAsistencia(this.formData).subscribe({
        next: () => {
          this.loadAsistencias();
          this.closeForm();
        },
        error: (error) => console.error('Error al crear:', error)
      });
    }
  }

  deleteAsistencia(id: number): void {
    if (confirm('¿Estás seguro de que deseas eliminar este registro de asistencia?')) {
      this.asistenciaService.eliminarAsistencia(id).subscribe({
        next: () => this.loadAsistencias(),
        error: (error) => console.error('Error al eliminar:', error)
      });
    }
  }

  getEstadoClass(estado: string): string {
    switch(estado) {
      case 'presente':
        return 'badge bg-success';
      case 'ausente':
        return 'badge bg-danger';
      case 'justificado':
        return 'badge bg-warning';
      default:
        return 'badge bg-secondary';
    }
  }
}
