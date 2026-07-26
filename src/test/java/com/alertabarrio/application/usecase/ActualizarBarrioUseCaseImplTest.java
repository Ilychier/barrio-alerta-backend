package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.ActualizarBarrioCommand;
import com.alertabarrio.application.dto.BarrioDTO;
import com.alertabarrio.application.mapper.BarrioDomainMapper;
import com.alertabarrio.domain.exception.ResourceConflictException;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Barrio;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.domain.model.valueobject.CuadranteId;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import com.alertabarrio.domain.port.out.CuadranteRepositoryPort;
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
@DisplayName("ActualizarBarrioUseCaseImpl")
class ActualizarBarrioUseCaseImplTest {

    @Mock
    private BarrioRepositoryPort barrioRepository;

    @Mock
    private CuadranteRepositoryPort cuadranteRepository;

    @Mock
    private BarrioDomainMapper mapper;

    @InjectMocks
    private ActualizarBarrioUseCaseImpl useCase;

    @Test
    @DisplayName("con command válido actualiza y devuelve DTO")
    void execute_conCommandValido_actualizaYDevuelveDTO() {
        ActualizarBarrioCommand command = new ActualizarBarrioCommand(1L, "Norte", 2L);
        Barrio existente = Barrio.reconstruir(1L, "Centro", 1L);
        Barrio actualizada = Barrio.reconstruir(1L, "Norte", 2L);
        BarrioDTO dtoEsperado = new BarrioDTO(1L, "Norte", 2L);

        when(barrioRepository.findById(new BarrioId(1L))).thenReturn(Optional.of(existente));
        when(barrioRepository.existsByNombre("Norte")).thenReturn(false);
        when(cuadranteRepository.existsById(new CuadranteId(2L))).thenReturn(true);
        when(barrioRepository.save(any(Barrio.class))).thenReturn(actualizada);
        when(mapper.toDto(actualizada)).thenReturn(dtoEsperado);

        BarrioDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Norte", resultado.nombre());
        assertEquals(2L, resultado.cuadranteId());

        verify(barrioRepository).save(any(Barrio.class));
        verify(mapper).toDto(actualizada);
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaResourceNotFoundException() {
        ActualizarBarrioCommand command = new ActualizarBarrioCommand(99L, "Norte", 2L);

        when(barrioRepository.findById(new BarrioId(99L))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(barrioRepository, never()).save(any());
    }

    @Test
    @DisplayName("con nombre duplicado lanza ResourceConflictException")
    void execute_conNombreDuplicado_lanzaResourceConflictException() {
        ActualizarBarrioCommand command = new ActualizarBarrioCommand(1L, "Norte", 2L);
        Barrio existente = Barrio.reconstruir(1L, "Centro", 1L);

        when(barrioRepository.findById(new BarrioId(1L))).thenReturn(Optional.of(existente));
        when(barrioRepository.existsByNombre("Norte")).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> useCase.execute(command));

        verify(barrioRepository, never()).save(any());
    }

    @Test
    @DisplayName("con cuadrante inexistente lanza ResourceNotFoundException")
    void execute_conCuadranteInexistente_lanzaResourceNotFoundException() {
        ActualizarBarrioCommand command = new ActualizarBarrioCommand(1L, "Norte", 99L);
        Barrio existente = Barrio.reconstruir(1L, "Centro", 1L);

        when(barrioRepository.findById(new BarrioId(1L))).thenReturn(Optional.of(existente));
        when(barrioRepository.existsByNombre("Norte")).thenReturn(false);
        when(cuadranteRepository.existsById(new CuadranteId(99L))).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(barrioRepository, never()).save(any());
    }
}
