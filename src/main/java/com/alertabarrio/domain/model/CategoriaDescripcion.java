package com.alertabarrio.domain.model;

import com.alertabarrio.domain.exception.CategoriaDescripcionInvalidaException;
import com.alertabarrio.domain.model.valueobject.CategoriaDescripcionId;
import com.alertabarrio.domain.model.valueobject.CategoriaId;

public final class CategoriaDescripcion {

    private final CategoriaDescripcionId id;
    private final String descripcion;
    private final CategoriaId categoriaId;
    private final String imagenUrl;

    private CategoriaDescripcion(CategoriaDescripcionId id, String descripcion, CategoriaId categoriaId, String imagenUrl) {
        this.id = id;
        this.descripcion = descripcion;
        this.categoriaId = categoriaId;
        this.imagenUrl = imagenUrl;
    }

    public static CategoriaDescripcion crear(String descripcion, Long categoriaId, String imagenUrl) {
        validarDescripcion(descripcion);
        if (categoriaId == null) throw new CategoriaDescripcionInvalidaException("El ID de la categoría no puede ser nulo");
        return new CategoriaDescripcion(null, descripcion.trim(), new CategoriaId(categoriaId),
                imagenUrl != null ? imagenUrl.trim() : null);
    }

    public static CategoriaDescripcion reconstruir(Long id, String descripcion, Long categoriaId, String imagenUrl) {
        validarDescripcion(descripcion);
        if (categoriaId == null) throw new CategoriaDescripcionInvalidaException("El ID de la categoría no puede ser nulo");
        return new CategoriaDescripcion(new CategoriaDescripcionId(id), descripcion.trim(), new CategoriaId(categoriaId),
                imagenUrl != null ? imagenUrl.trim() : null);
    }

    private static void validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new CategoriaDescripcionInvalidaException("La descripción no puede estar vacía");
        }
    }

    public CategoriaDescripcionId getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public CategoriaId getCategoriaId() { return categoriaId; }
    public String getImagenUrl() { return imagenUrl; }
}
