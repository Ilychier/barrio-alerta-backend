package com.alertabarrio.infrastructure.persistence.adapter.mascotas;

import com.alertabarrio.domain.model.mascotas.EstadoReporte;
import com.alertabarrio.domain.model.mascotas.ReporteMascota;
import com.alertabarrio.domain.model.mascotas.TipoReporte;
import com.alertabarrio.domain.model.mascotas.valueobject.ReporteMascotaId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.model.valueobject.UsuarioId;
import com.alertabarrio.infrastructure.persistence.entity.BarrioEntity;
import com.alertabarrio.infrastructure.persistence.entity.CuadranteEntity;
import com.alertabarrio.infrastructure.persistence.entity.UserEntity;
import com.alertabarrio.infrastructure.persistence.entity.mascotas.CiudadEntity;
import com.alertabarrio.infrastructure.persistence.entity.mascotas.ReporteMascotaEntity;
import com.alertabarrio.infrastructure.persistence.entity.mascotas.TipoMascotaEntity;
import com.alertabarrio.infrastructure.persistence.mapper.mascotas.ReporteMascotaEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.BarrioJpaRepository;
import com.alertabarrio.infrastructure.persistence.repository.CuadranteJpaRepository;
import com.alertabarrio.infrastructure.persistence.repository.UserJpaRepository;
import com.alertabarrio.infrastructure.persistence.repository.mascotas.CiudadJpaRepository;
import com.alertabarrio.infrastructure.persistence.repository.mascotas.ReporteMascotaJpaRepository;
import com.alertabarrio.infrastructure.persistence.repository.mascotas.TipoMascotaJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
@DisplayName("ReporteMascotaRepositoryAdapter (integración con H2)")
class ReporteMascotaRepositoryAdapterTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2026-08-11T10:00:00Z"), ZoneId.of("America/Bogota"));

    @Autowired
    private ReporteMascotaJpaRepository jpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private BarrioJpaRepository barrioJpaRepository;

    @Autowired
    private CuadranteJpaRepository cuadranteJpaRepository;

    @Autowired
    private CiudadJpaRepository ciudadJpaRepository;

    @Autowired
    private TipoMascotaJpaRepository tipoMascotaJpaRepository;

    private ReporteMascotaRepositoryAdapter adapter;
    private UserEntity usuario;
    private CiudadEntity ciudad;
    private TipoMascotaEntity tipoMascota;

    @BeforeEach
    void setUp() {
        ReporteMascotaEntityMapper mapper = new ReporteMascotaEntityMapper() {};
        adapter = new ReporteMascotaRepositoryAdapter(jpaRepository, mapper);
        CuadranteEntity cuadrante = cuadranteJpaRepository.save(
                new CuadranteEntity("Bomberos", "+573001234567", "bomberos@test.com"));
        BarrioEntity barrio = barrioJpaRepository.save(new BarrioEntity("Centro", cuadrante));
        usuario = userJpaRepository.save(
                new UserEntity("Juan", "juan@test.com", "+573001111111", "Calle 1", "pass123", barrio));
        ciudad = ciudadJpaRepository.save(new CiudadEntity("Cali", "Valle del Cauca", "Colombia"));
        tipoMascota = tipoMascotaJpaRepository.save(new TipoMascotaEntity("Perro", true));
    }

    private ReporteMascota crearReporte(String tipoReporte) {
        return ReporteMascota.crear(
                tipoReporte, tipoMascota.getId(), ciudad.getId(),
                "Barrio La Soledad", "+573001234567", "Perro criollo", usuario.getId(), FIXED_CLOCK);
    }

    @Test
    @DisplayName("save: persiste nuevo ReporteMascota y asigna id")
    void save_nueva_persisteYDevuelveConId() {
        ReporteMascota saved = adapter.save(crearReporte("LOST"));

        assertNotNull(saved.getId());
        assertTrue(saved.getId().value() > 0);
        assertEquals(TipoReporte.LOST, saved.getTipoReporte());
        assertEquals(EstadoReporte.ACTIVE, saved.getEstado());
        assertEquals("Barrio La Soledad", saved.getUbicacion());
    }

    @Test
    @DisplayName("save: actualiza ReporteMascota existente")
    void save_existente_actualiza() {
        ReporteMascotaEntity entity = new ReporteMascotaEntity(
                usuario, "LOST", tipoMascota, ciudad, "Original", "+573001234567", null,
                "ACTIVE", LocalDateTime.now(), LocalDateTime.now());
        entity = jpaRepository.save(entity);

        ReporteMascota rescatada = ReporteMascota.reconstruir(
                entity.getId(), "LOST", tipoMascota.getId(), ciudad.getId(),
                "Actualizada", "+573001234567", null, "RESCUED",
                entity.getCreatedAt(), entity.getUpdatedAt(), usuario.getId());

        ReporteMascota actualizada = adapter.save(rescatada);

        assertEquals("Actualizada", actualizada.getUbicacion());
        assertEquals(EstadoReporte.RESCUED, actualizada.getEstado());
    }

    @Test
    @DisplayName("findById: existente devuelve ReporteMascota")
    void findById_existente_devuelve() {
        ReporteMascota saved = adapter.save(crearReporte("FOUND"));

        Optional<ReporteMascota> result = adapter.findById(new ReporteMascotaId(saved.getId().value()));

        assertTrue(result.isPresent());
        assertEquals(TipoReporte.FOUND, result.get().getTipoReporte());
        assertEquals(usuario.getId(), result.get().getUsuarioId().value());
    }

    @Test
    @DisplayName("findById: inexistente devuelve empty")
    void findById_inexistente_devuelveEmpty() {
        assertFalse(adapter.findById(new ReporteMascotaId(999L)).isPresent());
    }

    @Test
    @DisplayName("existsById: true para existente, false para inexistente")
    void existsById_verifica() {
        ReporteMascota saved = adapter.save(crearReporte("LOST"));
        assertTrue(adapter.existsById(new ReporteMascotaId(saved.getId().value())));
        assertFalse(adapter.existsById(new ReporteMascotaId(999L)));
    }

    @Test
    @DisplayName("findByFilters: filtra por estado y tipo y ciudad")
    void findByFilters_filtra() {
        adapter.save(crearReporte("LOST"));
        adapter.save(crearReporte("FOUND"));
        ReporteMascota rescatada = adapter.save(crearReporte("LOST"))
                .cambiarEstado(EstadoReporte.RESCUED, FIXED_CLOCK);
        adapter.save(rescatada);

        Pagina<ReporteMascota> pagina = adapter.findByFilters(
                EstadoReporte.ACTIVE, TipoReporte.LOST, ciudad.getId(), Paginacion.of(0, 10));

        assertEquals(1, pagina.totalElementos());
        assertEquals("LOST", pagina.contenido().get(0).getTipoReporte().name());
    }

    @Test
    @DisplayName("findByFilters: sin filtros excluye DELETED (regresión E2E soft delete)")
    void findByFilters_excluyeEliminados() {
        adapter.save(crearReporte("LOST"));
        ReporteMascota eliminada = adapter.save(crearReporte("FOUND")).eliminar(FIXED_CLOCK);
        adapter.save(eliminada);

        Pagina<ReporteMascota> pagina = adapter.findByFilters(null, null, null, Paginacion.of(0, 10));

        assertEquals(1, pagina.totalElementos());
        assertEquals(TipoReporte.LOST, pagina.contenido().get(0).getTipoReporte());
    }

    @Test
    @DisplayName("findByFilters: solo tipoReporte excluye DELETED (regresión E2E)")
    void findByFilters_tipoReporte_excluyeEliminados() {
        adapter.save(crearReporte("LOST"));
        ReporteMascota eliminada = adapter.save(crearReporte("LOST")).eliminar(FIXED_CLOCK);
        adapter.save(eliminada);

        Pagina<ReporteMascota> pagina = adapter.findByFilters(null, TipoReporte.LOST, null, Paginacion.of(0, 10));

        assertEquals(1, pagina.totalElementos());
    }

    @Test
    @DisplayName("findByFilters: solo ciudadId excluye DELETED (regresión E2E)")
    void findByFilters_ciudad_excluyeEliminados() {
        adapter.save(crearReporte("LOST"));
        ReporteMascota eliminada = adapter.save(crearReporte("FOUND")).eliminar(FIXED_CLOCK);
        adapter.save(eliminada);

        Pagina<ReporteMascota> pagina = adapter.findByFilters(null, null, ciudad.getId(), Paginacion.of(0, 10));

        assertEquals(1, pagina.totalElementos());
    }

    @Test
    @DisplayName("findByFilters: tipoReporte + ciudadId excluye DELETED (regresión E2E)")
    void findByFilters_tipoYCiudad_excluyeEliminados() {
        adapter.save(crearReporte("LOST"));
        ReporteMascota eliminada = adapter.save(crearReporte("LOST")).eliminar(FIXED_CLOCK);
        adapter.save(eliminada);

        Pagina<ReporteMascota> pagina = adapter.findByFilters(null, TipoReporte.LOST, ciudad.getId(), Paginacion.of(0, 10));

        assertEquals(1, pagina.totalElementos());
    }

    @Test
    @DisplayName("findByFilters: solo tipoReporte filtra correctamente (regresión E2E)")
    void findByFilters_soloTipoReporte_filtra() {
        adapter.save(crearReporte("LOST"));
        adapter.save(crearReporte("FOUND"));

        Pagina<ReporteMascota> lost = adapter.findByFilters(null, TipoReporte.LOST, null, Paginacion.of(0, 10));
        Pagina<ReporteMascota> found = adapter.findByFilters(null, TipoReporte.FOUND, null, Paginacion.of(0, 10));

        assertEquals(1, lost.totalElementos());
        assertEquals(TipoReporte.LOST, lost.contenido().get(0).getTipoReporte());
        assertEquals(1, found.totalElementos());
    }

    @Test
    @DisplayName("findByFilters: solo ciudadId filtra correctamente (regresión E2E)")
    void findByFilters_soloCiudad_filtra() {
        adapter.save(crearReporte("LOST")); // ciudad 1
        adapter.save(crearReporte("FOUND")); // ciudad 1

        CiudadEntity otraCiudad = ciudadJpaRepository.save(new CiudadEntity("Quibdó", "Chocó", "Colombia"));
        ReporteMascota enQuibdo = ReporteMascota.crear(
                "FOUND", tipoMascota.getId(), otraCiudad.getId(),
                "Parque de Quibdó", "+573173784522", "Gato gris", usuario.getId(), FIXED_CLOCK);
        adapter.save(enQuibdo);

        Pagina<ReporteMascota> deCiudad1 = adapter.findByFilters(null, null, ciudad.getId(), Paginacion.of(0, 10));
        Pagina<ReporteMascota> deQuibdo = adapter.findByFilters(null, null, otraCiudad.getId(), Paginacion.of(0, 10));

        assertEquals(2, deCiudad1.totalElementos());
        assertEquals(1, deQuibdo.totalElementos());
    }

    @Test
    @DisplayName("findByFilters: tipoReporte + ciudadId combina filtros (regresión E2E)")
    void findByFilters_tipoYCiudad_combina() {
        adapter.save(crearReporte("LOST")); // ciudad 1, LOST
        adapter.save(crearReporte("FOUND")); // ciudad 1, FOUND

        Pagina<ReporteMascota> lostEnCiudad1 = adapter.findByFilters(null, TipoReporte.LOST, ciudad.getId(), Paginacion.of(0, 10));
        Pagina<ReporteMascota> foundEnCiudad1 = adapter.findByFilters(null, TipoReporte.FOUND, ciudad.getId(), Paginacion.of(0, 10));

        assertEquals(1, lostEnCiudad1.totalElementos());
        assertEquals(TipoReporte.LOST, lostEnCiudad1.contenido().get(0).getTipoReporte());
        assertEquals(1, foundEnCiudad1.totalElementos());
    }

    @Test
    @DisplayName("findByEstado: devuelve solo reportes con ese estado")
    void findByEstado_filtra() {
        adapter.save(crearReporte("LOST"));
        ReporteMascota rescatada = adapter.save(crearReporte("FOUND"))
                .cambiarEstado(EstadoReporte.RESCUED, FIXED_CLOCK);
        adapter.save(rescatada);

        Pagina<ReporteMascota> rescatadas = adapter.findByEstado(EstadoReporte.RESCUED, Paginacion.of(0, 10));
        Pagina<ReporteMascota> activas = adapter.findByEstado(EstadoReporte.ACTIVE, Paginacion.of(0, 10));

        assertEquals(1, rescatadas.totalElementos());
        assertEquals(1, activas.totalElementos());
    }

    @Test
    @DisplayName("findByUsuarioId: devuelve solo reportes del usuario")
    void findByUsuarioId_filtra() {
        adapter.save(crearReporte("LOST"));
        adapter.save(crearReporte("FOUND"));

        // Usuario sin reportes
        UserEntity otro = userJpaRepository.save(new UserEntity(
                "Ana", "ana@test.com", "+573002222222", "Calle 2", "pass123",
                barrioJpaRepository.save(new BarrioEntity("Norte",
                        cuadranteJpaRepository.save(new CuadranteEntity("CAI Norte", "+573003333333", "cai@test.com"))))));

        Pagina<ReporteMascota> delUsuario = adapter.findByUsuarioId(new UsuarioId(usuario.getId()), Paginacion.of(0, 10));
        Pagina<ReporteMascota> delOtro = adapter.findByUsuarioId(new UsuarioId(otro.getId()), Paginacion.of(0, 10));

        assertEquals(2, delUsuario.totalElementos());
        assertEquals(0, delOtro.totalElementos());
    }

    @Test
    @DisplayName("findAll con paginación: respeta size y page")
    void findAll_paginacion_respeta() {
        adapter.save(crearReporte("LOST"));
        adapter.save(crearReporte("FOUND"));

        Pagina<ReporteMascota> pagina = adapter.findByFilters(null, null, null, Paginacion.of(0, 1));

        assertEquals(1, pagina.contenido().size());
        assertEquals(2, pagina.totalElementos());
        assertEquals(0, pagina.pagina());
    }
}
