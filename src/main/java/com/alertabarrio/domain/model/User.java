package com.alertabarrio.domain.model;

import com.alertabarrio.domain.exception.UsuarioInvalidoException;
import com.alertabarrio.domain.model.valueobject.*;

public final class User {

    private final UsuarioId id;
    private final String name;
    private final Email email;
    private final Telefono phone;
    private final String address;
    private final String password;
    private final BarrioId barrioId;
    private final boolean passwordTemporal;

    private User(UsuarioId id, String name, Email email, Telefono phone, String address, String password,
                 BarrioId barrioId, boolean passwordTemporal) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.barrioId = barrioId;
        this.passwordTemporal = passwordTemporal;
    }

    public static User crear(String name, String email, String phone, String address, String password, Long barrioId) {
        validarName(name);
        validarAddress(address);
        validarPassword(password);
        Email emailVo = new Email(email);
        Telefono phoneVo = new Telefono(phone);
        BarrioId barrioIdVo = barrioId != null ? new BarrioId(barrioId) : null;
        return new User(null, name.trim(), emailVo, phoneVo, address.trim(), password, barrioIdVo, false);
    }

    /**
     * Factory para el registro rápido de emergencia (BC Mascotas).
     * <p>
     * El usuario solo aporta su celular (WhatsApp) y una contraseña temporal
     * autogenerada. No tiene nombre, dirección ni barrio propios: la dirección
     * que reporta es la del animalito (vive en {@code ReporteMascota.ubicacion}).
     * <p>
     * Invariantes relajadas (Factory Method — no rompe {@link #crear}):
     * name/address usan valores por defecto y el flag {@code passwordTemporal}
     * nace en {@code true} para forzar el cambio de clave en el primer login.
     */
    public static User crearRapido(String phone, String password, String emailSintetico) {
        validarPassword(password);
        Email emailVo = new Email(emailSintetico);
        Telefono phoneVo = new Telefono(phone);
        return new User(null, "Vecino", emailVo, phoneVo, "Sin dirección", password, null, true);
    }

    public static User reconstruir(Long id, String name, String email, String phone, String address, String password, Long barrioId) {
        return reconstruir(id, name, email, phone, address, password, barrioId, false);
    }

    public static User reconstruir(Long id, String name, String email, String phone, String address, String password,
                                   Long barrioId, boolean passwordTemporal) {
        validarName(name);
        validarAddress(address);
        validarPassword(password);
        Email emailVo = new Email(email);
        Telefono phoneVo = new Telefono(phone);
        BarrioId barrioIdVo = barrioId != null ? new BarrioId(barrioId) : null;
        return new User(new UsuarioId(id), name.trim(), emailVo, phoneVo, address.trim(), password, barrioIdVo, passwordTemporal);
    }

    /**
     * Cambia la contraseña y desactiva el flag temporal (inmutable).
     * <p>
     * El flag {@code passwordTemporal} solo se limpia aquí: el usuario
     * autenticado con clave temporal debe pasar por este flujo antes de
     * usar la app normalmente.
     */
    public User cambiarPassword(String nuevaPassword) {
        validarPassword(nuevaPassword);
        return new User(this.id, this.name, this.email, this.phone, this.address, nuevaPassword,
                this.barrioId, false);
    }

    private static void validarName(String name) {
        if (name == null || name.isBlank()) throw new UsuarioInvalidoException("El nombre no puede estar vacío");
        if (name.trim().length() > 100) throw new UsuarioInvalidoException("El nombre no puede exceder 100 caracteres");
    }

    private static void validarAddress(String address) {
        if (address == null || address.isBlank()) throw new UsuarioInvalidoException("La dirección no puede estar vacía");
        if (address.trim().length() > 255) throw new UsuarioInvalidoException("La dirección no puede exceder 255 caracteres");
    }

    private static void validarPassword(String password) {
        if (password == null || password.isBlank()) throw new UsuarioInvalidoException("La contraseña no puede estar vacía");
    }

    public UsuarioId getId() { return id; }
    public String getName() { return name; }
    public Email getEmail() { return email; }
    public Telefono getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getPassword() { return password; }
    public BarrioId getBarrioId() { return barrioId; }
    public boolean isPasswordTemporal() { return passwordTemporal; }
}
