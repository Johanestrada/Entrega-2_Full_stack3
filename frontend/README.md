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

## Patrones de diseño aplicados

- `Facade pattern` en `src/services/academicApi.js` para centralizar el acceso al BFF.
- `Custom Hook` en `src/hooks/useAcademicData.js` para encapsular la lógica de carga y estado.
- `Container / Presentational` en `src/App.jsx` junto con `src/components/SearchForm.jsx` y `src/components/AcademicCards.jsx` para separar la interfaz de usuario de la lógica.

## Pruebas

- Se agregó un test de componentes en `src/components/SearchForm.test.jsx`.
- Ejecuta las pruebas con:

```bash
npm install
npm test
```
