package com.alertabarrio.ingsoft.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.ConfiguracionResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.ConfiguracionSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Barrio;
import com.alertabarrio.ingsoft.models.entities.Configuracion;
import com.alertabarrio.ingsoft.models.entities.Cuadrante;
import com.alertabarrio.ingsoft.models.entities.User;
import com.alertabarrio.ingsoft.repositories.BarrioRepository;
import com.alertabarrio.ingsoft.repositories.ConfiguracionRepository;
import com.alertabarrio.ingsoft.repositories.CuadranteRepository;
import com.alertabarrio.ingsoft.repositories.UserRepository;
import com.alertabarrio.ingsoft.services.ConfiguracionService;

@SpringBootTest
@Transactional
class ConfiguracionIntegrationTest {

    @Autowired
    private ConfiguracionService configuracionService;

    @Autowired
    private ConfiguracionRepository configuracionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BarrioRepository barrioRepository;

    @Autowired
    private CuadranteRepository cuadranteRepository;

    @Test
    void shouldCreateConfiguracionSuccessfully() {

        User user = crearUsuario();

        ConfiguracionResponseDTO created =
                configuracionService.save(
                        new ConfiguracionSaveDTO(
                                user.getId(),
                                true,
                                false
                        )
                );

        assertNotNull(created);
        assertNotNull(created.id());
        assertEquals(user.getId(), created.usuarioId());
        assertTrue(created.recibirNotificaciones());
        assertFalse(created.modoSilencioso());
    }

    @Test
    void shouldFindConfiguracionByIdSuccessfully() {

        User user = crearUsuario();

        Configuracion config = new Configuracion();
        config.setUsuario(user);
        config.setRecibirNotificaciones(true);
        config.setModoSilencioso(false);

        config = configuracionRepository.save(config);

        ConfiguracionResponseDTO found =
                configuracionService.findById(config.getId());

        assertEquals(config.getId(), found.id());
        assertEquals(user.getId(), found.usuarioId());
    }

    @Test
    void shouldUpdateConfiguracionSuccessfully() {

        User user = crearUsuario();

        Configuracion config = new Configuracion();
        config.setUsuario(user);
        config.setRecibirNotificaciones(true);
        config.setModoSilencioso(false);

        config = configuracionRepository.save(config);

        ConfiguracionResponseDTO updated =
                configuracionService.update(
                        config.getId(),
                        new ConfiguracionSaveDTO(
                                user.getId(),
                                false,
                                true
                        )
                );

        assertFalse(updated.recibirNotificaciones());
        assertTrue(updated.modoSilencioso());
    }

    @Test
    void shouldPatchConfiguracionSuccessfully() {

        User user = crearUsuario();

        Configuracion config = new Configuracion();
        config.setUsuario(user);
        config.setRecibirNotificaciones(true);
        config.setModoSilencioso(false);

        config = configuracionRepository.save(config);

        ConfiguracionResponseDTO patched =
                configuracionService.patch(
                        config.getId(),
                        new ConfiguracionSaveDTO(
                                null,
                                false,
                                null
                        )
                );

        assertFalse(patched.recibirNotificaciones());
        assertFalse(patched.modoSilencioso());
    }

    @Test
    void shouldDeleteConfiguracionSuccessfully() {

        User user = crearUsuario();

        Configuracion config = new Configuracion();
        config.setUsuario(user);
        config.setRecibirNotificaciones(true);
        config.setModoSilencioso(false);

        config = configuracionRepository.save(config);

        Long id = config.getId();

        configuracionService.delete(id);

        assertFalse(
            configuracionRepository.existsById(id)
        );
    }

    @Test
    void shouldThrowExceptionWhenFindingNonExistingConfiguracion() {

        assertThrows(
                ResourceNotFoundException.class,
                () -> configuracionService.findById(999999L)
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingConfiguracion() {

        User user = crearUsuario();

        assertThrows(
                ResourceNotFoundException.class,
                () -> configuracionService.update(
                        999999L,
                        new ConfiguracionSaveDTO(
                                user.getId(),
                                true,
                                false
                        )
                )
        );
    }

    private User crearUsuario() {

        Cuadrante cuadrante = new Cuadrante();
        cuadrante.setNombreUnidad("CAI Test");
        cuadrante.setTelefonoEmergencia(
                "300" + System.nanoTime() % 10000000
        );

        cuadrante = cuadranteRepository.save(cuadrante);

        Barrio barrio = new Barrio();
        barrio.setNombre("Barrio Test");
        barrio.setCuadrante(cuadrante);

        barrio = barrioRepository.save(barrio);

        User user = new User();
        user.setName("Usuario Test");
        user.setEmail("test" + System.nanoTime() + "@mail.com");
        user.setPhone("3001234567");
        user.setAddress("Direccion Test");
        user.setPassword("123456");
        user.setBarrio(barrio);

        return userRepository.save(user);
    }
}
