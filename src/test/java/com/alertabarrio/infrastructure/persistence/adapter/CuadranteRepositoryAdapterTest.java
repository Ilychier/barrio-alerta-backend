package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.Cuadrante;
import com.alertabarrio.domain.model.valueobject.CuadranteId;
import com.alertabarrio.infrastructure.persistence.entity.CuadranteEntity;
import com.alertabarrio.infrastructure.persistence.mapper.CuadranteEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.CuadranteJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("CuadranteRepositoryAdapter (integración con H2)")
class CuadranteRepositoryAdapterTest {

    @Autowired
    private CuadranteJpaRepository jpaRepository;

    private CuadranteRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        CuadranteEntityMapper mapper = new CuadranteEntityMapper() {};
        adapter = new CuadranteRepositoryAdapter(jpaRepository, mapper);
        jpaRepository.deleteAll();
    }

    @Test
    @DisplayName("save: persiste nuevo Cuadrante y asigna id")
    void save_cuadranteNuevo_persisteYDevuelveConId() {
        Cuadrante cuadrante = Cuadrante.crear("Bomberos", "+573001234567");
        Cuadrante saved = adapter.save(cuadrante);
        assertNotNull(saved.getId());
        assertTrue(saved.getId().value() > 0);
        assertEquals("Bomberos", saved.getNombreUnidad());
    }

    @Test
    @DisplayName("save: actualiza Cuadrante existente")
    void save_cuadranteExistente_actualiza() {
        CuadranteEntity entity = new CuadranteEntity("Bomberos", "+573001234567");
        entity = jpaRepository.save(entity);
        Cuadrante paraActualizar = Cuadrante.reconstruir(entity.getId(), "Policía", "+573009876543");
        Cuadrante actualizada = adapter.save(paraActualizar);
        assertEquals("Policía", actualizada.getNombreUnidad());
        assertEquals("+573009876543", actualizada.getTelefonoEmergencia());
    }

    @Test
    @DisplayName("findById: existente devuelve Cuadrante")
    void findById_existente_devuelveCuadrante() {
        CuadranteEntity entity = new CuadranteEntity("Bomberos", "+573001234567");
        entity = jpaRepository.save(entity);
        Optional<Cuadrante> result = adapter.findById(new CuadranteId(entity.getId()));
        assertTrue(result.isPresent());
        assertEquals("Bomberos", result.get().getNombreUnidad());
    }

    @Test
    @DisplayName("findById: inexistente devuelve empty")
    void findById_inexistente_devuelveEmpty() {
        assertFalse(adapter.findById(new CuadranteId(999L)).isPresent());
    }

    @Test
    @DisplayName("existsById: existente devuelve true")
    void existsById_existente_devuelveTrue() {
        CuadranteEntity entity = jpaRepository.save(new CuadranteEntity("Bomberos", "+573001234567"));
        assertTrue(adapter.existsById(new CuadranteId(entity.getId())));
    }

    @Test
    @DisplayName("existsById: inexistente devuelve false")
    void existsById_inexistente_devuelveFalse() {
        assertFalse(adapter.existsById(new CuadranteId(999L)));
    }

    @Test
    @DisplayName("deleteById: elimina y ya no existe")
    void deleteById_existente_elimina() {
        CuadranteEntity entity = jpaRepository.save(new CuadranteEntity("Bomberos", "+573001234567"));
        CuadranteId id = new CuadranteId(entity.getId());
        adapter.deleteById(id);
        assertFalse(adapter.existsById(id));
    }

    @Test
    @DisplayName("existsByTelefonoEmergencia: existente devuelve true")
    void existsByTelefonoEmergencia_existente_devuelveTrue() {
        jpaRepository.save(new CuadranteEntity("Bomberos", "+573001234567"));
        assertTrue(adapter.existsByTelefonoEmergencia("+573001234567"));
    }

    @Test
    @DisplayName("existsByTelefonoEmergencia: inexistente devuelve false")
    void existsByTelefonoEmergencia_inexistente_devuelveFalse() {
        assertFalse(adapter.existsByTelefonoEmergencia("+579999999999"));
    }

    @Test
    @DisplayName("findAll: devuelve página paginada")
    void findAll_devuelvePagina() {
        jpaRepository.save(new CuadranteEntity("A", "+571111111111"));
        jpaRepository.save(new CuadranteEntity("B", "+572222222222"));
        jpaRepository.save(new CuadranteEntity("C", "+573333333333"));
        Page<Cuadrante> pagina = adapter.findAll(PageRequest.of(0, 2));
        assertEquals(2, pagina.getNumberOfElements());
        assertEquals(3, pagina.getTotalElements());
    }
}
