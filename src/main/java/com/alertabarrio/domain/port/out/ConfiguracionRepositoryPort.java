package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.Configuracion;
import com.alertabarrio.domain.model.valueobject.ConfiguracionId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import java.util.Optional;

public interface ConfiguracionRepositoryPort {
    Configuracion save(Configuracion config);
    Optional<Configuracion> findById(ConfiguracionId id);
    boolean existsById(ConfiguracionId id);
    void deleteById(ConfiguracionId id);
    Optional<Configuracion> findByUsuarioId(Long usuarioId);
    Pagina<Configuracion> findAll(Paginacion paginacion);
}
