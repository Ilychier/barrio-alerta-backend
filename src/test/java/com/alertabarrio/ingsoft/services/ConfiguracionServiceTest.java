package com.alertabarrio.ingsoft.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.ConfiguracionResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.ConfiguracionSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Configuracion;
import com.alertabarrio.ingsoft.models.entities.User;
import com.alertabarrio.ingsoft.repositories.ConfiguracionRepository;
import com.alertabarrio.ingsoft.repositories.UserRepository;
import com.alertabarrio.ingsoft.services.implementation.ConfiguracionServicelmpl;

@ExtendWith(MockitoExtension.class)
class ConfiguracionServiceTest {

    @Mock
    private ConfiguracionRepository configuracionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ConfiguracionServicelmpl configuracionService;

    private User user;
    private Configuracion configuracion;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setName("Juan");

        configuracion = new Configuracion();
        configuracion.setId(1L);
        configuracion.setUsuario(user);
        configuracion.setRecibirNotificaciones(true);
        configuracion.setModoSilencioso(false);
    }

    @Test
    void shouldSaveSuccessfully() {

        ConfiguracionSaveDTO dto =
                new ConfiguracionSaveDTO(
                        1L,
                        true,
                        false
                );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(configuracionRepository.save(any(Configuracion.class)))
                .thenReturn(configuracion);

        ConfiguracionResponseDTO result =
                configuracionService.save(dto);

        assertNotNull(result);
        assertEquals(1L, result.usuarioId());
        assertTrue(result.recibirNotificaciones());
    }

    @Test
    void shouldThrowWhenSavingWithNonExistingUser() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> configuracionService.save(
                        new ConfiguracionSaveDTO(
                                999L,
                                true,
                                false
                        )
                )
        );
    }

    @Test
    void shouldFindByIdSuccessfully() {

        when(configuracionRepository.findByUsuarioId(1L))
                .thenReturn(Optional.of(configuracion));

        ConfiguracionResponseDTO result =
                configuracionService.findById(1L);

        assertEquals(1L, result.id());
    }

    @Test
    void shouldCreateDefaultConfigurationWhenUserExists() {

        when(configuracionRepository.findByUsuarioId(1L))
                .thenReturn(Optional.empty());

        when(configuracionRepository.findById(1L))
                .thenReturn(Optional.empty());

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(configuracionRepository.save(any(Configuracion.class)))
                .thenAnswer(i -> {
                    Configuracion c = i.getArgument(0);
                    c.setId(1L);
                    return c;
                });

        ConfiguracionResponseDTO result =
                configuracionService.findById(1L);

        assertNotNull(result);
        assertTrue(result.recibirNotificaciones());
        assertFalse(result.modoSilencioso());
    }

    @Test
    void shouldThrowWhenFindingNonExistingConfigurationAndUser() {

        when(configuracionRepository.findByUsuarioId(999L))
                .thenReturn(Optional.empty());

        when(configuracionRepository.findById(999L))
                .thenReturn(Optional.empty());

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> configuracionService.findById(999L)
        );
    }

    @Test
    void shouldUpdateSuccessfully() {

        ConfiguracionSaveDTO dto =
                new ConfiguracionSaveDTO(
                        1L,
                        false,
                        true
                );

        when(configuracionRepository.findByUsuarioId(1L))
                .thenReturn(Optional.of(configuracion));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(configuracionRepository.save(any(Configuracion.class)))
                .thenAnswer(i -> i.getArgument(0));

        ConfiguracionResponseDTO result =
                configuracionService.update(1L, dto);

        assertFalse(result.recibirNotificaciones());
        assertTrue(result.modoSilencioso());
    }

    @Test
    void shouldPatchSuccessfully() {

        ConfiguracionSaveDTO dto =
                new ConfiguracionSaveDTO(
                        null,
                        false,
                        null
                );

        when(configuracionRepository.findByUsuarioId(1L))
                .thenReturn(Optional.of(configuracion));

        when(configuracionRepository.save(any(Configuracion.class)))
                .thenAnswer(i -> i.getArgument(0));

        ConfiguracionResponseDTO result =
                configuracionService.patch(1L, dto);

        assertFalse(result.recibirNotificaciones());
        assertFalse(result.modoSilencioso());
    }

    @Test
    void shouldDeleteSuccessfully() {

        when(configuracionRepository.existsById(1L))
                .thenReturn(true);

        configuracionService.delete(1L);

        verify(configuracionRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingConfiguration() {

        when(configuracionRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> configuracionService.delete(999L)
        );
    }
}