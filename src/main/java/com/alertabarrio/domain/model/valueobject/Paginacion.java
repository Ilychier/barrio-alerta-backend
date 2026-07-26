package com.alertabarrio.domain.model.valueobject;

public record Paginacion(int pagina, int tamanio, String orden, String direccion) {

    public Paginacion {
        if (pagina < 0) throw new IllegalArgumentException("pagina no puede ser negativo");
        if (tamanio < 1) throw new IllegalArgumentException("tamanio debe ser >= 1");
    }

    public static Paginacion of(int pagina, int tamanio) {
        return new Paginacion(pagina, tamanio, null, null);
    }

    public static Paginacion of(int pagina, int tamanio, String orden, String direccion) {
        return new Paginacion(pagina, tamanio, orden, direccion);
    }
}
