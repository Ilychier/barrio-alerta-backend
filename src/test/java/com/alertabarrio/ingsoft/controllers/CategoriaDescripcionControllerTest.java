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

import com.alertabarrio.ingsoft.config.AuthInterceptor;
import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.CategoriaDescripcionResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CategoriaDescripcionSaveDTO;
import com.alertabarrio.ingsoft.services.CategoriaDescripcionService;
import com.alertabarrio.ingsoft.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CategoriaDescripcionController.class)
class CategoriaDescripcionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaDescripcionService categoriaDescripcionService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthInterceptor authInterceptor;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String AUTH_HEADER = "Bearer token";

    @BeforeEach
    void setUp() {
        when(jwtService.validateTokenAndGetEmail(any()))
            .thenReturn("test@test.com");
    }

    @Test
    void shouldCreateSuccessfully() throws Exception {

        CategoriaDescripcionSaveDTO dto =
            new CategoriaDescripcionSaveDTO(
                "Descripción",
                1L,
                "imagen.jpg"
            );

        CategoriaDescripcionResponseDTO response =
            new CategoriaDescripcionResponseDTO(
                1L,
                "Descripción",
                1L,
                "imagen.jpg"
            );

        when(categoriaDescripcionService.save(any()))
            .thenReturn(response);

        mockMvc.perform(
                post("/api/categoria-descripciones")
                    .header("Authorization", AUTH_HEADER)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto))
        )
        .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenDescripcionIsBlank() throws Exception {

        String json = """
            {
                "descripcion":"",
                "categoriaId":1
            }
            """;

        mockMvc.perform(
                post("/api/categoria-descripciones")
                    .header("Authorization", AUTH_HEADER)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetByIdSuccessfully() throws Exception {

        when(categoriaDescripcionService.findById(1L))
            .thenReturn(
                new CategoriaDescripcionResponseDTO(
                    1L,
                    "Descripción",
                    1L,
                    "imagen.jpg"
                )
            );

        mockMvc.perform(
                get("/api/categoria-descripciones/1")
                    .header("Authorization", AUTH_HEADER)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenCategoriaDescripcionDoesNotExist() throws Exception {

        when(categoriaDescripcionService.findById(999L))
            .thenThrow(
                new ResourceNotFoundException(
                    "CategoriaDescripcion",
                    999L
                )
            );

        mockMvc.perform(
                get("/api/categoria-descripciones/999")
                    .header("Authorization", AUTH_HEADER)
        )
        .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateSuccessfully() throws Exception {

        CategoriaDescripcionSaveDTO dto =
            new CategoriaDescripcionSaveDTO(
                "Actualizada",
                1L,
                "nueva.jpg"
            );

        when(categoriaDescripcionService.update(any(), any()))
            .thenReturn(
                new CategoriaDescripcionResponseDTO(
                    1L,
                    "Actualizada",
                    1L,
                    "nueva.jpg"
                )
            );

        mockMvc.perform(
                put("/api/categoria-descripciones/1")
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
                "imagenUrl":"patch.jpg"
            }
            """;

        when(categoriaDescripcionService.patch(any(), any()))
            .thenReturn(
                new CategoriaDescripcionResponseDTO(
                    1L,
                    "Descripción",
                    1L,
                    "patch.jpg"
                )
            );

        mockMvc.perform(
                patch("/api/categoria-descripciones/1")
                    .header("Authorization", AUTH_HEADER)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
        )
        .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteSuccessfully() throws Exception {

        doNothing()
            .when(categoriaDescripcionService)
            .delete(1L);

        mockMvc.perform(
                delete("/api/categoria-descripciones/1")
                    .header("Authorization", AUTH_HEADER)
        )
        .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingCategoriaDescripcion() throws Exception {

        doThrow(
            new ResourceNotFoundException(
                "CategoriaDescripcion",
                999L
            )
        )
        .when(categoriaDescripcionService)
        .delete(999L);

        mockMvc.perform(
                delete("/api/categoria-descripciones/999")
                    .header("Authorization", AUTH_HEADER)
        )
        .andExpect(status().isNotFound());
    }
}
