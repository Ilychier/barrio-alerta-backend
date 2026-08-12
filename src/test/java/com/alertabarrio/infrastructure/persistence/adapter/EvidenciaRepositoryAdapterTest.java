package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.Evidencia;
import com.alertabarrio.domain.model.valueobject.EvidenciaId;
import com.alertabarrio.infrastructure.persistence.entity.*;
import com.alertabarrio.infrastructure.persistence.mapper.EvidenciaEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.*;
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
    "TRUNCATE TABLE localidades RESTART IDENTITY",
    "TRUNCATE TABLE ciudades RESTART IDENTITY",
    "TRUNCATE TABLE cuadrantes RESTART IDENTITY",
    "TRUNCATE TABLE categorias RESTART IDENTITY",
    "SET REFERENTIAL_INTEGRITY TRUE"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("EvidenciaRepositoryAdapter (integración con H2)")
class EvidenciaRepositoryAdapterTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2024-06-15T10:00:00Z"), ZoneId.of("America/Bogota"));

    @Autowired
    private EvidenciaJpaRepository jpaRepository;

    @Autowired
    private AlertaJpaRepository alertaJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private BarrioJpaRepository barrioJpaRepository;

    @Autowired
    private CuadranteJpaRepository cuadranteJpaRepository;

    @Autowired
    private CategoriaJpaRepository categoriaJpaRepository;

    @Autowired
    private CiudadInfoJpaRepository ciudadJpaRepository;

    @Autowired
    private LocalidadJpaRepository localidadJpaRepository;

    private EvidenciaRepositoryAdapter adapter;
    private AlertaEntity alerta;

    @BeforeEach
    void setUp() {
        EvidenciaEntityMapper mapper = new EvidenciaEntityMapper() {};
        adapter = new EvidenciaRepositoryAdapter(jpaRepository, mapper);
        CuadranteEntity cuadrante = cuadranteJpaRepository.save(new CuadranteEntity("Bomberos", "+573001234567", "bomberos@test.com"));
        CiudadInfoEntity ciudad = ciudadJpaRepository.save(new CiudadInfoEntity("Medellín", "Antioquia", "Colombia"));
        LocalidadEntity localidad = localidadJpaRepository.save(new LocalidadEntity("Castilla", ciudad));
        BarrioEntity barrio = barrioJpaRepository.save(new BarrioEntity("Centro", cuadrante, localidad));
        UserEntity usuario = userJpaRepository.save(new UserEntity("Juan", "juan@test.com", "+573001111111", "Calle 1", "pass123", barrio));
        CategoriaEntity categoria = categoriaJpaRepository.save(new CategoriaEntity("Robo", "icon-robbery.png"));
        alerta = alertaJpaRepository.save(new AlertaEntity("Test", true, LocalDateTime.now(), usuario, categoria));
    }

    @Test
    @DisplayName("save: persiste nueva Evidencia y asigna id")
    void save_nueva_persisteYDevuelveConId() {
        Evidencia evidencia = Evidencia.crear("https://ejemplo.com/foto.jpg", alerta.getId(), FIXED_CLOCK);
        Evidencia saved = adapter.save(evidencia);
        assertNotNull(saved.getId());
        assertTrue(saved.getId().value() > 0);
        assertEquals("https://ejemplo.com/foto.jpg", saved.getArchivoUrl());
    }

    @Test
    @DisplayName("save: actualiza Evidencia existente")
    void save_existente_actualiza() {
        EvidenciaEntity entity = new EvidenciaEntity("https://ejemplo.com/old.jpg", LocalDateTime.now(), alerta);
        entity = jpaRepository.save(entity);
        Evidencia paraActualizar = Evidencia.reconstruir(entity.getId(), "https://ejemplo.com/new.jpg", entity.getFechaSubida(), alerta.getId());
        Evidencia actualizada = adapter.save(paraActualizar);
        assertEquals("https://ejemplo.com/new.jpg", actualizada.getArchivoUrl());
    }

    @Test
    @DisplayName("findById: existente devuelve Evidencia")
    void findById_existente_devuelve() {
        EvidenciaEntity entity = new EvidenciaEntity("https://ejemplo.com/foto.jpg", LocalDateTime.now(), alerta);
        entity = jpaRepository.save(entity);
        Optional<Evidencia> result = adapter.findById(new EvidenciaId(entity.getId()));
        assertTrue(result.isPresent());
        assertEquals("https://ejemplo.com/foto.jpg", result.get().getArchivoUrl());
    }

    @Test
    @DisplayName("findById: inexistente devuelve empty")
    void findById_inexistente_devuelveEmpty() {
        assertFalse(adapter.findById(new EvidenciaId(999L)).isPresent());
    }

    @Test
    @DisplayName("deleteById: elimina y ya no existe")
    void deleteById_existente_elimina() {
        EvidenciaEntity entity = new EvidenciaEntity("https://ejemplo.com/foto.jpg", LocalDateTime.now(), alerta);
        entity = jpaRepository.save(entity);
        EvidenciaId id = new EvidenciaId(entity.getId());
        adapter.deleteById(id);
        assertFalse(adapter.existsById(id));
    }

    @Test
    @DisplayName("findByAlertaId: filtra por alerta")
    void findByAlertaId_filtra() {
        jpaRepository.save(new EvidenciaEntity("https://ejemplo.com/1.jpg", LocalDateTime.now(), alerta));
        jpaRepository.save(new EvidenciaEntity("https://ejemplo.com/2.jpg", LocalDateTime.now(), alerta));
        Pagina<Evidencia> pagina = adapter.findByAlertaId(alerta.getId(), Paginacion.of(0, 10));
        assertEquals(2, pagina.totalElementos());
    }

    @Test
    @DisplayName("findAll: devuelve página paginada")
    void findAll_devuelvePagina() {
        jpaRepository.save(new EvidenciaEntity("https://ejemplo.com/1.jpg", LocalDateTime.now(), alerta));
        jpaRepository.save(new EvidenciaEntity("https://ejemplo.com/2.jpg", LocalDateTime.now(), alerta));
        Pagina<Evidencia> pagina = adapter.findAll(Paginacion.of(0, 1));
        assertEquals(1, pagina.contenido().size());
        assertEquals(2, pagina.totalElementos());
    }
}
