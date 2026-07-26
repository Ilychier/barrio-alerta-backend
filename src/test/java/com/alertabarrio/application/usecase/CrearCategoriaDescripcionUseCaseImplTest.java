package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.CrearCategoriaDescripcionCommand;
import com.alertabarrio.application.dto.CategoriaDescripcionDTO;
import com.alertabarrio.application.mapper.CategoriaDescripcionDomainMapper;
import com.alertabarrio.domain.exception.CategoriaDescripcionInvalidaException;
import com.alertabarrio.domain.model.CategoriaDescripcion;
import com.alertabarrio.domain.port.out.CategoriaDescripcionRepositoryPort;
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
@DisplayName("CrearCategoriaDescripcionUseCaseImpl")
class CrearCategoriaDescripcionUseCaseImplTest {

    @Mock
    private CategoriaDescripcionRepositoryPort repository;

    @Mock
    private CategoriaDescripcionDomainMapper mapper;

    @InjectMocks
    private CrearCategoriaDescripcionUseCaseImpl useCase;

    @Test
    @DisplayName("con command válido persiste y devuelve DTO")
    void execute_conCommandValido_persisteYDevuelveDTO() {
        CrearCategoriaDescripcionCommand command = new CrearCategoriaDescripcionCommand("Zona peligrosa", 1L, "img.jpg");
        CategoriaDescripcion persistida = CategoriaDescripcion.reconstruir(1L, "Zona peligrosa", 1L, "img.jpg");
        CategoriaDescripcionDTO dtoEsperado = new CategoriaDescripcionDTO(1L, "Zona peligrosa", 1L, "img.jpg");

        when(repository.save(any(CategoriaDescripcion.class))).thenReturn(persistida);
        when(mapper.toDto(persistida)).thenReturn(dtoEsperado);

        CategoriaDescripcionDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Zona peligrosa", resultado.descripcion());
        assertEquals(1L, resultado.categoriaId());
        assertEquals("img.jpg", resultado.imagenUrl());

        verify(repository).save(any(CategoriaDescripcion.class));
        verify(mapper).toDto(persistida);
    }

    @Test
    @DisplayName("con descripcion inválida propaga excepción del dominio sin tocar repositorio")
    void execute_conDescripcionInvalida_propagaExcepcion() {
        CrearCategoriaDescripcionCommand command = new CrearCategoriaDescripcionCommand(null, 1L, "img.jpg");

        assertThrows(CategoriaDescripcionInvalidaException.class, () -> useCase.execute(command));

        verify(repository, never()).save(any());
    }
}
