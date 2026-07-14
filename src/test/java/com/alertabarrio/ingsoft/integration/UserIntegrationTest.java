package com.alertabarrio.ingsoft.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.alertabarrio.ingsoft.exceptions.ResourceConflictException;
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

    @Test
    void shouldUpdateUserSuccessfully() {

        Cuadrante cuadrante = new Cuadrante();
        cuadrante.setNombreUnidad("Unidad IT-USR-006");
        cuadrante.setTelefonoEmergencia("3000000006");
        cuadrante = cuadranteRepository.save(cuadrante);

        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio IT-USR-006");
        barrio.setCuadrante(cuadrante);
        barrio = barrioRepository.save(barrio);

        UserSaveDTO createDto = new UserSaveDTO(
            "Usuario Original",
            "original@test.com",
            "3001111111",
            "Direccion Original",
            barrio.getId()
        );

        UserResponseDTO created = userService.save(createDto);

        UserSaveDTO updateDto = new UserSaveDTO(
            "Usuario Actualizado",
            "actualizado@test.com",
            "3009999999",
            "Direccion Actualizada",
            barrio.getId()
        );

        UserResponseDTO updated =
                userService.update(created.id(), updateDto);

        assertEquals("Usuario Actualizado", updated.name());
        assertEquals("actualizado@test.com", updated.email());
        assertEquals("3009999999", updated.phone());
        assertEquals("Direccion Actualizada", updated.address());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingUser() {

        UserSaveDTO dto = new UserSaveDTO(
            "Usuario",
            "usuario@test.com",
            "3001111111",
            "Direccion",
            null
        );

        assertThrows(
            ResourceNotFoundException.class,
            () -> userService.update(999999L, dto)
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithDuplicatedEmail() {

        Cuadrante cuadrante = new Cuadrante();
        cuadrante.setNombreUnidad("Unidad IT-USR-008");
        cuadrante.setTelefonoEmergencia("3000000008");
        cuadrante = cuadranteRepository.save(cuadrante);

        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio IT-USR-008");
        barrio.setCuadrante(cuadrante);
        barrio = barrioRepository.save(barrio);

        UserResponseDTO user1 = userService.save(
            new UserSaveDTO(
                "Usuario 1",
                "user1@test.com",
                "3001111111",
                "Dir1",
                barrio.getId()
            )
        );

        UserResponseDTO user2 = userService.save(
            new UserSaveDTO(
                "Usuario 2",
                "user2@test.com",
                "3002222222",
                "Dir2",
                barrio.getId()
            )
        );

        UserSaveDTO updateDto = new UserSaveDTO(
            "Usuario 2",
            "user1@test.com",
            "3002222222",
            "Dir2",
            barrio.getId()
        );

        assertThrows(
            ResourceConflictException.class,
            () -> userService.update(user2.id(), updateDto)
        );
    }

}
