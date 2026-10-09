package com.colegio.evaluacionService;

import com.colegio.evaluacionService.event.EvaluacionCalificadaEvent;
import com.colegio.evaluacionService.model.Evaluacion;
import com.colegio.evaluacionService.service.EvaluacionEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentCaptor.forClass;

class EvaluacionEventPublisherTest {

    @Test
    void publicarEvaluacionCalificada_publicaMensajePersistenteEnExchangeYRutaConfigurados() throws Exception {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        EvaluacionEventPublisher publisher = new EvaluacionEventPublisher(
                rabbitTemplate, "eventos.exchange", "evento.evaluacion.calificada");
        Evaluacion evaluacion = new Evaluacion(7L, "Matemáticas", 6.5);
        evaluacion.setId(31L);
        var eventCaptor = forClass(EvaluacionCalificadaEvent.class);
        var postProcessorCaptor = forClass(MessagePostProcessor.class);

        publisher.publicarEvaluacionCalificada(evaluacion);

        verify(rabbitTemplate).convertAndSend(
                eq("eventos.exchange"),
                eq("evento.evaluacion.calificada"),
                eventCaptor.capture(),
                postProcessorCaptor.capture());

        EvaluacionCalificadaEvent evento = eventCaptor.getValue();
        assertNotNull(evento.getId());
        assertEquals("evaluacion.calificada", evento.getTipo());
        assertEquals("evaluacion-service", evento.getFuente());
        assertEquals(31L, evento.getEvaluacionId());
        assertEquals(7L, evento.getEstudianteId());
        assertEquals(6.5, evento.getCalificacion());
        assertEquals("Matemáticas", evento.getMateria());

        Message message = new Message(new byte[0], new MessageProperties());
        postProcessorCaptor.getValue().postProcessMessage(message);
        assertEquals(MessageDeliveryMode.PERSISTENT, message.getMessageProperties().getDeliveryMode());
    }
}
