package com.colegio.asistenciaservice;

import com.colegio.asistenciaservice.model.Asistencia;
import com.colegio.asistenciaservice.repository.AsistenciaRepository;
import com.colegio.asistenciaservice.service.AsistenciaEventPublisher;
import com.colegio.asistenciaservice.service.AsistenciaKafkaProducer;
import com.colegio.asistenciaservice.service.AsistenciaService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.kafka.KafkaException;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AsistenciaServiceTest {

    @Test
    void guardar_intentaPublicarEnKafkaAunqueFalleRabbit() {
        AsistenciaRepository repository = mock(AsistenciaRepository.class);
        AsistenciaEventPublisher rabbitPublisher = mock(AsistenciaEventPublisher.class);
        AsistenciaKafkaProducer kafkaProducer = mock(AsistenciaKafkaProducer.class);
        AsistenciaService service = new AsistenciaService(repository, rabbitPublisher, kafkaProducer);
        Asistencia asistencia = new Asistencia(1L, 7L, "2026-10-08", true);
        when(repository.save(asistencia)).thenReturn(asistencia);
        doThrow(new AmqpException("RabbitMQ no disponible"))
                .when(rabbitPublisher).publicarAsistenciaRegistrada(asistencia);

        assertSame(asistencia, service.guardar(asistencia));

        verify(rabbitPublisher).publicarAsistenciaRegistrada(asistencia);
        verify(kafkaProducer).publicarAsistencia(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void guardar_conservaLaPublicacionRabbitAunqueFalleKafka() {
        AsistenciaRepository repository = mock(AsistenciaRepository.class);
        AsistenciaEventPublisher rabbitPublisher = mock(AsistenciaEventPublisher.class);
        AsistenciaKafkaProducer kafkaProducer = mock(AsistenciaKafkaProducer.class);
        AsistenciaService service = new AsistenciaService(repository, rabbitPublisher, kafkaProducer);
        Asistencia asistencia = new Asistencia(1L, 7L, "2026-10-08", true);
        when(repository.save(asistencia)).thenReturn(asistencia);
        doThrow(new KafkaException("Kafka no disponible"))
                .when(kafkaProducer).publicarAsistencia(org.mockito.ArgumentMatchers.any());

        assertSame(asistencia, service.guardar(asistencia));

        verify(rabbitPublisher).publicarAsistenciaRegistrada(asistencia);
        verify(kafkaProducer).publicarAsistencia(org.mockito.ArgumentMatchers.any());
    }
}
