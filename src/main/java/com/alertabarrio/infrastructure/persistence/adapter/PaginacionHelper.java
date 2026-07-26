package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.valueobject.Paginacion;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

final class PaginacionHelper {

    private PaginacionHelper() {}

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
