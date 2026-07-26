package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.BarrioDTO;
import com.alertabarrio.application.mapper.BarrioDomainMapper;
import com.alertabarrio.application.query.BuscarBarrioQuery;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Barrio;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
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
@DisplayName("BuscarBarrioUseCaseImpl")
class BuscarBarrioUseCaseImplTest {

    @Mock
    private BarrioRepositoryPort barrioRepository;

    @Mock
    private BarrioDomainMapper mapper;

    @InjectMocks
    private BuscarBarrioUseCaseImpl useCase;

    @Test
    @DisplayName("con id existente devuelve DTO")
    void execute_conIdExistente_devuelveDTO() {
        BuscarBarrioQuery query = new BuscarBarrioQuery(1L);
        Barrio barrio = Barrio.reconstruir(1L, "Centro", 1L);
        BarrioDTO dtoEsperado = new BarrioDTO(1L, "Centro", 1L);

        when(barrioRepository.findById(new BarrioId(1L))).thenReturn(Optional.of(barrio));
        when(mapper.toDto(barrio)).thenReturn(dtoEsperado);

        BarrioDTO resultado = useCase.execute(query);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Centro", resultado.nombre());
        assertEquals(1L, resultado.cuadranteId());

        verify(mapper).toDto(barrio);
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaResourceNotFoundException() {
        BuscarBarrioQuery query = new BuscarBarrioQuery(99L);

        when(barrioRepository.findById(new BarrioId(99L))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(query));

        verify(mapper, never()).toDto(any());
    }
}
