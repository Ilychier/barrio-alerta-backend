package com.alertabarrio.infrastructure.persistence.adapter.mascotas;

import com.alertabarrio.domain.model.mascotas.TipoMascota;
import com.alertabarrio.domain.model.mascotas.valueobject.TipoMascotaId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.infrastructure.persistence.mapper.mascotas.TipoMascotaEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.mascotas.TipoMascotaJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Sql(statements = "SET REFERENTIAL_INTEGRITY FALSE", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(statements = {
    "TRUNCATE TABLE reportes_mascotas RESTART IDENTITY",
    "TRUNCATE TABLE tipos_mascota RESTART IDENTITY",
    "TRUNCATE TABLE ciudades RESTART IDENTITY",
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
@DisplayName("TipoMascotaRepositoryAdapter (integración con H2)")
class TipoMascotaRepositoryAdapterTest {

    @Autowired
    private TipoMascotaJpaRepository jpaRepository;

    private TipoMascotaRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        TipoMascotaEntityMapper mapper = new TipoMascotaEntityMapper() {};
        adapter = new TipoMascotaRepositoryAdapter(jpaRepository, mapper);
    }

    @Test
    @DisplayName("save: persiste nuevo TipoMascota y asigna id")
    void save_nuevo_persisteYDevuelveConId() {
        TipoMascota saved = adapter.save(TipoMascota.crear("Perro"));

        assertNotNull(saved.getId());
        assertTrue(saved.getId().value() > 0);
        assertEquals("Perro", saved.getNombre());
        assertTrue(saved.isActivo());
    }

    @Test
    @DisplayName("findById: existente devuelve TipoMascota")
    void findById_existente_devuelve() {
        TipoMascota saved = adapter.save(TipoMascota.crear("Gato"));

        Optional<TipoMascota> result = adapter.findById(new TipoMascotaId(saved.getId().value()));

        assertTrue(result.isPresent());
        assertEquals("Gato", result.get().getNombre());
    }

    @Test
    @DisplayName("findById: inexistente devuelve empty")
    void findById_inexistente_devuelveEmpty() {
        assertFalse(adapter.findById(new TipoMascotaId(999L)).isPresent());
    }

    @Test
    @DisplayName("findAllActivos: devuelve solo los activos")
    void findAllActivos_devuelveSoloActivos() {
        adapter.save(TipoMascota.crear("Perro"));
        adapter.save(TipoMascota.crear("Gato"));
        jpaRepository.save(new com.alertabarrio.infrastructure.persistence.entity.mascotas.TipoMascotaEntity("Conejo", false));

        List<TipoMascota> activos = adapter.findAllActivos();

        assertEquals(2, activos.size());
        assertTrue(activos.stream().allMatch(TipoMascota::isActivo));
    }

    @Test
    @DisplayName("findAll: devuelve página paginada")
    void findAll_devuelvePagina() {
        adapter.save(TipoMascota.crear("Perro"));
        adapter.save(TipoMascota.crear("Gato"));

        Pagina<TipoMascota> pagina = adapter.findAll(Paginacion.of(0, 1));

        assertEquals(1, pagina.contenido().size());
        assertEquals(2, pagina.totalElementos());
    }
}
