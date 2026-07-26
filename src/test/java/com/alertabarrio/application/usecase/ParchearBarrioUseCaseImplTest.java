package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.ParchearBarrioCommand;
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
@DisplayName("ParchearBarrioUseCaseImpl")
class ParchearBarrioUseCaseImplTest {

    @Mock
    private BarrioRepositoryPort barrioRepository;

    @Mock
    private CuadranteRepositoryPort cuadranteRepository;

    @Mock
    private BarrioDomainMapper mapper;

    @InjectMocks
    private ParchearBarrioUseCaseImpl useCase;

    @Test
    @DisplayName("con nombre y cuadranteId nuevos parchea correctamente")
    void execute_conNombreYCuadranteIdNuevos_parcheaCorrectamente() {
        ParchearBarrioCommand command = new ParchearBarrioCommand(1L, "Norte", 2L);
        Barrio existente = Barrio.reconstruir(1L, "Centro", 1L);
        Barrio parcheada = Barrio.reconstruir(1L, "Norte", 2L);
        BarrioDTO dtoEsperado = new BarrioDTO(1L, "Norte", 2L);

        when(barrioRepository.findById(new BarrioId(1L))).thenReturn(Optional.of(existente));
        when(barrioRepository.existsByNombre("Norte")).thenReturn(false);
        when(cuadranteRepository.existsById(new CuadranteId(2L))).thenReturn(true);
        when(barrioRepository.save(any(Barrio.class))).thenReturn(parcheada);
        when(mapper.toDto(parcheada)).thenReturn(dtoEsperado);

        BarrioDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Norte", resultado.nombre());
        assertEquals(2L, resultado.cuadranteId());

        verify(barrioRepository).save(any(Barrio.class));
        verify(mapper).toDto(parcheada);
    }

    @Test
    @DisplayName("con solo nombre nuevo parchea correctamente")
    void execute_conSoloNombreNuevo_parcheaCorrectamente() {
        ParchearBarrioCommand command = new ParchearBarrioCommand(1L, "Norte", null);
        Barrio existente = Barrio.reconstruir(1L, "Centro", 1L);
        Barrio parcheada = Barrio.reconstruir(1L, "Norte", 1L);
        BarrioDTO dtoEsperado = new BarrioDTO(1L, "Norte", 1L);

        when(barrioRepository.findById(new BarrioId(1L))).thenReturn(Optional.of(existente));
        when(barrioRepository.existsByNombre("Norte")).thenReturn(false);
        when(barrioRepository.save(any(Barrio.class))).thenReturn(parcheada);
        when(mapper.toDto(parcheada)).thenReturn(dtoEsperado);

        BarrioDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Norte", resultado.nombre());
        assertEquals(1L, resultado.cuadranteId());

        verify(barrioRepository).save(any(Barrio.class));
        verify(mapper).toDto(parcheada);
    }

    @Test
    @DisplayName("con solo cuadranteId nuevo parchea correctamente")
    void execute_conSoloCuadranteIdNuevo_parcheaCorrectamente() {
        ParchearBarrioCommand command = new ParchearBarrioCommand(1L, null, 2L);
        Barrio existente = Barrio.reconstruir(1L, "Centro", 1L);
        Barrio parcheada = Barrio.reconstruir(1L, "Centro", 2L);
        BarrioDTO dtoEsperado = new BarrioDTO(1L, "Centro", 2L);

        when(barrioRepository.findById(new BarrioId(1L))).thenReturn(Optional.of(existente));
        when(cuadranteRepository.existsById(new CuadranteId(2L))).thenReturn(true);
        when(barrioRepository.save(any(Barrio.class))).thenReturn(parcheada);
        when(mapper.toDto(parcheada)).thenReturn(dtoEsperado);

        BarrioDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Centro", resultado.nombre());
        assertEquals(2L, resultado.cuadranteId());

        verify(barrioRepository).save(any(Barrio.class));
        verify(mapper).toDto(parcheada);
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaResourceNotFoundException() {
        ParchearBarrioCommand command = new ParchearBarrioCommand(99L, "Norte", 2L);

        when(barrioRepository.findById(new BarrioId(99L))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(barrioRepository, never()).save(any());
    }

    @Test
    @DisplayName("con nombre duplicado lanza ResourceConflictException")
    void execute_conNombreDuplicado_lanzaResourceConflictException() {
        ParchearBarrioCommand command = new ParchearBarrioCommand(1L, "Norte", null);
        Barrio existente = Barrio.reconstruir(1L, "Centro", 1L);

        when(barrioRepository.findById(new BarrioId(1L))).thenReturn(Optional.of(existente));
        when(barrioRepository.existsByNombre("Norte")).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> useCase.execute(command));

        verify(barrioRepository, never()).save(any());
    }

    @Test
    @DisplayName("con cuadrante inexistente lanza ResourceNotFoundException")
    void execute_conCuadranteInexistente_lanzaResourceNotFoundException() {
        ParchearBarrioCommand command = new ParchearBarrioCommand(1L, null, 99L);
        Barrio existente = Barrio.reconstruir(1L, "Centro", 1L);

        when(barrioRepository.findById(new BarrioId(1L))).thenReturn(Optional.of(existente));
        when(cuadranteRepository.existsById(new CuadranteId(99L))).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(barrioRepository, never()).save(any());
    }
}
