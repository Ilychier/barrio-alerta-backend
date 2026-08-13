package com.alertabarrio.domain.port.in;

import com.alertabarrio.application.command.CambiarPasswordCommand;
import com.alertabarrio.application.dto.UsuarioDTO;

/**
 * Puerto de entrada para cambiar la contraseña.
 * <p>
 * Si el usuario tiene clave temporal ({@code passwordTemporal=true}), no se
 * exige la clave actual (el JWT ya autentica). Si tiene clave real, se
 * verifica la actual antes de cambiarla.
 */
public interface CambiarPasswordUseCase {
    UsuarioDTO execute(CambiarPasswordCommand command);
}
