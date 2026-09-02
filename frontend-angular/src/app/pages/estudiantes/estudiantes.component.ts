import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EstudianteService, Estudiante } from '../../services/estudiante.service';

@Component({
  selector: 'app-estudiantes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './estudiantes.component.html',
  styleUrl: './estudiantes.component.css'
})
export class EstudiantesComponent implements OnInit {
  estudiantes: Estudiante[] = [];
  selectedEstudiante: Estudiante | null = null;
  showForm = false;
  loading = false;
  
  formData: Estudiante = {
    nombre: '',
    apellido: '',
    email: '',
    matricula: '',
    rut: ''
  };

  constructor(private estudianteService: EstudianteService) {}

  ngOnInit(): void {
    this.loadEstudiantes();
  }

  loadEstudiantes(): void {
    this.loading = true;
    this.estudianteService.obtenerEstudiantes().subscribe({
      next: (data) => {
        this.estudiantes = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar estudiantes:', error);
        this.loading = false;
      }
    });
  }

  openForm(estudiante?: Estudiante): void {
    if (estudiante) {
      this.formData = { ...estudiante };
      this.selectedEstudiante = estudiante;
    } else {
      this.resetForm();
    }
    this.showForm = true;
  }

  resetForm(): void {
    this.formData = {
      nombre: '',
      apellido: '',
      email: '',
      matricula: '',
      rut: ''
    };
    this.selectedEstudiante = null;
  }

  closeForm(): void {
    this.showForm = false;
    this.resetForm();
  }

  saveEstudiante(): void {
    if (this.selectedEstudiante?.id) {
      this.estudianteService.actualizarEstudiante(this.selectedEstudiante.id, this.formData).subscribe({
        next: () => {
          this.loadEstudiantes();
          this.closeForm();
        },
        error: (error) => console.error('Error al actualizar:', error)
      });
    } else {
      this.estudianteService.crearEstudiante(this.formData).subscribe({
        next: () => {
          this.loadEstudiantes();
          this.closeForm();
        },
        error: (error) => console.error('Error al crear:', error)
      });
    }
  }

  deleteEstudiante(id: number): void {
    if (confirm('¿Estás seguro de que deseas eliminar este estudiante?')) {
      this.estudianteService.eliminarEstudiante(id).subscribe({
        next: () => this.loadEstudiantes(),
        error: (error) => console.error('Error al eliminar:', error)
      });
    }
  }
}
