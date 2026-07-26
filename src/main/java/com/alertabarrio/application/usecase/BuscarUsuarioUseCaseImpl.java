package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.mapper.UsuarioDomainMapper;
import com.alertabarrio.application.query.BuscarUsuarioQuery;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.UsuarioId;
import com.alertabarrio.domain.port.in.BuscarUsuarioUseCase;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BuscarUsuarioUseCaseImpl implements BuscarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final UsuarioDomainMapper mapper;

    public BuscarUsuarioUseCaseImpl(UsuarioRepositoryPort usuarioRepository, UsuarioDomainMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    @Override
    public UsuarioDTO execute(BuscarUsuarioQuery query) {
        return usuarioRepository.findById(new UsuarioId(query.id()))
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", query.id()));
    }
}
