package com.colegio.estudianteservice.service;

import com.colegio.estudianteservice.event.AsistenciaEvent;
import com.colegio.estudianteservice.event.EstudianteCreadoEvent;
import com.colegio.estudianteservice.event.NotaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AnaliticaAcademicaService {

    private static final Logger log = LoggerFactory.getLogger(AnaliticaAcademicaService.class);

    private final Map<Long, List<Double>> notasPorEstudiante = new ConcurrentHashMap<>();
    private final Map<Long, AtomicLong> presentesPorEstudiante = new ConcurrentHashMap<>();
    private final Map<Long, AtomicLong> totalAsistenciasPorEstudiante = new ConcurrentHashMap<>();
    private final Set<Long> estudiantesRegistrados = ConcurrentHashMap.newKeySet();

    public void registrarEstudiante(EstudianteCreadoEvent evento) {
        if (evento == null || evento.getId() == null) {
            log.warn("Evento de estudiante inválido para analítica: {}", evento);
            return;
        }

        estudiantesRegistrados.add(evento.getId());
    }

    public void registrarNota(NotaEvent evento) {
        if (evento == null || evento.getEstudianteId() == null || evento.getNota() == null) {
            log.warn("Nota inválida para analítica: {}", evento);
            return;
        }

        notasPorEstudiante
                .computeIfAbsent(evento.getEstudianteId(), key -> new ArrayList<>())
                .add(evento.getNota());
        log.debug("Promedio analítico actualizado para estudiante {} con nota {}", evento.getEstudianteId(), evento.getNota());
    }

    public void registrarAsistencia(AsistenciaEvent evento) {
        if (evento == null || evento.getEstudianteId() == null) {
            log.warn("Asistencia inválida para analítica: {}", evento);
            return;
        }

        totalAsistenciasPorEstudiante.computeIfAbsent(evento.getEstudianteId(), key -> new AtomicLong()).incrementAndGet();
        if ("PRESENTE".equalsIgnoreCase(evento.getEstado())) {
            presentesPorEstudiante.computeIfAbsent(evento.getEstudianteId(), key -> new AtomicLong()).incrementAndGet();
        }
        log.debug("Asistencia analítica actualizada para estudiante {} con estado {}", evento.getEstudianteId(), evento.getEstado());
    }

    public Map<String, Object> obtenerResumen() {
        double promedioNotas = notasPorEstudiante.values().stream()
                .flatMap(List::stream)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0d);

        long totalRegistrosAsistencia = totalAsistenciasPorEstudiante.values().stream()
                .mapToLong(AtomicLong::get)
                .sum();
        long totalPresentes = presentesPorEstudiante.values().stream()
                .mapToLong(AtomicLong::get)
                .sum();
        double tasaAsistencia = totalRegistrosAsistencia > 0 ? (double) totalPresentes / totalRegistrosAsistencia : 0.0d;

        Map<String, Object> resumen = new ConcurrentHashMap<>();
        resumen.put("promedioGeneralNotas", Math.round(promedioNotas * 100.0) / 100.0);
        resumen.put("tasaAsistenciaGlobal", Math.round(tasaAsistencia * 10000.0) / 10000.0);
        resumen.put("totalEventosNotas", notasPorEstudiante.values().stream().mapToLong(List::size).sum());
        resumen.put("totalEventosAsistencia", totalRegistrosAsistencia);
        resumen.put("totalEstudiantesRegistrados", estudiantesRegistrados.size());
        resumen.put("promedioPorEstudiante", construirPromediosPorEstudiante());
        return resumen;
    }

    private Map<Long, Double> construirPromediosPorEstudiante() {
        Map<Long, Double> promedioPorEstudiante = new ConcurrentHashMap<>();
        notasPorEstudiante.forEach((estudianteId, notas) -> {
            double promedio = notas.stream().mapToDouble(Double::doubleValue).average().orElse(0.0d);
            promedioPorEstudiante.put(estudianteId, Math.round(promedio * 100.0) / 100.0);
        });
        return promedioPorEstudiante;
    }
}
