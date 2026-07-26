package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.AlertaDTO;
import com.alertabarrio.application.mapper.AlertaDomainMapper;
import com.alertabarrio.application.query.BuscarAlertaQuery;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Alerta;
import com.alertabarrio.domain.model.valueobject.AlertaId;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BuscarAlertaUseCaseImpl")
class BuscarAlertaUseCaseImplTest {

    @Mock
    private AlertaRepositoryPort repository;

    @Mock
    private AlertaDomainMapper mapper;

    @InjectMocks
    private BuscarAlertaUseCaseImpl useCase;

    @Test
    @DisplayName("encuentra alerta por id y devuelve DTO")
    void execute_conIdValido_encuentraYDevuelveDTO() {
        BuscarAlertaQuery query = new BuscarAlertaQuery(1L);
        Alerta alerta = Alerta.reconstruir(1L, "Robo", true, LocalDateTime.now(), 1L, 1L);
        AlertaDTO dtoEsperado = new AlertaDTO(1L, "Robo", true, alerta.getFechaHora(), 1L, 1L);

        when(repository.findById(new AlertaId(1L))).thenReturn(Optional.of(alerta));
        when(mapper.toDto(alerta)).thenReturn(dtoEsperado);

        AlertaDTO resultado = useCase.execute(query);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Robo", resultado.descripcion());
        assertTrue(resultado.esSos());

        verify(repository).findById(new AlertaId(1L));
        verify(mapper).toDto(alerta);
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaResourceNotFoundException() {
        BuscarAlertaQuery query = new BuscarAlertaQuery(99L);

        when(repository.findById(new AlertaId(99L))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(query));

        verify(repository).findById(new AlertaId(99L));
        verify(mapper, never()).toDto(any());
    }
}
