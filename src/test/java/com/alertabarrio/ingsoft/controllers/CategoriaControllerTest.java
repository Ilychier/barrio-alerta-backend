package com.alertabarrio.ingsoft.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.CategoriaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CategoriaSaveDTO;
import com.alertabarrio.ingsoft.services.CategoriaService;
import com.alertabarrio.ingsoft.services.JwtService;

import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    @MockitoBean
    private JwtService jwtService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        when(jwtService.validateTokenAndGetEmail(anyString()))
            .thenReturn("test@test.com");
    }

    @Test
    void shouldCreateCategoriaSuccessfully() throws Exception {

        CategoriaSaveDTO dto =
            new CategoriaSaveDTO(
                "Seguridad",
                "icono-seguridad"
            );

        CategoriaResponseDTO response =
            new CategoriaResponseDTO(
                1L,
                "Seguridad",
                "icono-seguridad"
            );

        when(categoriaService.save(any(CategoriaSaveDTO.class)))
            .thenReturn(response);

        mockMvc.perform(
                post("/api/categorias")
                    .header("Authorization", "Bearer token-test")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto))
            )
            .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenNombreIsBlank() throws Exception {

        String json = """
            {
                "nombre":"",
                "iconoReferencia":"icono"
            }
            """;

        mockMvc.perform(
                post("/api/categorias")
                    .header("Authorization", "Bearer token-test")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenIconoIsBlank() throws Exception {

        String json = """
            {
                "nombre":"Seguridad",
                "iconoReferencia":""
            }
            """;

        mockMvc.perform(
                post("/api/categorias")
                    .header("Authorization", "Bearer token-test")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetCategoriaByIdSuccessfully() throws Exception {

        CategoriaResponseDTO response =
            new CategoriaResponseDTO(
                1L,
                "Seguridad",
                "icono"
            );

        when(categoriaService.findById(1L))
            .thenReturn(response);

        mockMvc.perform(
                get("/api/categorias/1")
                    .header("Authorization", "Bearer token-test")
            )
            .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenCategoriaDoesNotExist() throws Exception {

        when(categoriaService.findById(999L))
            .thenThrow(
                new ResourceNotFoundException(
                    "Categoria",
                    999L
                )
            );

        mockMvc.perform(
                get("/api/categorias/999")
                    .header("Authorization", "Bearer token-test")
            )
            .andExpect(status().isNotFound());
    }
}
