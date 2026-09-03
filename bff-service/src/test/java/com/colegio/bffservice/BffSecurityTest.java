package com.colegio.bffservice;

import com.colegio.bffservice.facade.AcademicoFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(com.colegio.bffservice.config.SecurityConfig.class)
@AutoConfigureMockMvc
class BffSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AcademicoFacade academicoFacade;

    @MockBean
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        when(academicoFacade.listarEstudiantes()).thenReturn(java.util.List.of());
    }

    @Test
    void endpointAcademicoSinTokenDebeResponder401() throws Exception {
        mockMvc.perform(get("/academico/estudiantes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void endpointAcademicoSinScopeDebeResponder403() throws Exception {
        mockMvc.perform(get("/academico/estudiantes")
                        .with(jwt().authorities(() -> "SCOPE_otro.permiso")))
                .andExpect(status().isForbidden());
    }

    @Test
    void endpointAcademicoConScopeDebeResponder200() throws Exception {
        mockMvc.perform(get("/academico/estudiantes")
                        .with(jwt().authorities(() -> "SCOPE_api.access")))
                .andExpect(status().isOk());
    }
}