package com.alertabarrio.adapters.rest.controller;

import com.alertabarrio.application.dto.CategoriaDTO;
import com.alertabarrio.domain.port.in.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("CategoriaController (API REST)")
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CrearCategoriaUseCase crearCategoriaUseCase;

    @Autowired
    private BuscarCategoriaUseCase buscarCategoriaUseCase;

    @Autowired
    private ActualizarCategoriaUseCase actualizarCategoriaUseCase;

    @Autowired
    private ParchearCategoriaUseCase parchearCategoriaUseCase;

    @Autowired
    private EliminarCategoriaUseCase eliminarCategoriaUseCase;

    @Autowired
    private ListarCategoriasUseCase listarCategoriasUseCase;

    @BeforeEach
    void setUp() {
        reset(crearCategoriaUseCase, buscarCategoriaUseCase, actualizarCategoriaUseCase,
              parchearCategoriaUseCase, eliminarCategoriaUseCase, listarCategoriasUseCase);
    }

    @TestConfiguration
    static class MockConfig {
        @Bean @Primary CrearCategoriaUseCase mockCrear() { return mock(); }
        @Bean @Primary BuscarCategoriaUseCase mockBuscar() { return mock(); }
        @Bean @Primary ActualizarCategoriaUseCase mockActualizar() { return mock(); }
        @Bean @Primary ParchearCategoriaUseCase mockParchear() { return mock(); }
        @Bean @Primary EliminarCategoriaUseCase mockEliminar() { return mock(); }
        @Bean @Primary ListarCategoriasUseCase mockListar() { return mock(); }
    }

    @Test
    @DisplayName("POST /api/categorias → 201 con body")
    void create_conBodyValido_devuelve201YCategoria() throws Exception {
        when(crearCategoriaUseCase.execute(any())).thenReturn(new CategoriaDTO(1L, "Robo", "icon-robbery.png"));

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Robo\",\"iconoReferencia\":\"icon-robbery.png\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Robo"))
                .andExpect(jsonPath("$.iconoReferencia").value("icon-robbery.png"));
    }

    @Test
    @DisplayName("GET /api/categorias/{id} existente → 200")
    void findById_existente_devuelve200() throws Exception {
        when(buscarCategoriaUseCase.execute(any())).thenReturn(Optional.of(new CategoriaDTO(1L, "Robo", "icon.png")));

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Robo"));
    }

    @Test
    @DisplayName("GET /api/categorias/{id} inexistente → 404")
    void findById_inexistente_devuelve404() throws Exception {
        when(buscarCategoriaUseCase.execute(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categorias/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/categorias/{id} → 200")
    void update_conBodyValido_devuelve200() throws Exception {
        when(actualizarCategoriaUseCase.execute(any())).thenReturn(new CategoriaDTO(1L, "Robo Actualizado", "icon-new.png"));

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Robo Actualizado\",\"iconoReferencia\":\"icon-new.png\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Robo Actualizado"))
                .andExpect(jsonPath("$.iconoReferencia").value("icon-new.png"));
    }

    @Test
    @DisplayName("PATCH /api/categorias/{id} → 200")
    void patch_conCamposParciales_devuelve200() throws Exception {
        when(parchearCategoriaUseCase.execute(any())).thenReturn(new CategoriaDTO(1L, "Robo Patch", "icon.png"));

        mockMvc.perform(patch("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Robo Patch\",\"iconoReferencia\":null}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Robo Patch"))
                .andExpect(jsonPath("$.iconoReferencia").value("icon.png"));
    }

    @Test
    @DisplayName("DELETE /api/categorias/{id} → 204")
    void delete_existente_devuelve204() throws Exception {
        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/categorias → 200 con página")
    void findAll_devuelvePagina200() throws Exception {
        Page<CategoriaDTO> page = new PageImpl<>(List.of(new CategoriaDTO(1L, "A", "icon-a.png")), PageRequest.of(0, 1), 2);
        when(listarCategoriasUseCase.execute(any())).thenReturn(page);

        mockMvc.perform(get("/api/categorias?size=1&page=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombre").value("A"))
                .andExpect(jsonPath("$.totalElements").value(2));
    }
}
