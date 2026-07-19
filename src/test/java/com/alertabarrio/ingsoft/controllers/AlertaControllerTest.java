package com.alertabarrio.ingsoft.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.alertabarrio.ingsoft.config.AuthInterceptor;
import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.AlertaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.AlertaSaveDTO;
import com.alertabarrio.ingsoft.models.dtos.CategoriaResponseDTO;
import com.alertabarrio.ingsoft.services.AlertaService;
import com.alertabarrio.ingsoft.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebMvcTest(AlertaController.class)
class AlertaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlertaService alertaService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthInterceptor authInterceptor;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() throws Exception {
        when(authInterceptor.preHandle(
                any(HttpServletRequest.class),
                any(HttpServletResponse.class),
                any()))
            .thenReturn(true);
    }

    @Test
    void shouldCreateAlertaSuccessfully() throws Exception {
        AlertaSaveDTO dto = new AlertaSaveDTO("Descripción", true, 1L, 2L);

        AlertaResponseDTO response = new AlertaResponseDTO(
            10L,
            "Descripción",
            true,
            LocalDateTime.now(),
            1L,
            new CategoriaResponseDTO(2L, "Seguridad", "icono")
        );

        when(alertaService.save(any(AlertaSaveDTO.class))).thenReturn(response);

        mockMvc.perform(
            post("/api/alertas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenDescriptionIsBlank() throws Exception {
        String invalidJson = """
            {
              "descripcion": "",
              "esSos": true,
              "usuarioId": 1,
              "categoriaId": 2
            }
            """;

        mockMvc.perform(
            post("/api/alertas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAlertaByIdSuccessfully() throws Exception {
        AlertaResponseDTO response = new AlertaResponseDTO(
            10L,
            "Descripción",
            true,
            LocalDateTime.now(),
            1L,
            new CategoriaResponseDTO(2L, "Seguridad", "icono")
        );

        when(alertaService.findById(10L)).thenReturn(response);

        mockMvc.perform(get("/api/alertas/10"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenAlertaDoesNotExist() throws Exception {
        when(alertaService.findById(999L))
            .thenThrow(new ResourceNotFoundException("Alerta", 999L));

        mockMvc.perform(get("/api/alertas/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteAlertaSuccessfully() throws Exception {
        doNothing().when(alertaService).delete(10L);

        mockMvc.perform(delete("/api/alertas/10"))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingAlerta() throws Exception {
        doThrow(new ResourceNotFoundException("Alerta", 999L))
            .when(alertaService)
            .delete(999L);

        mockMvc.perform(delete("/api/alertas/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateAlertaSuccessfully() throws Exception {

        AlertaSaveDTO dto =
                new AlertaSaveDTO(
                        "Descripción actualizada",
                        true,
                        1L,
                        2L
                );

        AlertaResponseDTO response =
                new AlertaResponseDTO(
                        10L,
                        "Descripción actualizada",
                        true,
                        LocalDateTime.now(),
                        1L,
                        new CategoriaResponseDTO(
                                2L,
                                "Seguridad",
                                "icono"
                        )
                );

        when(alertaService.update(any(Long.class), any(AlertaSaveDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/alertas/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingAlerta() throws Exception {

        AlertaSaveDTO dto =
                new AlertaSaveDTO(
                        "Descripción",
                        true,
                        1L,
                        2L
                );

        when(alertaService.update(any(Long.class), any(AlertaSaveDTO.class)))
                .thenThrow(new ResourceNotFoundException("Alerta", 999L));

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/alertas/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isNotFound());
    }

    @Test
    void shouldPatchAlertaSuccessfully() throws Exception {

        AlertaSaveDTO dto =
                new AlertaSaveDTO(
                        "Nueva descripción",
                        null,
                        null,
                        null
                );

        AlertaResponseDTO response =
                new AlertaResponseDTO(
                        10L,
                        "Nueva descripción",
                        true,
                        LocalDateTime.now(),
                        1L,
                        new CategoriaResponseDTO(
                                2L,
                                "Seguridad",
                                "icono"
                        )
                );

        when(alertaService.patch(any(Long.class), any(AlertaSaveDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/alertas/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenPatchingNonExistingAlerta() throws Exception {

        AlertaSaveDTO dto =
                new AlertaSaveDTO(
                        "Nueva descripción",
                        null,
                        null,
                        null
                );

        when(alertaService.patch(any(Long.class), any(AlertaSaveDTO.class)))
                .thenThrow(new ResourceNotFoundException("Alerta", 999L));

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/alertas/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAlertasPageSuccessfully() throws Exception {

        org.springframework.data.domain.Page<AlertaResponseDTO> page =
                new org.springframework.data.domain.PageImpl<>(
                        java.util.List.of(
                                new AlertaResponseDTO(
                                        10L,
                                        "Descripción",
                                        true,
                                        LocalDateTime.now(),
                                        1L,
                                        null
                                )
                        )
                );

        when(alertaService.findByFecha(
                any(java.time.LocalDate.class),
                any(org.springframework.data.domain.Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(
                get("/api/alertas")
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldFilterAlertasByFecha() throws Exception {

        org.springframework.data.domain.Page<AlertaResponseDTO> page =
                new org.springframework.data.domain.PageImpl<>(
                        java.util.List.of()
                );

        when(alertaService.findByFecha(
                any(java.time.LocalDate.class),
                any(org.springframework.data.domain.Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(
                get("/api/alertas")
                        .param("fecha", "2025-01-15")
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldFilterAlertasByBarrioAndFecha() throws Exception {

        org.springframework.data.domain.Page<AlertaResponseDTO> page =
                new org.springframework.data.domain.PageImpl<>(
                        java.util.List.of()
                );

        when(alertaService.findByBarrioAndFecha(
                any(Long.class),
                any(java.time.LocalDate.class),
                any(org.springframework.data.domain.Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(
                get("/api/alertas")
                        .param("fecha", "2025-01-15")
                        .param("barrioId", "5")
        )
        .andExpect(status().isOk());
    }
}