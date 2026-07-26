package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.CrearBarrioCommand;
import com.alertabarrio.application.dto.BarrioDTO;
import com.alertabarrio.application.mapper.BarrioDomainMapper;
import com.alertabarrio.domain.exception.ResourceConflictException;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Barrio;
import com.alertabarrio.domain.model.valueobject.CuadranteId;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import com.alertabarrio.domain.port.out.CuadranteRepositoryPort;
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
@DisplayName("CrearBarrioUseCaseImpl")
class CrearBarrioUseCaseImplTest {

    @Mock
    private BarrioRepositoryPort barrioRepository;

    @Mock
    private CuadranteRepositoryPort cuadranteRepository;

    @Mock
    private BarrioDomainMapper mapper;

    @InjectMocks
    private CrearBarrioUseCaseImpl useCase;

    @Test
    @DisplayName("con command válido persiste y devuelve DTO")
    void execute_conCommandValido_persisteYDevuelveDTO() {
        CrearBarrioCommand command = new CrearBarrioCommand("Centro", 1L);
        Barrio barrioPersistido = Barrio.reconstruir(1L, "Centro", 1L);
        BarrioDTO dtoEsperado = new BarrioDTO(1L, "Centro", 1L);

        when(barrioRepository.existsByNombre("Centro")).thenReturn(false);
        when(cuadranteRepository.existsById(new CuadranteId(1L))).thenReturn(true);
        when(barrioRepository.save(any(Barrio.class))).thenReturn(barrioPersistido);
        when(mapper.toDto(barrioPersistido)).thenReturn(dtoEsperado);

        BarrioDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Centro", resultado.nombre());
        assertEquals(1L, resultado.cuadranteId());

        verify(barrioRepository).save(any(Barrio.class));
        verify(mapper).toDto(barrioPersistido);
    }

    @Test
    @DisplayName("con nombre duplicado lanza ResourceConflictException")
    void execute_conNombreDuplicado_lanzaResourceConflictException() {
        CrearBarrioCommand command = new CrearBarrioCommand("Centro", 1L);

        when(barrioRepository.existsByNombre("Centro")).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> useCase.execute(command));

        verify(barrioRepository, never()).save(any());
    }

    @Test
    @DisplayName("con cuadrante inexistente lanza ResourceNotFoundException")
    void execute_conCuadranteInexistente_lanzaResourceNotFoundException() {
        CrearBarrioCommand command = new CrearBarrioCommand("Centro", 99L);

        when(barrioRepository.existsByNombre("Centro")).thenReturn(false);
        when(cuadranteRepository.existsById(new CuadranteId(99L))).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(command));

        verify(barrioRepository, never()).save(any());
    }
}
