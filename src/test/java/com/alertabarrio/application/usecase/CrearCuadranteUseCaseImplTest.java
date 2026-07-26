package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.CrearCuadranteCommand;
import com.alertabarrio.application.dto.CuadranteDTO;
import com.alertabarrio.application.mapper.CuadranteDomainMapper;
import com.alertabarrio.domain.exception.CuadranteInvalidaException;
import com.alertabarrio.domain.model.Cuadrante;
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
@DisplayName("CrearCuadranteUseCaseImpl")
class CrearCuadranteUseCaseImplTest {

    @Mock
    private CuadranteRepositoryPort repository;

    @Mock
    private CuadranteDomainMapper mapper;

    @InjectMocks
    private CrearCuadranteUseCaseImpl useCase;

    @Test
    @DisplayName("con command válido persiste y devuelve DTO")
    void execute_conCommandValido_persisteYDevuelveDTO() {
        CrearCuadranteCommand command = new CrearCuadranteCommand("Bomberos", "+573001234567");
        Cuadrante cuadrantePersistido = Cuadrante.reconstruir(1L, "Bomberos", "+573001234567");
        CuadranteDTO dtoEsperado = new CuadranteDTO(1L, "Bomberos", "+573001234567");

        when(repository.save(any(Cuadrante.class))).thenReturn(cuadrantePersistido);
        when(mapper.toDto(cuadrantePersistido)).thenReturn(dtoEsperado);

        CuadranteDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Bomberos", resultado.nombreUnidad());
        assertEquals("+573001234567", resultado.telefonoEmergencia());

        verify(repository).save(any(Cuadrante.class));
        verify(mapper).toDto(cuadrantePersistido);
    }

    @Test
    @DisplayName("con nombre inválido propaga excepción del dominio sin tocar repositorio")
    void execute_conNombreInvalido_propagaExcepcion() {
        CrearCuadranteCommand command = new CrearCuadranteCommand(null, "+573001234567");

        assertThrows(CuadranteInvalidaException.class, () -> useCase.execute(command));

        verify(repository, never()).save(any());
    }
}
