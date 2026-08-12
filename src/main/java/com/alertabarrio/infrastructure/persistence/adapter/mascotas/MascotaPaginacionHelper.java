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
            return PageRequest.of(p.pagina(), p.tamanio(), Sort.by(dir, p.orden()));
        }
        return PageRequest.of(p.pagina(), p.tamanio());
    }
}
