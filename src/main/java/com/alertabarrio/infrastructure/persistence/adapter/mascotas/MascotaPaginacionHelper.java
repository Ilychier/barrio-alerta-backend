package com.alertabarrio.infrastructure.persistence.adapter.mascotas;

import com.alertabarrio.domain.model.valueobject.Paginacion;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Equivalente local de {@code PaginacionHelper} (package-private en el BC Alertas).
 * <p>
 * Se duplica intencionalmente para no modificar infraestructura compartida
 * del BC Alertas. 15 líneas, cero riesgo.
 */
final class MascotaPaginacionHelper {

    private MascotaPaginacionHelper() {}

    static Pageable toPageable(Paginacion p) {
        if (p.orden() != null && !p.orden().isEmpty()) {
            Sort.Direction dir = "desc".equalsIgnoreCase(p.direccion())
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            // Tiebreaker por id (ASC): sort determinista y estable. Sin él, filas con
            // el mismo valor de orden pueden aparecer en dos páginas consecutivas,
            // duplicando registros en el feed (keys duplicadas en el frontend).
            Sort sort = Sort.by(dir, p.orden());
            if (!"id".equalsIgnoreCase(p.orden())) {
                sort = sort.and(Sort.by(Sort.Direction.ASC, "id"));
            }
            return PageRequest.of(p.pagina(), p.tamanio(), sort);
        }
        return PageRequest.of(p.pagina(), p.tamanio());
    }
}
