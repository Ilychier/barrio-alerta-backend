package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.RegistrarUsuarioCommand;
import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.mapper.UsuarioDomainMapper;
import com.alertabarrio.domain.exception.ResourceConflictException;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.User;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.domain.port.in.RegistrarUsuarioUseCase;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import com.alertabarrio.domain.port.out.PasswordEncoderPort;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;

@UseCase
public class RegistrarUsuarioUseCaseImpl implements RegistrarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final BarrioRepositoryPort barrioRepository;
    private final UsuarioDomainMapper mapper;

    public RegistrarUsuarioUseCaseImpl(UsuarioRepositoryPort usuarioRepository,
                                       PasswordEncoderPort passwordEncoder,
                                       BarrioRepositoryPort barrioRepository,
                                       UsuarioDomainMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.barrioRepository = barrioRepository;
        this.mapper = mapper;
    }

    @Override
    public UsuarioDTO execute(RegistrarUsuarioCommand command) {
        if (usuarioRepository.existsByEmail(command.email())) {
            throw new ResourceConflictException("Usuario", command.email());
        }

        if (command.barrioId() != null && !barrioRepository.existsById(new BarrioId(command.barrioId()))) {
            throw new ResourceNotFoundException("Barrio", command.barrioId());
        }

        String hashedPassword = passwordEncoder.hashear(command.password());
        User user = User.crear(command.name(), command.email(), command.phone(), command.address(), hashedPassword, command.barrioId());
        User saved = usuarioRepository.save(user);
        return mapper.toDto(saved);
    }
}
