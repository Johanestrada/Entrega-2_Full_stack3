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
  errorMessage = '';
  
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
    this.errorMessage = '';
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
    this.errorMessage = '';
  }

  saveEstudiante(): void {
    if (!this.formData.nombre?.trim() || !(this.formData.run ?? this.formData.rut)?.trim() || !(this.formData.curso ?? this.formData.matricula)?.trim()) {
      this.errorMessage = 'Completa nombre, RUN y curso antes de guardar.';
      return;
    }

    this.errorMessage = '';
    if (this.selectedEstudiante?.id) {
      this.estudianteService.actualizarEstudiante(this.selectedEstudiante.id, this.formData).subscribe({
        next: () => {
          this.loadEstudiantes();
          this.closeForm();
        },
        error: () => this.errorMessage = 'No se pudo actualizar el estudiante. Comprueba que la API esté disponible.'
      });
    } else {
      this.estudianteService.crearEstudiante(this.formData).subscribe({
        next: () => {
          this.loadEstudiantes();
          this.closeForm();
        },
        error: () => this.errorMessage = 'No se pudo crear el estudiante. Comprueba que la API esté disponible.'
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
