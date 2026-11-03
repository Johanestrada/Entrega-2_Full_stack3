package com.colegio.evaluacionService;

import com.colegio.evaluacionService.model.Evaluacion;
import com.colegio.evaluacionService.repository.EvaluacionRepository;
import com.colegio.evaluacionService.service.EvaluacionEventPublisher;
import com.colegio.evaluacionService.service.EvaluacionService;
import com.colegio.evaluacionService.service.NotaKafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.kafka.KafkaException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EvaluacionServiceTest {

    @Test
    void guardar_intentaPublicarEnKafkaAunqueFalleRabbit() {
        EvaluacionRepository repository = mock(EvaluacionRepository.class);
        EvaluacionEventPublisher rabbitPublisher = mock(EvaluacionEventPublisher.class);
        NotaKafkaProducer kafkaProducer = mock(NotaKafkaProducer.class);
        EvaluacionService service = new EvaluacionService(rabbitPublisher, kafkaProducer);
        ReflectionTestUtils.setField(service, "repository", repository);
        Evaluacion evaluacion = new Evaluacion(7L, "Matemáticas", 6.5);
        evaluacion.setId(1L);
        when(repository.save(org.mockito.ArgumentMatchers.any(Evaluacion.class))).thenReturn(evaluacion);
        doThrow(new AmqpException("RabbitMQ no disponible"))
                .when(rabbitPublisher).publicarEvaluacionCalificada(evaluacion);

        assertSame(evaluacion, service.guardar("7", "Matemáticas", 6.5));

        verify(rabbitPublisher).publicarEvaluacionCalificada(evaluacion);
        verify(kafkaProducer).publicarNota(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void guardar_conservaLaPublicacionRabbitAunqueFalleKafka() {
        EvaluacionRepository repository = mock(EvaluacionRepository.class);
        EvaluacionEventPublisher rabbitPublisher = mock(EvaluacionEventPublisher.class);
        NotaKafkaProducer kafkaProducer = mock(NotaKafkaProducer.class);
        EvaluacionService service = new EvaluacionService(rabbitPublisher, kafkaProducer);
        ReflectionTestUtils.setField(service, "repository", repository);
        Evaluacion evaluacion = new Evaluacion(7L, "Matemáticas", 6.5);
        evaluacion.setId(1L);
        when(repository.save(org.mockito.ArgumentMatchers.any(Evaluacion.class))).thenReturn(evaluacion);
        doThrow(new KafkaException("Kafka no disponible"))
                .when(kafkaProducer).publicarNota(org.mockito.ArgumentMatchers.any());

        assertSame(evaluacion, service.guardar("7", "Matemáticas", 6.5));

        verify(rabbitPublisher).publicarEvaluacionCalificada(evaluacion);
        verify(kafkaProducer).publicarNota(org.mockito.ArgumentMatchers.any());
    }
}
