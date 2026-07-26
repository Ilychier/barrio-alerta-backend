package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.CrearCategoriaCommand;
import com.alertabarrio.application.dto.CategoriaDTO;
import com.alertabarrio.application.mapper.CategoriaDomainMapper;
import com.alertabarrio.domain.exception.CategoriaInvalidaException;
import com.alertabarrio.domain.model.Categoria;
import com.alertabarrio.domain.port.out.CategoriaRepositoryPort;
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
@DisplayName("CrearCategoriaUseCaseImpl")
class CrearCategoriaUseCaseImplTest {

    @Mock
    private CategoriaRepositoryPort repository;

    @Mock
    private CategoriaDomainMapper mapper;

    @InjectMocks
    private CrearCategoriaUseCaseImpl useCase;

    @Test
    @DisplayName("con command válido persiste y devuelve DTO")
    void execute_conCommandValido_persisteYDevuelveDTO() {
        CrearCategoriaCommand command = new CrearCategoriaCommand("Robo", "icon.png");
        Categoria categoriaPersistida = Categoria.reconstruir(1L, "Robo", "icon.png");
        CategoriaDTO dtoEsperado = new CategoriaDTO(1L, "Robo", "icon.png");

        when(repository.save(any(Categoria.class))).thenReturn(categoriaPersistida);
        when(mapper.toDto(categoriaPersistida)).thenReturn(dtoEsperado);

        CategoriaDTO resultado = useCase.execute(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Robo", resultado.nombre());
        assertEquals("icon.png", resultado.iconoReferencia());

        verify(repository).save(any(Categoria.class));
        verify(mapper).toDto(categoriaPersistida);
    }

    @Test
    @DisplayName("con nombre inválido propaga excepción del dominio sin tocar repositorio")
    void execute_conNombreInvalido_propagaExcepcion() {
        CrearCategoriaCommand command = new CrearCategoriaCommand(null, "icon.png");

        assertThrows(CategoriaInvalidaException.class, () -> useCase.execute(command));

        verify(repository, never()).save(any());
    }
}
