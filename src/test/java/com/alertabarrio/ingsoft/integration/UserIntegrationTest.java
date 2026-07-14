package com.alertabarrio.ingsoft.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.UserResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.UserSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Barrio;
import com.alertabarrio.ingsoft.models.entities.Cuadrante;
import com.alertabarrio.ingsoft.repositories.BarrioRepository;
import com.alertabarrio.ingsoft.repositories.CuadranteRepository;
import com.alertabarrio.ingsoft.services.UserService;

@SpringBootTest
@Transactional
class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BarrioRepository barrioRepository;

    @Autowired
    private CuadranteRepository cuadranteRepository;

    @Test
    void shouldCreateUserSuccessfully() {

        Cuadrante cuadrante = new Cuadrante();
        cuadrante.setNombreUnidad("Unidad IT-USR-001-B");
        cuadrante.setTelefonoEmergencia("3000009999");

        cuadrante = cuadranteRepository.save(cuadrante);

        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio-integracion");
        barrio.setCuadrante(cuadrante);

        barrio = barrioRepository.save(barrio);

        UserSaveDTO dto = new UserSaveDTO(
            "Usuario Integracion 2",
            "integracion2@test.com",
            "+573001112200",
            "Calle Integracion",
            barrio.getId()
        );

        UserResponseDTO saved = userService.save(dto);

        assertNotNull(saved);
        assertNotNull(saved.id());

        assertEquals("Usuario Integracion 2", saved.name());
        assertEquals("integracion2@test.com", saved.email());
        assertEquals(barrio.getId(), saved.barrioId());
    }

    @Test
    void shouldFindUserByIdSuccessfully() {

        Cuadrante cuadrante = new Cuadrante();
        cuadrante.setNombreUnidad("Unidad IT-USR-002");
        cuadrante.setTelefonoEmergencia("3000000002");

        cuadrante = cuadranteRepository.save(cuadrante);

        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio IT-USR-002");
        barrio.setCuadrante(cuadrante);

        barrio = barrioRepository.save(barrio);

        UserSaveDTO dto = new UserSaveDTO(
            "Usuario Buscar",
            "buscar@test.com",
            "+573001111111",
            "Calle Buscar",
            barrio.getId()
        );

        UserResponseDTO created = userService.save(dto);

        UserResponseDTO found = userService.findById(created.id());

        assertNotNull(found);

        assertEquals(created.id(), found.id());
        assertEquals("Usuario Buscar", found.name());
        assertEquals("buscar@test.com", found.email());
        assertEquals(barrio.getId(), found.barrioId());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        assertThrows(
            ResourceNotFoundException.class,
            () -> userService.findById(999999L)
        );
    }

    @Test
    void shouldDeleteUserSuccessfully() {

        Cuadrante cuadrante = new Cuadrante();
        cuadrante.setNombreUnidad("Unidad IT-USR-004");
        cuadrante.setTelefonoEmergencia("3000000004");

        cuadrante = cuadranteRepository.save(cuadrante);

        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio IT-USR-004");
        barrio.setCuadrante(cuadrante);

        barrio = barrioRepository.save(barrio);

        UserSaveDTO dto = new UserSaveDTO(
            "Usuario Eliminar",
            "eliminar@test.com",
            "3001111111",
            "Direccion",
            barrio.getId()
        );

        UserResponseDTO created = userService.save(dto);

        userService.delete(created.id());

        assertThrows(
            ResourceNotFoundException.class,
            () -> userService.findById(created.id())
        );
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser() {

        assertThrows(
            ResourceNotFoundException.class,
            () -> userService.delete(999999L)
        );
    }

}
