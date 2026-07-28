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
    Pagina<User> findAll(Paginacion paginacion);
}
