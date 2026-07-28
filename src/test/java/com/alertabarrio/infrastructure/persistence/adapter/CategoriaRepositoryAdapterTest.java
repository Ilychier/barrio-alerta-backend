package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.Categoria;
import com.alertabarrio.domain.model.valueobject.CategoriaId;
import com.alertabarrio.infrastructure.persistence.entity.CategoriaEntity;
import com.alertabarrio.infrastructure.persistence.mapper.CategoriaEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.CategoriaJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("CategoriaRepositoryAdapter (integración con H2)")
class CategoriaRepositoryAdapterTest {

    @Autowired
    private CategoriaJpaRepository jpaRepository;

    private CategoriaRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        CategoriaEntityMapper mapper = new CategoriaEntityMapper() {};
        adapter = new CategoriaRepositoryAdapter(jpaRepository, mapper);
        jpaRepository.deleteAll();
    }

    @Test
    @DisplayName("save: persiste nueva Categoria y asigna id")
    void save_categoriaNueva_persisteYDevuelveConId() {
        Categoria categoria = Categoria.crear("Robo", "icon-robbery.png");
        Categoria saved = adapter.save(categoria);
        assertNotNull(saved.getId());
        assertTrue(saved.getId().value() > 0);
        assertEquals("Robo", saved.getNombre());
    }

    @Test
    @DisplayName("save: actualiza Categoria existente")
    void save_categoriaExistente_actualiza() {
        CategoriaEntity entity = new CategoriaEntity("Robo", "icon-robbery.png");
        entity = jpaRepository.save(entity);
        Categoria paraActualizar = Categoria.reconstruir(entity.getId(), "Robo Actualizado", "icon-new.png");
        Categoria actualizada = adapter.save(paraActualizar);
        assertEquals("Robo Actualizado", actualizada.getNombre());
        assertEquals("icon-new.png", actualizada.getIconoReferencia());
    }

    @Test
    @DisplayName("findById: existente devuelve Categoria")
    void findById_existente_devuelveCategoria() {
        CategoriaEntity entity = new CategoriaEntity("Robo", "icon.png");
        entity = jpaRepository.save(entity);
        Optional<Categoria> result = adapter.findById(new CategoriaId(entity.getId()));
        assertTrue(result.isPresent());
        assertEquals("Robo", result.get().getNombre());
    }

    @Test
    @DisplayName("findById: inexistente devuelve empty")
    void findById_inexistente_devuelveEmpty() {
        assertFalse(adapter.findById(new CategoriaId(999L)).isPresent());
    }

    @Test
    @DisplayName("existsById: existente devuelve true")
    void existsById_existente_devuelveTrue() {
        CategoriaEntity entity = jpaRepository.save(new CategoriaEntity("Robo", "icon.png"));
        assertTrue(adapter.existsById(new CategoriaId(entity.getId())));
    }

    @Test
    @DisplayName("existsById: inexistente devuelve false")
    void existsById_inexistente_devuelveFalse() {
        assertFalse(adapter.existsById(new CategoriaId(999L)));
    }

    @Test
    @DisplayName("deleteById: elimina y ya no existe")
    void deleteById_existente_elimina() {
        CategoriaEntity entity = jpaRepository.save(new CategoriaEntity("Robo", "icon.png"));
        CategoriaId id = new CategoriaId(entity.getId());
        adapter.deleteById(id);
        assertFalse(adapter.existsById(id));
    }

    @Test
    @DisplayName("existsByNombre: existente devuelve true")
    void existsByNombre_existente_devuelveTrue() {
        jpaRepository.save(new CategoriaEntity("Robo", "icon.png"));
        assertTrue(adapter.existsByNombre("Robo"));
    }

    @Test
    @DisplayName("existsByNombre: inexistente devuelve false")
    void existsByNombre_inexistente_devuelveFalse() {
        assertFalse(adapter.existsByNombre("Inexistente"));
    }

    @Test
    @DisplayName("findAll: devuelve página paginada")
    void findAll_devuelvePagina() {
        jpaRepository.save(new CategoriaEntity("A", "icon-a.png"));
        jpaRepository.save(new CategoriaEntity("B", "icon-b.png"));
        jpaRepository.save(new CategoriaEntity("C", "icon-c.png"));
        Pagina<Categoria> pagina = adapter.findAll(Paginacion.of(0, 2));
        assertEquals(2, pagina.contenido().size());
        assertEquals(3, pagina.totalElementos());
    }
}
