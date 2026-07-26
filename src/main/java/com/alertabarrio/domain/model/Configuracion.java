package com.alertabarrio.domain.model;

import com.alertabarrio.domain.exception.ConfiguracionInvalidaException;
import com.alertabarrio.domain.model.valueobject.ConfiguracionId;
import com.alertabarrio.domain.model.valueobject.UsuarioId;

public final class Configuracion {

    private final ConfiguracionId id;
    private final UsuarioId usuarioId;
    private final boolean recibirNotificaciones;
    private final boolean modoSilencioso;

    private Configuracion(ConfiguracionId id, UsuarioId usuarioId, boolean recibirNotificaciones, boolean modoSilencioso) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.recibirNotificaciones = recibirNotificaciones;
        this.modoSilencioso = modoSilencioso;
    }

    public static Configuracion crear(Long usuarioId, boolean recibirNotificaciones, boolean modoSilencioso) {
        if (usuarioId == null) throw new ConfiguracionInvalidaException("El ID del usuario no puede ser nulo");
        return new Configuracion(null, new UsuarioId(usuarioId), recibirNotificaciones, modoSilencioso);
    }

    public static Configuracion reconstruir(Long id, Long usuarioId, boolean recibirNotificaciones, boolean modoSilencioso) {
        if (usuarioId == null) throw new ConfiguracionInvalidaException("El ID del usuario no puede ser nulo");
        return new Configuracion(new ConfiguracionId(id), new UsuarioId(usuarioId), recibirNotificaciones, modoSilencioso);
    }

    public ConfiguracionId getId() { return id; }
    public UsuarioId getUsuarioId() { return usuarioId; }
    public boolean isRecibirNotificaciones() { return recibirNotificaciones; }
    public boolean isModoSilencioso() { return modoSilencioso; }
}
