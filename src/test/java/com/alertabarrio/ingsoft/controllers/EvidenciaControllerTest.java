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

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.EvidenciaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.EvidenciaSaveDTO;
import com.alertabarrio.ingsoft.services.EvidenciaService;
import com.alertabarrio.ingsoft.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = EvidenciaController.class)
class EvidenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EvidenciaService evidenciaService;

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

        EvidenciaSaveDTO dto =
            new EvidenciaSaveDTO(
                "archivo.jpg",
                1L
            );

        EvidenciaResponseDTO response =
            new EvidenciaResponseDTO(
                1L,
                "archivo.jpg",
                LocalDateTime.now(),
                1L
            );

        when(evidenciaService.save(any()))
            .thenReturn(response);

        mockMvc.perform(
                post("/api/evidencias")
                    .header("Authorization", AUTH_HEADER)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenArchivoUrlIsBlank() throws Exception {

        String json = """
            {
                "archivoUrl":"",
                "alertaId":1
            }
            """;

        mockMvc.perform(
                post("/api/evidencias")
                    .header("Authorization", AUTH_HEADER)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetByIdSuccessfully() throws Exception {

        when(evidenciaService.findById(1L))
            .thenReturn(
                new EvidenciaResponseDTO(
                    1L,
                    "archivo.jpg",
                    LocalDateTime.now(),
                    1L
                )
            );

        mockMvc.perform(
                get("/api/evidencias/1")
                    .header("Authorization", AUTH_HEADER)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenEvidenciaDoesNotExist() throws Exception {

        when(evidenciaService.findById(999L))
            .thenThrow(
                new ResourceNotFoundException(
                    "Evidencia",
                    999L
                )
            );

        mockMvc.perform(
                get("/api/evidencias/999")
                    .header("Authorization", AUTH_HEADER)
        )
        .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateSuccessfully() throws Exception {

        EvidenciaSaveDTO dto =
            new EvidenciaSaveDTO(
                "nuevo.jpg",
                1L
            );

        when(evidenciaService.update(any(), any()))
            .thenReturn(
                new EvidenciaResponseDTO(
                    1L,
                    "nuevo.jpg",
                    LocalDateTime.now(),
                    1L
                )
            );

        mockMvc.perform(
                put("/api/evidencias/1")
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
                "archivoUrl":"patch.jpg"
            }
            """;

        when(evidenciaService.patch(any(), any()))
            .thenReturn(
                new EvidenciaResponseDTO(
                    1L,
                    "patch.jpg",
                    LocalDateTime.now(),
                    1L
                )
            );

        mockMvc.perform(
                patch("/api/evidencias/1")
                    .header("Authorization", AUTH_HEADER)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteSuccessfully() throws Exception {

        doNothing()
            .when(evidenciaService)
            .delete(1L);

        mockMvc.perform(
                delete("/api/evidencias/1")
                    .header("Authorization", AUTH_HEADER)
        )
        .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingEvidencia() throws Exception {

        doThrow(
            new ResourceNotFoundException(
                "Evidencia",
                999L
            )
        )
        .when(evidenciaService)
        .delete(999L);

        mockMvc.perform(
                delete("/api/evidencias/999")
                    .header("Authorization", AUTH_HEADER)
        )
        .andExpect(status().isNotFound());
    }
}