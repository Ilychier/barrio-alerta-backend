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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.AlertaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.AlertaSaveDTO;
import com.alertabarrio.ingsoft.models.dtos.CategoriaResponseDTO;
import com.alertabarrio.ingsoft.services.AlertaService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AlertaController.class)
class AlertaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlertaService alertaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
}