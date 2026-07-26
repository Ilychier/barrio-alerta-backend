package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.Alerta;
import com.alertabarrio.domain.model.valueobject.AlertaId;
import com.alertabarrio.infrastructure.persistence.entity.*;
import com.alertabarrio.infrastructure.persistence.mapper.AlertaEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.*;
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

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
@DisplayName("AlertaRepositoryAdapter (integración con H2)")
class AlertaRepositoryAdapterTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2024-06-15T10:00:00Z"), ZoneId.of("America/Bogota"));

    @Autowired
    private AlertaJpaRepository jpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private BarrioJpaRepository barrioJpaRepository;

    @Autowired
    private CuadranteJpaRepository cuadranteJpaRepository;

    @Autowired
    private CategoriaJpaRepository categoriaJpaRepository;

    private AlertaRepositoryAdapter adapter;
    private UserEntity usuario;
    private CategoriaEntity categoria;

    @BeforeEach
    void setUp() {
        AlertaEntityMapper mapper = new AlertaEntityMapper() {};
        adapter = new AlertaRepositoryAdapter(jpaRepository, mapper);
        CuadranteEntity cuadrante = cuadranteJpaRepository.save(new CuadranteEntity("Bomberos", "+573001234567"));
        BarrioEntity barrio = barrioJpaRepository.save(new BarrioEntity("Centro", cuadrante));
        usuario = userJpaRepository.save(new UserEntity("Juan", "juan@test.com", "+573001111111", "Calle 1", "pass123", barrio));
        categoria = categoriaJpaRepository.save(new CategoriaEntity("Robo", "icon-robbery.png"));
    }

    @Test
    @DisplayName("save: persiste nueva Alerta y asigna id")
    void save_nueva_persisteYDevuelveConId() {
        Alerta alerta = Alerta.crear("Descripción", true, usuario.getId(), categoria.getId(), FIXED_CLOCK);
        Alerta saved = adapter.save(alerta);
        assertNotNull(saved.getId());
        assertTrue(saved.getId().value() > 0);
        assertEquals("Descripción", saved.getDescripcion());
        assertTrue(saved.isEsSos());
    }

    @Test
    @DisplayName("save: actualiza Alerta existente")
    void save_existente_actualiza() {
        AlertaEntity entity = new AlertaEntity("Original", true, LocalDateTime.now(), usuario, categoria);
        entity = jpaRepository.save(entity);
        Alerta paraActualizar = Alerta.reconstruir(entity.getId(), "Actualizada", false, entity.getFechaHora(), usuario.getId(), categoria.getId());
        Alerta actualizada = adapter.save(paraActualizar);
        assertEquals("Actualizada", actualizada.getDescripcion());
        assertFalse(actualizada.isEsSos());
    }

    @Test
    @DisplayName("findById: existente devuelve Alerta")
    void findById_existente_devuelve() {
        AlertaEntity entity = new AlertaEntity("Test", true, LocalDateTime.now(), usuario, categoria);
        entity = jpaRepository.save(entity);
        Optional<Alerta> result = adapter.findById(new AlertaId(entity.getId()));
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getDescripcion());
    }

    @Test
    @DisplayName("findById: inexistente devuelve empty")
    void findById_inexistente_devuelveEmpty() {
        assertFalse(adapter.findById(new AlertaId(999L)).isPresent());
    }

    @Test
    @DisplayName("deleteById: elimina y ya no existe")
    void deleteById_existente_elimina() {
        AlertaEntity entity = new AlertaEntity("Test", true, LocalDateTime.now(), usuario, categoria);
        entity = jpaRepository.save(entity);
        AlertaId id = new AlertaId(entity.getId());
        adapter.deleteById(id);
        assertFalse(adapter.existsById(id));
    }

    @Test
    @DisplayName("findByFechaHoraBetween: filtra por rango de fechas")
    void findByFechaHoraBetween_filtra() {
        jpaRepository.save(new AlertaEntity("A", true, LocalDateTime.of(2024, 6, 1, 0, 0), usuario, categoria));
        jpaRepository.save(new AlertaEntity("B", true, LocalDateTime.of(2024, 6, 15, 0, 0), usuario, categoria));
        jpaRepository.save(new AlertaEntity("C", true, LocalDateTime.of(2024, 7, 1, 0, 0), usuario, categoria));
        Page<Alerta> pagina = adapter.findByFechaHoraBetween(
                LocalDateTime.of(2024, 6, 1, 0, 0),
                LocalDateTime.of(2024, 6, 30, 23, 59),
                PageRequest.of(0, 10));
        assertEquals(2, pagina.getTotalElements());
    }

    @Test
    @DisplayName("findAll: devuelve página paginada")
    void findAll_devuelvePagina() {
        jpaRepository.save(new AlertaEntity("A", true, LocalDateTime.now(), usuario, categoria));
        jpaRepository.save(new AlertaEntity("B", true, LocalDateTime.now(), usuario, categoria));
        Page<Alerta> pagina = adapter.findAll(PageRequest.of(0, 1));
        assertEquals(1, pagina.getNumberOfElements());
        assertEquals(2, pagina.getTotalElements());
    }
}
