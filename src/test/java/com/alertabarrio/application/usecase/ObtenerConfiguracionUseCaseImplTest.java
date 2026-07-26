package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.ConfiguracionDTO;
import com.alertabarrio.application.mapper.ConfiguracionDomainMapper;
import com.alertabarrio.application.query.ObtenerConfiguracionQuery;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObtenerConfiguracionUseCaseImpl")
class ObtenerConfiguracionUseCaseImplTest {

    @Mock
    private ConfiguracionRepositoryPort configuracionRepository;

    @Mock
    private ConfiguracionDomainMapper mapper;

    @InjectMocks
    private ObtenerConfiguracionUseCaseImpl useCase;

    @Test
    @DisplayName("encuentra por id y devuelve DTO")
    void execute_conIdExistente_devuelveDTO() {
        ObtenerConfiguracionQuery query = new ObtenerConfiguracionQuery(1L);
        ConfiguracionId id = new ConfiguracionId(1L);
        Configuracion configuracion = Configuracion.reconstruir(1L, 1L, true, false);
        ConfiguracionDTO dtoEsperado = new ConfiguracionDTO(1L, 1L, true, false);

        when(configuracionRepository.findById(id)).thenReturn(Optional.of(configuracion));
        when(mapper.toDto(configuracion)).thenReturn(dtoEsperado);

        ConfiguracionDTO resultado = useCase.execute(query);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals(1L, resultado.usuarioId());
        assertTrue(resultado.recibirNotificaciones());
        assertFalse(resultado.modoSilencioso());

        verify(configuracionRepository).findById(id);
        verify(mapper).toDto(configuracion);
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaExcepcion() {
        ObtenerConfiguracionQuery query = new ObtenerConfiguracionQuery(99L);
        ConfiguracionId id = new ConfiguracionId(99L);

        when(configuracionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(query));

        verify(configuracionRepository).findById(id);
        verify(mapper, never()).toDto(any());
    }
}
