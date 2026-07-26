package com.alertabarrio.adapters.rest.controller;

import com.alertabarrio.application.dto.CuadranteDTO;
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
@DisplayName("CuadranteController (API REST)")
class CuadranteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CrearCuadranteUseCase crearCuadranteUseCase;

    @Autowired
    private BuscarCuadranteUseCase buscarCuadranteUseCase;

    @Autowired
    private ActualizarCuadranteUseCase actualizarCuadranteUseCase;

    @Autowired
    private ParchearCuadranteUseCase parchearCuadranteUseCase;

    @Autowired
    private EliminarCuadranteUseCase eliminarCuadranteUseCase;

    @Autowired
    private ListarCuadrantesUseCase listarCuadrantesUseCase;

    @BeforeEach
    void setUp() {
        reset(crearCuadranteUseCase, buscarCuadranteUseCase, actualizarCuadranteUseCase,
              parchearCuadranteUseCase, eliminarCuadranteUseCase, listarCuadrantesUseCase);
    }

    @TestConfiguration
    static class MockConfig {
        @Bean @Primary CrearCuadranteUseCase mockCrear() { return mock(); }
        @Bean @Primary BuscarCuadranteUseCase mockBuscar() { return mock(); }
        @Bean @Primary ActualizarCuadranteUseCase mockActualizar() { return mock(); }
        @Bean @Primary ParchearCuadranteUseCase mockParchear() { return mock(); }
        @Bean @Primary EliminarCuadranteUseCase mockEliminar() { return mock(); }
        @Bean @Primary ListarCuadrantesUseCase mockListar() { return mock(); }
    }

    @Test
    @DisplayName("POST /api/cuadrantes → 201 con body")
    void create_conBodyValido_devuelve201YCuadrante() throws Exception {
        when(crearCuadranteUseCase.execute(any())).thenReturn(new CuadranteDTO(1L, "Bomberos", "+573001234567"));

        mockMvc.perform(post("/api/cuadrantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombreUnidad\":\"Bomberos\",\"telefonoEmergencia\":\"+573001234567\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombreUnidad").value("Bomberos"))
                .andExpect(jsonPath("$.telefonoEmergencia").value("+573001234567"));
    }

    @Test
    @DisplayName("GET /api/cuadrantes/{id} existente → 200")
    void findById_existente_devuelve200() throws Exception {
        when(buscarCuadranteUseCase.execute(any())).thenReturn(Optional.of(new CuadranteDTO(1L, "Bomberos", "+573001234567")));

        mockMvc.perform(get("/api/cuadrantes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombreUnidad").value("Bomberos"));
    }

    @Test
    @DisplayName("GET /api/cuadrantes/{id} inexistente → 404")
    void findById_inexistente_devuelve404() throws Exception {
        when(buscarCuadranteUseCase.execute(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cuadrantes/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/cuadrantes/{id} → 200")
    void update_conBodyValido_devuelve200() throws Exception {
        when(actualizarCuadranteUseCase.execute(any())).thenReturn(new CuadranteDTO(1L, "Policía", "+573009876543"));

        mockMvc.perform(put("/api/cuadrantes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombreUnidad\":\"Policía\",\"telefonoEmergencia\":\"+573009876543\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreUnidad").value("Policía"))
                .andExpect(jsonPath("$.telefonoEmergencia").value("+573009876543"));
    }

    @Test
    @DisplayName("PATCH /api/cuadrantes/{id} → 200")
    void patch_conCamposParciales_devuelve200() throws Exception {
        when(parchearCuadranteUseCase.execute(any())).thenReturn(new CuadranteDTO(1L, "Bomberos Patch", "+573001234567"));

        mockMvc.perform(patch("/api/cuadrantes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombreUnidad\":\"Bomberos Patch\",\"telefonoEmergencia\":null}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreUnidad").value("Bomberos Patch"))
                .andExpect(jsonPath("$.telefonoEmergencia").value("+573001234567"));
    }

    @Test
    @DisplayName("DELETE /api/cuadrantes/{id} → 204")
    void delete_existente_devuelve204() throws Exception {
        mockMvc.perform(delete("/api/cuadrantes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/cuadrantes → 200 con página")
    void findAll_devuelvePagina200() throws Exception {
        Page<CuadranteDTO> page = new PageImpl<>(List.of(new CuadranteDTO(1L, "A", "+571111111111")), PageRequest.of(0, 1), 2);
        when(listarCuadrantesUseCase.execute(any())).thenReturn(page);

        mockMvc.perform(get("/api/cuadrantes?size=1&page=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombreUnidad").value("A"))
                .andExpect(jsonPath("$.totalElements").value(2));
    }
}
