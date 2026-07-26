package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.Barrio;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.infrastructure.persistence.entity.BarrioEntity;
import com.alertabarrio.infrastructure.persistence.entity.CuadranteEntity;
import com.alertabarrio.infrastructure.persistence.mapper.BarrioEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.BarrioJpaRepository;
import com.alertabarrio.infrastructure.persistence.repository.CuadranteJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
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
@DisplayName("BarrioRepositoryAdapter (integración con H2)")
class BarrioRepositoryAdapterTest {

    @Autowired
    private BarrioJpaRepository jpaRepository;

    @Autowired
    private CuadranteJpaRepository cuadranteJpaRepository;

    private BarrioRepositoryAdapter adapter;
    private CuadranteEntity cuadrante;

    @BeforeEach
    void setUp() {
        BarrioEntityMapper mapper = new BarrioEntityMapper() {};
        adapter = new BarrioRepositoryAdapter(jpaRepository, mapper);
        cuadrante = cuadranteJpaRepository.save(new CuadranteEntity("Bomberos", "+573001234567"));
    }

    @Test
    @DisplayName("save: persiste nuevo Barrio y asigna id")
    void save_barrioNuevo_persisteYDevuelveConId() {
        Barrio barrio = Barrio.crear("Centro", cuadrante.getId());
        Barrio saved = adapter.save(barrio);
        assertNotNull(saved.getId());
        assertTrue(saved.getId().value() > 0);
        assertEquals("Centro", saved.getNombre());
    }

    @Test
    @DisplayName("save: actualiza Barrio existente")
    void save_barrioExistente_actualiza() {
        BarrioEntity entity = new BarrioEntity("Centro", cuadrante);
        entity = jpaRepository.save(entity);
        Barrio paraActualizar = Barrio.reconstruir(entity.getId(), "Norte", cuadrante.getId());
        Barrio actualizada = adapter.save(paraActualizar);
        assertEquals("Norte", actualizada.getNombre());
    }

    @Test
    @DisplayName("findById: existente devuelve Barrio")
    void findById_existente_devuelveBarrio() {
        BarrioEntity entity = new BarrioEntity("Centro", cuadrante);
        entity = jpaRepository.save(entity);
        Optional<Barrio> result = adapter.findById(new BarrioId(entity.getId()));
        assertTrue(result.isPresent());
        assertEquals("Centro", result.get().getNombre());
    }

    @Test
    @DisplayName("findById: inexistente devuelve empty")
    void findById_inexistente_devuelveEmpty() {
        assertFalse(adapter.findById(new BarrioId(999L)).isPresent());
    }

    @Test
    @DisplayName("existsById: existente devuelve true")
    void existsById_existente_devuelveTrue() {
        BarrioEntity entity = jpaRepository.save(new BarrioEntity("Centro", cuadrante));
        assertTrue(adapter.existsById(new BarrioId(entity.getId())));
    }

    @Test
    @DisplayName("existsById: inexistente devuelve false")
    void existsById_inexistente_devuelveFalse() {
        assertFalse(adapter.existsById(new BarrioId(999L)));
    }

    @Test
    @DisplayName("deleteById: elimina y ya no existe")
    void deleteById_existente_elimina() {
        BarrioEntity entity = jpaRepository.save(new BarrioEntity("Centro", cuadrante));
        BarrioId id = new BarrioId(entity.getId());
        adapter.deleteById(id);
        assertFalse(adapter.existsById(id));
    }

    @Test
    @DisplayName("existsByNombre: existente devuelve true")
    void existsByNombre_existente_devuelveTrue() {
        jpaRepository.save(new BarrioEntity("Centro", cuadrante));
        assertTrue(adapter.existsByNombre("Centro"));
    }

    @Test
    @DisplayName("existsByNombre: inexistente devuelve false")
    void existsByNombre_inexistente_devuelveFalse() {
        assertFalse(adapter.existsByNombre("Inexistente"));
    }

    @Test
    @DisplayName("findAll: devuelve página paginada")
    void findAll_devuelvePagina() {
        jpaRepository.save(new BarrioEntity("A", cuadrante));
        jpaRepository.save(new BarrioEntity("B", cuadrante));
        jpaRepository.save(new BarrioEntity("C", cuadrante));
        Pagina<Barrio> pagina = adapter.findAll(Paginacion.of(0, 2));
        assertEquals(2, pagina.contenido().size());
        assertEquals(3, pagina.totalElementos());
    }
}
