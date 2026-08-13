package com.alertabarrio.application.usecase;

import com.alertabarrio.application.command.CambiarPasswordCommand;
import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.mapper.UsuarioDomainMapper;
import com.alertabarrio.application.usecase.mascotas.CambiarPasswordUseCaseImpl;
import com.alertabarrio.domain.model.User;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.model.valueobject.UsuarioId;
import com.alertabarrio.domain.port.out.PasswordEncoderPort;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CambiarPasswordUseCase")
class CambiarPasswordUseCaseTest {

    private UsuarioRepoFake usuarioRepo;
    private PasswordEncoderFake passwordEncoder;
    private CambiarPasswordUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        usuarioRepo = new UsuarioRepoFake();
        passwordEncoder = new PasswordEncoderFake();
        UsuarioDomainMapper mapper = new UsuarioDomainMapper() {
            @Override
            public UsuarioDTO toDto(User u) {
                return new UsuarioDTO(u.getId() != null ? u.getId().value() : null, u.getName(),
                        u.getEmail().value(), u.getPhone().value(), u.getAddress(),
                        u.getBarrioId() != null ? u.getBarrioId().value() : null, u.isPasswordTemporal());
            }
        };
        useCase = new CambiarPasswordUseCaseImpl(usuarioRepo, passwordEncoder, mapper);
    }

    @Test
    @DisplayName("usuario temporal: cambia clave sin verificar la actual y limpia el flag")
    void usuarioTemporal_cambiaSinActual() {
        usuarioRepo.seed(User.reconstruir(1L, "Vecino", "x@mascotas.temp", "+573001112233",
                "Sin dirección", "hashTEMP", null, true));

        UsuarioDTO result = useCase.execute(new CambiarPasswordCommand("x@mascotas.temp", null, "nuevaClave"));

        assertFalse(result.passwordTemporal());
        assertEquals("hash(nuevaClave)", usuarioRepo.db.get(0).getPassword());
    }

    @Test
    @DisplayName("usuario normal: verifica la clave actual antes de cambiar")
    void usuarioNormal_verificaActual() {
        usuarioRepo.seed(User.reconstruir(1L, "Juan", "juan@test.com", "+573001111111",
                "Calle 1", "hash(actual)", 1L, false));

        UsuarioDTO result = useCase.execute(new CambiarPasswordCommand("juan@test.com", "actual", "nueva"));

        assertFalse(result.passwordTemporal());
        assertEquals("hash(nueva)", usuarioRepo.db.get(0).getPassword());
    }

    @Test
    @DisplayName("usuario normal con clave actual incorrecta lanza IllegalArgumentException")
    void usuarioNormal_claveIncorrecta_lanza() {
        usuarioRepo.seed(User.reconstruir(1L, "Juan", "juan@test.com", "+573001111111",
                "Calle 1", "hash(actual)", 1L, false));

        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(new CambiarPasswordCommand("juan@test.com", "mal", "nueva")));
    }

    @Test
    @DisplayName("usuario inexistente lanza excepción")
    void usuarioInexistente_lanza() {
        assertThrows(RuntimeException.class,
                () -> useCase.execute(new CambiarPasswordCommand("no@existe.com", null, "nueva")));
    }

    // ── Fakes ──────────────────────────────────────────────

    static class UsuarioRepoFake implements UsuarioRepositoryPort {
        final List<User> db = new ArrayList<>();

        void seed(User u) { db.add(u); }

        @Override public User save(User user) {
            db.removeIf(u -> u.getId() != null && u.getId().equals(user.getId()));
            db.add(user);
            return user;
        }
        @Override public Optional<User> findById(UsuarioId id) { return db.stream().filter(u -> u.getId() != null && u.getId().equals(id)).findFirst(); }
        @Override public boolean existsById(UsuarioId id) { return findById(id).isPresent(); }
        @Override public void deleteById(UsuarioId id) { db.removeIf(u -> u.getId() != null && u.getId().equals(id)); }
        @Override public boolean existsByEmail(String email) { return db.stream().anyMatch(u -> u.getEmail().value().equals(email)); }
        @Override public Optional<User> findByEmail(String email) { return db.stream().filter(u -> u.getEmail().value().equals(email)).findFirst(); }
        @Override public Optional<User> findByPhone(String phone) { return db.stream().filter(u -> u.getPhone().value().equals(phone)).findFirst(); }
        @Override public Pagina<User> findAll(Paginacion p) { return new Pagina<>(db, 0, db.size(), db.size(), 1); }
    }

    static class PasswordEncoderFake implements PasswordEncoderPort {
        @Override public String hashear(String raw) { return "hash(" + raw + ")"; }
        @Override public boolean verificar(String raw, String hash) { return hash.equals(hashear(raw)); }
    }
}
