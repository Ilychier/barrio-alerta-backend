package com.alertabarrio.application.usecase;

import com.alertabarrio.application.dto.UsuarioDTO;
import com.alertabarrio.application.mapper.UsuarioDomainMapper;
import com.alertabarrio.application.query.BuscarUsuarioPorEmailQuery;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.port.in.BuscarUsuarioPorEmailUseCase;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BuscarUsuarioPorEmailUseCaseImpl implements BuscarUsuarioPorEmailUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final UsuarioDomainMapper mapper;

    public BuscarUsuarioPorEmailUseCaseImpl(UsuarioRepositoryPort usuarioRepository, UsuarioDomainMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    @Override
    public UsuarioDTO execute(BuscarUsuarioPorEmailQuery query) {
        return usuarioRepository.findByEmail(query.email())
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", query.email()));
    }
}
