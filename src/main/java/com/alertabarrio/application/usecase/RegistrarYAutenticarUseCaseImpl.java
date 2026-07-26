package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.CrearConfiguracionCommand;
import com.alertabarrio.application.command.LoginCommand;
import com.alertabarrio.application.command.RegistrarUsuarioCommand;
import com.alertabarrio.application.dto.AuthResultDTO;
import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.domain.port.in.CrearConfiguracionUseCase;
import com.alertabarrio.domain.port.in.LoginUseCase;
import com.alertabarrio.domain.port.in.RegistrarUsuarioUseCase;
import com.alertabarrio.domain.port.in.RegistrarYAutenticarUseCase;

@UseCase
public class RegistrarYAutenticarUseCaseImpl implements RegistrarYAutenticarUseCase {

    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
    private final LoginUseCase loginUseCase;
    private final CrearConfiguracionUseCase crearConfiguracionUseCase;

    public RegistrarYAutenticarUseCaseImpl(RegistrarUsuarioUseCase registrarUsuarioUseCase, LoginUseCase loginUseCase, CrearConfiguracionUseCase crearConfiguracionUseCase) {
        this.registrarUsuarioUseCase = registrarUsuarioUseCase;
        this.loginUseCase = loginUseCase;
        this.crearConfiguracionUseCase = crearConfiguracionUseCase;
    }

    @Override
    public AuthResultDTO execute(RegistrarUsuarioCommand command) {
        UsuarioDTO user = registrarUsuarioUseCase.execute(command);
        crearConfiguracionUseCase.execute(new CrearConfiguracionCommand(user.id(), true, false));
        AuthResultDTO loginResult = loginUseCase.execute(new LoginCommand(command.email(), command.password()));
        return new AuthResultDTO(loginResult.token(), user);
    }
}
