package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.ActualizarUsuarioCommand;
import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.mapper.UsuarioDomainMapper;
import com.alertabarrio.domain.exception.ResourceConflictException;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.User;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.domain.model.valueobject.UsuarioId;
import com.alertabarrio.domain.port.in.ActualizarUsuarioUseCase;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import com.alertabarrio.domain.port.out.PasswordEncoderPort;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;

@UseCase
public class ActualizarUsuarioUseCaseImpl implements ActualizarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final BarrioRepositoryPort barrioRepository;
    private final UsuarioDomainMapper mapper;

    public ActualizarUsuarioUseCaseImpl(UsuarioRepositoryPort usuarioRepository,
                                        PasswordEncoderPort passwordEncoder,
                                        BarrioRepositoryPort barrioRepository,
                                        UsuarioDomainMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.barrioRepository = barrioRepository;
        this.mapper = mapper;
    }

    @Override
    public UsuarioDTO execute(ActualizarUsuarioCommand command) {
        UsuarioId id = new UsuarioId(command.id());
        User existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", command.id()));

        if (!existente.getEmail().value().equals(command.email()) && usuarioRepository.existsByEmail(command.email())) {
            throw new ResourceConflictException("Usuario", command.email());
        }

        if (command.barrioId() != null && !barrioRepository.existsById(new BarrioId(command.barrioId()))) {
            throw new ResourceNotFoundException("Barrio", command.barrioId());
        }

        String password = (command.password() != null && !command.password().isBlank())
                ? passwordEncoder.hashear(command.password())
                : existente.getPassword();

        Long barrioId = command.barrioId() != null ? command.barrioId() : null;

        User actualizado = User.reconstruir(command.id(), command.name(), command.email(), command.phone(), command.address(), password, barrioId);
        User saved = usuarioRepository.save(actualizado);
        return mapper.toDto(saved);
    }
}
