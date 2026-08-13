package com.alertabarrio.infrastructure.persistence.adapter.mascotas;

import com.alertabarrio.domain.model.mascotas.Ciudad;
import com.alertabarrio.domain.model.mascotas.valueobject.CiudadId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.infrastructure.persistence.mapper.mascotas.CiudadEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.mascotas.CiudadJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
@DisplayName("CiudadRepositoryAdapter (integración con H2)")
class CiudadRepositoryAdapterTest {

    @Autowired
    private CiudadJpaRepository jpaRepository;

    private CiudadRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        CiudadEntityMapper mapper = new CiudadEntityMapper() {};
        adapter = new CiudadRepositoryAdapter(jpaRepository, mapper);
    }

    @Test
    @DisplayName("save: persiste nueva Ciudad y asigna id")
    void save_nueva_persisteYDevuelveConId() {
        Ciudad saved = adapter.save(Ciudad.crear("Quibdó", "Chocó", "Colombia"));

        assertNotNull(saved.getId());
        assertTrue(saved.getId().value() > 0);
        assertEquals("Quibdó", saved.getNombre());
        assertEquals("Chocó", saved.getDepartamento());
    }

    @Test
    @DisplayName("findById: existente devuelve Ciudad")
    void findById_existente_devuelve() {
        Ciudad saved = adapter.save(Ciudad.crear("Cali", "Valle del Cauca", "Colombia"));

        Optional<Ciudad> result = adapter.findById(new CiudadId(saved.getId().value()));

        assertTrue(result.isPresent());
        assertEquals("Cali", result.get().getNombre());
    }

    @Test
    @DisplayName("findById: inexistente devuelve empty")
    void findById_inexistente_devuelveEmpty() {
        assertFalse(adapter.findById(new CiudadId(999L)).isPresent());
    }

    @Test
    @DisplayName("existsById: true para existente, false para inexistente")
    void existsById_verifica() {
        Ciudad saved = adapter.save(Ciudad.crear("Bogotá", "Bogotá", "Colombia"));
        assertTrue(adapter.existsById(new CiudadId(saved.getId().value())));
        assertFalse(adapter.existsById(new CiudadId(999L)));
    }

    @Test
    @DisplayName("findAll: devuelve página paginada")
    void findAll_devuelvePagina() {
        adapter.save(Ciudad.crear("Cali", "Valle del Cauca", "Colombia"));
        adapter.save(Ciudad.crear("Quibdó", "Chocó", "Colombia"));
        adapter.save(Ciudad.crear("Bogotá", "Bogotá", "Colombia"));

        Pagina<Ciudad> pagina = adapter.findAll(Paginacion.of(0, 2));

        assertEquals(2, pagina.contenido().size());
        assertEquals(3, pagina.totalElementos());
        assertEquals(2, pagina.totalPaginas());
    }
}
