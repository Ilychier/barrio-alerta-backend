package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.EvidenciaDTO;
import com.alertabarrio.application.mapper.EvidenciaDomainMapper;
import com.alertabarrio.application.query.BuscarEvidenciaQuery;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Evidencia;
import com.alertabarrio.domain.model.valueobject.EvidenciaId;
import com.alertabarrio.domain.port.out.EvidenciaRepositoryPort;
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
@DisplayName("BuscarEvidenciaUseCaseImpl")
class BuscarEvidenciaUseCaseImplTest {

    @Mock
    private EvidenciaRepositoryPort evidenciaRepository;

    @Mock
    private EvidenciaDomainMapper mapper;

    @InjectMocks
    private BuscarEvidenciaUseCaseImpl useCase;

    @Test
    @DisplayName("encuentra evidencia por id y devuelve DTO")
    void execute_conIdExistente_encuentraYDevuelveDTO() {
        BuscarEvidenciaQuery query = new BuscarEvidenciaQuery(1L);
        LocalDateTime fecha = LocalDateTime.of(2024, 1, 1, 0, 0);
        Evidencia evidencia = Evidencia.reconstruir(1L, "foto.jpg", fecha, 1L);
        EvidenciaDTO dtoEsperado = new EvidenciaDTO(1L, "foto.jpg", fecha, 1L);

        when(evidenciaRepository.findById(new EvidenciaId(1L))).thenReturn(Optional.of(evidencia));
        when(mapper.toDto(evidencia)).thenReturn(dtoEsperado);

        EvidenciaDTO resultado = useCase.execute(query);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("foto.jpg", resultado.archivoUrl());
        assertEquals(1L, resultado.alertaId());

        verify(evidenciaRepository).findById(new EvidenciaId(1L));
        verify(mapper).toDto(evidencia);
    }

    @Test
    @DisplayName("con id inexistente lanza ResourceNotFoundException")
    void execute_conIdInexistente_lanzaExcepcion() {
        BuscarEvidenciaQuery query = new BuscarEvidenciaQuery(99L);

        when(evidenciaRepository.findById(new EvidenciaId(99L))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(query));

        verify(evidenciaRepository).findById(new EvidenciaId(99L));
        verify(mapper, never()).toDto(any());
    }
}
