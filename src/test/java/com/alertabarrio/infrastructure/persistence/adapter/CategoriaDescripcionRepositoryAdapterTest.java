package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.CategoriaDescripcion;
import com.alertabarrio.domain.model.valueobject.CategoriaDescripcionId;
import com.alertabarrio.infrastructure.persistence.entity.CategoriaDescripcionEntity;
import com.alertabarrio.infrastructure.persistence.entity.CategoriaEntity;
import com.alertabarrio.infrastructure.persistence.mapper.CategoriaDescripcionEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.CategoriaDescripcionJpaRepository;
import com.alertabarrio.infrastructure.persistence.repository.CategoriaJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Sql(statements = "SET REFERENTIAL_INTEGRITY FALSE", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(statements = {
    "TRUNCATE TABLE evidencias RESTART IDENTITY",
    "TRUNCATE TABLE alertas RESTART IDENTITY",
    "TRUNCATE TABLE configuraciones RESTART IDENTITY",
    "TRUNCATE TABLE users RESTART IDENTITY",
    "TRUNCATE TABLE categoria_descripciones RESTART IDENTITY",
    "TRUNCATE TABLE barrios RESTART IDENTITY",
    "TRUNCATE TABLE cuadrantes RESTART IDENTITY",
    "TRUNCATE TABLE categorias RESTART IDENTITY",
    "SET REFERENTIAL_INTEGRITY TRUE"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("CategoriaDescripcionRepositoryAdapter (integración con H2)")
class CategoriaDescripcionRepositoryAdapterTest {

    @Autowired
    private CategoriaDescripcionJpaRepository jpaRepository;

    @Autowired
    private CategoriaJpaRepository categoriaJpaRepository;

    private CategoriaDescripcionRepositoryAdapter adapter;
    private CategoriaEntity categoria;

    @BeforeEach
    void setUp() {
        CategoriaDescripcionEntityMapper mapper = new CategoriaDescripcionEntityMapper() {};
        adapter = new CategoriaDescripcionRepositoryAdapter(jpaRepository, mapper);
        categoria = categoriaJpaRepository.save(new CategoriaEntity("Robo", "icon-robbery.png"));
    }

    @Test
    @DisplayName("save: persiste nueva CategoriaDescripcion y asigna id")
    void save_nueva_persisteYDevuelveConId() {
        CategoriaDescripcion cd = CategoriaDescripcion.crear("Descripción de prueba", categoria.getId(), "img.jpg");
        CategoriaDescripcion saved = adapter.save(cd);
        assertNotNull(saved.getId());
        assertTrue(saved.getId().value() > 0);
        assertEquals("Descripción de prueba", saved.getDescripcion());
    }

    @Test
    @DisplayName("save: actualiza CategoriaDescripcion existente")
    void save_existente_actualiza() {
        CategoriaDescripcionEntity entity = new CategoriaDescripcionEntity("Original", categoria, "img.jpg");
        entity = jpaRepository.save(entity);
        CategoriaDescripcion paraActualizar = CategoriaDescripcion.reconstruir(entity.getId(), "Actualizada", categoria.getId(), "new.jpg");
        CategoriaDescripcion actualizada = adapter.save(paraActualizar);
        assertEquals("Actualizada", actualizada.getDescripcion());
        assertEquals("new.jpg", actualizada.getImagenUrl());
    }

    @Test
    @DisplayName("findById: existente devuelve CategoriaDescripcion")
    void findById_existente_devuelve() {
        CategoriaDescripcionEntity entity = new CategoriaDescripcionEntity("Test", categoria, "img.jpg");
        entity = jpaRepository.save(entity);
        Optional<CategoriaDescripcion> result = adapter.findById(new CategoriaDescripcionId(entity.getId()));
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getDescripcion());
    }

    @Test
    @DisplayName("findById: inexistente devuelve empty")
    void findById_inexistente_devuelveEmpty() {
        assertFalse(adapter.findById(new CategoriaDescripcionId(999L)).isPresent());
    }

    @Test
    @DisplayName("existsById: existente devuelve true")
    void existsById_existente_devuelveTrue() {
        CategoriaDescripcionEntity entity = jpaRepository.save(new CategoriaDescripcionEntity("Test", categoria, "img.jpg"));
        assertTrue(adapter.existsById(new CategoriaDescripcionId(entity.getId())));
    }

    @Test
    @DisplayName("deleteById: elimina y ya no existe")
    void deleteById_existente_elimina() {
        CategoriaDescripcionEntity entity = jpaRepository.save(new CategoriaDescripcionEntity("Test", categoria, "img.jpg"));
        CategoriaDescripcionId id = new CategoriaDescripcionId(entity.getId());
        adapter.deleteById(id);
        assertFalse(adapter.existsById(id));
    }

    @Test
    @DisplayName("findByCategoriaId: filtra por categoría")
    void findByCategoriaId_filtra() {
        jpaRepository.save(new CategoriaDescripcionEntity("A", categoria, null));
        jpaRepository.save(new CategoriaDescripcionEntity("B", categoria, null));
        Page<CategoriaDescripcion> pagina = adapter.findByCategoriaId(categoria.getId(), PageRequest.of(0, 10));
        assertEquals(2, pagina.getTotalElements());
    }

    @Test
    @DisplayName("findAll: devuelve página paginada")
    void findAll_devuelvePagina() {
        jpaRepository.save(new CategoriaDescripcionEntity("A", categoria, null));
        jpaRepository.save(new CategoriaDescripcionEntity("B", categoria, null));
        Page<CategoriaDescripcion> pagina = adapter.findAll(PageRequest.of(0, 1));
        assertEquals(1, pagina.getNumberOfElements());
        assertEquals(2, pagina.getTotalElements());
    }
}
