package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.ActualizarConfiguracionCommand;
import com.alertabarrio.application.dto.ConfiguracionDTO;
import com.alertabarrio.application.mapper.ConfiguracionDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Configuracion;
import com.alertabarrio.domain.model.valueobject.ConfiguracionId;
import com.alertabarrio.domain.port.out.ConfiguracionRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ActualizarConfiguracionUseCaseImpl")
class ActualizarConfiguracionUseCaseImplTest {

    @Mock
    private ConfiguracionRepositoryPort configuracionRepository;

    @Mock
    private ConfiguracionDomainMapper mapper;

    @InjectMocks
    private ActualizarConfiguracionUseCaseImpl useCase;

    @Test
    @DisplayName("actualiza correctamente y devuelve DTO")
    void execute_conCommandValido_actualizaYDevuelveDTO() {
        ActualizarConfiguracionCommand command = new ActualizarConfiguracionCommand(1L, 1L, false, true);
        ConfiguracionId id = new ConfiguracionId(1L);
        Configuracion existente = Configuracion.reconstruir(1L, 1L, true, false);
        Configuracion actualizada = Configuracion.reconstruir(1L, 1L, false, true);
        ConfiguracionDTO dtoEsperado = new ConfiguracionDTO(1L, 1L, false, true);

        when(configuracionRepository.findById(id)).thenReturn(Optional.of(existente));
        when(configuracionRepository.save(any(Configuracion.class))).thenReturn(actualizada);
        when(mapper.toDto(actualizada)).thenReturn(dtoEsperado);

        ConfiguracionDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals(1L, resultado.usuarioId());
        assertFalse(resultado.recibirNotificaciones());
        assertTrue(resultado.modoSilencioso());

        verify(configuracionRepository).findById(id);
        verify(configuracionRepository).save(any(Configuracion.class));
        verify(mapper).toDto(actualizada);
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaExcepcion() {
        ActualizarConfiguracionCommand command = new ActualizarConfiguracionCommand(99L, 1L, true, false);
        ConfiguracionId id = new ConfiguracionId(99L);

        when(configuracionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(configuracionRepository).findById(id);
        verify(configuracionRepository, never()).save(any());
        verify(mapper, never()).toDto(any());
    }
}
