package com.colegio.bffservice;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

@ActiveProfiles("test")
public abstract class AbstractMockWebServerTest {

    protected MockWebServer mockWebServer;

    @BeforeEach
    void startMockWebServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.setDispatcher(createDispatcher());
        mockWebServer.start(8081);
    }

    @AfterEach
    void shutdownMockWebServer() throws IOException {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    private Dispatcher createDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                String path = request.getPath();
                String method = request.getMethod();

                if (path == null) {
                    return new MockResponse().setResponseCode(404);
                }

                if ("/estudiantes".equals(path) && "POST".equals(method)) {
                    return new MockResponse()
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json")
                            .setBody("{\"id\":999,\"run\":\"99.999.999-9\",\"nombre\":\"Test Usuario\",\"curso\":\"5-C\"}");
                }

                if (path.matches("/estudiantes/\\d+")) {
                    return new MockResponse()
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json")
                            .setBody("{\"id\":1,\"run\":\"20.111.222-3\",\"nombre\":\"Juan Perez\",\"curso\":\"1-A\"}");
                }

                if (path.matches("/estudiantes/run/.*")) {
                    return new MockResponse()
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json")
                            .setBody("{\"id\":1,\"run\":\"20.111.222-3\",\"nombre\":\"Juan Perez\",\"curso\":\"1-A\"}");
                }

                if (path.matches("/estudiantes/curso/.*")) {
                    return new MockResponse()
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json")
                            .setBody("[{\"id\":1,\"run\":\"20.111.222-3\",\"nombre\":\"Juan Perez\",\"curso\":\"1-A\"}]");
                }

                if (path.matches("/asistencias/estudiante/\\d+")) {
                    return new MockResponse()
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json")
                            .setBody("[]");
                }

                if ("/asistencias".equals(path) && "POST".equals(method)) {
                    return new MockResponse()
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json")
                            .setBody("{\"status\":\"creado\"}");
                }

                if ("/evaluaciones".equals(path) && "POST".equals(method)) {
                    return new MockResponse()
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json")
                            .setBody("{\"id\":100}");
                }

                if (path.matches("/evaluaciones/estudiante/\\d+")) {
                    return new MockResponse()
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json")
                            .setBody("[]");
                }

                if (path.matches("/evaluaciones/\\d+")) {
                    if ("PUT".equals(method)) {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setHeader("Content-Type", "application/json")
                                .setBody("{\"id\":1,\"materia\":\"Historia\",\"nota\":7.5}");
                    }
                    if ("DELETE".equals(method)) {
                        return new MockResponse().setResponseCode(204);
                    }
                }

                return new MockResponse().setResponseCode(404);
            }
        };
    }
}
