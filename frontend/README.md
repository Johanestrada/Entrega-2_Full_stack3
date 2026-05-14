# Frontend React

Esta carpeta contiene el frontend en React para consumir el BFF del proyecto.

## Instalación

1. Abre una terminal en `frontend`
2. Ejecuta:
   ```bash
   npm install
   ```

## Ejecución en desarrollo

```bash
npm run dev
```

## Qué hace

- Solicita al BFF el endpoint `http://localhost:8084/academico/{estudianteId}`
- Muestra los datos del estudiante, las asistencias y las evaluaciones
- Se puede usar como base para expandir el frontend con más formularios y vistas
