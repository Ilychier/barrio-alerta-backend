package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.User;
import com.alertabarrio.domain.model.valueobject.UsuarioId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import java.util.Optional;

public interface UsuarioRepositoryPort {
    User save(User user);
    Optional<User> findById(UsuarioId id);
    boolean existsById(UsuarioId id);
    void deleteById(UsuarioId id);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    /**
     * Busca un usuario por su teléfono (llave principal del registro rápido
     * de emergencia). Extiende el contrato sin romper los métodos existentes
     * (OCP): el login acepta email o phone indistintamente.
     */
    Optional<User> findByPhone(String phone);

    Pagina<User> findAll(Paginacion paginacion);
}
