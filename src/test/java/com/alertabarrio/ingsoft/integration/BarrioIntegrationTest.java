package com.alertabarrio.ingsoft.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.BarrioResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.BarrioSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Barrio;
import com.alertabarrio.ingsoft.models.entities.Cuadrante;
import com.alertabarrio.ingsoft.repositories.BarrioRepository;
import com.alertabarrio.ingsoft.repositories.CuadranteRepository;
import com.alertabarrio.ingsoft.services.BarrioService;

@SpringBootTest
@Transactional
class BarrioIntegrationTest {

    @Autowired
    private BarrioService barrioService;

    @Autowired
    private BarrioRepository barrioRepository;

    @Autowired
    private CuadranteRepository cuadranteRepository;

    @Test
    void shouldCreateBarrioSuccessfully() {

        Cuadrante cuadrante = crearCuadrante("Unidad Centro");

        BarrioResponseDTO created = barrioService.save(
                new BarrioSaveDTO(
                        "Barrio Integracion",
                        cuadrante.getId()
                )
        );

        assertNotNull(created);
        assertNotNull(created.id());
        assertEquals("Barrio Integracion", created.nombre());
        assertEquals(cuadrante.getId(), created.cuadrante().id());
    }

    @Test
    void shouldFindBarrioByIdSuccessfully() {

        Cuadrante cuadrante = crearCuadrante("Unidad Norte");

        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio Buscar");
        barrio.setCuadrante(cuadrante);

        barrio = barrioRepository.save(barrio);

        BarrioResponseDTO found =
                barrioService.findById(barrio.getId());

        assertEquals(barrio.getId(), found.id());
        assertEquals("Barrio Buscar", found.nombre());
    }

    @Test
    void shouldUpdateBarrioSuccessfully() {

        Cuadrante cuadrante1 = crearCuadrante("Unidad 1");
        Cuadrante cuadrante2 = crearCuadrante("Unidad 2");

        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio Viejo");
        barrio.setCuadrante(cuadrante1);

        barrio = barrioRepository.save(barrio);

        BarrioResponseDTO updated =
                barrioService.update(
                        barrio.getId(),
                        new BarrioSaveDTO(
                                "Barrio Nuevo",
                                cuadrante2.getId()
                        )
                );

        assertEquals("Barrio Nuevo", updated.nombre());
        assertEquals(cuadrante2.getId(), updated.cuadrante().id());
    }

    @Test
    void shouldPatchBarrioSuccessfully() {

        Cuadrante cuadrante = crearCuadrante("Unidad Patch");

        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio Original");
        barrio.setCuadrante(cuadrante);

        barrio = barrioRepository.save(barrio);

        BarrioResponseDTO patched =
                barrioService.patch(
                        barrio.getId(),
                        new BarrioSaveDTO(
                                "Barrio Modificado",
                                null
                        )
                );

        assertEquals("Barrio Modificado", patched.nombre());
        assertEquals(cuadrante.getId(), patched.cuadrante().id());
    }

    @Test
    void shouldDeleteBarrioSuccessfully() {

        Cuadrante cuadrante = crearCuadrante("Unidad Delete");

        final Barrio barrio = new Barrio();
        barrio.setNombre("Barrio Delete");
        barrio.setCuadrante(cuadrante);

        final Barrio savedBarrio = barrioRepository.save(barrio);

        barrioService.delete(savedBarrio.getId());

        assertThrows(
                ResourceNotFoundException.class,
                () -> barrioService.findById(savedBarrio.getId())
        );
    }

    @Test
    void shouldThrowExceptionWhenFindingNonExistingBarrio() {

        assertThrows(
                ResourceNotFoundException.class,
                () -> barrioService.findById(999999L)
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingBarrio() {

        Cuadrante cuadrante = crearCuadrante("Unidad Test");

        assertThrows(
                ResourceNotFoundException.class,
                () -> barrioService.update(
                        999999L,
                        new BarrioSaveDTO(
                                "No Existe",
                                cuadrante.getId()
                        )
                )
        );
    }

    private Cuadrante crearCuadrante(String nombreUnidad) {

        Cuadrante cuadrante = new Cuadrante();
        cuadrante.setNombreUnidad(nombreUnidad);
        cuadrante.setTelefonoEmergencia("3001234567");

        return cuadranteRepository.save(cuadrante);
    }
}