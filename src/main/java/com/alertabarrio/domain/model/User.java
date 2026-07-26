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

    private User(UsuarioId id, String name, Email email, Telefono phone, String address, String password, BarrioId barrioId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.barrioId = barrioId;
    }

    public static User crear(String name, String email, String phone, String address, String password, Long barrioId) {
        validarName(name);
        validarAddress(address);
        validarPassword(password);
        Email emailVo = new Email(email);
        Telefono phoneVo = new Telefono(phone);
        BarrioId barrioIdVo = barrioId != null ? new BarrioId(barrioId) : null;
        return new User(null, name.trim(), emailVo, phoneVo, address.trim(), password, barrioIdVo);
    }

    public static User reconstruir(Long id, String name, String email, String phone, String address, String password, Long barrioId) {
        validarName(name);
        validarAddress(address);
        Email emailVo = new Email(email);
        Telefono phoneVo = new Telefono(phone);
        BarrioId barrioIdVo = barrioId != null ? new BarrioId(barrioId) : null;
        return new User(new UsuarioId(id), name.trim(), emailVo, phoneVo, address.trim(), password, barrioIdVo);
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
}
