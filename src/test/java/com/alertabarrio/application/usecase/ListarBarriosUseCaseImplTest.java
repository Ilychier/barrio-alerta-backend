package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.BarrioDTO;
import com.alertabarrio.application.mapper.BarrioDomainMapper;
import com.alertabarrio.application.query.ListarBarriosQuery;
import com.alertabarrio.domain.model.Barrio;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListarBarriosUseCaseImpl")
class ListarBarriosUseCaseImplTest {

    @Mock
    private BarrioRepositoryPort barrioRepository;

    @Mock
    private BarrioDomainMapper mapper;

    @InjectMocks
    private ListarBarriosUseCaseImpl useCase;

    @Test
    @DisplayName("devuelve página de DTOs")
    void execute_devuelvePaginaDeDTOs() {
        Pageable pageable = Pageable.ofSize(10);
        ListarBarriosQuery query = new ListarBarriosQuery(pageable);

        Barrio barrio1 = Barrio.reconstruir(1L, "Centro", 1L);
        Barrio barrio2 = Barrio.reconstruir(2L, "Norte", 2L);
        Page<Barrio> barrioPage = new PageImpl<>(List.of(barrio1, barrio2), pageable, 2);

        BarrioDTO dto1 = new BarrioDTO(1L, "Centro", 1L);
        BarrioDTO dto2 = new BarrioDTO(2L, "Norte", 2L);

        when(barrioRepository.findAll(pageable)).thenReturn(barrioPage);
        when(mapper.toDto(barrio1)).thenReturn(dto1);
        when(mapper.toDto(barrio2)).thenReturn(dto2);

        Page<BarrioDTO> resultado = useCase.execute(query);

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(1L, resultado.getContent().get(0).id());
        assertEquals("Centro", resultado.getContent().get(0).nombre());
        assertEquals(2L, resultado.getContent().get(1).id());
        assertEquals("Norte", resultado.getContent().get(1).nombre());

        verify(barrioRepository).findAll(pageable);
        verify(mapper).toDto(barrio1);
        verify(mapper).toDto(barrio2);
    }

    @Test
    @DisplayName("devuelve página vacía cuando no hay barrios")
    void execute_devuelvePaginaVacia_cuandoNoHayBarrios() {
        Pageable pageable = Pageable.ofSize(10);
        ListarBarriosQuery query = new ListarBarriosQuery(pageable);
        Page<Barrio> paginaVacia = Page.empty(pageable);

        when(barrioRepository.findAll(pageable)).thenReturn(paginaVacia);

        Page<BarrioDTO> resultado = useCase.execute(query);

        assertNotNull(resultado);
        assertEquals(0, resultado.getTotalElements());

        verify(barrioRepository).findAll(pageable);
        verify(mapper, never()).toDto(any());
    }
}
