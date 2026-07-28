package com.alertabarrio.domain.model.valueobject;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;

public record Pagina<T>(
        @JsonProperty("content") List<T> contenido,
        @JsonProperty("page") int pagina,
        @JsonProperty("size") int tamanio,
        @JsonProperty("totalElements") long totalElementos,
        @JsonProperty("totalPages") int totalPaginas) {

    public Pagina {
        contenido = Collections.unmodifiableList(contenido);
    }

    public boolean isEmpty() {
        return contenido.isEmpty();
    }

    public boolean hasPrevious() {
        return pagina > 0;
    }

    public boolean hasNext() {
        return pagina + 1 < totalPaginas;
    }
}
