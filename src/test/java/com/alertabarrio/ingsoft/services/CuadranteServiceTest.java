package com.alertabarrio.ingsoft.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alertabarrio.ingsoft.exceptions.ResourceConflictException;
import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.CuadranteResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CuadranteSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Cuadrante;
import com.alertabarrio.ingsoft.repositories.CuadranteRepository;
import com.alertabarrio.ingsoft.services.implementation.CuadranteServiceImpl;

@ExtendWith(MockitoExtension.class)
class CuadranteServiceTest {

    @Mock
    private CuadranteRepository cuadranteRepository;

    @InjectMocks
    private CuadranteServiceImpl cuadranteService;

    private Cuadrante cuadrante;

    @BeforeEach
    void setUp() {
        cuadrante = new Cuadrante();
        cuadrante.setId(1L);
        cuadrante.setNombreUnidad("CAI Norte");
        cuadrante.setTelefonoEmergencia("3001234567");
    }

    @Test
    void shouldSaveCuadranteSuccessfully() {

        CuadranteSaveDTO dto =
            new CuadranteSaveDTO(
                "CAI Norte",
                "3001234567"
            );

        when(cuadranteRepository.existsByTelefonoEmergencia(dto.telefonoEmergencia()))
            .thenReturn(false);

        when(cuadranteRepository.save(any(Cuadrante.class)))
            .thenReturn(cuadrante);

        CuadranteResponseDTO result = cuadranteService.save(dto);

        assertNotNull(result);
        assertEquals("CAI Norte", result.nombreUnidad());
        assertEquals("3001234567", result.telefonoEmergencia());
    }

    @Test
    void shouldThrowConflictWhenPhoneAlreadyExists() {

        CuadranteSaveDTO dto =
            new CuadranteSaveDTO(
                "CAI Norte",
                "3001234567"
            );

        when(cuadranteRepository.existsByTelefonoEmergencia("3001234567"))
            .thenReturn(true);

        assertThrows(
            ResourceConflictException.class,
            () -> cuadranteService.save(dto)
        );
    }

    @Test
    void shouldFindByIdSuccessfully() {

        when(cuadranteRepository.findById(1L))
            .thenReturn(Optional.of(cuadrante));

        CuadranteResponseDTO result =
            cuadranteService.findById(1L);

        assertEquals(1L, result.id());
        assertEquals("CAI Norte", result.nombreUnidad());
    }

    @Test
    void shouldThrowWhenFindByIdNotExists() {

        when(cuadranteRepository.findById(999L))
            .thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> cuadranteService.findById(999L)
        );
    }

    @Test
    void shouldUpdateSuccessfully() {

        CuadranteSaveDTO dto =
            new CuadranteSaveDTO(
                "CAI Sur",
                "3009999999"
            );

        when(cuadranteRepository.findById(1L))
            .thenReturn(Optional.of(cuadrante));

        when(cuadranteRepository.existsByTelefonoEmergencia("3009999999"))
            .thenReturn(false);

        when(cuadranteRepository.save(any(Cuadrante.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CuadranteResponseDTO result =
            cuadranteService.update(1L, dto);

        assertEquals("CAI Sur", result.nombreUnidad());
        assertEquals("3009999999", result.telefonoEmergencia());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingCuadrante() {

        when(cuadranteRepository.findById(999L))
            .thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> cuadranteService.update(
                999L,
                new CuadranteSaveDTO("Nuevo", "3001111111")
            )
        );
    }

    @Test
    void shouldPatchSuccessfully() {

        CuadranteSaveDTO dto =
            new CuadranteSaveDTO(
                null,
                "3007777777"
            );

        when(cuadranteRepository.findById(1L))
            .thenReturn(Optional.of(cuadrante));

        when(cuadranteRepository.existsByTelefonoEmergencia("3007777777"))
            .thenReturn(false);

        when(cuadranteRepository.save(any(Cuadrante.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CuadranteResponseDTO result =
            cuadranteService.patch(1L, dto);

        assertEquals("CAI Norte", result.nombreUnidad());
        assertEquals("3007777777", result.telefonoEmergencia());
    }

    @Test
    void shouldDeleteSuccessfully() {

        when(cuadranteRepository.existsById(1L))
            .thenReturn(true);

        cuadranteService.delete(1L);

        verify(cuadranteRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingCuadrante() {

        when(cuadranteRepository.existsById(999L))
            .thenReturn(false);

        assertThrows(
            ResourceNotFoundException.class,
            () -> cuadranteService.delete(999L)
        );
    }
}
