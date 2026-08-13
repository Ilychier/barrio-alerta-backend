package com.alertabarrio.infrastructure.persistence.adapter.mascotas;

import com.alertabarrio.domain.model.valueobject.Paginacion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

/**
 * El feed público pagina por {@code createdAt DESC}. Cuando varios reportes
 * comparten el mismo timestamp (importaciones masivas, reportes de la misma
 * hora), un sort sin desempate puede devolver el mismo registro en dos páginas
 * consecutivas → keys duplicadas en el frontend (React: "two children with the
 * same key"). El tiebreaker por {@code id} hace el orden determinista y estable.
 */
@DisplayName("MascotaPaginacionHelper (tiebreaker de paginación)")
class MascotaPaginacionHelperTest {

    @Test
    @DisplayName("toPageable: agrega id como tiebreaker al sort por createdAt DESC")
    void toPageable_conOrden_agregaTiebreakerId() {
        Pageable pageable = MascotaPaginacionHelper.toPageable(
                Paginacion.of(0, 10, "createdAt", "desc"));

        Sort.Order createdAt = pageable.getSort().getOrderFor("createdAt");
        Sort.Order id = pageable.getSort().getOrderFor("id");

        assertNotNull(createdAt, "debe ordenar por createdAt");
        assertEquals(Sort.Direction.DESC, createdAt.getDirection());
        assertNotNull(id, "debe incluir id como tiebreaker");
        assertEquals(Sort.Direction.ASC, id.getDirection());
    }

    @Test
    @DisplayName("toPageable: sin orden no agrega tiebreaker")
    void toPageable_sinOrden_noAgregaTiebreaker() {
        Pageable pageable = MascotaPaginacionHelper.toPageable(Paginacion.of(0, 10));

        assertNull(pageable.getSort().getOrderFor("id"));
    }
}
