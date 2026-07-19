package com.alertabarrio.ingsoft.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.ConfiguracionResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.ConfiguracionSaveDTO;
import com.alertabarrio.ingsoft.services.ConfiguracionService;
import com.alertabarrio.ingsoft.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ConfiguracionController.class)
class ConfiguracionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConfiguracionService configuracionService;

    @MockitoBean
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String AUTH_HEADER = "Bearer token";

    @BeforeEach
    void setUp() {
        when(jwtService.validateTokenAndGetEmail(any()))
                .thenReturn("test@test.com");
    }

    @Test
    void shouldCreateSuccessfully() throws Exception {

        ConfiguracionSaveDTO dto =
                new ConfiguracionSaveDTO(
                        1L,
                        true,
                        false
                );

        when(configuracionService.save(any()))
                .thenReturn(
                        new ConfiguracionResponseDTO(
                                1L,
                                1L,
                                true,
                                false
                        )
                );

        mockMvc.perform(
                post("/api/configuraciones")
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenUsuarioIdIsNull() throws Exception {

        String json = """
            {
                "usuarioId": null,
                "recibirNotificaciones": true,
                "modoSilencioso": false
            }
            """;

        mockMvc.perform(
                post("/api/configuraciones")
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetByIdSuccessfully() throws Exception {

        when(configuracionService.findById(1L))
                .thenReturn(
                        new ConfiguracionResponseDTO(
                                1L,
                                1L,
                                true,
                                false
                        )
                );

        mockMvc.perform(
                get("/api/configuraciones/1")
                        .header("Authorization", AUTH_HEADER)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenConfigurationDoesNotExist() throws Exception {

        when(configuracionService.findById(999L))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Configuracion",
                                999L
                        )
                );

        mockMvc.perform(
                get("/api/configuraciones/999")
                        .header("Authorization", AUTH_HEADER)
        )
        .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateSuccessfully() throws Exception {

        ConfiguracionSaveDTO dto =
                new ConfiguracionSaveDTO(
                        1L,
                        false,
                        true
                );

        when(configuracionService.update(any(), any()))
                .thenReturn(
                        new ConfiguracionResponseDTO(
                                1L,
                                1L,
                                false,
                                true
                        )
                );

        mockMvc.perform(
                put("/api/configuraciones/1")
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldPatchSuccessfully() throws Exception {

        String json = """
            {
                "modoSilencioso": true
            }
            """;

        when(configuracionService.patch(any(), any()))
                .thenReturn(
                        new ConfiguracionResponseDTO(
                                1L,
                                1L,
                                true,
                                true
                        )
                );

        mockMvc.perform(
                patch("/api/configuraciones/1")
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteSuccessfully() throws Exception {

        doNothing()
                .when(configuracionService)
                .delete(1L);

        mockMvc.perform(
                delete("/api/configuraciones/1")
                        .header("Authorization", AUTH_HEADER)
        )
        .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingConfiguration() throws Exception {

        doThrow(
                new ResourceNotFoundException(
                        "Configuracion",
                        999L
                )
        )
        .when(configuracionService)
        .delete(999L);

        mockMvc.perform(
                delete("/api/configuraciones/999")
                        .header("Authorization", AUTH_HEADER)
        )
        .andExpect(status().isNotFound());
    }
}