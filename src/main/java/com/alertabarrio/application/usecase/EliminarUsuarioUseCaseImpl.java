package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.EliminarUsuarioCommand;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.UsuarioId;
import com.alertabarrio.domain.port.in.EliminarUsuarioUseCase;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;

@UseCase
public class EliminarUsuarioUseCaseImpl implements EliminarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    public EliminarUsuarioUseCaseImpl(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void execute(EliminarUsuarioCommand command) {
        UsuarioId id = new UsuarioId(command.id());
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", command.id());
        }
        usuarioRepository.deleteById(id);
    }
}
