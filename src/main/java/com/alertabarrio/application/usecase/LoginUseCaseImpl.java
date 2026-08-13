package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.LoginCommand;
import com.alertabarrio.application.dto.AuthResultDTO;
import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.mapper.UsuarioDomainMapper;
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
    private final UsuarioDomainMapper mapper;

    public LoginUseCaseImpl(UsuarioRepositoryPort usuarioRepository, PasswordEncoderPort passwordEncoder, TokenServicePort tokenService, UsuarioDomainMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.mapper = mapper;
    }

    @Override
    public AuthResultDTO execute(LoginCommand command) {
        // OCP: el identificador puede ser un email o un teléfono (registro rápido).
        // Si contiene '@' se busca por email; si no, por phone (llave del usuario rápido).
        var user = command.email().contains("@")
                ? usuarioRepository.findByEmail(command.email())
                : usuarioRepository.findByPhone(command.email());
        var usuario = user.orElseThrow(() -> new ResourceNotFoundException("User", command.email()));

        if (!passwordEncoder.verificar(command.password(), usuario.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = tokenService.generarToken(usuario.getEmail());
        UsuarioDTO userDto = mapper.toDto(usuario);
        return new AuthResultDTO(token, userDto);
    }
}
