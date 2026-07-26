package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.LoginCommand;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.port.in.LoginUseCase;
import com.alertabarrio.domain.port.out.PasswordEncoderPort;
import com.alertabarrio.domain.port.out.TokenServicePort;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;

@UseCase
public class LoginUseCaseImpl implements LoginUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenServicePort tokenService;

    public LoginUseCaseImpl(UsuarioRepositoryPort usuarioRepository, PasswordEncoderPort passwordEncoder, TokenServicePort tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public String execute(LoginCommand command) {
        var user = usuarioRepository.findByEmail(command.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", command.email()));

        if (!passwordEncoder.verificar(command.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return tokenService.generarToken(user.getEmail());
    }
}
