package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.User;
import com.alertabarrio.domain.model.valueobject.UsuarioId;
import com.alertabarrio.infrastructure.persistence.entity.BarrioEntity;
import com.alertabarrio.infrastructure.persistence.entity.CiudadInfoEntity;
import com.alertabarrio.infrastructure.persistence.entity.CuadranteEntity;
import com.alertabarrio.infrastructure.persistence.entity.UserEntity;
import com.alertabarrio.infrastructure.persistence.mapper.UserEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.BarrioJpaRepository;
import com.alertabarrio.infrastructure.persistence.repository.CiudadInfoJpaRepository;
import com.alertabarrio.infrastructure.persistence.repository.CuadranteJpaRepository;
import com.alertabarrio.infrastructure.persistence.repository.UserJpaRepository;
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
    "TRUNCATE TABLE ciudades RESTART IDENTITY",
    "TRUNCATE TABLE cuadrantes RESTART IDENTITY",
    "TRUNCATE TABLE categorias RESTART IDENTITY",
    "SET REFERENTIAL_INTEGRITY TRUE"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("UsuarioRepositoryAdapter (integración con H2)")
class UsuarioRepositoryAdapterTest {

    @Autowired
    private UserJpaRepository jpaRepository;

    @Autowired
    private BarrioJpaRepository barrioJpaRepository;

    @Autowired
    private CuadranteJpaRepository cuadranteJpaRepository;

    @Autowired
    private CiudadInfoJpaRepository ciudadJpaRepository;

    private UsuarioRepositoryAdapter adapter;
    private BarrioEntity barrio;

    @BeforeEach
    void setUp() {
        UserEntityMapper mapper = new UserEntityMapper() {};
        adapter = new UsuarioRepositoryAdapter(jpaRepository, mapper);
        CuadranteEntity cuadrante = cuadranteJpaRepository.save(new CuadranteEntity("Bomberos", "+573001234567", "bomberos@test.com"));
        CiudadInfoEntity ciudad = ciudadJpaRepository.save(new CiudadInfoEntity("Medellín", "Antioquia", "Colombia"));
        barrio = barrioJpaRepository.save(new BarrioEntity("Centro", cuadrante, ciudad));
    }

    @Test
    @DisplayName("save: persiste nuevo User y asigna id")
    void save_nuevo_persisteYDevuelveConId() {
        User user = User.crear("Juan", "juan@test.com", "+573001111111", "Calle 1", "pass123", barrio.getId());
        User saved = adapter.save(user);
        assertNotNull(saved.getId());
        assertTrue(saved.getId().value() > 0);
        assertEquals("Juan", saved.getName());
    }

    @Test
    @DisplayName("save: actualiza User existente")
    void save_existente_actualiza() {
        UserEntity entity = new UserEntity("Juan", "juan@test.com", "+573001111111", "Calle 1", "pass123", barrio);
        entity = jpaRepository.save(entity);
        User paraActualizar = User.reconstruir(entity.getId(), "Juan Actualizado", "juan@test.com", "+573001111111", "Calle 2", "newpass", barrio.getId());
        User actualizado = adapter.save(paraActualizar);
        assertEquals("Juan Actualizado", actualizado.getName());
        assertEquals("Calle 2", actualizado.getAddress());
    }

    @Test
    @DisplayName("findById: existente devuelve User")
    void findById_existente_devuelve() {
        UserEntity entity = new UserEntity("Juan", "juan@test.com", "+573001111111", "Calle 1", "pass123", barrio);
        entity = jpaRepository.save(entity);
        Optional<User> result = adapter.findById(new UsuarioId(entity.getId()));
        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getName());
    }

    @Test
    @DisplayName("findById: inexistente devuelve empty")
    void findById_inexistente_devuelveEmpty() {
        assertFalse(adapter.findById(new UsuarioId(999L)).isPresent());
    }

    @Test
    @DisplayName("existsByEmail: existente devuelve true")
    void existsByEmail_existente_devuelveTrue() {
        jpaRepository.save(new UserEntity("Juan", "juan@test.com", "+573001111111", "Calle 1", "pass123", barrio));
        assertTrue(adapter.existsByEmail("juan@test.com"));
    }

    @Test
    @DisplayName("existsByEmail: inexistente devuelve false")
    void existsByEmail_inexistente_devuelveFalse() {
        assertFalse(adapter.existsByEmail("noexiste@test.com"));
    }

    @Test
    @DisplayName("findByEmail: existente devuelve User")
    void findByEmail_existente_devuelve() {
        jpaRepository.save(new UserEntity("Juan", "juan@test.com", "+573001111111", "Calle 1", "pass123", barrio));
        Optional<User> result = adapter.findByEmail("juan@test.com");
        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getName());
    }

    @Test
    @DisplayName("findByEmail: inexistente devuelve empty")
    void findByEmail_inexistente_devuelveEmpty() {
        assertFalse(adapter.findByEmail("noexiste@test.com").isPresent());
    }

    @Test
    @DisplayName("deleteById: elimina y ya no existe")
    void deleteById_existente_elimina() {
        UserEntity entity = jpaRepository.save(new UserEntity("Juan", "juan@test.com", "+573001111111", "Calle 1", "pass123", barrio));
        UsuarioId id = new UsuarioId(entity.getId());
        adapter.deleteById(id);
        assertFalse(adapter.existsById(id));
    }

    @Test
    @DisplayName("findAll: devuelve página paginada")
    void findAll_devuelvePagina() {
        jpaRepository.save(new UserEntity("A", "a@test.com", "+573001111111", "Dir1", "pass", barrio));
        jpaRepository.save(new UserEntity("B", "b@test.com", "+573002222222", "Dir2", "pass", barrio));
        Pagina<User> pagina = adapter.findAll(Paginacion.of(0, 1));
        assertEquals(1, pagina.contenido().size());
        assertEquals(2, pagina.totalElementos());
    }
}
