package com.alertabarrio.ingsoft.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.AlertaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.AlertaSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Barrio;
import com.alertabarrio.ingsoft.models.entities.Categoria;
import com.alertabarrio.ingsoft.models.entities.Cuadrante;
import com.alertabarrio.ingsoft.models.entities.User;
import com.alertabarrio.ingsoft.repositories.BarrioRepository;
import com.alertabarrio.ingsoft.repositories.CategoriaRepository;
import com.alertabarrio.ingsoft.repositories.CuadranteRepository;
import com.alertabarrio.ingsoft.repositories.UserRepository;
import com.alertabarrio.ingsoft.services.AlertaService;

@SpringBootTest
@Transactional
class AlertaIntegrationTest {

    @Autowired
    private AlertaService alertaService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BarrioRepository barrioRepository;

    @Autowired
    private CuadranteRepository cuadranteRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void shouldCreateAlertaSuccessfully() {
        User user = crearUsuario("usuario-integracion@test.com");
        Categoria categoria = crearCategoria("Seguridad");

        AlertaResponseDTO created = alertaService.save(
            new AlertaSaveDTO("Alerta de integración", true, user.getId(), categoria.getId())
        );

        assertNotNull(created);
        assertNotNull(created.id());
        assertEquals("Alerta de integración", created.descripcion());
        assertEquals(user.getId(), created.usuarioId());
        assertEquals(categoria.getId(), created.categoria().id());
    }

    @Test
    void shouldFindAlertaByIdSuccessfully() {
        User user = crearUsuario("buscar-alerta@test.com");
        Categoria categoria = crearCategoria("Emergencia");

        AlertaResponseDTO created = alertaService.save(
            new AlertaSaveDTO("Alerta buscada", false, user.getId(), categoria.getId())
        );

        AlertaResponseDTO found = alertaService.findById(created.id());

        assertEquals(created.id(), found.id());
        assertEquals("Alerta buscada", found.descripcion());
        assertEquals(user.getId(), found.usuarioId());
    }

    @Test
    void shouldDeleteAlertaSuccessfully() {
        User user = crearUsuario("eliminar-alerta@test.com");
        Categoria categoria = crearCategoria("Vial");

        AlertaResponseDTO created = alertaService.save(
            new AlertaSaveDTO("Alerta eliminar", true, user.getId(), categoria.getId())
        );

        alertaService.delete(created.id());

        assertThrows(
            ResourceNotFoundException.class,
            () -> alertaService.findById(created.id())
        );
    }

    @Test
    void shouldUpdateAlertaSuccessfully() {
        User user = crearUsuario("actualizar-alerta@test.com");
        Categoria categoria = crearCategoria("Transporte");

        AlertaResponseDTO created = alertaService.save(
            new AlertaSaveDTO("Alerta original", true, user.getId(), categoria.getId())
        );

        AlertaResponseDTO updated = alertaService.update(
            created.id(),
            new AlertaSaveDTO("Alerta actualizada", false, user.getId(), categoria.getId())
        );

        assertEquals("Alerta actualizada", updated.descripcion());
        assertEquals(false, updated.esSos());
        assertEquals(user.getId(), updated.usuarioId());
    }

    @Test
    void shouldPatchAlertaSuccessfully() {
        User user = crearUsuario("patch-alerta@test.com");
        Categoria categoria = crearCategoria("Salud");

        AlertaResponseDTO created = alertaService.save(
            new AlertaSaveDTO("Alerta patch", true, user.getId(), categoria.getId())
        );

        AlertaResponseDTO patched = alertaService.patch(
            created.id(),
            new AlertaSaveDTO("Alerta modificada", null, null, null)
        );

        assertEquals("Alerta modificada", patched.descripcion());
        assertEquals(true, patched.esSos());
    }

    private User crearUsuario(String email) {
        Cuadrante cuadrante = new Cuadrante();
        cuadrante.setNombreUnidad("Unidad IT-ALERTA-" + email);
        cuadrante.setTelefonoEmergencia("3000000000");
        cuadrante = cuadranteRepository.save(cuadrante);

        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio " + email);
        barrio.setCuadrante(cuadrante);
        barrio = barrioRepository.save(barrio);

        User user = new User();
        user.setName("Usuario " + email);
        user.setEmail(email);
        user.setPhone("3001111111");
        user.setAddress("Dirección");
        user.setBarrio(barrio);
        user.setPassword("password123");

        return userRepository.save(user);
    }

    private Categoria crearCategoria(String nombre) {
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setIconoReferencia("icono-" + nombre.toLowerCase());
        return categoriaRepository.save(categoria);
    }
}