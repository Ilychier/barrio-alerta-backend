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
import com.alertabarrio.ingsoft.models.dtos.CuadranteResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CuadranteSaveDTO;
import com.alertabarrio.ingsoft.services.CuadranteService;
import com.alertabarrio.ingsoft.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CuadranteController.class)
class CuadranteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CuadranteService cuadranteService;

    @MockitoBean
    private JwtService jwtService;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        when(jwtService.validateTokenAndGetEmail(any()))
            .thenReturn("test@test.com");
    }

    private static final String AUTH_HEADER = "Bearer token";

    @Test
    void shouldCreateCuadranteSuccessfully() throws Exception {

        CuadranteSaveDTO dto =
            new CuadranteSaveDTO("CAI Norte", "3001234567");

        CuadranteResponseDTO response =
            new CuadranteResponseDTO(
                1L,
                "CAI Norte",
                "3001234567"
            );

        when(cuadranteService.save(any(CuadranteSaveDTO.class)))
            .thenReturn(response);

        mockMvc.perform(
                post("/api/cuadrantes")
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenNombreUnidadIsBlank() throws Exception {

        String json = """
            {
                "nombreUnidad":"",
                "telefonoEmergencia":"3001234567"
            }
            """;

        mockMvc.perform(
                post("/api/cuadrantes")
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetCuadranteByIdSuccessfully() throws Exception {

        CuadranteResponseDTO response =
            new CuadranteResponseDTO(
                1L,
                "CAI Norte",
                "3001234567"
            );

        when(cuadranteService.findById(1L))
            .thenReturn(response);

        mockMvc.perform(
                get("/api/cuadrantes/1")
                        .header("Authorization", AUTH_HEADER)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenCuadranteDoesNotExist() throws Exception {

        when(cuadranteService.findById(999L))
            .thenThrow(new ResourceNotFoundException("Cuadrante", 999L));

        mockMvc.perform(
                get("/api/cuadrantes/999")
                        .header("Authorization", AUTH_HEADER)
        )
        .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateCuadranteSuccessfully() throws Exception {

        CuadranteSaveDTO dto =
            new CuadranteSaveDTO("CAI Sur", "3007654321");

        CuadranteResponseDTO response =
            new CuadranteResponseDTO(
                1L,
                "CAI Sur",
                "3007654321"
            );

        when(cuadranteService.update(any(), any()))
            .thenReturn(response);

        mockMvc.perform(
                put("/api/cuadrantes/1")
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldPatchCuadranteSuccessfully() throws Exception {

        String json = """
            {
                "telefonoEmergencia":"3009999999"
            }
            """;

        when(cuadranteService.patch(any(), any()))
            .thenReturn(
                new CuadranteResponseDTO(
                    1L,
                    "CAI Norte",
                    "3009999999"
                )
            );

        mockMvc.perform(
                patch("/api/cuadrantes/1")
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteCuadranteSuccessfully() throws Exception {

        doNothing()
            .when(cuadranteService)
            .delete(1L);

        mockMvc.perform(
                delete("/api/cuadrantes/1")
                        .header("Authorization", AUTH_HEADER)
        )
        .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingCuadrante() throws Exception {

        doThrow(new ResourceNotFoundException("Cuadrante", 999L))
            .when(cuadranteService)
            .delete(999L);

        mockMvc.perform(
                delete("/api/cuadrantes/999")
                        .header("Authorization", AUTH_HEADER)
        )
        .andExpect(status().isNotFound());
    }
}