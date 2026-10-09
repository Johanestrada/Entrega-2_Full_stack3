package com.colegio.asistenciaservice;

import com.colegio.asistenciaservice.event.AsistenciaRegistradaEvent;
import com.colegio.asistenciaservice.model.Asistencia;
import com.colegio.asistenciaservice.service.AsistenciaEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentCaptor.forClass;

class AsistenciaEventPublisherTest {

    @Test
    void publicarAsistenciaRegistrada_publicaMensajePersistenteEnExchangeYRutaConfigurados() throws Exception {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        AsistenciaEventPublisher publisher = new AsistenciaEventPublisher(
                rabbitTemplate, "eventos.exchange", "evento.asistencia.registrada");
        Asistencia asistencia = new Asistencia(22L, 7L, "2026-10-08", true);
        var eventCaptor = forClass(AsistenciaRegistradaEvent.class);
        var postProcessorCaptor = forClass(MessagePostProcessor.class);

        publisher.publicarAsistenciaRegistrada(asistencia);

        verify(rabbitTemplate).convertAndSend(
                eq("eventos.exchange"),
                eq("evento.asistencia.registrada"),
                eventCaptor.capture(),
                postProcessorCaptor.capture());

        AsistenciaRegistradaEvent evento = eventCaptor.getValue();
        assertNotNull(evento.getId());
        assertEquals("asistencia.registrada", evento.getTipo());
        assertEquals("asistencia-service", evento.getFuente());
        assertEquals(22L, evento.getAsistenciaId());
        assertEquals(7L, evento.getEstudianteId());
        assertEquals("PRESENTE", evento.getEstado());

        Message message = new Message(new byte[0], new MessageProperties());
        postProcessorCaptor.getValue().postProcessMessage(message);
        assertEquals(MessageDeliveryMode.PERSISTENT, message.getMessageProperties().getDeliveryMode());
    }
}
