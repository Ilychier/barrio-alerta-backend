package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.CrearConfiguracionCommand;
import com.alertabarrio.application.dto.ConfiguracionDTO;
import com.alertabarrio.application.mapper.ConfiguracionDomainMapper;
import com.alertabarrio.domain.exception.ConfiguracionInvalidaException;
import com.alertabarrio.domain.model.Configuracion;
import com.alertabarrio.domain.port.out.ConfiguracionRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CrearConfiguracionUseCaseImpl")
class CrearConfiguracionUseCaseImplTest {

    @Mock
    private ConfiguracionRepositoryPort configuracionRepository;

    @Mock
    private ConfiguracionDomainMapper mapper;

    @InjectMocks
    private CrearConfiguracionUseCaseImpl useCase;

    @Test
    @DisplayName("con command válido persiste y devuelve DTO")
    void execute_conCommandValido_persisteYDevuelveDTO() {
        CrearConfiguracionCommand command = new CrearConfiguracionCommand(1L, true, false);
        Configuracion configuracionPersistida = Configuracion.reconstruir(1L, 1L, true, false);
        ConfiguracionDTO dtoEsperado = new ConfiguracionDTO(1L, 1L, true, false);

        when(configuracionRepository.save(any(Configuracion.class))).thenReturn(configuracionPersistida);
        when(mapper.toDto(configuracionPersistida)).thenReturn(dtoEsperado);

        ConfiguracionDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals(1L, resultado.usuarioId());
        assertTrue(resultado.recibirNotificaciones());
        assertFalse(resultado.modoSilencioso());

        verify(configuracionRepository).save(any(Configuracion.class));
        verify(mapper).toDto(configuracionPersistida);
    }

    @Test
    @DisplayName("con usuarioId nulo lanza ConfiguracionInvalidaException")
    void execute_conUsuarioIdNulo_lanzaExcepcion() {
        CrearConfiguracionCommand command = new CrearConfiguracionCommand(null, true, false);

        assertThrows(ConfiguracionInvalidaException.class, () -> useCase.execute(command));

        verify(configuracionRepository, never()).save(any());
    }
}
