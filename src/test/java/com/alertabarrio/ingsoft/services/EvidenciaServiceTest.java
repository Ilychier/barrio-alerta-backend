package com.alertabarrio.ingsoft.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.EvidenciaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.EvidenciaSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Alerta;
import com.alertabarrio.ingsoft.models.entities.Evidencia;
import com.alertabarrio.ingsoft.repositories.AlertaRepository;
import com.alertabarrio.ingsoft.repositories.EvidenciaRepository;
import com.alertabarrio.ingsoft.services.implementation.EvidenciaServicelmpl;

@ExtendWith(MockitoExtension.class)
class EvidenciaServiceTest {

    @Mock
    private EvidenciaRepository evidenciaRepository;

    @Mock
    private AlertaRepository alertaRepository;

    @InjectMocks
    private EvidenciaServicelmpl evidenciaService;

    private Evidencia evidencia;
    private Alerta alerta;

    @BeforeEach
    void setUp() {

        alerta = new Alerta();
        alerta.setId(1L);

        evidencia = new Evidencia();
        evidencia.setId(1L);
        evidencia.setArchivoUrl("archivo.jpg");
        evidencia.setFechaSubida(LocalDateTime.now());
        evidencia.setAlerta(alerta);
    }

    @Test
    void shouldSaveSuccessfully() {

        EvidenciaSaveDTO dto =
            new EvidenciaSaveDTO(
                "archivo.jpg",
                1L
            );

        when(alertaRepository.findById(1L))
            .thenReturn(Optional.of(alerta));

        when(evidenciaRepository.save(any(Evidencia.class)))
            .thenReturn(evidencia);

        EvidenciaResponseDTO result =
            evidenciaService.save(dto);

        assertNotNull(result);
        assertEquals("archivo.jpg", result.archivoUrl());
        assertEquals(1L, result.alertaId());
    }

    @Test
    void shouldThrowWhenAlertaDoesNotExistOnSave() {

        when(alertaRepository.findById(999L))
            .thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> evidenciaService.save(
                new EvidenciaSaveDTO(
                    "archivo.jpg",
                    999L
                )
            )
        );
    }

    @Test
    void shouldFindByIdSuccessfully() {

        when(evidenciaRepository.findById(1L))
            .thenReturn(Optional.of(evidencia));

        EvidenciaResponseDTO result =
            evidenciaService.findById(1L);

        assertEquals(1L, result.id());
        assertEquals("archivo.jpg", result.archivoUrl());
    }

    @Test
    void shouldThrowWhenFindByIdNotExists() {

        when(evidenciaRepository.findById(999L))
            .thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> evidenciaService.findById(999L)
        );
    }

    @Test
    void shouldUpdateSuccessfully() {

        Alerta nuevaAlerta = new Alerta();
        nuevaAlerta.setId(2L);

        EvidenciaSaveDTO dto =
            new EvidenciaSaveDTO(
                "nuevo.jpg",
                2L
            );

        when(evidenciaRepository.findById(1L))
            .thenReturn(Optional.of(evidencia));

        when(alertaRepository.findById(2L))
            .thenReturn(Optional.of(nuevaAlerta));

        when(evidenciaRepository.save(any(Evidencia.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        EvidenciaResponseDTO result =
            evidenciaService.update(1L, dto);

        assertEquals("nuevo.jpg", result.archivoUrl());
        assertEquals(2L, result.alertaId());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingEvidencia() {

        when(evidenciaRepository.findById(999L))
            .thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> evidenciaService.update(
                999L,
                new EvidenciaSaveDTO(
                    "archivo.jpg",
                    1L
                )
            )
        );
    }

    @Test
    void shouldPatchSuccessfully() {

        Alerta nuevaAlerta = new Alerta();
        nuevaAlerta.setId(2L);

        EvidenciaSaveDTO dto =
            new EvidenciaSaveDTO(
                "patch.jpg",
                2L
            );

        when(evidenciaRepository.findById(1L))
            .thenReturn(Optional.of(evidencia));

        when(alertaRepository.findById(2L))
            .thenReturn(Optional.of(nuevaAlerta));

        when(evidenciaRepository.save(any(Evidencia.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        EvidenciaResponseDTO result =
            evidenciaService.patch(1L, dto);

        assertEquals("patch.jpg", result.archivoUrl());
        assertEquals(2L, result.alertaId());
    }

    @Test
    void shouldDeleteSuccessfully() {

        when(evidenciaRepository.existsById(1L))
            .thenReturn(true);

        evidenciaService.delete(1L);

        verify(evidenciaRepository)
            .deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingEvidencia() {

        when(evidenciaRepository.existsById(999L))
            .thenReturn(false);

        assertThrows(
            ResourceNotFoundException.class,
            () -> evidenciaService.delete(999L)
        );
    }
}