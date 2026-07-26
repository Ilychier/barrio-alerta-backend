package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.EvidenciaDTO;
import com.alertabarrio.application.mapper.EvidenciaDomainMapper;
import com.alertabarrio.application.query.ListarEvidenciasQuery;
import com.alertabarrio.domain.model.Evidencia;
import com.alertabarrio.domain.port.out.EvidenciaRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListarEvidenciasUseCaseImpl")
class ListarEvidenciasUseCaseImplTest {

    @Mock
    private EvidenciaRepositoryPort evidenciaRepository;

    @Mock
    private EvidenciaDomainMapper mapper;

    @InjectMocks
    private ListarEvidenciasUseCaseImpl useCase;

    @Test
    @DisplayName("con alertaId filtra por alerta")
    void execute_conAlertaId_filtraPorAlerta() {
        Pageable pageable = Pageable.ofSize(10);
        ListarEvidenciasQuery query = new ListarEvidenciasQuery(1L, pageable);
        LocalDateTime fecha = LocalDateTime.of(2024, 1, 1, 0, 0);
        Evidencia evidencia = Evidencia.reconstruir(1L, "foto.jpg", fecha, 1L);
        EvidenciaDTO dto = new EvidenciaDTO(1L, "foto.jpg", fecha, 1L);
        Page<Evidencia> page = new PageImpl<>(List.of(evidencia));

        when(evidenciaRepository.findByAlertaId(1L, pageable)).thenReturn(page);
        when(mapper.toDto(evidencia)).thenReturn(dto);

        Page<EvidenciaDTO> resultado = useCase.execute(query);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("foto.jpg", resultado.getContent().get(0).archivoUrl());

        verify(evidenciaRepository).findByAlertaId(1L, pageable);
        verify(evidenciaRepository, never()).findAll(any(Pageable.class));
        verify(mapper).toDto(evidencia);
    }

    @Test
    @DisplayName("sin alertaId devuelve todas")
    void execute_sinAlertaId_devuelveTodas() {
        Pageable pageable = Pageable.ofSize(10);
        ListarEvidenciasQuery query = new ListarEvidenciasQuery(null, pageable);
        LocalDateTime fecha = LocalDateTime.of(2024, 1, 1, 0, 0);
        Evidencia evidencia1 = Evidencia.reconstruir(1L, "foto1.jpg", fecha, 1L);
        Evidencia evidencia2 = Evidencia.reconstruir(2L, "foto2.jpg", fecha, 2L);
        EvidenciaDTO dto1 = new EvidenciaDTO(1L, "foto1.jpg", fecha, 1L);
        EvidenciaDTO dto2 = new EvidenciaDTO(2L, "foto2.jpg", fecha, 2L);
        Page<Evidencia> page = new PageImpl<>(List.of(evidencia1, evidencia2));

        when(evidenciaRepository.findAll(pageable)).thenReturn(page);
        when(mapper.toDto(evidencia1)).thenReturn(dto1);
        when(mapper.toDto(evidencia2)).thenReturn(dto2);

        Page<EvidenciaDTO> resultado = useCase.execute(query);

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());

        verify(evidenciaRepository).findAll(pageable);
        verify(evidenciaRepository, never()).findByAlertaId(any(), any());
        verify(mapper).toDto(evidencia1);
        verify(mapper).toDto(evidencia2);
    }
}
