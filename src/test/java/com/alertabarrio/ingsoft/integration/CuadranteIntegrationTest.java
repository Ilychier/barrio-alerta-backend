package com.alertabarrio.ingsoft.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.CuadranteResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CuadranteSaveDTO;
import com.alertabarrio.ingsoft.services.CuadranteService;

@SpringBootTest
@Transactional
class CuadranteIntegrationTest {

    @Autowired
    private CuadranteService cuadranteService;

    @Test
    void shouldCreateCuadranteSuccessfully() {

        CuadranteResponseDTO created =
            cuadranteService.save(
                new CuadranteSaveDTO(
                    "CAI Norte",
                    "3001234567"
                )
            );

        assertNotNull(created);
        assertNotNull(created.id());
        assertEquals("CAI Norte", created.nombreUnidad());
        assertEquals("3001234567", created.telefonoEmergencia());
    }

    @Test
    void shouldFindCuadranteByIdSuccessfully() {

        CuadranteResponseDTO created =
            cuadranteService.save(
                new CuadranteSaveDTO(
                    "CAI Sur",
                    "3007654321"
                )
            );

        CuadranteResponseDTO found =
            cuadranteService.findById(created.id());

        assertEquals(created.id(), found.id());
        assertEquals("CAI Sur", found.nombreUnidad());
        assertEquals("3007654321", found.telefonoEmergencia());
    }

    @Test
    void shouldUpdateCuadranteSuccessfully() {

        CuadranteResponseDTO created =
            cuadranteService.save(
                new CuadranteSaveDTO(
                    "CAI Centro",
                    "3001111111"
                )
            );

        CuadranteResponseDTO updated =
            cuadranteService.update(
                created.id(),
                new CuadranteSaveDTO(
                    "CAI Centro Actualizado",
                    "3002222222"
                )
            );

        assertEquals(
            "CAI Centro Actualizado",
            updated.nombreUnidad()
        );
        assertEquals(
            "3002222222",
            updated.telefonoEmergencia()
        );
    }

    @Test
    void shouldPatchCuadranteSuccessfully() {

        CuadranteResponseDTO created =
            cuadranteService.save(
                new CuadranteSaveDTO(
                    "CAI Occidente",
                    "3003333333"
                )
            );

        CuadranteResponseDTO patched =
            cuadranteService.patch(
                created.id(),
                new CuadranteSaveDTO(
                    null,
                    "3004444444"
                )
            );

        assertEquals(
            "CAI Occidente",
            patched.nombreUnidad()
        );

        assertEquals(
            "3004444444",
            patched.telefonoEmergencia()
        );
    }

    @Test
    void shouldDeleteCuadranteSuccessfully() {

        CuadranteResponseDTO created =
            cuadranteService.save(
                new CuadranteSaveDTO(
                    "CAI Eliminar",
                    "3005555555"
                )
            );

        cuadranteService.delete(created.id());

        assertThrows(
            ResourceNotFoundException.class,
            () -> cuadranteService.findById(created.id())
        );
    }

    @Test
    void shouldThrowExceptionWhenFindingNonExistingCuadrante() {

        assertThrows(
            ResourceNotFoundException.class,
            () -> cuadranteService.findById(99999L)
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingCuadrante() {

        assertThrows(
            ResourceNotFoundException.class,
            () -> cuadranteService.update(
                99999L,
                new CuadranteSaveDTO(
                    "No Existe",
                    "3009999999"
                )
            )
        );
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingCuadrante() {

        assertThrows(
            ResourceNotFoundException.class,
            () -> cuadranteService.delete(99999L)
        );
    }
}
