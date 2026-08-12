package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.mascotas.RegistrarReporteRapidoCommand;
import com.alertabarrio.application.dto.mascotas.ReporteRapidoResultDTO;
import com.alertabarrio.application.mapper.mascotas.ReporteMascotaDomainMapper;
import com.alertabarrio.application.usecase.mascotas.RegistrarReporteRapidoUseCaseImpl;
import com.alertabarrio.domain.model.User;
import com.alertabarrio.domain.model.mascotas.EstadoReporte;
import com.alertabarrio.domain.model.mascotas.ReporteMascota;
import com.alertabarrio.domain.model.mascotas.TipoReporte;
import com.alertabarrio.domain.model.mascotas.valueobject.ReporteMascotaId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.model.valueobject.UsuarioId;
import com.alertabarrio.domain.port.out.ClaveTemporalPort;
import com.alertabarrio.domain.port.out.EmailSinteticoPort;
import com.alertabarrio.domain.port.out.PasswordEncoderPort;
import com.alertabarrio.domain.port.out.TokenServicePort;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;
import com.alertabarrio.domain.port.out.mascotas.ReporteMascotaRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RegistrarReporteRapidoUseCase (Facade transaccional)")
class RegistrarReporteRapidoUseCaseTest {

    private static final Clock RELOJ = Clock.fixed(Instant.parse("2026-08-12T10:00:00Z"), ZoneId.of("America/Bogota"));
    private static final String PHONE = "+573001112233";
    private static final String TELEFONO_CONTACTO = "+573009998877";

    private UsuarioRepoFake usuarioRepo;
    private ReporteRepoFake reporteRepo;
    private PasswordEncoderFake passwordEncoder;
    private TokenServiceFake tokenService;
    private EmailSinteticoFake emailSintetico;
    private ClaveTemporalFake claveTemporal;
    private RegistrarReporteRapidoUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        usuarioRepo = new UsuarioRepoFake();
        reporteRepo = new ReporteRepoFake();
        passwordEncoder = new PasswordEncoderFake();
        tokenService = new TokenServiceFake();
        emailSintetico = new EmailSinteticoFake();
        claveTemporal = new ClaveTemporalFake();
        ReporteMascotaDomainMapper mapper = new ReporteMascotaDomainMapper() {
            @Override
            public com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO toDto(ReporteMascota r) {
                return new com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO(
                        r.getId() != null ? r.getId().value() : null,
                        r.getTipoReporte().name(),
                        r.getTipoMascotaId().value(),
                        r.getOtroTipoMascota(),
                        r.getFotoUrl(),
                        r.getCiudadId().value(),
                        r.getUbicacion(),
                        r.getTelefono().value(),
                        r.getDescripcion(),
                        r.getEstado().name(),
                        r.getCreatedAt(),
                        r.getUpdatedAt(),
                        r.getUsuarioId().value());
            }
        };
        useCase = new RegistrarReporteRapidoUseCaseImpl(
                usuarioRepo, reporteRepo, passwordEncoder, tokenService,
                emailSintetico, claveTemporal, mapper, RELOJ);
    }

    private RegistrarReporteRapidoCommand command() {
        return new RegistrarReporteRapidoCommand(
                PHONE, TELEFONO_CONTACTO, "LOST", 1L, null, 1007L,
                "Montebello — Santiago de Cali", "Perro criollo", null);
    }

    @Test
    @DisplayName("usuario nuevo: crea usuario temporal + reporte + token (autologin)")
    void usuarioNuevo_creaTodoYDevuelveToken() {
        ReporteRapidoResultDTO result = useCase.execute(command());

        assertTrue(result.esNuevo());
        assertTrue(result.passwordTemporal());
        assertNotNull(result.token());
        assertNotNull(result.reporte().id());
        assertEquals(TELEFONO_CONTACTO, result.reporte().telefono());
        assertEquals(1, usuarioRepo.saved.size());
        assertTrue(usuarioRepo.saved.get(0).isPasswordTemporal());
        assertEquals(1, reporteRepo.saved.size());
        assertEquals(usuarioRepo.saved.get(0).getId().value(), reporteRepo.saved.get(0).getUsuarioId().value());
    }

    @Test
    @DisplayName("usuario existente no temporal: reutiliza usuario, crea reporte, sin token (login manual)")
    void usuarioExistenteNoTemporal_sinToken() {
        usuarioRepo.seed(User.reconstruir(5L, "Vecino", "573001112233@mascotas.temp", PHONE,
                "Sin dirección", "hashReal", null, false));

        ReporteRapidoResultDTO result = useCase.execute(command());

        assertFalse(result.esNuevo());
        assertFalse(result.passwordTemporal());
        assertNull(result.token());
        assertEquals(0, usuarioRepo.saved.size()); // no se crea ni actualiza usuario
        assertEquals(1, reporteRepo.saved.size());
        assertEquals(5L, reporteRepo.saved.get(0).getUsuarioId().value());
    }

    @Test
    @DisplayName("usuario existente temporal: reutiliza usuario, crea reporte, con token (autologin para cambiar clave)")
    void usuarioExistenteTemporal_conToken() {
        usuarioRepo.seed(User.reconstruir(5L, "Vecino", "573001112233@mascotas.temp", PHONE,
                "Sin dirección", "hashTemporal", null, true));

        ReporteRapidoResultDTO result = useCase.execute(command());

        assertFalse(result.esNuevo());
        assertTrue(result.passwordTemporal());
        assertNotNull(result.token());
        assertEquals(1, reporteRepo.saved.size());
    }

    @Test
    @DisplayName("el reporte guarda el teléfono de contacto de la mascota (no el personal)")
    void reporteGuardaTelefonoContacto() {
        ReporteRapidoResultDTO result = useCase.execute(command());
        assertEquals(TELEFONO_CONTACTO, result.reporte().telefono());
        assertEquals(PHONE, usuarioRepo.saved.get(0).getPhone().value());
    }

    // ── Fakes manuales (sin Mockito — dominio puro) ──────────────

    static class UsuarioRepoFake implements UsuarioRepositoryPort {
        final List<User> saved = new ArrayList<>();
        private final List<User> db = new ArrayList<>();

        void seed(User u) { db.add(u); }

        @Override public User save(User user) {
            final User aGuardar;
            if (user.getId() == null) {
                aGuardar = User.reconstruir(99L, user.getName(), user.getEmail().value(), user.getPhone().value(),
                        user.getAddress(), user.getPassword(), user.getBarrioId() != null ? user.getBarrioId().value() : null,
                        user.isPasswordTemporal());
            } else {
                aGuardar = user;
            }
            saved.add(aGuardar);
            db.removeIf(u -> u.getId() != null && u.getId().equals(aGuardar.getId()));
            db.add(aGuardar);
            return aGuardar;
        }
        @Override public Optional<User> findById(UsuarioId id) { return db.stream().filter(u -> u.getId() != null && u.getId().equals(id)).findFirst(); }
        @Override public boolean existsById(UsuarioId id) { return findById(id).isPresent(); }
        @Override public void deleteById(UsuarioId id) { db.removeIf(u -> u.getId() != null && u.getId().equals(id)); }
        @Override public boolean existsByEmail(String email) { return db.stream().anyMatch(u -> u.getEmail().value().equals(email)); }
        @Override public Optional<User> findByEmail(String email) { return db.stream().filter(u -> u.getEmail().value().equals(email)).findFirst(); }
        @Override public Optional<User> findByPhone(String phone) { return db.stream().filter(u -> u.getPhone().value().equals(phone)).findFirst(); }
        @Override public Pagina<User> findAll(Paginacion p) { return new Pagina<>(db, 0, db.size(), db.size(), 1); }
    }

    static class ReporteRepoFake implements ReporteMascotaRepositoryPort {
        final List<ReporteMascota> saved = new ArrayList<>();
        private long nextId = 1;

        @Override public ReporteMascota save(ReporteMascota reporte) {
            ReporteMascota conId = ReporteMascota.reconstruir(nextId++, reporte.getTipoReporte().name(),
                    reporte.getTipoMascotaId().value(), reporte.getCiudadId().value(), reporte.getUbicacion(),
                    reporte.getTelefono().value(), reporte.getDescripcion(), reporte.getEstado().name(),
                    reporte.getCreatedAt(), reporte.getUpdatedAt(), reporte.getUsuarioId().value(),
                    reporte.getOtroTipoMascota(), reporte.getFotoUrl());
            saved.add(conId);
            return conId;
        }
        @Override public Optional<ReporteMascota> findById(ReporteMascotaId id) { return Optional.empty(); }
        @Override public boolean existsById(ReporteMascotaId id) { return false; }
        @Override public void deleteById(ReporteMascotaId id) {}
        @Override public Pagina<ReporteMascota> findByFilters(EstadoReporte e, TipoReporte t, Long c, String b, Paginacion p) { return new Pagina<>(List.of(), 0, 0, 0, 0); }
        @Override public Pagina<ReporteMascota> findByEstado(EstadoReporte e, Paginacion p) { return new Pagina<>(List.of(), 0, 0, 0, 0); }
        @Override public Pagina<ReporteMascota> findByUsuarioId(UsuarioId u, Paginacion p) { return new Pagina<>(List.of(), 0, 0, 0, 0); }
    }

    static class PasswordEncoderFake implements PasswordEncoderPort {
        @Override public String hashear(String raw) { return "hash(" + raw + ")"; }
        @Override public boolean verificar(String raw, String hash) { return hash.equals(hashear(raw)); }
    }

    static class TokenServiceFake implements TokenServicePort {
        @Override public String generarToken(com.alertabarrio.domain.model.valueobject.Email email) { return "jwt-" + email.value(); }
        @Override public com.alertabarrio.domain.model.valueobject.Email validarToken(String token) { return new com.alertabarrio.domain.model.valueobject.Email("x@test.com"); }
    }

    static class EmailSinteticoFake implements EmailSinteticoPort {
        @Override public String generar(String phone) { return phone.replace("+", "") + "@mascotas.temp"; }
    }

    static class ClaveTemporalFake implements ClaveTemporalPort {
        @Override public String generar() { return "TEMP1234"; }
    }
}
