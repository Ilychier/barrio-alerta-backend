package com.alertabarrio.ingsoft.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.EvidenciaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.EvidenciaSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Alerta;
import com.alertabarrio.ingsoft.models.entities.Barrio;
import com.alertabarrio.ingsoft.models.entities.Categoria;
import com.alertabarrio.ingsoft.models.entities.Cuadrante;
import com.alertabarrio.ingsoft.models.entities.Evidencia;
import com.alertabarrio.ingsoft.models.entities.User;
import com.alertabarrio.ingsoft.repositories.AlertaRepository;
import com.alertabarrio.ingsoft.repositories.BarrioRepository;
import com.alertabarrio.ingsoft.repositories.CategoriaRepository;
import com.alertabarrio.ingsoft.repositories.CuadranteRepository;
import com.alertabarrio.ingsoft.repositories.EvidenciaRepository;
import com.alertabarrio.ingsoft.repositories.UserRepository;
import com.alertabarrio.ingsoft.services.EvidenciaService;

@SpringBootTest
@Transactional
class EvidenciaIntegrationTest {

    @Autowired
    private EvidenciaService evidenciaService;

    @Autowired
    private EvidenciaRepository evidenciaRepository;

    @Autowired
    private AlertaRepository alertaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BarrioRepository barrioRepository;

    @Autowired
    private CuadranteRepository cuadranteRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void shouldCreateEvidenciaSuccessfully() {

        Alerta alerta = crearAlerta();

        EvidenciaResponseDTO created =
                evidenciaService.save(
                        new EvidenciaSaveDTO(
                                "archivo.jpg",
                                alerta.getId()
                        )
                );

        assertNotNull(created);
        assertNotNull(created.id());
        assertEquals("archivo.jpg", created.archivoUrl());
        assertEquals(alerta.getId(), created.alertaId());
    }

    @Test
    void shouldFindEvidenciaByIdSuccessfully() {

        Alerta alerta = crearAlerta();

        Evidencia evidencia = new Evidencia();
        evidencia.setArchivoUrl("archivo.jpg");
        evidencia.setFechaSubida(LocalDateTime.now());
        evidencia.setAlerta(alerta);

        evidencia = evidenciaRepository.save(evidencia);

        EvidenciaResponseDTO found =
                evidenciaService.findById(evidencia.getId());

        assertEquals(evidencia.getId(), found.id());
        assertEquals("archivo.jpg", found.archivoUrl());
    }

    @Test
    void shouldUpdateEvidenciaSuccessfully() {

        Alerta alerta = crearAlerta();

        Evidencia evidencia = new Evidencia();
        evidencia.setArchivoUrl("viejo.jpg");
        evidencia.setFechaSubida(LocalDateTime.now());
        evidencia.setAlerta(alerta);

        evidencia = evidenciaRepository.save(evidencia);

        EvidenciaResponseDTO updated =
                evidenciaService.update(
                        evidencia.getId(),
                        new EvidenciaSaveDTO(
                                "nuevo.jpg",
                                alerta.getId()
                        )
                );

        assertEquals("nuevo.jpg", updated.archivoUrl());
    }

    @Test
    void shouldPatchEvidenciaSuccessfully() {

        Alerta alerta = crearAlerta();

        Evidencia evidencia = new Evidencia();
        evidencia.setArchivoUrl("original.jpg");
        evidencia.setFechaSubida(LocalDateTime.now());
        evidencia.setAlerta(alerta);

        evidencia = evidenciaRepository.save(evidencia);

        EvidenciaResponseDTO patched =
                evidenciaService.patch(
                        evidencia.getId(),
                        new EvidenciaSaveDTO(
                                "modificado.jpg",
                                null
                        )
                );

        assertEquals("modificado.jpg", patched.archivoUrl());
    }

    @Test
    void shouldDeleteEvidenciaSuccessfully() {

        Alerta alerta = crearAlerta();

        Evidencia evidencia = new Evidencia();
        evidencia.setArchivoUrl("delete.jpg");
        evidencia.setFechaSubida(LocalDateTime.now());
        evidencia.setAlerta(alerta);

        Evidencia saved = evidenciaRepository.save(evidencia);

        evidenciaService.delete(saved.getId());

        assertThrows(
                ResourceNotFoundException.class,
                () -> evidenciaService.findById(saved.getId())
        );
    }

    @Test
    void shouldThrowExceptionWhenFindingNonExistingEvidencia() {

        assertThrows(
                ResourceNotFoundException.class,
                () -> evidenciaService.findById(999999L)
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingEvidencia() {

        Alerta alerta = crearAlerta();

        assertThrows(
                ResourceNotFoundException.class,
                () -> evidenciaService.update(
                        999999L,
                        new EvidenciaSaveDTO(
                                "noexiste.jpg",
                                alerta.getId()
                        )
                )
        );
    }

    private Alerta crearAlerta() {

        Cuadrante cuadrante = new Cuadrante();
        cuadrante.setNombreUnidad("CAI Test");
        cuadrante.setTelefonoEmergencia("3001234567");
        cuadrante = cuadranteRepository.save(cuadrante);

        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio Test");
        barrio.setCuadrante(cuadrante);
        barrio = barrioRepository.save(barrio);

        User user = new User();
        user.setName("Usuario");
        user.setEmail("usuario@test.com");
        user.setPhone("3001111111");
        user.setAddress("Direccion");
        user.setPassword("123456");
        user.setBarrio(barrio);
        user = userRepository.save(user);

        Categoria categoria = new Categoria();
        categoria.setNombre("Robo");
        categoria.setIconoReferencia("icono.png");
        categoria = categoriaRepository.save(categoria);

        Alerta alerta = new Alerta();
        alerta.setDescripcion("Alerta de prueba");
        alerta.setEsSos(false);
        alerta.setFechaHora(LocalDateTime.now());
        alerta.setUsuario(user);
        alerta.setCategoria(categoria);

        return alertaRepository.save(alerta);
    }
}