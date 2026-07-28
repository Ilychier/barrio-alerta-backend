package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.ParchearUsuarioCommand;
import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.mapper.UsuarioDomainMapper;
import com.alertabarrio.domain.exception.ResourceConflictException;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.User;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.domain.model.valueobject.UsuarioId;
import com.alertabarrio.domain.port.in.ParchearUsuarioUseCase;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import com.alertabarrio.domain.port.out.PasswordEncoderPort;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;

@UseCase
public class ParchearUsuarioUseCaseImpl implements ParchearUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final BarrioRepositoryPort barrioRepository;
    private final UsuarioDomainMapper mapper;

    public ParchearUsuarioUseCaseImpl(UsuarioRepositoryPort usuarioRepository,
                                      PasswordEncoderPort passwordEncoder,
                                      BarrioRepositoryPort barrioRepository,
                                      UsuarioDomainMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.barrioRepository = barrioRepository;
        this.mapper = mapper;
    }

    @Override
    public UsuarioDTO execute(ParchearUsuarioCommand command) {
        UsuarioId id = new UsuarioId(command.id());
        User existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", command.id()));

        String name = command.name() != null ? command.name() : existente.getName();
        String email = command.email() != null ? command.email() : existente.getEmail().value();
        String phone = command.phone() != null ? command.phone() : existente.getPhone().value();
        String address = command.address() != null ? command.address() : existente.getAddress();

        if (!existente.getEmail().value().equals(email) && usuarioRepository.existsByEmail(email)) {
            throw new ResourceConflictException("Usuario", email);
        }

        String password;
        if (command.password() != null && !command.password().isBlank()) {
            password = passwordEncoder.hashear(command.password());
        } else {
            password = existente.getPassword();
        }

        Long barrioId;
        if (command.barrioId() != null) {
            if (!barrioRepository.existsById(new BarrioId(command.barrioId()))) {
                throw new ResourceNotFoundException("Barrio", command.barrioId());
            }
            barrioId = command.barrioId();
        } else {
            barrioId = existente.getBarrioId() != null ? existente.getBarrioId().value() : null;
        }

        User parcheado = User.reconstruir(command.id(), name, email, phone, address, password, barrioId);
        User saved = usuarioRepository.save(parcheado);
        return mapper.toDto(saved);
    }
}
