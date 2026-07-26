package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.Configuracion;
import com.alertabarrio.domain.model.valueobject.ConfiguracionId;
import com.alertabarrio.infrastructure.persistence.entity.*;
import com.alertabarrio.infrastructure.persistence.mapper.ConfiguracionEntityMapper;
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
@DisplayName("ConfiguracionRepositoryAdapter (integración con H2)")
class ConfiguracionRepositoryAdapterTest {

    @Autowired
    private ConfiguracionJpaRepository jpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private BarrioJpaRepository barrioJpaRepository;

    @Autowired
    private CuadranteJpaRepository cuadranteJpaRepository;

    private ConfiguracionRepositoryAdapter adapter;
    private UserEntity usuario;

    @BeforeEach
    void setUp() {
        ConfiguracionEntityMapper mapper = new ConfiguracionEntityMapper() {};
        adapter = new ConfiguracionRepositoryAdapter(jpaRepository, mapper);
        CuadranteEntity cuadrante = cuadranteJpaRepository.save(new CuadranteEntity("Bomberos", "+573001234567"));
        BarrioEntity barrio = barrioJpaRepository.save(new BarrioEntity("Centro", cuadrante));
        usuario = userJpaRepository.save(new UserEntity("Juan", "juan@test.com", "+573001111111", "Calle 1", "pass123", barrio));
    }

    @Test
    @DisplayName("save: persiste nueva Configuracion y asigna id")
    void save_nueva_persisteYDevuelveConId() {
        Configuracion config = Configuracion.crear(usuario.getId(), true, false);
        Configuracion saved = adapter.save(config);
        assertNotNull(saved.getId());
        assertTrue(saved.getId().value() > 0);
        assertTrue(saved.isRecibirNotificaciones());
    }

    @Test
    @DisplayName("save: actualiza Configuracion existente")
    void save_existente_actualiza() {
        ConfiguracionEntity entity = new ConfiguracionEntity(usuario, true, false);
        entity = jpaRepository.save(entity);
        Configuracion paraActualizar = Configuracion.reconstruir(entity.getId(), usuario.getId(), false, true);
        Configuracion actualizada = adapter.save(paraActualizar);
        assertFalse(actualizada.isRecibirNotificaciones());
        assertTrue(actualizada.isModoSilencioso());
    }

    @Test
    @DisplayName("findById: existente devuelve Configuracion")
    void findById_existente_devuelve() {
        ConfiguracionEntity entity = new ConfiguracionEntity(usuario, true, false);
        entity = jpaRepository.save(entity);
        Optional<Configuracion> result = adapter.findById(new ConfiguracionId(entity.getId()));
        assertTrue(result.isPresent());
        assertTrue(result.get().isRecibirNotificaciones());
    }

    @Test
    @DisplayName("findById: inexistente devuelve empty")
    void findById_inexistente_devuelveEmpty() {
        assertFalse(adapter.findById(new ConfiguracionId(999L)).isPresent());
    }

    @Test
    @DisplayName("findByUsuarioId: existente devuelve Configuracion")
    void findByUsuarioId_existente_devuelve() {
        jpaRepository.save(new ConfiguracionEntity(usuario, true, false));
        Optional<Configuracion> result = adapter.findByUsuarioId(usuario.getId());
        assertTrue(result.isPresent());
        assertTrue(result.get().isRecibirNotificaciones());
    }

    @Test
    @DisplayName("findByUsuarioId: inexistente devuelve empty")
    void findByUsuarioId_inexistente_devuelveEmpty() {
        assertFalse(adapter.findByUsuarioId(999L).isPresent());
    }

    @Test
    @DisplayName("deleteById: elimina y ya no existe")
    void deleteById_existente_elimina() {
        ConfiguracionEntity entity = jpaRepository.save(new ConfiguracionEntity(usuario, true, false));
        ConfiguracionId id = new ConfiguracionId(entity.getId());
        adapter.deleteById(id);
        assertFalse(adapter.existsById(id));
    }

    @Test
    @DisplayName("findAll: devuelve página paginada")
    void findAll_devuelvePagina() {
        jpaRepository.save(new ConfiguracionEntity(usuario, true, false));
        jpaRepository.save(new ConfiguracionEntity(usuario, false, true));
        Page<Configuracion> pagina = adapter.findAll(PageRequest.of(0, 1));
        assertEquals(1, pagina.getNumberOfElements());
        assertEquals(2, pagina.getTotalElements());
    }
}
