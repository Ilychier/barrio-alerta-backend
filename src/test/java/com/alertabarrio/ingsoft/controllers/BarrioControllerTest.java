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
import com.alertabarrio.ingsoft.models.dtos.BarrioResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.BarrioSaveDTO;
import com.alertabarrio.ingsoft.models.dtos.CuadranteResponseDTO;
import com.alertabarrio.ingsoft.services.BarrioService;
import com.alertabarrio.ingsoft.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BarrioController.class)
class BarrioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BarrioService barrioService;

    @MockitoBean
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        when(jwtService.validateTokenAndGetEmail("token"))
                .thenReturn("test@test.com");
    }

    @Test
    void shouldCreateBarrioSuccessfully() throws Exception {

        BarrioSaveDTO dto =
                new BarrioSaveDTO("Centro", 1L);

        BarrioResponseDTO response =
                new BarrioResponseDTO(
                        1L,
                        "Centro",
                        new CuadranteResponseDTO(
                                1L,
                                "Unidad Centro",
                                "123456789"));

        when(barrioService.save(any(BarrioSaveDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                post("/api/barrios")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenNombreIsBlank() throws Exception {

        String invalidJson = """
            {
              "nombre": "",
              "cuadranteId": 1
            }
            """;

        mockMvc.perform(
                post("/api/barrios")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBarrioByIdSuccessfully() throws Exception {

        when(barrioService.findById(1L))
                .thenReturn(
                        new BarrioResponseDTO(
                                1L,
                                "Centro",
                                new CuadranteResponseDTO(
                                        1L,
                                        "Unidad Centro",
                                        "123456789")));

        mockMvc.perform(
                get("/api/barrios/1")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenBarrioDoesNotExist() throws Exception {

        when(barrioService.findById(999L))
                .thenThrow(new ResourceNotFoundException("Barrio", 999L));

        mockMvc.perform(
                get("/api/barrios/999")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateBarrioSuccessfully() throws Exception {

        BarrioSaveDTO dto =
                new BarrioSaveDTO("Nuevo Barrio", 1L);

        when(barrioService.update(any(Long.class), any(BarrioSaveDTO.class)))
                .thenReturn(
                        new BarrioResponseDTO(
                                1L,
                                "Nuevo Barrio",
                                new CuadranteResponseDTO(
                                        1L,
                                        "Unidad Centro",
                                        "123456789")));

        mockMvc.perform(
                put("/api/barrios/1")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldPatchBarrioSuccessfully() throws Exception {

        String json = """
            {
              "nombre":"Barrio Actualizado"
            }
            """;

        when(barrioService.patch(any(Long.class), any(BarrioSaveDTO.class)))
                .thenReturn(
                        new BarrioResponseDTO(
                                1L,
                                "Barrio Actualizado",
                                null));

        mockMvc.perform(
                patch("/api/barrios/1")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteBarrioSuccessfully() throws Exception {

        doNothing().when(barrioService).delete(1L);

        mockMvc.perform(
                delete("/api/barrios/1")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingBarrio() throws Exception {

        doThrow(new ResourceNotFoundException("Barrio", 999L))
                .when(barrioService)
                .delete(999L);

        mockMvc.perform(
                delete("/api/barrios/999")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isNotFound());
    }
}